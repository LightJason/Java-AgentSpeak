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

import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.util.Collection;
import java.util.stream.Stream;


/**
 * interface of beliefbase definition
 *
 * @tparam T agent type
 */
public interface IBeliefBase<T extends IAgent<?>> extends IStructure<T>
{

    /**
     * returns all trigger of the beliefbase
     *
     * @param p_view mask for events
     * @return set with trigger events
     */
    Stream<ITrigger> getTrigger( final IView<T> p_view );

    /**
     * returns a stream over all literals
     *
     * @return literal stream
     */
    Stream<ILiteral> streamLiteral();

    /**
     * returns a stream over all views
     *
     * @return view stream
     */
    Stream<IView<T>> streamView();

    /**
     * clears all elements
     */
    void clear();

    /**
     * adds a literal
     * @param p_literal literal without path
     */
    void add( final ILiteral p_literal );

    /**
     * adds a view
     * @param p_view view
     */
    void add( final IView<T> p_view );

    /**
     * removes a literal
     * @param p_literal without path
     */
    void remove( final ILiteral p_literal );

    /**
     * removes a view
     * @param p_view view
     */
    void remove( final IView<T> p_view );

    /**
     * removes single- and multi-elements
     *
     * @param p_name name
     */
    void remove( final String p_name );

    /**
     * contains a multi-element
     *
     * @param p_key key
     * @return boolean existing flag
     */
    boolean containsLiteral( final String p_key );

    /**
     * contains a single-element
     *
     * @param p_key key
     * @return boolean existing flag
     */
    boolean containsView( final String p_key );

    /**
     * returns a view element
     *
     * @param p_key name of the view
     * @return view or null
     */
    IView<T> getView( final String p_key );

    /**
     * returns a view element
     *
     * @param p_key name of the view
     * @return view or default element
     */
    IView<T> getViewOrDefault( final String p_key, final IView<T> p_default );

    /**
     * returns a literal by the name
     *
     * @param p_key name of the literal
     * @return collection of pairs with negated and literal
     */
    Collection<Pair<Boolean, ILiteral>> getLiteral( final String p_key );

}
