/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.beliefbase;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * thread-safe storage of the data
 *
 * @tparam N element type
 * @tparam M mask type
 */
@SuppressWarnings( "serial" )
public class CBeliefStorage<N, M> implements IStorage<N, M>
{
    /**
     * map with elements
     **/
    protected final SetMultimap<String, N> m_multielements = Multimaps.synchronizedSetMultimap( HashMultimap.create() );
    /**
     * map with masks
     **/
    protected final Map<String, M> m_singleelements = new ConcurrentHashMap<>();

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
    public void clear()
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
    public void update()
    {
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
