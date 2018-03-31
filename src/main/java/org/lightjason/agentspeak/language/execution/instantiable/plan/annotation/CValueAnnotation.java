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

package org.lightjason.agentspeak.language.execution.instantiable.plan.annotation;

import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.stream.Stream;


/**
 * number annotation
 */
public final class CValueAnnotation<T> extends IBaseAnnotation<T>
{
    /**
     * name of the annotation
     */
    private final String m_name;

    /**
     * ctor
     *
     * @param p_type type
     * @param p_name name of the annotation
     * @param p_data number
     */
    public CValueAnnotation( @Nonnull final EType p_type, @Nonnull final String p_name, final T p_data )
    {
        super( p_type, p_data );
        m_name = p_name;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}({1}, {2})", m_type, m_name, m_value );
    }

    @Override
    public final int hashCode()
    {
        return m_type.hashCode() ^ m_name.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object instanceof IAnnotation<?> ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.of( new CConstant<>( m_name, m_value ) );
    }
}
