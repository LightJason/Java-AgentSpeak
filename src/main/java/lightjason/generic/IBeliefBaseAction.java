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

package lightjason.generic;


import java.util.Iterator;


/**
 * interface for equal method names on masks and beliefbases
 */
public interface IBeliefBaseAction
{

    /**
     * adds a literal in the current structure
     *
     * @param p_literal literal
     */
    public void add( final ILiteral p_literal );

    /**
     * adds a mask into the current structure
     *
     * @param p_mask mask
     * @note the mask that is put in the method will be cloned, so the returned mask are not equal, the parameter is a template object only
     * @returns returns the added mask
     */
    public IBeliefBaseMask add( final IBeliefBaseMask p_mask );

    /**
     * clears all elements
     */
    public void clear();

    /**
     * returns a new mask of the belief base
     *
     * @param p_name name of the mask
     * @return mask
     */
    public <E extends IBeliefBaseMask> E createMask( final String p_name );

    /**
     * returns the storage of the beliefbase
     *
     * @return storage
     *
     * @tparam L typecast
     */
    public <L extends IStorage<ILiteral, IBeliefBaseMask>> L getStorage();

    /**
     * checks if the structure empty
     *
     * @return empty boolean
     */
    public boolean isEmpty();

    /**
     * removes a mask in the current structure
     *
     * @param p_mask mask
     */
    public boolean remove( final IBeliefBaseMask p_mask );

    /**
     * removes a literal in the current structure
     *
     * @param p_literal literal
     */
    public boolean remove( final ILiteral p_literal );

    /**
     * removes mask and literal at the current structure
     *
     * @param p_name name
     */
    boolean remove( final String p_name );

    /**
     * updates all items
     *
     * @warning call update on a storage and on all storage-masks, if exists different masks
     * which are point to the same storage, the update is called more than once, so the storage must
     * limit the number of update calls
     */
    public void update();

    /**
     * number of masks
     *
     * @return size
     */
    public int sizeMask();

    /**
     * number of literals
     *
     * @return size
     */
    public int sizeLiteral();

    /**
     * number of element
     *
     * @return size
     */
    public int size();

    /**
     * iterator over all multielements
     *
     * @return iterator
     */
    public Iterator<ILiteral> iteratorLiteral();

    /**
     * iterator over all singlelements
     *
     * @return iterator
     */
    public Iterator<IBeliefBaseMask> iteratorBeliefBaseMask();

}
