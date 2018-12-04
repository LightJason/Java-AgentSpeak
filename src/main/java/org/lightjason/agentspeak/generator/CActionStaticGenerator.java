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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action lazy-loader static generator
 */
public final class CActionStaticGenerator implements IActionGenerator
{
    /**
     * loaded actions
     */
    private final Map<IPath, IAction> m_actions;


    /**
     * ctor
     */
    public CActionStaticGenerator()
    {
        m_actions = Collections.emptyMap();
    }

    /**
     * ctor
     *
     * @param p_actions action set
     */
    public CActionStaticGenerator( @NonNull final Collection<IAction> p_actions )
    {
        this( p_actions.stream() );
    }

    /**
     * ctor
     *
     * @param p_actions action stream
     */
    public CActionStaticGenerator( @NonNull final Stream<IAction> p_actions )
    {
        m_actions = Collections.unmodifiableMap( p_actions.collect( Collectors.toMap( IAction::name, i -> i ) ) );
    }

    @Override
    public IAction apply( @NonNull final IPath p_path )
    {
        if ( m_actions.containsKey( p_path ) )
            return m_actions.get( p_path );

        throw new CNoSuchElementException( CCommon.languagestring( this, "notfound", p_path ) );
    }
}
