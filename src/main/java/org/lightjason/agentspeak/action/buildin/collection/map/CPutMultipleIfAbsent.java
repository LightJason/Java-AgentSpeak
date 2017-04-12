/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.collection.map;

import org.lightjason.agentspeak.action.buildin.collection.IMapApplyMultiple;

import java.util.Map;


/**
 * adds multiple element to a single map iif not exists.
 * First argument is a map and all other arguments
 * are key-value pairs, all pars are added to the map,
 * the action fails on wrong input number
 *
 * @code collection/map/putmultipleifabsent( Map, Key1, Value1, [Key2, Value2]); @endcode
 */
public final class CPutMultipleIfAbsent extends IMapApplyMultiple<Map<Object, Object>>
{
    @Override
    protected final void apply( final Map<Object, Object> p_instance, final Object p_key, final Object p_value )
    {
        p_instance.putIfAbsent( p_key, p_value );
    }
}
