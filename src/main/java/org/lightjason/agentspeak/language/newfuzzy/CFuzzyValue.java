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

package org.lightjason.agentspeak.language.newfuzzy;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;

import javax.annotation.Nonnull;
import java.text.MessageFormat;


/**
 * immutable fuzzy value
 */
public final class CFuzzyValue<E extends Enum<?>> implements IFuzzyValue<E>
{
    /**
     * value
     */
    private final E m_value;
    /**
     * fuzzy value
     */
    private final Number m_fuzzy;

    /**
     * ctor
     *
     * @param p_value value
     * @param p_fuzzy fuzzy
     */
    public CFuzzyValue( @Nonnull final E p_value, @NonNull final Number p_fuzzy )
    {
        if ( !( p_fuzzy.doubleValue() >= 0 && p_fuzzy.doubleValue() <= 1 ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "fuzzyvalue", p_fuzzy ) );

        m_fuzzy = p_fuzzy;
        m_value = p_value;
    }

    @Nonnull
    @Override
    public E get()
    {
        return m_value;
    }

    @NonNull
    @Override
    public Number fuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})", m_value, m_fuzzy );
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
    public static <N extends Enum<?>> IFuzzyValue<N> of( @Nonnull final N p_value, @NonNull final Number p_fuzzy )
    {
        return new CFuzzyValue<>( p_value, p_fuzzy );
    }

}

