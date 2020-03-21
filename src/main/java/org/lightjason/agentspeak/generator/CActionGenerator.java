/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action lazy-loader generator
 */
public final class CActionGenerator extends IBaseActionGenerator
{

    /**
     * Java package for searching
     */
    private final Set<String> m_packages;
    /**
     * agent classes with action
     */
    private final Set<Class<? extends IAgent<?>>> m_classes;

    /**
     * ctor
     */
    public CActionGenerator()
    {
        super();
        m_packages = Collections.emptySet();
        m_classes = Collections.emptySet();
    }

    /**
     * ctor
     *
     * @param p_packages list of packages
     */
    public CActionGenerator( @NonNull final Stream<String> p_packages )
    {
        this( p_packages, Stream.empty() );
    }

    /**
     * ctor
     *
     * @param p_packages list of packages
     * @param p_class list of agent classes
     */
    public CActionGenerator( @NonNull final Stream<String> p_packages, @NonNull final Stream<Class<? extends IAgent<?>>> p_class )
    {
        super( Stream.empty() );
        m_packages = p_packages.collect( Collectors.toUnmodifiableSet() );
        m_classes = p_class.collect( Collectors.toUnmodifiableSet() );
    }

    /**
     * streams over all actions
     *
     * @param p_path action name
     * @return optional action object
     */
    protected Optional<IAction> stream( @Nonnull final IPath p_path )
    {
        return Stream.concat(
            CCommon.actionsFromPackage( m_packages.stream() ),
            CCommon.actionsFromAgentClass( m_classes.stream() )
        ).parallel().filter( i -> i.name().equals( p_path ) ).findFirst();
    }

}
