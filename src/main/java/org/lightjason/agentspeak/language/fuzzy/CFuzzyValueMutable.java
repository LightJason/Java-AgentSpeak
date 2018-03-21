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

package org.lightjason.agentspeak.language.fuzzy;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;


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
    public CFuzzyValueMutable( @Nonnull final T p_value )
    {
        this( p_value, 1 );
    }

    /**
     * ctor
     *
     * @param p_value fuzzy value
     */
    public CFuzzyValueMutable( @Nonnull final IFuzzyValue<T> p_value )
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
    public CFuzzyValueMutable( @Nonnull final T p_value, final double p_fuzzy )
    {
        if ( !( ( p_fuzzy >= 0 ) && ( p_fuzzy <= 1 ) ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "fuzzyvalue", p_value ) );

        m_value = p_value;
        m_fuzzy = p_fuzzy;
    }

    @Nonnull
    @Override
    public final IFuzzyValueMutable<T> value( @Nullable final T p_value )
    {
        m_value = p_value;
        return this;
    }

    @Nonnull
    @Override
    public final IFuzzyValueMutable<T> fuzzy( final double p_value )
    {
        if ( !( ( p_value >= 0 ) && ( p_value <= 1 ) ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "fuzzyvalue", p_value ) );
        m_fuzzy = p_value;
        return this;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<T> immutable()
    {
        return CFuzzyValue.<T>of( m_value, m_fuzzy );
    }

    @Nonnull
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
    public final boolean valueassignableto( @Nonnull final Class<?>... p_class )
    {
        return Objects.isNull( m_value ) || Arrays.stream( p_class ).anyMatch( i -> i.isAssignableFrom( m_value.getClass() ) );
    }

    @Nullable
    @Override
    public final T throwvaluenotassignableto( @Nonnull final Class<?>... p_class ) throws IllegalArgumentException
    {
        if ( !this.valueassignableto( p_class ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "notassignable", Arrays.asList( p_class ) ) );

        return m_value;
    }

    /**
     * factory
     *
     * @param p_value value
     * @return fuzzy value
     *
     * @tparam N fuzzy type
     */
    @Nonnull
    public static <N> IFuzzyValueMutable<N> of( @Nonnull final N p_value )
    {
        return new CFuzzyValueMutable<>( p_value );
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
    @Nonnull
    public static <N> IFuzzyValueMutable<N> of( @Nonnull final N p_value, final double p_fuzzy )
    {
        return new CFuzzyValueMutable<>( p_value, p_fuzzy );
    }

}
