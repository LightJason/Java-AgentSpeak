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

package org.lightjason.agentspeak.configuration;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.IPath;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * default action configuration lazy-loader
 */
public final class CDefaultActionConfiguration implements IActionConfiguration
{
    /**
     * loaded actions
     */
    private final Map<IPath, IAction> m_actions = new ConcurrentHashMap<>();
    /**
     * Java package for searching
     */
    private final Set<String> m_packages;
    /**
     * agent classes for action
     */
    private final Set<Class<?>> m_agentclasses;

    public CDefaultActionConfiguration( @NonNull final Stream<String> p_packages, @NonNull final Stream<Class<?>> p_agentclass )
    {
        m_packages = p_packages.collect( Collectors.toUnmodifiableSet() );
        m_agentclasses = p_agentclass.collect( Collectors.toUnmodifiableSet() );
    }



    @Override
    public IAction apply( @NonNull final IPath p_path )
    {
        // get action from cache
        if (m_actions.containsKey( p_path ))
            return m_actions.get( p_path );




        return null;
    }
}
