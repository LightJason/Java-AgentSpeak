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

package org.lightjason.agentspeak.language.fuzzy.membership;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * membership which represent a strict crisp structure
 *
 * @tparam E fuzzy enum
 */
public final class CCrisp<E extends Enum<?>> implements IFuzzyMembership<E>
{
    /**
     * enum values
     */
    private final E[] m_values;
    /**
     * evan / odd element flag
     */
    private final boolean m_evan;
    /**
     * index value of the middle
     */
    private final int m_splitindex;


    /**
     * ctor
     *
     * @param p_type enum class type
     */
    public CCrisp( final Class<E> p_type )
    {
        m_values = p_type.getEnumConstants();
        m_evan = m_values.length % 2 == 0;
        m_splitindex = m_values.length / 2;
    }

    @NonNull
    @Override
    public Stream<IFuzzyValue<?>> success()
    {
        return m_evan
               ? Stream.concat(
                   IntStream.range( 0, m_splitindex ).mapToObj( i -> CFuzzyValue.of( m_values[i], 0 ) ),
                   IntStream.range( m_splitindex, m_values.length ).mapToObj( i -> CFuzzyValue.of( m_values[i], 1 ) )
                 )
               : Stream.of();
    }

    @NonNull
    @Override
    public Stream<IFuzzyValue<?>> fail()
    {
        return m_evan
               ? Stream.concat(
                    IntStream.range( 0, m_splitindex ).mapToObj( i -> CFuzzyValue.of( m_values[i], 1 ) ),
                    IntStream.range( m_splitindex, m_values.length ).mapToObj( i -> CFuzzyValue.of( m_values[i], 0 ) )
                )
               : Stream.of();
    }

    @Override
    public Stream<Number> range( @NonNull final E p_value )
    {
        // linear interpolation on the middle value on odd elements
        return m_evan
               ? Stream.of( 1 )
               : Stream.of();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends Enum<?>> IFuzzyMembership<T> raw()
    {
        return (IFuzzyMembership<T>) this;
    }

    @Override
    public Stream<IFuzzyValue<?>> apply( final Number p_number )
    {
        return p_number.doubleValue() <= 0.5
               ? this.fail()
               : this.success();
    }

    @Nonnull
    @Override
    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return p_agent;
    }
}
