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

package org.lightjason.agentspeak.action.builtin.collection.multimap;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.builtin.collection.IMapApplySingle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * adds an single element to multiple multimap arguments.
 * First argument is a key, second the value, all
 * other values are multimap references, the key-value pair
 * is added to all multimaps and the action never fails
 *
 * @code collection/multimap/putsingle( "key", "value", MultiMap1, MultiMap2 ); @endcode
 */
public final class CPutSingle extends IMapApplySingle<Multimap<Object, Object>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7300831158175726919L;

    @Override
    protected final void apply( @Nonnull final Multimap<Object, Object> p_instance, @Nonnull final Object p_key, @Nullable final Object p_value )
    {
        p_instance.put( p_key, p_value );
    }
}
