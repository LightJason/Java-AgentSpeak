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

package org.lightjason.agentspeak.action.builtin.map.map;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IBaseLambdaStreaming;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Stream;


/**
 * streaming of a map
 */
public final class CLambdaStreaming extends IBaseLambdaStreaming<Map<?, ?>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8776927369080417712L;

    @Override
    public Stream<?> apply( @Nonnull final Map<?, ?> p_map )
    {
        return p_map.entrySet().stream();
    }

    @NonNull
    @Override
    public Stream<Class<?>> assignable()
    {
        return Stream.of( AbstractMap.class, Map.class );
    }
}
