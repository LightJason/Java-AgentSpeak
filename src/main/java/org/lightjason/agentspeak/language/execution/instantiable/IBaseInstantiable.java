/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.instantiable;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * base structure of instantiable elements
 */
public abstract class IBaseInstantiable implements IInstantiable
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 8843291880722926104L;
    /**
     * action list
     */
    protected final IExecution[] m_execution;
    /**
     * description
     */
    protected final String m_description;
    /**
     * parallel execution
     */
    protected final boolean m_parallel;
    /**
     * atomic execution
     */
    protected final boolean m_atomic;
    /**
     * constants
     */
    protected final IVariable<?>[] m_constant;
    /**
     * hash code
     */
    private final int m_hash;


    /**
     * ctor
     *
     * @param p_execution executed actions
     * @param p_annotation annotation map
     * @param p_hash hash code
     */
    protected IBaseInstantiable( @Nonnull final IExecution[] p_execution, @Nonnull final IAnnotation<?>[] p_annotation, final int p_hash )
    {
        m_hash = p_hash;
        m_execution = p_execution;

        m_description = Arrays.stream( p_annotation )
                              .parallel()
                              .filter( i -> IAnnotation.EType.DESCRIPTION.equals( i.id() ) )
                              .findFirst()
                              .map( i -> i.value().toString() )
                              .orElse( "" );

        m_constant = Arrays.stream( p_annotation )
                           .parallel()
                           .filter( i -> IAnnotation.EType.CONSTANT.equals( i.id() ) )
                           .flatMap( IAnnotation::variables )
                           .toArray( IVariable<?>[]::new );

        m_parallel = Arrays.stream( p_annotation ).parallel().anyMatch( i -> IAnnotation.EType.PARALLEL.equals( i.id() ) );
        m_atomic = Arrays.stream( p_annotation ).parallel().anyMatch( i -> IAnnotation.EType.ATOMIC.equals( i.id() ) );
    }

    @Nonnull
    @Override
    public final String description()
    {
        return m_description;
    }

    @Override
    public final int hashCode()
    {
        return m_hash;
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object instanceof IInstantiable ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Nonnull
    @Override
    public final IContext instantiate( @Nonnull final IAgent<?> p_agent, @Nonnull final Stream<IVariable<?>> p_variable )
    {
        return CCommon.instantiate( this, p_agent, p_variable );
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Arrays.stream( m_execution ).flatMap( IExecution::variables );
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        // execution must be the first call, because all elements must be executed and iif the execution fails the @atomic flag can be checked,
        // each item gets its own parameters, annotation and return stack, so it will be created locally, but the return list did not to be an "empty-list"
        // because we need to allocate memory of any possible element, otherwise an unsupported operation exception is thrown
        final List<IFuzzyValue<Boolean>> l_result = m_parallel
                                                    ? this.executeparallel( p_context )
                                                    : this.executesequential( p_context );
        // if atomic flag if exists use this for return value
        return m_atomic
               ? CFuzzyValue.of( true )
               : l_result.stream().collect( p_context.agent().fuzzy().getKey() );
    }

    /**
     * execute plan sequential
     *
     * @param p_context execution context
     * @return list with execution results
     *
     * @note stream is stopped iif an execution is failed
     */
    @SuppressWarnings( "ResultOfMethodCallIgnored" )
    private List<IFuzzyValue<Boolean>> executesequential( final IContext p_context )
    {
        final List<IFuzzyValue<Boolean>> l_result = Collections.synchronizedList( new LinkedList<>() );

        Arrays.stream( m_execution )
                .map( i ->
                {
                    final IFuzzyValue<Boolean> l_return = i.execute( false, p_context, Collections.<ITerm>emptyList(), new LinkedList<>() );
                    l_result.add( l_return );
                    return p_context.agent().fuzzy().getValue().defuzzify( l_return );
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
        return Arrays.stream( m_execution ).parallel()
                     .map( i -> i.execute( false, p_context, Collections.<ITerm>emptyList(), new LinkedList<>() ) )
                     .collect( Collectors.toList() );
    }

}
