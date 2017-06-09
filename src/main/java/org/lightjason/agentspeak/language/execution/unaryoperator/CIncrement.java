/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.unaryoperator;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;


/**
 * unary increment
 */
public final class CIncrement<T extends Number> implements IOperator<T>
{
    /**
     * variable
     */
    private final IVariable<T> m_variable;

    /**
     * ctor
     *
     * @param p_variable variable
     */
    @Nonnull
    public CIncrement( @Nonnull final IVariable<T> p_variable )
    {
        m_variable = p_variable;
    }

    @Override
    public final String toString()
    {
        return m_variable.toString() + "++";
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final IVariable<T> l_variable = ( (IVariable<T>) CCommon.replaceFromContext( p_context, m_variable ) ).throwNotAllocated();

        if ( l_variable.valueAssignableTo( Double.class ) )
            l_variable.set( (T) Double.valueOf( l_variable.<Number>raw().doubleValue() + 1 ) );
        if ( l_variable.valueAssignableTo( Long.class ) )
            l_variable.set( (T) Long.valueOf( l_variable.<Number>raw().longValue() + 1 ) );
        if ( l_variable.valueAssignableTo( Float.class ) )
            l_variable.set( (T) Float.valueOf( l_variable.<Number>raw().floatValue() + 1 ) );
        if ( l_variable.valueAssignableTo( Integer.class ) )
            l_variable.set( (T) Integer.valueOf( l_variable.<Number>raw().intValue() + 1 ) );

        return CFuzzyValue.from( true );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.of( m_variable );
    }

}
