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

package org.lightjason.agentspeak.action.buildin.collection.map;

import org.lightjason.agentspeak.action.buildin.collection.IMapGetSingle;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.List;
import java.util.Map;


/**
 * returns a single element of all map elements.
 * The first argument will be used as key and all
 * arguments are map references, the key will be
 * returned from each map, the action fails never,
 *
 * @code [A|B|C] = collection/map/getsingle( "key", Map1, Map2, Map3 ); @endcode
 */
public final class CGetSingle extends IMapGetSingle<Map<Object, Object>>
{

    @Override
    protected final void apply( final Map<Object, Object> p_instance, final Object p_key, final boolean p_parallel, final List<ITerm> p_return )
    {
        p_return.add(
            CRawTerm.from(
                p_instance.get( p_key )
            )
        );
    }
}
