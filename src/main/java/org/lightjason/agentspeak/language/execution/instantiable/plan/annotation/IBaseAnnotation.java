/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.execution.instantiable.plan.annotation;

import org.lightjason.agentspeak.error.CTypeNotAssignable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;


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
    protected final EAnnotation m_type;

    /**
     * ctor
     *
     * @param p_type type
     * @param p_value data
     */
    protected IBaseAnnotation( @Nonnull final EAnnotation p_type, @Nullable final T p_value )
    {
        m_value = p_value;
        m_type = p_type;
    }

    @Nonnull
    @Override
    public final EAnnotation id()
    {
        return m_type;
    }

    @Nullable
    @Override
    @SuppressWarnings( "unchecked" )
    public final <N> N value()
    {
        return (N) m_value;
    }

    @Override
    public final boolean valueassignableto( @Nonnull final Class<?> p_class )
    {
        return Objects.isNull( m_value ) || p_class.isAssignableFrom( m_value.getClass() );
    }

    @Nullable
    @Override
    public final T throwvaluenotassignableto( @Nonnull final Class<?> p_class ) throws TypeNotPresentException
    {
        if ( !this.valueassignableto( p_class ) )
            throw new CTypeNotAssignable( p_class );

        return m_value;
    }
}
