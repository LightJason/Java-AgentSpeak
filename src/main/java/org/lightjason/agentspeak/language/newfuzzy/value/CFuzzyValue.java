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

package org.lightjason.agentspeak.language.newfuzzy.value;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.checkerframework.checker.index.qual.NonNegative;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;

import javax.annotation.Nonnull;


/**
 * fuzzy value
 *
 * @tparam T fuzzy type
 */
public final class CFuzzyValue<T extends Enum<?>> implements IFuzzyValue<T>
{
    /**
     * fuzzy type
     */
    private final T m_type;
    /**
     * fuzzy value
     */
    private final Number m_value;

    /**
     * ctor
     *
     * @param p_type fuzzy type
     * @param p_value fuzzy value
     */
    public CFuzzyValue( @NonNull final T p_type, @NonNull @NonNegative final Number p_value )
    {
        if ( p_value.doubleValue() < 0 || p_value.doubleValue() > 1 )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "valuerange", p_value )  );

        m_type = p_type;
        m_value = p_value;
    }

    @Nonnull
    @Override
    public T type()
    {
        return m_type;
    }

    @Override
    public Number fuzzy()
    {
        return m_value;
    }

    @Override
    public final int hashCode()
    {
        return m_type.hashCode() ^ m_value.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return p_object instanceof IFuzzyValue<?> && this.hashCode() == p_object.hashCode();
    }
}
