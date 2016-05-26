/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.beliefbase;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import org.lightjason.agentspeak.agent.IAgent;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * thread-safe storage of the data
 *
 * @tparam N multi-element type
 * @tparam M single-element type
 * @tparam T agent type
 */
public final class CStorage<N, M, T extends IAgent<?>> implements IStorage<N, M, T>
{
    /**
     * map with elements
     **/
    private final SetMultimap<String, N> m_multielements = Multimaps.synchronizedSetMultimap( HashMultimap.create() );
    /**
     * map with single elements
     **/
    private final Map<String, M> m_singleelements = new ConcurrentHashMap<>();
    /**
     * beliefbase update object
     */
    private final IBeliefBaseUpdate<T> m_update;

    /**
     * ctor
     */
    public CStorage()
    {
        this( null );
    }

    /**
     * ctor
     *
     * @param p_update update object
     */
    public CStorage( final IBeliefBaseUpdate<T> p_update )
    {
        m_update = p_update;
    }


    @Override
    public final SetMultimap<String, N> getMultiElements()
    {
        return m_multielements;
    }

    @Override
    public final Map<String, M> getSingleElements()
    {
        return m_singleelements;
    }

    @Override
    public final void clear()
    {
        m_multielements.clear();
        m_singleelements.clear();
    }

    @Override
    public final boolean contains( final String p_key )
    {
        return m_multielements.containsKey( p_key ) || m_singleelements.containsKey( p_key );
    }

    @Override
    public final boolean isEmpty()
    {
        return m_multielements.isEmpty() && m_singleelements.isEmpty();
    }

    @Override
    public final T update( final T p_agent )
    {
        return m_update != null ? m_update.beliefupdate( p_agent ) : p_agent;
    }

    @Override
    public final int size()
    {
        return m_multielements.asMap().values().stream().mapToInt( i -> i.size() ).sum() + m_singleelements.size();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "[multi elements: {0}, single elements: {1}]", m_multielements, m_singleelements );
    }
}
