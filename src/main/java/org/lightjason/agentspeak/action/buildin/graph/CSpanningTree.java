/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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
import edu.uci.ics.jung.algorithms.shortestpath.PrimMinimumSpanningTree;
import edu.uci.ics.jung.graph.DelegateTree;
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


/**
 * creates a minimal spanning tree of a graph.
 * The action creates from each graph argument a spanning
 * tree, within the argument list a default cost value
 * and a cost-matrix can be added, if an adge is not found
 * on the cost-map the default value is used, the ordering
 * of the arguments can be completly unordered
 *
 * @code
    [SP1|SP2] = graph/spanningtree( Graph1, Graph2 );
    [SP3|SP4] = graph/spanningtree( CostMap, Graph3, 1, Graph4 );
 * @endcode
 */
public final class CSpanningTree extends IBuildinAction
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
        final double l_defaultcost = CCommon.flatcollection( p_argument )
                                            .filter( i -> CCommon.rawvalueAssignableTo( i, Number.class ) )
                                            .findFirst()
                                            .map( ITerm::<Number>raw )
                                            .map( Number::doubleValue )
                                            .orElseGet( () -> 0D );

        final Map<?, Number> l_costmap = CCommon.flatcollection( p_argument )
                                                .filter( i -> CCommon.rawvalueAssignableTo( i, Map.class ) )
                                                .findFirst()
                                                .map( ITerm::<Map<?, Number>>raw )
                                                .orElseGet( Collections::emptyMap );

        final Function<Object, Double> l_weightfunction = ( e ) -> l_costmap.getOrDefault( e, l_defaultcost ).doubleValue();
        final PrimMinimumSpanningTree<Object, Object> l_treefactory = new PrimMinimumSpanningTree<>( DelegateTree.getFactory(), l_weightfunction );

        // --- filter graphs ---
        CCommon.flatcollection( p_argument )
               .filter( i -> CCommon.rawvalueAssignableTo( i, Graph.class ) )
               .map( ITerm::<Graph<Object, Object>>raw )
               .map( l_treefactory )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
