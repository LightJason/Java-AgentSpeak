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

package org.lightjason.agentspeak.action.buildin.graph;

import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * calculates the distance of two vertices within each graph instance.
 * The ordering of the arguments can be arbitrary, for any graph
 * instance the distance path is calculated, the first map instance
 * will be used as weight-map, a tuple of the string "defaultweight"
 * and a numeric value defines the default weight value of the weight-map
 * (the default value is zero), a tuple which will not fit this definition
 * defines the start- and end-vertex, the action fails on wrong input
 *
 * @code [D1|D2] = graph/distancepath( StartVertex, EndVertex, Graph1, Graph2 );
 * [D3|D4] = graph/distancepath( "defaultweight", 3, CostMap, StartVertex, EndVertex, Graph1, Graph2 );
 * @endcode
 *
 * @note the weight-map does not need an entry for each edge non-existing edges have got on default zero weight
 */
public class CDistancePath extends IApplyPathAlgorithm
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 2146213818742863048L;

    @Override
    protected final Object apply( @Nonnull final List<ITerm> p_vertices, @Nonnull final Graph<Object, Object> p_graph,
                                  @Nonnull final Function<Object, Number> p_weightfunction )
    {
        return new DijkstraShortestPath<>( p_graph, p_weightfunction )
                                   .getDistance( p_vertices.get( 0 ).raw(), p_vertices.get( 1 ).raw() )
                                   .doubleValue();
    }

}
