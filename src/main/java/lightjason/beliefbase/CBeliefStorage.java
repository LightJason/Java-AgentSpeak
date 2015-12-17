/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.beliefbase;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


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
    public void addMultiElement( final String p_key, final N p_element )
    {
        m_multielements.put( p_key, p_element );
    }

    @Override
    public void addSingleElement( final String p_key, final M p_element )
    {
        m_singleelements.put( p_key, p_element );
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
    public final boolean containsMultiElement( final String p_key )
    {
        return m_multielements.containsKey( p_key );
    }

    @Override
    public final boolean containsSingleElement( String p_key )
    {
        return m_singleelements.containsKey( p_key );
    }

    @Override
    public final Set<N> getMultiElement( final String p_key )
    {
        return m_multielements.get( p_key );
    }

    @Override
    public final M getSingleElement( final String p_key )
    {
        return m_singleelements.get( p_key );
    }

    @Override
    public final boolean isEmpty()
    {
        return m_multielements.isEmpty() && m_singleelements.isEmpty();
    }

    @Override
    public boolean remove( final String p_key )
    {
        return ( m_singleelements.remove( p_key ) != null ) || ( m_multielements.removeAll( p_key ) != null );
    }

    @Override
    public boolean removeMultiElement( final String p_key, final N p_element )
    {
        return m_multielements.remove( p_key, p_element );
    }

    @Override
    public boolean removeSingleElement( final String p_key )
    {
        return m_singleelements.remove( p_key ) != null;
    }

    @Override
    public void update()
    {
    }

    @Override
    public final int sizeMultiElement()
    {
        return m_multielements.asMap().values().stream().mapToInt( i -> i.size() ).sum();
    }

    @Override
    public final int sizeSingleElement()
    {
        return m_singleelements.size();
    }

    @Override
    public final int size()
    {
        return this.sizeMultiElement() + this.sizeSingleElement();
    }

    @Override
    public Iterator<N> iteratorMultiElement()
    {
        return new Iterator<N>()
        {
            /**
             * stack with all iterators
             **/
            private final List<Iterator<N>> m_iterator = m_multielements.asMap().values().stream().map( i -> i.iterator() ).collect( Collectors.toList() );

            @Override
            public boolean hasNext()
            {
                if ( m_iterator.isEmpty() )
                    return false;

                if ( m_iterator.get( 0 ).hasNext() )
                    return true;

                m_iterator.remove( 0 );
                return this.hasNext();
            }

            @Override
            public N next()
            {
                return m_iterator.get( 0 ).next();
            }
        };
    }

    @Override
    public Iterator<M> iteratorSingleElement()
    {
        return m_singleelements.values().iterator();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "[multi elements: {0}, single elements: {1}]", m_multielements, m_singleelements );
    }
}
