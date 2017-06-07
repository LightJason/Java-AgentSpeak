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

import java.text.MessageFormat;
import java.util.Arrays;


/**
 * immutable fuzzy value
 */
public final class CFuzzyValue<T> implements IFuzzyValue<T>
{
    /**
     * value
     */
    private final T m_value;
    /**
     * fuzzy value
     */
    private final double m_fuzzy;

    /**
     * ctor
     *
     * @param p_value fuzzy value
     */
    public CFuzzyValue( final IFuzzyValue<T> p_value )
    {
        this( p_value.value(), p_value.fuzzy() );
    }

    /**
     * ctor
     *
     * @param p_value value
     */
    public CFuzzyValue( final T p_value )
    {
        this( p_value, 1 );
    }

    /**
     * ctor
     *
     * @param p_value value
     * @param p_fuzzy fuzzy
     */
    public CFuzzyValue( final T p_value, final double p_fuzzy )
    {
        if ( !( ( p_fuzzy >= 0 ) && ( p_fuzzy <= 1 ) ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "fuzzyvalue", p_fuzzy ) );

        m_fuzzy = p_fuzzy;
        m_value = p_value;
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

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}({1})", m_value, m_fuzzy );
    }

    /**
     * factory
     *
     * @param p_value value
     * @return fuzzy value
     *
     * @tparam N fuzzy type
     */
    public static <N> IFuzzyValue<N> from( final N p_value )
    {
        return new CFuzzyValue<>( p_value );
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
    public static <N> IFuzzyValue<N> from( final N p_value, final double p_fuzzy )
    {
        return new CFuzzyValue<>( p_value, p_fuzzy );
    }
}
