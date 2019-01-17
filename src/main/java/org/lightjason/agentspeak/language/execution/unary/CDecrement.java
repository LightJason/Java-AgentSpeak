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

package org.lightjason.agentspeak.language.execution.unary;

import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;


/**
 * unary increment
 */
public final class CDecrement implements IUnary
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5980848121313572183L;
    /**
     * variable
     */
    private final IVariable<?> m_variable;

    /**
     * ctor
     *
     * @param p_variable variable
     */
    @Nonnull
    public CDecrement( @Nonnull final IVariable<?> p_variable )
    {
        m_variable = p_variable;
    }

    @Override
    public String toString()
    {
        return m_variable.toString() + "--";
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final IVariable<Number> l_variable = CCommon.replacebycontext( p_context, m_variable ).<IVariable<Number>>term().thrownotallocated();
        if ( !l_variable.valueassignableto( Number.class ) )
            throw new CExecutionIllegealArgumentException(
                p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "variable must contains a number" ) );


        l_variable.set( l_variable.<Number>raw().doubleValue() - 1 );
        return Stream.of();
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Stream.of( m_variable );
    }
}
