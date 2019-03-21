/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.grid;

import org.lightjason.agentspeak.action.grid.routing.CAStarRouting;
import org.lightjason.agentspeak.action.grid.routing.EDistance;
import org.lightjason.agentspeak.action.grid.routing.ESearchDirection;
import org.lightjason.agentspeak.action.grid.routing.IDistance;
import org.lightjason.agentspeak.action.grid.routing.ISearchDirection;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;


/**
 * calcluate a route with a-star routing.
 * The action calls a route based on the a-star algorithm,
 * the first argument is the grid, the second argument
 * is the start position and the last argument is the goal
 * position, all other arguments are intermediate goal,
 * arguments can be numerical pairs or lists or blas vectors
 *
 * {@code L = .grid/astar(Grid, 3,3, [1,1, 8,7])}
 *
 * @see https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public final class CAStar extends IBaseRoutingAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 8106677065329220585L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CAStar.class, "grid" );

    /**
     * ctor
     */
    public CAStar()
    {

        this( EDistance.MANHATTAN, ESearchDirection.NEVER, CAStarRouting.APROXIMATIONWEIGHT );
    }

    /**
     * ctor
     *
     * @param p_distance distance
     */
    public CAStar( @Nonnull final IDistance p_distance )
    {
        this( p_distance, ESearchDirection.NEVER, CAStarRouting.APROXIMATIONWEIGHT );
    }

    /**
     * ctor
     *
     * @param p_distance distance
     * @param p_searchdirection search direction
     */
    public CAStar( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection )
    {
        this( p_distance, p_searchdirection, CAStarRouting.APROXIMATIONWEIGHT );
    }

    /**
     * ctor
     *
     * @param p_distance distance
     * @param p_searchdirection search direction
     * @param p_weight approximation weight
     */
    public CAStar( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection, @Nonnull final Number p_weight )
    {
        super( new CAStarRouting( p_distance, p_searchdirection, p_weight ) );
    }

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

}
