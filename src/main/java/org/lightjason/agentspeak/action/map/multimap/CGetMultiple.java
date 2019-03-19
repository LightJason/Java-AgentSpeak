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

package org.lightjason.agentspeak.action.map.multimap;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.map.IMapGetMultiple;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * returns a multiple element of a single multimap.
 * The first argument is a multimap reference and all
 * other arguments are key values, the action
 * returns the value of each key
 *
 * {@code [V1|V2] = .collection/multimap/getmultiple( MultiMap, "key1", "key2" );}
 */
public final class CGetMultiple extends IMapGetMultiple<Multimap<Object, Object>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2745391712791242535L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CGetMultiple.class, "collection", "multimap" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Override
    protected void apply( final boolean p_parallel, @Nonnull final Multimap<Object, Object> p_instance,
                          @Nonnull final Object p_key, @Nonnull final List<ITerm> p_return
    )
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
