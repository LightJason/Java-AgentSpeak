/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.language.execution.fuzzy;

import java.util.Arrays;


/**
 * mutable fuzzy boolean
 */
public final class CValueMutable<T> implements IFuzzyValueMutable<T>
{
    /**
     * value
     */
    private T m_value;
    private double m_fuzzy;



    @Override
    public final IFuzzyValueMutable<T> setValue( final T p_value )
    {
        m_value = p_value;
        return this;
    }

    @Override
    public final IFuzzyValueMutable<T> setFuzzy( final double p_value )
    {
        m_fuzzy = p_value;
        return this;
    }

    @Override
    public final IFuzzyValue<T> immutable()
    {
        return null;
    }

    @Override
    public final T getValue()
    {
        return m_value;
    }

    @Override
    public final double getFuzzy()
    {
        return m_fuzzy;
    }

    @Override
    public final boolean isValueAssignableTo( final Class<?>... p_class )
    {
        return m_value == null ? true : Arrays.asList( p_class ).stream().map( i -> i.isAssignableFrom( m_value.getClass() ) ).anyMatch( i -> i );
    }
}
