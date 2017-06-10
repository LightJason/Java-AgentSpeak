/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.Stream;


/**
 * interface of beliefbase definition,
 * that create the trigger events for the agent
 *
 * @tparam T agent type
 */
public interface IBeliefbase<T extends IAgent<?>> extends IStructure<T>
{

    /**
     * returns all trigger of the beliefbase
     *
     * @param p_view mask for events
     * @return set with trigger events
     */
    @Nonnull
    Stream<ITrigger> trigger( @Nonnull final IView<T> p_view );



    /**
     * returns a stream over all literals
     *
     * @return literal stream
     */
    @Nonnull
    Stream<ILiteral> streamLiteral();

    /**
     * returns a stream over all views
     *
     * @return view stream
     */
    @Nonnull
    Stream<IView<T>> streamView();



    /**
     * clears all elements
     *
     * @return beliefbase reference
     */
    @Nonnull
    IBeliefbase<T> clear();



    /**
     * adds a literal
     *
     * @param p_literal literal without path
     * @return beliefbase reference
     */
    @Nonnull
    ILiteral add( @Nonnull final ILiteral p_literal );

    /**
     * adds a view
     *
     * @param p_view view
     * @return beliefbase reference
     */
    @Nonnull
    IView<T> add( @Nonnull final IView<T> p_view );



    /**
     * removes a literal
     *
     * @param p_literal without path
     * @return beliefbase reference
     */
    @Nonnull
    ILiteral remove( @Nonnull final ILiteral p_literal );

    /**
     * removes a view
     *
     * @param p_view view
     * @return beliefbase reference
     */
    @Nonnull
    IView<T> remove( @Nonnull final IView<T> p_view );



    /**
     * contains a multi-element
     *
     * @param p_key key
     * @return boolean existing flag
     */
    boolean containsLiteral( @Nonnull final String p_key );

    /**
     * contains a single-element
     *
     * @param p_key key
     * @return boolean existing flag
     */
    boolean containsView( @Nonnull final String p_key );



    /**
     * returns a view element
     *
     * @param p_key name of the view
     * @return view or null
     */
    @Nullable
    IView<T> view( @Nonnull final String p_key );

    /**
     * returns a literal by the name
     *
     * @param p_key name of the literal
     * @return collection of pairs with negated and literal
     */
    @Nonnull
    Collection<ILiteral> literal( @Nonnull final String p_key );

    /**
     * returns a view element
     *
     * @param p_key name of the view
     * @param p_default view
     * @return view or default element
     */
    @Nullable
    IView<T> viewOrDefault( @Nonnull final String p_key, @Nullable final IView<T> p_default );



    /**
     * returns a new view of the belief base
     *
     * @param p_name name of the view
     * @return created view
     */
    @Nonnull
    IView<T> create( @Nonnull final String p_name );

    /**
     * returns a new view of the belief base
     *
     * @param p_name name of the view
     * @param p_parent parent view
     * @return view
     */
    @Nonnull
    IView<T> create( @Nonnull final String p_name, @Nullable final IView<T> p_parent );

}
