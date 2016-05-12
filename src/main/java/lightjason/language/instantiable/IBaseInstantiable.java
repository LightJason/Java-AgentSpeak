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

package lightjason.language.instantiable;

import lightjason.agent.IAgent;
import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.annotation.CNumberAnnotation;
import lightjason.language.execution.annotation.IAnnotation;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.variable.IVariable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * base structure of instantiable elements
 */
public abstract class IBaseInstantiable implements IInstantiable
{
    /**
     * action list
     */
    protected final List<IExecution> m_action;

    /**
     * map with annotation (enum value for getting annotation object)
     */
    protected final Map<IAnnotation.EType, IAnnotation<?>> m_annotation;
    /**
     * hash code
     */
    private final int m_hash;

    /**
     * ctor
     *
     * @param p_action executed actions
     * @param p_annotation annotation map
     * @param p_hash hash code
     */
    protected IBaseInstantiable( final List<IExecution> p_action, final Set<IAnnotation<?>> p_annotation, final int p_hash )
    {
        m_hash = p_hash;
        m_action = Collections.unmodifiableList( p_action );

        // set default annotations
        final Map<IAnnotation.EType, IAnnotation<?>> l_map = p_annotation.stream().collect( HashMap::new, ( m, s ) -> m.put( s.getID(), s ), Map::putAll );
        l_map.putIfAbsent( IAnnotation.EType.FUZZY, new CNumberAnnotation<>( IAnnotation.EType.FUZZY, 1.0 ) );
        m_annotation = Collections.unmodifiableMap( l_map );
    }

    @Override
    public final int hashCode()
    {
        return m_hash;
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public double score( final IAgent p_agent )
    {
        return p_agent.getAggregation().evaluate( m_action.parallelStream().mapToDouble( i -> i.score( p_agent ) ).boxed() );
    }

    @Override
    public final IContext instantiate( final IAgent p_agent, final Stream<IVariable<?>> p_variable )
    {
        return CCommon.instantiate( this, p_agent, p_variable );
    }

    @Override
    public Stream<IVariable<?>> getVariables()
    {
        return m_action.stream().flatMap( i -> i.getVariables() );
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
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
