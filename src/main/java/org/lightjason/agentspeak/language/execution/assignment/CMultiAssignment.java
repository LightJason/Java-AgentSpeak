/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.execution.assignment;

import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * assignment action of a multi-variable list
 */
public final class CMultiAssignment extends IBaseExecution<List<IVariable<?>>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -6123210880356077509L;
    /**
     * right-hand argument
     */
    private final IExecution m_rhs;

    /**
     * ctor
     *
     * @param p_lhs left-hand variable list
     * @param p_rhs right-hand argument
     */
    public CMultiAssignment( @Nonnull final Stream<IVariable<?>> p_lhs, @Nonnull final IExecution p_rhs )
    {
        super( Collections.unmodifiableList( p_lhs.collect( Collectors.toList() ) ) );
        m_rhs = p_rhs;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_result = CCommon.argumentlist();

        if ( !p_context.agent().fuzzy().defuzzification().success(
                p_context.agent().fuzzy().defuzzification().apply( m_rhs.execute( p_parallel, p_context, Collections.emptyList(), l_result ) )
             )
        )
            return p_context.agent().fuzzy().membership().fail();

        if ( l_result.isEmpty() )
            throw new CExecutionIllegalStateException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "rhsincorrect" )
            );


        // position matching on list index
        final List<ITerm> l_flatresult = CCommon.flatten( l_result ).collect( Collectors.toList() );
        final List<ITerm> l_assign = CCommon.replacebycontext( p_context, m_value.stream() ).collect( Collectors.toList() );

        IntStream.range( 0, Math.min( l_assign.size(), l_flatresult.size() ) )
                 .boxed()
                 .forEach( i -> l_assign.get( i ).<IVariable<Object>>term().set( l_flatresult.get( i ).raw() ) );


        // tail matching
        if ( l_assign.size() < l_flatresult.size() )
            l_assign.get( l_assign.size() - 1 ).<IVariable<Object>>term().set( l_flatresult.subList( l_assign.size() - 1, l_flatresult.size() ) );

        return Stream.of();
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() ^ m_rhs.hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof IExecution && this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0} = {1}", m_value, m_rhs );
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            Objects.isNull( m_value ) ? Stream.empty() : m_value.stream(),
            m_rhs.variables()
        );
    }
}
