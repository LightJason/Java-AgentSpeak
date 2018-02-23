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

package org.lightjason.agentspeak.language.execution.action;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;


/**
 * assignment action of a single-variable
 */
public final class CSingleAssignment<M extends IExecution> extends IBaseExecution<IVariable<?>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3668379813439056262L;
    /**
     * right-hand argument
     */
    private final M m_righthand;

    /**
     * ctor
     *
     * @param p_lefthand left-hand argument (variable)
     * @param p_righthand right-hand argument
     */
    public CSingleAssignment( @Nonnull final IVariable<?> p_lefthand, @Nonnull final M p_righthand )
    {
        super( p_lefthand );
        m_righthand = p_righthand;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_return = new LinkedList<>();
        final IFuzzyValue<Boolean> l_rightreturn = m_righthand.execute(
            p_parallel, p_context, Collections.emptyList(), l_return );
        if ( ( !l_rightreturn.value() ) || ( l_return.isEmpty() ) )
            return CFuzzyValue.from( false );

        CCommon.replaceFromContext( p_context, m_value ).<IVariable<Object>>term().set( l_return.get( 0 ).raw() );
        return CFuzzyValue.from( true );
    }

    @Override
    public final int hashCode()
    {
        return ( m_value == null ? 0 : m_value.hashCode() ) ^ m_righthand.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IExecution ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} = {1}", m_value, m_righthand );
    }

    @Override
    @Nonnull
    public final Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            Stream.of( m_value ),
            m_righthand.variables()
        );
    }
}
