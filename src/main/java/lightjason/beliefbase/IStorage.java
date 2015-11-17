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


import java.util.Iterator;
import java.util.Set;


/**
 * interface of a beliefbase storage
 *
 * @tparam N multiple elements
 * @tparam M single elements
 */
public interface IStorage<N, M>
{

    /**
     * adds an element
     *
     * @param p_key key name
     * @param p_element element
     */
    public void addMultiElement( final String p_key, final N p_element );

    /**
     * adds a new mask
     *
     * @param p_key key name
     * @param p_element mask element
     */
    public void addSingleElement( final String p_key, final M p_element );

    /**
     * clears all elements
     */
    public void clear();

    /**
     * checks any element exists
     *
     * @param p_key key name
     * @return exist boolean
     */
    public boolean contains( final String p_key );

    /**
     * check if an element exists
     *
     * @param p_key key name
     * @return exist boolean
     */
    public boolean containsMultiElement( final String p_key );

    /**
     * checks if a mask exists
     *
     * @param p_key key name
     * @return exist boolean
     */
    public boolean containsSingleElement( String p_key );

    /**
     * returns a set of elements
     *
     * @param p_key key name
     * @return set
     */
    public Set<N> getMultiElement( final String p_key );

    /**
     * returns a mask
     *
     * @param p_key key name
     * @return mask
     */
    public M getSingleElement( final String p_key );

    /**
     * checks if a storage is empty
     *
     * @return empty boolean
     */
    public boolean isEmpty();

    /**
     * removes all elements by its name
     *
     * @param p_key key name
     * @return boolean flag that elements could be removed
     */
    public boolean remove( final String p_key );

    /**
     * removes an element
     *
     * @param p_key key name
     * @param p_element element
     * @return boolean flag, that the element is removed
     */
    public boolean removeMultiElement( final String p_key, final N p_element );

    /**
     * removes a mask
     *
     * @param p_key key name
     * @return boolean flag the element is removed
     */
    public boolean removeSingleElement( final String p_key );

    /**
     * updates all items
     */
    public void update();

    /**
     * number of multielements
     *
     * @return size
     */
    public int sizeMultiElement();

    /**
     * number of singleelements
     *
     * @return size
     */
    public int sizeSingleElement();


    /**
     * number of all elements
     */
    public int size();

    /**
     * iterator over all multielements
     *
     * @return iterator
     */
    public Iterator<N> iteratorMultiElement();

    /**
     * iterator over all singlelements
     *
     * @return iterator
     */
    public Iterator<M> iteratorSingleElement();

}
