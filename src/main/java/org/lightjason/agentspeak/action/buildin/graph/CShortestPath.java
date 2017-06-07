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

import java.util.List;


/**
 * calculates the edge list of the shortest path of two vertices within each graph instance.
 * The ordering of the arguments can be arbitrary, for any graph
 * instance the edge list of the shortest path is calculated, the first map instance
 * will be used as weight-map, a tuple of the string "defaultweight"
 * and a numeric value defines the default weight value of the weight-map
 * (the default value is zero), a tuple which will not fit this definition
 * defines the start- and end-vertex, the action fails on wrong input
 *
 * @code [D1|D2] = graph/shortestpath( StartVertex, EndVertex, Graph1, Graph2 );
 * [D3|D4] = graph/shortestpath( "defaultweight", 3, CostMap, StartVertex, EndVertex, Graph1, Graph2 );
 * @endcode
 *
 * @note the weight-map does not need an entry for each edge non-existing edges have got on default zero weight
 */
public final class CShortestPath extends IApplyPathAlgorithm
{

    @Override
    protected final Object apply( final List<ITerm> p_vertices, final Graph<Object, Object> p_graph, final Function<Object, Number> p_weightfunction )
    {
        return new DijkstraShortestPath<>( p_graph, p_weightfunction )
                                   .getPath( p_vertices.get( 0 ).raw(), p_vertices.get( 1 ).raw() );
    }

}
