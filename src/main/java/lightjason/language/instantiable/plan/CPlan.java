/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.instantiable.plan;

import lightjason.agent.IAgent;
import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.annotation.IAnnotation;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.IBaseInstantiable;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.variable.IVariable;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * plan structure
 *
 * @todo annotation handling is incomplete
 */
public final class CPlan extends IBaseInstantiable implements IPlan
{
    /**
     * trigger event
     */
    protected final ITrigger m_triggerevent;
    /**
     * number of runs
     */
    protected long m_runs;
    /**
     * number of fail runs
     */
    protected long m_failruns;
    /**
     * execution condition / expression
     */
    protected final IExpression m_condition;


    /**
     * ctor
     *
     * @param p_event trigger event
     * @param p_body plan body
     * @param p_annotation annotations
     */
    public CPlan( final ITrigger p_event, final List<IExecution> p_body, final Set<IAnnotation<?>> p_annotation )
    {
        this( p_event, null, p_body, p_annotation );
    }

    /**
     * ctor
     *
     * @param p_event trigger event
     * @param p_condition execution condition
     * @param p_body plan body
     * @param p_annotation annotations
     */
    public CPlan( final ITrigger p_event, final IExpression p_condition, final List<IExecution> p_body, final Set<IAnnotation<?>> p_annotation
    )
    {
        super(
                p_body,
                p_annotation,
                p_event.hashCode()
                + ( p_condition == null ? 0 : p_condition.hashCode() )
                + p_body.stream().mapToInt( i -> i.hashCode() ).sum()
                + p_annotation.stream().mapToInt( i -> i.hashCode() ).sum()
        );


        m_triggerevent = p_event;
        m_condition = p_condition;
    }

    @Override
    public final ITrigger getTrigger()
    {
        return m_triggerevent;
    }

    @Override
    public final Collection<IAnnotation<?>> getAnnotations()
    {
        return m_annotation.values();
    }

    @Override
    public final List<IExecution> getBodyActions()
    {
        return m_action;
    }

    @Override
    public final IFuzzyValue<Boolean> condition( final IContext p_context )
    {
        if ( m_condition == null )
            return CFuzzyValue.from( true );

        final List<ITerm> l_return = new LinkedList<>();
        return CFuzzyValue.from(
                m_condition.execute( p_context, false, Collections.<ITerm>emptyList(), l_return, Collections.<ITerm>emptyList() ).getValue()
                && ( l_return.size() == 1 )
        );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format(
                "{0} ({1} | {2}{3} ==>> {4})",
                super.toString(),
                m_annotation.values(),
                m_triggerevent,
                m_condition == null ? "" : MessageFormat.format( " |- {0}", m_condition ),
                StringUtils.join( m_action, "; " )
        );
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // execution must be the first call, because all elements must be executed and iif the execution fails the @atomic flag can be checked,
        // each item gets its own parameters, annotation and return stack, so it will be created locally, but the return list did not to be an "empty-list"
        // because we need to allocate memory of any possible element, otherwise an unsupported operation exception is thrown
        final List<IFuzzyValue<Boolean>> l_result = m_annotation.containsKey( IAnnotation.EType.PARALLEL )
                                                    ? this.executeparallel( p_context )
                                                    : this.executesequential( p_context );
        // if atomic flag if exists use this for return value
        return m_annotation.containsKey( IAnnotation.EType.ATOMIC )
               ? CFuzzyValue.from( true )
               : l_result.stream().collect( p_context.getAgent().getFuzzy().getResultOperator() );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final double score( final IAgent p_agent )
    {
        return p_agent.getAggregation().evaluate(
                Stream.concat(
                        Stream.of( super.score( p_agent ) ),
                        Stream.of(
                                m_annotation.containsKey( IAnnotation.EType.SCORE )
                                ? ( (Number) m_annotation.get( IAnnotation.EType.SCORE ).getValue() ).doubleValue()
                                : 0
                        )
                )
        );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final Stream<IVariable<?>> getVariables()
    {
        return (Stream<IVariable<?>>) Stream.of(
                m_condition != null
                ? m_condition.getVariables()
                : Stream.<IVariable<?>>empty(),

                super.getVariables(),

                CCommon.recursiveterm( m_triggerevent.getLiteral().orderedvalues() )
                       .filter( i -> i instanceof IVariable<?> )
                       .map( i -> (IVariable<?>) i ),

                CCommon.recursiveliteral( m_triggerevent.getLiteral().annotations() )
                       .filter( i -> i instanceof IVariable<?> )
                       .map( i -> ( (IVariable<?>) i ) )
        )
                                            .reduce( Stream::concat )
                                            .orElseGet( Stream::<IVariable<?>>empty );
    }

    /**
     * execute plan sequential
     *
     * @param p_context execution context
     * @return list with execution results
     *
     * @note stream is stopped iif an execution is failed
     */
    private List<IFuzzyValue<Boolean>> executesequential( final IContext p_context )
    {
        final List<IFuzzyValue<Boolean>> l_result = Collections.synchronizedList( new LinkedList<>() );

        m_action.stream()
                .map( i -> {
                    final IFuzzyValue<Boolean> l_return = i.execute(
                            p_context, false, Collections.<ITerm>emptyList(), new LinkedList<>(), Collections.<ITerm>emptyList() );
                    l_result.add( l_return );
                    return p_context.getAgent().getFuzzy().getDefuzzyfication().defuzzify( l_return );
                } )
                .filter( i -> !i )
                .findFirst();

        return l_result;
    }

    /**
     * execute plan parallel
     *
     * @param p_context execution context
     * @return list with execution results
     *
     * @note each element is executed
     */
    private List<IFuzzyValue<Boolean>> executeparallel( final IContext p_context )
    {
        return m_action.parallelStream()
                       .map( i -> i.execute( p_context, false, Collections.<ITerm>emptyList(), new LinkedList<>(), Collections.<ITerm>emptyList() ) )
                       .collect( Collectors.toList() );
    }
}
