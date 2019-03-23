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

package org.lightjason.agentspeak.action.generic;

import java.io.Serializable;


/**
 * formating class
 *
 * @tparam any type
 */
public abstract class IBaseFormatter<T> implements Serializable, IFormatter
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4997526550642055213L;

    @Override
    public final int hashCode()
    {
        return this.get().hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return p_object instanceof IFormatter && this.hashCode() == p_object.hashCode();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final String apply( final Object p_data )
    {
        return this.format( (T) p_data );
    }

    /**
     * formatter call
     *
     * @param p_data object type
     * @return formatted string
     */
    protected abstract String format( final T p_data );
}
