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

import javax.annotation.Nonnull;
import java.text.MessageFormat;


/**
 * fuzzy value
 *
 * @tparam T fuzzy type
 *
 * @todo range checking
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
    public CFuzzyValue( @NonNull final T p_type, @NonNull final Number p_value )
    {
        m_type = p_type;
        m_value = p_value;
    }

    @Nonnull
    @Override
    public T type()
    {
        return m_type;
    }

    @NonNull
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

    @Override
    public IFuzzyValue<T> apply( final Number p_number )
    {
        return new CFuzzyValue<>( m_type, p_number );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}[{1}]", m_type, m_value );
    }
}
