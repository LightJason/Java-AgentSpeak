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

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.jet.math.Functions;
import com.codepoetics.protonpack.StreamUtils;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.action.buildin.math.blas.EType;
import org.lightjason.agentspeak.action.buildin.math.blas.IAlgebra;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * creates from a graph the adjacency matrix.
 * The action converts graphs into a matrix,
 * if a string is put on the argument list
 * it must be "dense|sparse" to define the resulting
 * matrix, a map defines the costs of an edge, a number
 * defines the default costs, the ordering of
 * the arguments is completly independed, for each
 * graph two arguments will be returned, the
 * adjacency matrix and the node names and the action
 * never fails
 *
 * @code
    [M1|N1|M2|N2] = graph/adjacencymatrix( Graph1, "dense|sparse", Graph2 );
    [M1|N1|M2|N2] = graph/adjacencymatrix( CostMap, Graph1, Graph2 );
    [M1|N1|M2|N2] = graph/adjacencymatrix( Graph1, 1, Graph2 );
    [M1|N1|M2|N2] = graph/adjacencymatrix( CostMap, Graph1, Graph2, "dense|sparse", );
 * @endcode
 * @note the cost-map does not need an entry for each edge
 * non-existing edges have got on default zero costs with 1
 * @see https://en.wikipedia.org/wiki/Adjacency_matrix
 */
public final class CAdjacencyMatrix extends IBuildinAction
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( @Nonnull final IContext p_context, final boolean p_parallel,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        // --- filter parameters ---
        final EType l_type = CCommon.flatcollection( p_argument )
                                    .filter( i -> CCommon.rawvalueAssignableTo( i, String.class ) )
                                    .findFirst()
                                    .map( ITerm::<String>raw )
                                    .map( EType::from )
                                    .orElseGet( () -> EType.SPARSE );

        final double l_defaultcost = CCommon.flatcollection( p_argument )
                                            .filter( i -> CCommon.rawvalueAssignableTo( i, Number.class ) )
                                            .findFirst()
                                            .map( ITerm::<Number>raw )
                                            .map( Number::doubleValue )
                                            .orElseGet( () -> 1D );

        final Map<?, Number> l_costmap = CCommon.flatcollection( p_argument )
                                                .filter( i -> CCommon.rawvalueAssignableTo( i, Map.class ) )
                                                .findFirst()
                                                .map( ITerm::<Map<?, Number>>raw )
                                                .orElseGet( Collections::emptyMap );


        // --- filter graphs ---
        CCommon.flatcollection( p_argument )
               .filter( i -> CCommon.rawvalueAssignableTo( i, Graph.class ) )
               .map( ITerm::<Graph<Object, Object>>raw )
               .map( i -> CAdjacencyMatrix.apply( i, l_costmap, l_defaultcost, l_type ) )
               .forEach( i -> {
                   p_return.add( CRawTerm.from( i.getLeft() ) );
                   p_return.add( CRawTerm.from( i.getRight() ) );
               } );

        return CFuzzyValue.from( true );
    }

    /**
     * converts a graph into an adjacency matrix
     *
     * @param p_graph graph
     * @param p_cost map with edges and costs
     * @param p_defaultcost default cost value (on non-existing map values)
     * @param p_type matrix type
     * @return pair of double matrix and vertices
     */
    private static Pair<DoubleMatrix2D, Collection<?>> apply( @Nonnull final Graph<Object, Object> p_graph, @Nonnull final Map<?, Number> p_cost,
                                                              final double p_defaultcost, @Nonnull final EType p_type )
    {
        // index map for matching vertex to index position within matrix
        final Map<Object, Integer> l_index = new HashMap<>();

        // extract vertices from edges
        p_graph.getEdges()
            .stream()
            .map( p_graph::getEndpoints )
            .flatMap( i -> Stream.of( i.getFirst(), i.getSecond() ) )
            .forEach( i -> l_index.putIfAbsent( i, 0 ) );

        // define for each vertex an index number in [0, size)
        StreamUtils.zip(
            l_index.keySet().stream(),
            IntStream.range( 0, l_index.size() ).boxed(),
            ImmutablePair::new
        ).forEach( i -> l_index.put( i.getKey(), i.getValue() ) );

        final DoubleMatrix2D l_matrix;
        switch ( p_type )
        {
            case SPARSE:
                l_matrix = new SparseDoubleMatrix2D( l_index.size(), l_index.size() );
                break;

            default:
                l_matrix = new DenseDoubleMatrix2D( l_index.size(), l_index.size() );
        }

        // map costs to matrix
        p_graph.getEdges()
               .stream()
               .map( i -> new ImmutablePair<>( p_graph.getEndpoints( i ), p_cost.getOrDefault( i, p_defaultcost ).doubleValue() ) )
               .forEach( i -> l_matrix.setQuick(
                                  l_index.get( i.getLeft().getFirst() ), l_index.get( i.getLeft().getSecond() ),
                                  i.getRight() + l_matrix.getQuick( l_index.get( i.getLeft().getFirst() ), l_index.get( i.getLeft().getSecond() ) )
               ) );

        // on undirected graphs, add the transposefor cost duplicating
        if ( p_graph instanceof UndirectedGraph<?, ?> )
            l_matrix.assign( IAlgebra.ALGEBRA.transpose( l_matrix ).copy(), Functions.plus );

        return new ImmutablePair<>( l_matrix, new ArrayList<>( l_index.keySet() ) );
    }
}
