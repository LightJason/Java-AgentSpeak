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

package org.lightjason.agentspeak.generator;


import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * lambda-streaming generator lazy-loader
 */
public final class CLambdaStreamingGenerator implements ILambdaStreamingGenerator
{
    /**
     * loaded lambdas
     */
    private final Map<IPath, IAction> m_actions = new ConcurrentHashMap<>();
    /**
     * Java package for searching
     */
    private final Set<String> m_packages;


    /**
     * ctor
     */
    public CLambdaStreamingGenerator()
    {
        this( Stream.of() );
    }

    /**
     * ctor
     *
     * @param p_packages
     */
    public CLambdaStreamingGenerator( @NonNull final Stream<String> p_packages )
    {
        m_packages = p_packages.collect( Collectors.toUnmodifiableSet() );
    }

    @Override
    public ILambdaStreaming<?> apply( @NonNull final IPath p_path )
    {
        return null;
    }
}
