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
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.execution.annotation.CNumberAnnotation;
import lightjason.language.execution.annotation.IAnnotation;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.score.IAggregation;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * plan structure
 *
 * @todo hashcode / equals are missing
 * @todo annotation handling is incomplete
 */
public final class CPlan implements IPlan
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
     * action list
     */
    protected final List<IExecution> m_action;
    /**
     * map with annotation (enum value for getting annotation object)
     */
    protected final Map<IAnnotation.EType, IAnnotation<?>> m_annotation;


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
        m_triggerevent = p_event;
        m_action = Collections.unmodifiableList( p_body );
        m_annotation = this.addDefault( p_annotation );
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
        // add atomic flag
        l_result.add( CFuzzyValue.from( m_annotation.containsKey( IAnnotation.EType.ATOMIC ) ) );
        return l_result.stream().collect( p_context.getAgent().getFuzzy().getResultOperator() );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        final Collection<Double> l_values = m_action.parallelStream().mapToDouble( i -> i.score( p_aggregate, p_agent ) ).boxed().collect(
                Collectors.toList() );

        final CNumberAnnotation<Number> l_planscore = (CNumberAnnotation) m_annotation.get( IAnnotation.EType.SCORE );
        if ( l_planscore != null )
            l_values.add( l_planscore.getData().doubleValue() );

        return p_aggregate.evaluate( Collections.unmodifiableCollection( l_values ) );
    }

    @Override
    @SuppressWarnings( {"serial", "unchecked"} )
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            m_action.stream().flatMap( i -> i.getVariables().stream() ).forEach( i -> add( i ) );

            addAll( Stream.concat(
                    CCommon.recursiveterm( m_triggerevent.getLiteral().orderedvalues() ),
                    CCommon.recursiveliteral( m_triggerevent.getLiteral().annotations() )
            ).filter( i -> i instanceof IVariable<?> ).map( i -> ( (IVariable<?>) i ).shallowcopy() ).collect( Collectors.toSet() ) );

            if ( m_condition != null )
                addAll( m_condition.getVariables() );
        }};
    }

    @Override
    public final IContext getContext( final IAgent p_agent, final IAggregation p_aggregation, final IVariableBuilder p_variablebuilder,
                                      final Set<IVariable<?>> p_variables
    )
    {
        return CCommon.getContext( this, p_agent, p_aggregation, p_variablebuilder, p_variables );
    }

    /**
     * add default values to the annotation map
     *
     * @param p_annotation set with annotation
     * @return unmodifiable map
     */
    protected Map<IAnnotation.EType, IAnnotation<?>> addDefault( final Set<IAnnotation<?>> p_annotation )
    {
        final Map<IAnnotation.EType, IAnnotation<?>> l_map = p_annotation.stream().collect( HashMap::new, ( m, s ) -> m.put( s.getID(), s ), Map::putAll );

        l_map.putIfAbsent( IAnnotation.EType.FUZZY, new CNumberAnnotation<>( IAnnotation.EType.FUZZY, 1.0 ) );
        return Collections.unmodifiableMap( l_map );
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
