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


import com.google.common.collect.SetMultimap;
import org.lightjason.agentspeak.agent.IAgent;

import java.util.Map;
import java.util.stream.Stream;


/**
 * interface of a beliefbase storage
 *
 * @tparam N multiple elements
 * @tparam M single elements
 * @tparam T agent type
 * @warning internal data structure must be thread-safe
 */
public interface IStorage<N, M, T extends IAgent<?>>
{

    /**
     * returns the map with multiple elements
     *
     * @return multimap
     * @deprecated replaced by individual calls
     */
    @Deprecated
    SetMultimap<String, N> getMultiElements();

    /**
     * returns the map with single elements
     *
     * @return map
     * @deprecated replaced by individual calls
     */
    @Deprecated
    Map<String, M> getSingleElements();

    /**
     * returns a stream over all multi-elements
     *
     * @return multi-element stream
     */
    Stream<N> streamMultiElements();

    /**
     * returns a stream over all single-elements
     *
     * @return single-element stream
     */
    Stream<M> streamSingleElements();


    /**
     * contains a multi-element
     *
     * @param p_key key
     * @return boolean existing flag
     */
    boolean containsMultiElement( final String p_key );

    /**
     * contains a single-element
     *
     * @param p_key key
     * @return boolean existing flag
     */
    boolean containsSingleElement( final String p_key );

    /**
     * puts a multi-element into the storage
     *
     * @param p_key key
     * @param p_value multi-element
     * @return boolean if the element can be stored
     */
    boolean putMultiElements( final String p_key, final N p_value );

    /**
     * puts a single-element into the storage
     *
     * @param p_key key
     * @param p_value single-element
     * @return boolean if the element can be stored
     */
    boolean putSingleElements( final String p_key, final M p_value );

    /**
     * removes a multi-element from the storage
     *
     * @param p_key key
     * @param p_value multi-element
     * @return boolean if the element can removed
     */
    boolean removeMultiElements( final String p_key, final N p_value );

    /**
     * removes a single-element from the storage
     *
     * @param p_key key
     * @return boolean if the element can be removed
     */
    boolean removeSingleElements( final String p_key );

    /**
     * returns a single-element by the name
     *
     * @param p_key key
     * @return single-element or null
     */
    M getSingleElement( final String p_key );

    /**
     * returns a single-element by the name
     *
     * @param p_key key
     * @return single-element or the default element
     */
    M getSingleElementOrDefault( final String p_key, final M p_default );

    /**
     * clears all elements
     */
    void clear();

    /**
     * checks any element exists
     *
     * @param p_key key name
     * @return exist boolean
     */
    boolean contains( final String p_key );

    /**
     * checks if a storage is empty
     *
     * @return empty boolean
     */
    boolean isEmpty();

    /**
     * updates all items
     *
     * @param p_agent agent which calls the update
     * @return agent
     */
    T update( final T p_agent );

    /**
     * number of multi elements
     */
    int size();

}
