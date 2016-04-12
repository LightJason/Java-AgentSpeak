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

package lightjason.language.execution.annotation;


import java.util.Arrays;


/**
 * annotation base
 *
 * @tparam T annotation data
 */
public abstract class IBaseAnnotation<T> implements IAnnotation<T>
{
    /**
     * number data
     */
    protected final T m_value;
    /**
     * annotation type
     */
    protected final EType m_type;

    /**
     * ctor
     *
     * @param p_type type
     * @param p_value data
     */
    protected IBaseAnnotation( final EType p_type, final T p_value )
    {
        m_value = p_value;
        m_type = p_type;
    }

    @Override
    public final int hashCode()
    {
        return m_type.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final EType getID()
    {
        return m_type;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final <N> N getValue()
    {
        return (N) m_value;
    }

    @Override
    public final boolean isValueAssignableTo( final Class<?>... p_class )
    {
        return m_value == null ? true : Arrays.asList( p_class ).stream().map( i -> i.isAssignableFrom( m_value.getClass() ) ).anyMatch( i -> i );
    }
}
