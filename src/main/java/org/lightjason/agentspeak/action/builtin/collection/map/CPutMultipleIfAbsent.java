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

package org.lightjason.agentspeak.action.builtin.collection.map;

import org.lightjason.agentspeak.action.builtin.collection.IMapApplyMultiple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;


/**
 * adds multiple element to a single map iif not exists.
 * First argument is a map and all other arguments
 * are key-value pairs, all pars are added to the map
 *
 * {@code .collection/map/putmultipleifabsent( Map, Key1, Value1, [Key2, Value2]);}
 */
public final class CPutMultipleIfAbsent extends IMapApplyMultiple<Map<Object, Object>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 6020101779012749616L;

    @Override
    protected void apply( @Nonnull final Map<Object, Object> p_instance, @Nonnull final Object p_key, @Nullable final Object p_value )
    {
        p_instance.putIfAbsent( p_key, p_value );
    }
}
