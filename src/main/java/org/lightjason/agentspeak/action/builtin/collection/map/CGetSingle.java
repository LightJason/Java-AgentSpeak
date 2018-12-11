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

import org.lightjason.agentspeak.action.builtin.collection.IMapGetSingle;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;


/**
 * returns a single element of all map elements.
 * The first argument will be used as key and all
 * arguments are map references, the key will be
 * returned of each map
 *
 * {@code [A|B|C] = .collection/map/getsingle( "key", Map1, Map2, Map3 );}
 */
public final class CGetSingle extends IMapGetSingle<Map<Object, Object>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5486345873377172889L;

    @Override
    protected void apply( final boolean p_parallel, @Nonnull final Map<Object, Object> p_instance,
                          @Nonnull final Object p_key, @Nonnull final List<ITerm> p_return )
    {
        p_return.add(
            CRawTerm.of(
                p_instance.get( p_key )
            )
        );
    }
}
