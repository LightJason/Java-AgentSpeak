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

package org.lightjason.agentspeak.action.buildin.collection.multimap;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.buildin.collection.IMapApplyMultiple;

/**
 * adds all elements to a single multimap argument.
 * First argument is a multimap and all other arguments
 * are key-value pairs, the action fails on wrong
 * input number
 *
 * @code collection/multimap/putmultiple( MultiMap, Key1, Value1, [Key2, Value2] ); @endcode
 */
public final class CPutMultiple extends IMapApplyMultiple<Multimap<Object, Object>>
{

    @Override
    protected void apply( final Multimap<Object, Object> p_instance, final Object p_key, final Object p_value )
    {
        p_instance.put( p_key, p_value );
    }

}
