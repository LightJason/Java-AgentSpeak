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


import lightjason.agent.IAgent;
import lightjason.language.ILiteral;
import org.apache.commons.lang3.tuple.Pair;


/**
 * interface for equal method names on masks and beliefbases
 */
public interface IStructure
{

    /**
     * adds a literal in the current structure
     *
     * @param p_literal literal
     * @return boolean flag for correct adding
     */
    boolean add( final ILiteral p_literal );

    /**
     * adds a mask into the current structure
     *
     * @param p_mask mask
     * @note the mask that is put in the method will be cloned, so the returned mask are not equal, the parameter is a template object only
     * @returns returns the added mask or null
     */
    IMask add( final IMask p_mask );



    /**
     * modify an literal
     *
     * @param p_before literal before modification
     * @param p_after literal after modification
     * @return boolean for correct modification
     * @note it is an atomic optation of remove and add
     */
    boolean modify( final ILiteral p_before, final ILiteral p_after );



    /**
     * removes a mask in the current structure
     *
     * @param p_mask mask
     * @return boolean flag for correct removing
     */
    boolean remove( final IMask p_mask );

    /**
     * removes a literal in the current structure
     *
     * @param p_literal literal
     * @return boolean flag for correct removing
     */
    boolean remove( final ILiteral p_literal );



    /**
     * clears all elements
     */
    void clear();

    /**
     * checks if the structure empty
     *
     * @return empty boolean
     */
    boolean isEmpty();

    /**
     * returns the size of literals
     *
     * @return size
     */
    int size();



    /**
     * updates all items
     *
     * @param p_agent agent which runs the update call
     * @warning call update on a storage and on all storage-masks, if exists different masks
     * which are point to the same storage, the update is called more than once, so the storage must
     * limit the number of update calls
     */
    <T extends IAgent> void update( final T p_agent );

    /**
     * returns a new mask of the belief base
     *
     * @param p_name name of the mask
     * @return mask
     *
     * @tparam E typecast
     */
    <E extends IMask> E create( final String p_name );

    /**
     * returns the storage of the beliefbase
     *
     * @return storage
     *
     * @tparam L typecast
     */
    <L extends IStorage<Pair<Boolean, ILiteral>, IMask>> L getStorage();

}
