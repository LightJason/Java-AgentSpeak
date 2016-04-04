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


import com.google.common.collect.SetMultimap;
import lightjason.agent.IAgent;

import java.util.Map;


/**
 * interface of a beliefbase storage
 *
 * @tparam N multiple elements
 * @tparam M single elements
 * @warning internal data structure must be thread-safe
 */
public interface IStorage<N, M>
{

    /**
     * returns the map with multiple elements
     *
     * @return multimap
     */
    SetMultimap<String, N> getMultiElements();

    /**
     * returns the map with single elements
     *
     * @return map
     */
    Map<String, M> getSingleElements();

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
     */
    void update( final IAgent p_agent );

    /**
     * number of multi elements
     */
    int size();

}
