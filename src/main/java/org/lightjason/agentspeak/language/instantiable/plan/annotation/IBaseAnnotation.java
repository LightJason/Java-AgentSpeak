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

package org.lightjason.agentspeak.language.instantiable.plan.annotation;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    protected IBaseAnnotation( @Nonnull final EType p_type, @Nullable final T p_value )
    {
        m_value = p_value;
        m_type = p_type;
    }

    @Nonnull
    @Override
    public final EType id()
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
    public final boolean valueassignableto( @Nonnull final Class<?>... p_class )
    {
        return m_value == null || Arrays.stream( p_class ).anyMatch( i -> i.isAssignableFrom( m_value.getClass() ) );
    }

    @Nullable
    @Override
    public final T throwvaluenotassignableto( @Nonnull final Class<?>... p_class ) throws IllegalArgumentException
    {
        if ( !this.valueassignableto( p_class ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "notassignable", Arrays.asList( p_class ) ) );

        return m_value;
    }
}
