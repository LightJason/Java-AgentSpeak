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
 * interface for equal method on views and beliefbases
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
     * adds view in the current structure
     *
     * @param p_view existing view
     * @return returns cloned view
     *
     * @note view that is put in the method will be cloned, so the returned view are not equal, the parameter is a template object only
     */
    IView add( final IView p_view );


    /**
     * removes a view in the current structure
     *
     * @param p_view view
     * @return boolean flag for correct removing
     */
    boolean remove( final IView p_view );

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
     * @return agent
     *
     * @warning call update on a storage and on all storage-view, if exists different views
     * which are point to the same storage, the update is called more than once, so the storage must
     * limit the number of update calls
     */
    IAgent update( final IAgent p_agent );

    /**
     * returns a new view of the belief base
     *
     * @param p_name name of the view
     * @return view
     *
     * @tparam E typecast
     */
    <E extends IView> E create( final String p_name );

    /**
     * returns the storage of the beliefbase
     *
     * @return storage
     *
     * @tparam L typecast
     */
    <L extends IStorage<Pair<Boolean, ILiteral>, IView>> L getStorage();

}
