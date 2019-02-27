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

package org.lightjason.agentspeak.action.builtin.map.multimap;

import com.google.common.collect.Multimap;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.antlr.v4.runtime.misc.MultiMap;
import org.lightjason.agentspeak.action.IBaseLambdaStreaming;

import javax.annotation.Nonnull;
import java.util.stream.Stream;


/**
 * streaming of a multimap
 */
public final class CLambdaStreaming extends IBaseLambdaStreaming<Multimap<?, ?>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -9088776927360487769L;

    @Override
    public Stream<?> apply( @Nonnull final Multimap<?, ?> p_multimap )
    {
        return p_multimap.asMap().entrySet().stream();
    }

    @NonNull
    @Override
    public Stream<Class<?>> assignable()
    {
        return Stream.of( MultiMap.class );
    }
}
