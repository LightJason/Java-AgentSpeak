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


import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.util.stream.Stream;


/**
 * view for a beliefbase
 *
 * @tparam T agent type
 */
public interface IView<T extends IAgent<?>> extends IStructure<T>
{
    /**
     * returns a stream to the root node
     *
     * @return stream of views
     */
    Stream<IView<T>> root();

    /**
     * returns the beliefbase
     *
     * @return beliefbase
     */
    IBeliefBase<T> getBeliefbase();

    /**
     * returns the full path
     *
     * @return path
     */
    IPath getPath();

    /**
     * returns only the element name
     *
     * @return name
     */
    String getName();

    /**
     * returns the parent of the view
     *
     * @return parent object or null
     */
    IView<T> getParent();

    /**
     * check if the view has got a parent
     *
     * @return boolean flag of the parent
     */
    boolean hasParent();



    /**
     * copy the current view
     *
     * @param p_parent new parent
     * @return new view instance
     */
    IView<T> copy( final IView<T> p_parent );

    /**
     * generates a path structure
     *
     * @param p_path path definition
     * @param p_generator generator function
     * @return self reference
     */
    IView<T> generate( final IPath p_path, final IViewGenerator<T> p_generator );



    /**
     * retruns all trigger of the beliefbase
     *
     * @return set with trigger events
     */
    Stream<ITrigger> getTrigger();



    /**
     * returns stream of literal
     *
     * @param p_path paths of the literals
     * @return literal stream
     */
    Stream<ILiteral> stream( final IPath... p_path );

    /**
     * returns stream of literal
     *
     * @param p_negated negated flag
     * @param p_path paths of the literals
     * @return literal stream
     */
    Stream<ILiteral> stream( final boolean p_negated, final IPath... p_path );



    /**
     * clears all elements
     *
     * @param p_path path values
     * @return self reference
     */
    IView<T> clear( final IPath... p_path );



    /**
     * adds a literal in the current structure
     *
     * @param p_literal literal
     * @return boolean flag for correct adding
     */
    IView<T> add( final ILiteral... p_literal );

    /**
     * adds view in the current structure
     *
     * @param p_view existing view
     * @return current view
     */
    @SuppressWarnings( "unchecked" )
    IView<T> add( final IView<T>... p_view );

    /**
     * adds view in the current structure
     *
     * @param p_path path
     * @param p_view existing view
     * @return current view
     */
    @SuppressWarnings( "unchecked" )
    IView<T> add( final IPath p_path, final IView<T>... p_view );



    /**
     * removes a literal in the current structure
     *
     * @param p_literal literal
     * @return boolean flag for correct removing
     */
    IView<T> remove( final ILiteral p_literal );

    /**
     * removes a view in the current structure
     *
     * @param p_view view
     * @return boolean flag for correct removing
     */
    IView<T> remove( final IView<T> p_view );



    /**
     * checks if a literal exists
     *
     * @param p_path path to a literal (suffix is literal name)
     * @return existance boolean
     */
    boolean containsLiteral( final IPath p_path );

    /**
     * view existing check
     *
     * @param p_path path to a view
     * @return existance boolean
     */
    boolean containsView( final IPath p_path );

}
