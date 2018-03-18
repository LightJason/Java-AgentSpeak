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

package org.lightjason.agentspeak.language.execution.action.lambda;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.action.IBaseExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * lambda expression definition
 * @deprecated old
 */
@Deprecated
public final class CLambdaExpression extends IBaseExecution<IVariable<?>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5128636076731831236L;
    /**
     * initialization expression
     */
    private final IExecution m_initialize;
    /**
     * execution body
     */
    private final List<IExecution> m_body;
    /**
     * flag of parallel execution
     */
    private final boolean m_parallel;
    /**
     * return variable
     */
    private final IVariable<?> m_return;


    /**
     * ctor
     *
     * @param p_parallel parallel execution flag
     * @param p_initialize expression
     * @param p_iterator iteration variable
     * @param p_body execution body
     */
    public CLambdaExpression( final boolean p_parallel, @Nonnull final IExecution p_initialize,
                              @Nonnull final IVariable<?> p_iterator, @Nonnull final List<IExecution> p_body )
    {
        this( p_parallel, p_initialize, p_iterator, null, p_body );
    }

    /**
     * ctor
     *
     * @param p_parallel parallel execution flag
     * @param p_initialize expression
     * @param p_iterator iteration variable
     * @param p_return return variable
     * @param p_body execution body
     */
    public CLambdaExpression( final boolean p_parallel, @Nonnull final IExecution p_initialize, @Nonnull final IVariable<?> p_iterator,
                              @Nullable final IVariable<?> p_return, @Nonnull final List<IExecution> p_body )
    {
        super( p_iterator );
        m_parallel = p_parallel;
        m_initialize = p_initialize;
        m_return = p_return;
        m_body = Collections.unmodifiableList( p_body );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        // run initialization
        final List<ITerm> l_initialization = new LinkedList<>();
        if ( !m_initialize.execute( p_parallel, p_context, p_argument, l_initialization ).value() )
            return CFuzzyValue.from( false );

        // run lambda expression
        final List<?> l_return = m_parallel ? this.executeParallel( p_context, l_initialization ) : this.executeSequential( p_context, l_initialization );
        if ( Objects.nonNull( m_return ) )
            CCommon.replaceFromContext( p_context, m_return ).<IVariable<List<?>>>term().set( l_return );

        return CFuzzyValue.from( true );
    }

    @Override
    public final int hashCode()
    {
        return m_initialize.hashCode() ^ m_value.hashCode() ^ m_body.hashCode() ^ ( m_parallel ? 9931 : 0 );
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object instanceof IExecution ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Objects.isNull( m_return )
               ? m_initialize.variables()
               : Stream.concat(
                   Stream.of( m_return ),
                   m_initialize.variables()
               );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}({1}) -> {2} | {3}", m_parallel ? "@" : "", m_initialize, m_value, m_body );
    }

    /**
     * run sequential execution
     *
     * @param p_context execution context
     * @param p_input input list
     * @return return list
     */
    @Nonnull
    private List<?> executeSequential( @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_input )
    {
        final Triple<IContext, IVariable<?>, IVariable<?>> l_localcontext = this.getLocalContext( p_context );

        return CCommon.flatten( p_input )
                      .map( i ->
                      {
                          l_localcontext.getMiddle().set( i.raw() );
                          m_body.forEach( j -> j.execute(
                                  m_parallel,
                                  l_localcontext.getLeft(),
                                  Collections.<ITerm>emptyList(),
                                  new LinkedList<>()
                               ) );
                          return l_localcontext.getRight() != null ? l_localcontext.getRight().raw() : null;
                      } )
                      .filter( Objects::nonNull )
                      .collect( Collectors.toList() );
    }

    /**
     * run parallel execution
     *
     * @param p_context execution context
     * @param p_input input list
     * @return return list
     */
    @Nonnull
    private List<?> executeParallel( @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_input )
    {
        return CCommon.flatten( p_input )
                      .parallel()
                      .map( i ->
                      {
                          final Triple<IContext, IVariable<?>, IVariable<?>> l_localcontext = this.getLocalContext( p_context );
                          l_localcontext.getMiddle().set( i.raw() );
                          m_body.forEach( j -> j.execute(
                                  m_parallel,
                                  l_localcontext.getLeft(),
                                  Collections.<ITerm>emptyList(),
                                  new LinkedList<>()
                                ) );
                          return l_localcontext.getRight() != null ? l_localcontext.getRight().raw() : null;
                      } )
                      .filter( Objects::nonNull )
                      .collect( Collectors.toList() );
    }


    /**
     * create the local context structure of the expression
     *
     * @param p_context local context
     * @return tripel with context, iterator variable and return variable
     */
    @Nonnull
    private Triple<IContext, IVariable<?>, IVariable<?>> getLocalContext( @Nonnull final IContext p_context )
    {
        final IVariable<?> l_iterator = m_value.shallowcopy();
        final IVariable<?> l_return = m_return != null ? m_return.shallowcopy() : null;

        final Set<IVariable<?>> l_variables = new HashSet<>( p_context.instancevariables().values() );

        m_body.stream().flatMap( IExecution::variables ).forEach( l_variables::add );
        l_variables.remove( l_iterator );
        l_variables.add( l_iterator );

        if ( Objects.nonNull( l_return ) )
        {
            l_variables.remove( l_return );
            l_variables.add( l_return );
        }

        return new ImmutableTriple<>( new CContext( p_context.agent(), p_context.instance(), l_variables ), l_iterator, l_return );
    }
}
