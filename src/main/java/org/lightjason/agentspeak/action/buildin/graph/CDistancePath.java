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
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * calculates the distance of a route within a graph.
 * The ordering of the arguments can be arbitrary, for
 * any graph instance the distance path is calculated,
 * the first map instance will be used as cost-map,
 * the first argument is used as cost-default value
 * if exists, the first two arguments which are
 * not a map or a graph, will be used as vertex
 * identifier (start & end vertex).
 *
 * @code [D1|D2] = graph/shortestpath( 3, StartVertex, CostMap, EndVertex, Graph1, Graph2 ); @endcode
 *
 * @note the cost-map does not need an entry for each edge
 * non-existing edges have got on default zero costs
 */
public class CDistancePath extends IBuildinAction
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );


        final int l_skip;
        final double l_defaultcost;
        if ( CCommon.rawvalueAssignableTo( l_arguments.get( 0 ), Number.class ) )
        {
            l_skip = 1;
            l_defaultcost = l_arguments.get( 0 ).<Number>raw().doubleValue();
        }
        else
        {
            l_skip = 0;
            l_defaultcost = 1D;
        }


        final List<Object> l_vertices = l_arguments.stream()
                                               .skip( l_skip )
                                               .filter( i -> !CCommon.rawvalueAssignableTo( i, Map.class ) )
                                               .filter( i -> !CCommon.rawvalueAssignableTo( i, Graph.class ) )
                                               .limit( 2 )
                                               .map( ITerm::raw )
                                               .collect( Collectors.toList() );

        if ( l_vertices.size() != 2 )
            return CFuzzyValue.from( false );

        final Map<?, Number> l_costmap = CCommon.flatcollection( p_argument )
                                                .filter( i -> CCommon.rawvalueAssignableTo( i, Map.class ) )
                                                .findFirst()
                                                .map( ITerm::<Map<?, Number>>raw )
                                                .orElseGet( Collections::emptyMap );

        final Function<Object, Double> l_weightfunction = ( e ) -> l_costmap.getOrDefault( e, l_defaultcost ).doubleValue();


        // --- filter graphs ---
        CCommon.flatcollection( p_argument )
               .filter( i -> CCommon.rawvalueAssignableTo( i, Graph.class ) )
               .map( ITerm::<Graph<Object, Object>>raw )
               .map( i -> new DijkstraShortestPath<Object, Object>( i, l_weightfunction ) )
               .map( i -> i.getDistance( l_vertices.get( 0 ), l_vertices.get( 1 ) ) )
               .mapToDouble( Number::doubleValue )
               .boxed()
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
