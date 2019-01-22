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
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Arrays;
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
     * constants
     */
    private final IVariable<?>[] m_constant;
    /**
     * parallel execution
     */
    private final boolean m_parallel;
    /**
     * atomic execution
     */
    private final boolean m_atomic;
    /**
     * description
     */
    private final String m_description;
    /**
     * tags
     */
    private final String[] m_tags;
    /**
     * variable description as constants
     */
    private final IVariable<?>[] m_variabledescription;
    /**
     * hash code
     */
    private final int m_hash;

    /**
     * ctor
     *
     * @param p_execution execution elements
     * @param p_hash hash code
     */
    protected IBaseInstantiable( @Nonnull final IExecution[] p_execution, final int p_hash )
    {
        m_hash = p_hash;
        m_execution = p_execution;

        m_atomic = false;
        m_parallel = false;
        m_description = "";
        m_tags = new String[0];
        m_constant = new IVariable<?>[0];
        m_variabledescription = new IVariable<?>[0];
    }

    /**
     * ctor
     *
     * @param p_annotation annotation map
     * @param p_execution execution elements
     * @param p_hash hash code
     */
    protected IBaseInstantiable( @Nonnull final IAnnotation<?>[] p_annotation, @Nonnull final IExecution[] p_execution, final int p_hash )
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

        m_tags = Arrays.stream( p_annotation )
                       .parallel()
                       .filter( i -> IAnnotation.EType.TAG.equals( i.id() ) )
                       .map( i -> i.value().toString() )
                       .toArray( String[]::new );

        m_variabledescription = Arrays.stream( p_annotation )
                                      .parallel()
                                      .filter( i -> IAnnotation.EType.VARIABLE.equals( i.id() ) )
                                      .flatMap( i -> i.variables() )
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

    @Nonnull
    @Override
    public final Stream<String> tags()
    {
        return Arrays.stream( m_tags );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variabledescription()
    {
        return Arrays.stream( m_variabledescription );
    }

    @Override
    public final int hashCode()
    {
        return m_hash;
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return p_object instanceof IInstantiable && this.hashCode() == p_object.hashCode();
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
        return Stream.concat(
            Arrays.stream( m_constant ),
            Arrays.stream( m_execution ).flatMap( IExecution::variables )
        );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        // execution must be the first call, because all elements must be executed and iif the execution fails the @atomic flag can be checked,
        // each item gets its own parameters, annotation and return stack, so it will be created locally, but the return list did not to be an "empty-list"
        // because we need to allocate memory of any possible element, otherwise an unsupported operation exception is thrown
        final List<IFuzzyValue<Boolean>> l_result = m_parallel
                                                    ? CCommon.executeparallel( p_context, Arrays.stream( m_execution ) )
                                                    : CCommon.executesequential( p_context, Arrays.stream( m_execution ) );

        // if atomic flag if exists use this for return value
        return m_atomic
               ? p_context.agent().fuzzy().membership().success()
               : l_result.stream().collect( p_context.agent().fuzzy().getKey() );
    }


    /**
     * returns all data as a string
     *
     * @return string represenation
     */
    protected final String datatoString()
    {
        return MessageFormat.format(
            "{0} {1} {2} {3} {4}",

            m_parallel ? IAnnotation.EType.PARALLEL : "",

            m_atomic ? IAnnotation.EType.ATOMIC : "",

            Arrays.stream( m_constant )
                  .map( i -> MessageFormat.format( "{0}({1},{2})", IAnnotation.EType.CONSTANT, i.functor(), i.raw() ) )
                  .collect( Collectors.joining( " " ) ),

            this.tags().map( i -> MessageFormat.format( "{0}({1})", IAnnotation.EType.TAG, i ) ).collect( Collectors.joining( " " ) ),

            m_description.isEmpty() ? "" : MessageFormat.format( "{0}({1})", IAnnotation.EType.DESCRIPTION, this.description() )
        );
    }
}
