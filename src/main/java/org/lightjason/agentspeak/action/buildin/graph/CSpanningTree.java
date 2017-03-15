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
import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.DelegateTree;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * creates a minimal spanning tree of a graph.
 * The action creates from each graph argument a spanning
 * tree, if the first argument is a cost-map for edges
 * the numerical values are used for the spanning tree
 *
 * @code
    [SP1|SP2] = graph/spanningtree( Graph1, Graph2 );
    [SP3|SP4] = graph/spanningtree( CostMap, Graph3, Graph4 );
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
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        // check if a cost-matrix is given
        final int l_skip;
        final PrimMinimumSpanningTree<Object, Object> l_treefactory;

        if ( !CCommon.rawvalueAssignableTo( l_arguments.get( 0 ), Map.class ) )
        {
            l_skip = 0;
            l_treefactory = new PrimMinimumSpanningTree<>( DelegateTree.getFactory() );
        }
        else
        {
            l_skip = 1;
            final Map<Object, Number> l_weights = l_arguments.get( 0 ).<Map<Object, Number>>raw();
            final Function<Object, Double> l_weightfunction = ( e) -> l_weights.getOrDefault( e, 0 ).doubleValue();
            l_treefactory = new PrimMinimumSpanningTree<>( DelegateTree.getFactory(), l_weightfunction );
        }

        // create spanning-tree
        l_arguments.stream()
               .skip( l_skip )
               .map( ITerm::<AbstractGraph<Object, Object>>raw )
               .map( l_treefactory::apply )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
