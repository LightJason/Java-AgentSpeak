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

package org.lightjason.agentspeak.language.fuzzy;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;

import java.util.Arrays;


/**
 * mutable fuzzy boolean
 */
public final class CFuzzyValueMutable<T> implements IFuzzyValueMutable<T>
{
    /**
     * value
     */
    private T m_value;
    /**
     * fuzzy value
     */
    private double m_fuzzy;

    /**
     * ctor
     *
     * @param p_value value
     */
    public CFuzzyValueMutable( final T p_value )
    {
        this( p_value, 1 );
    }

    /**
     * ctor
     *
     * @param p_value fuzzy value
     */
    public CFuzzyValueMutable( final IFuzzyValue<T> p_value )
    {
        m_value = p_value.value();
        m_fuzzy = p_value.fuzzy();
    }

    /**
     * ctor
     *
     * @param p_value value
     * @param p_fuzzy fuzzy value
     */
    public CFuzzyValueMutable( final T p_value, final double p_fuzzy )
    {
        if ( !( ( p_fuzzy >= 0 ) && ( p_fuzzy <= 1 ) ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "fuzzyvalue", p_value ) );

        m_value = p_value;
        m_fuzzy = p_fuzzy;
    }

    @Override
    public final IFuzzyValueMutable<T> value( final T p_value )
    {
        m_value = p_value;
        return this;
    }

    @Override
    public final IFuzzyValueMutable<T> fuzzy( final double p_value )
    {
        if ( !( ( p_value >= 0 ) && ( p_value <= 1 ) ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "fuzzyvalue", p_value ) );
        m_fuzzy = p_value;
        return this;
    }

    @Override
    public final IFuzzyValue<T> immutable()
    {
        return CFuzzyValue.<T>from( m_value, m_fuzzy );
    }

    @Override
    public final T value()
    {
        return m_value;
    }

    @Override
    public final double fuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final boolean valueAssignableTo( final Class<?>... p_class )
    {
        return m_value == null || Arrays.stream( p_class ).map( i -> i.isAssignableFrom( m_value.getClass() ) ).anyMatch( i -> i );
    }

    /**
     * factory
     *
     * @param p_value value
     * @return fuzzy value
     *
     * @tparam N fuzzy type
     */
    public static <N> IFuzzyValueMutable<N> from( final N p_value )
    {
        return new CFuzzyValueMutable<N>( p_value );
    }

    /**
     * factory
     *
     * @param p_value value
     * @param p_fuzzy fuzzy value
     * @return fuzzy value
     *
     * @tparam N fuzzy type
     */
    public static <N> IFuzzyValueMutable<N> from( final N p_value, final double p_fuzzy )
    {
        return new CFuzzyValueMutable<N>( p_value, p_fuzzy );
    }

}
