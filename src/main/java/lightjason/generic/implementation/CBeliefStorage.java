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

package lightjason.generic.implementation;


import lightjason.generic.IStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


/**
 * non-thread-safe storage of the data
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
    protected final Map<String, Set<N>> m_multielements = new HashMap<>();
    /**
     * map with masks
     **/
    protected final Map<String, M> m_singleelements = new HashMap<>();


    @Override
    public void addMultiElement( final String p_key, final N p_element )
    {
        final Set<N> l_element;

        if ( m_multielements.containsKey( p_key ) )
            l_element = m_multielements.get( p_key );
        else
        {
            l_element = new HashSet<>();
            m_multielements.put( p_key, l_element );
        }

        l_element.add( p_element );
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
        final Set<N> l_elements = m_multielements.get( p_key );
        if ( l_elements == null )
            return false;

        return !l_elements.isEmpty();
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
        return ( m_singleelements.remove( p_key ) != null ) || ( m_multielements.remove( p_key ) != null );
    }

    @Override
    public boolean removeMultiElement( final String p_key, final N p_element )
    {
        final Set<N> l_element = m_multielements.get( p_key );
        if ( l_element == null )
            return false;

        return l_element.remove( p_element );
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
    public int sizeMultiElement()
    {
        int l_sum = 0;
        for ( final Set<N> l_item : m_multielements.values() )
            l_sum += l_item.size();
        return 0;
    }

    @Override
    public int sizeSingleElement()
    {
        return m_multielements.size();
    }

    @Override
    public int size()
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
            private final Stack<Iterator<N>> m_stack = new Stack<Iterator<N>>()
            {{
                for ( final Set<N> l_item : m_multielements.values() )
                    add( l_item.iterator() );
            }};

            @Override
            public boolean hasNext()
            {
                if ( m_stack.isEmpty() )
                    return false;

                if ( m_stack.peek().hasNext() )
                    return true;

                m_stack.pop();
                return this.hasNext();
            }

            @Override
            public N next()
            {
                return m_stack.peek().next();
            }
        };
    }

    @Override
    public Iterator<M> iteratorSingleElement()
    {
        return m_singleelements.values().iterator();
    }

    @Override
    public String toString()
    {
        return "{ multi elements : " + m_multielements + ", single elements : " + m_singleelements + " }";
    }
}
