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
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CNoSuchElementException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action generator lazy-loader
 */
public final class CActionGenerator implements IActionGenerator
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
     * agent classes with action
     */
    private final Set<Class<?>> m_classes;

    /**
     * ctor
     */
    public CActionGenerator()
    {
        this( Stream.of(), Stream.of() );
    }

    /**
     * ctor
     *
     * @param p_packages list of packages
     */
    public CActionGenerator( @NonNull final Stream<String> p_packages )
    {
        this( p_packages, Stream.of() );
    }

    /**
     * ctor
     *
     * @param p_packages list of packages
     * @param p_class list of agent classes
     */
    public CActionGenerator( @NonNull final Stream<String> p_packages, @NonNull final Stream<Class<?>> p_class )
    {
        m_packages = p_packages.collect( Collectors.toUnmodifiableSet() );
        m_classes = p_class.collect( Collectors.toUnmodifiableSet() );
    }


    @Override
    public IAction apply( @NonNull final IPath p_path )
    {
        // get action from cache
        if ( m_actions.containsKey( p_path ) )
            return m_actions.get( p_path );

        // searching within packages
        CCommon.actionsFromPackage( m_packages.isEmpty() ? null : m_packages.toArray( String[]::new ) )
               .filter( i -> i.name().equals( p_path ) )
               .forEach( i -> m_actions.putIfAbsent( i.name(), i ) );

        // searching with agent classes
        if ( !m_classes.isEmpty() )
            CCommon.actionsFromAgentClass( m_classes.toArray( Class<?>[]::new ) )
                   .filter( i -> i.name().equals( p_path ) )
                   .forEach( i -> m_actions.putIfAbsent( i.name(), i ) );

        if ( m_actions.containsKey( p_path ) )
            return m_actions.get( p_path );

        throw new CNoSuchElementException( CCommon.languagestring( this, "notfound", p_path ) );
    }
}
