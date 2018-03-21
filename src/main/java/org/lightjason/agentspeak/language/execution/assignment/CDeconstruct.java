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

package org.lightjason.agentspeak.language.execution.assignment;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * deconstruct assignment
 */
public final class CDeconstruct extends IBaseExecution<IVariable<?>[]>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 6234582899813921890L;
    /**
     * right-hand argument (literal)
     */
    private final ITerm m_righthand;

    /**
     * ctor
     *
     * @param p_lefthand left-hand variable list
     * @param p_righthand right-hand argument
     */
    @Nonnull
    public CDeconstruct( @Nonnull final Stream<IVariable<?>> p_lefthand, @Nonnull final ITerm p_righthand )
    {
        super( p_lefthand.toArray( IVariable<?>[]::new ) );
        m_righthand = p_righthand;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        this.set( CCommon.replaceFromContext( p_context, Arrays.stream( m_value ) ).toArray( ITerm[]::new ), CCommon.replaceFromContext( p_context, m_righthand ).raw() );
        return CFuzzyValue.of( true );
    }

    @Override
    public final int hashCode()
    {
        return

            Objects.isNull( m_value ) ? 0 : Arrays.hashCode( m_value ) ^ m_righthand.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object instanceof IExecution ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} =.. {1}", m_value, m_righthand );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Arrays.stream( m_value );
    }

    /**
     * sets the values
     *
     * @param p_assignment variable list
     * @param p_term term
     */
    private void set( @Nonnull final ITerm[] p_assignment, @Nonnull final ILiteral p_term )
    {
        if ( p_assignment.length >= 1 )
            p_assignment[0].<IVariable<Object>>term().set( p_term.fqnfunctor().toString() );
        if ( p_assignment.length >= 2 )
            p_assignment[1].<IVariable<Object>>term().set( p_term.values().collect( Collectors.toList() ) );
    }
}
