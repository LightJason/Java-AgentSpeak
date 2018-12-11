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

package org.lightjason.agentspeak.action.builtin.collection.multimap;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.builtin.collection.IMapGetSingle;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * returns a single element of all multimap elements.
 * The first argument will be used as key and all
 * arguments are multimap references, the key will be
 * returned of each multimap
 *
 * {@code [A|B|C] = .collection/multimap/getsingle( "key", MultiMap1, MultiMap2, MultiMap3 );}
 */
public final class CGetSingle extends IMapGetSingle<Multimap<Object, Object>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 2277559384526092314L;

    @Override
    protected void apply( final boolean p_parallel, @Nonnull final Multimap<Object, Object> p_instance,
                          @Nonnull final Object p_key, @Nonnull final List<ITerm> p_return )
    {
        p_return.add(
            CRawTerm.of(
                p_parallel
                ? Collections.synchronizedList( new ArrayList<>( p_instance.asMap().get( p_key ) ) )
                : new ArrayList<>( p_instance.asMap().get( p_key ) )
            )
        );
    }

}
