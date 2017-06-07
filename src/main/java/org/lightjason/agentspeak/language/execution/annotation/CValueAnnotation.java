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

package org.lightjason.agentspeak.language.execution.annotation;

import java.text.MessageFormat;


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
     * @param p_data number
     */
    public CValueAnnotation( final EType p_type, final String p_name, final T p_data )
    {
        super( p_type, p_data );
        m_name = p_name;
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}({1}, {1})", m_type, m_name, m_value );
    }

}
