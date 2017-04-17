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

package org.lightjason.agentspeak.action.buildin;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.graph.CAddEdgeMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CAddEdgeSingle;
import org.lightjason.agentspeak.action.buildin.graph.CAddVertexMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CAddVertexSingle;
import org.lightjason.agentspeak.action.buildin.graph.CAdjacencyMatrix;
import org.lightjason.agentspeak.action.buildin.graph.CContainsEdge;
import org.lightjason.agentspeak.action.buildin.graph.CContainsVertex;
import org.lightjason.agentspeak.action.buildin.graph.CCreate;
import org.lightjason.agentspeak.action.buildin.graph.CDegreeMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CDegreeSingle;
import org.lightjason.agentspeak.action.buildin.graph.CDistancePath;
import org.lightjason.agentspeak.action.buildin.graph.CEdgeCount;
import org.lightjason.agentspeak.action.buildin.graph.CEdgeListMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CEdgeListSingle;
import org.lightjason.agentspeak.action.buildin.graph.CEdges;
import org.lightjason.agentspeak.action.buildin.graph.CFindEdgeMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CFindEdgeSingle;
import org.lightjason.agentspeak.action.buildin.graph.CVertexCount;
import org.lightjason.agentspeak.action.buildin.graph.CVertices;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test graph actions
 */
public final class TestCActionGraph extends IBaseTest
{

    /**
     * test graph creating
     */
    @Test
    public final void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            null,
            false,
            Stream.of( "sparse", "SPARSEMULTI", "DIRECTEDSPARSE", "DIRECTEDSPARSEMULTI", "UNDIRECTEDSPARSE", "UNDIRECTEDSPARSEMULTI" )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 6 );
        Assert.assertTrue( l_return.stream().map( ITerm::raw ).allMatch( i -> i instanceof Graph<?, ?> ) );
    }


    /**
     * test add-vertex single
     */
    @Test
    public final void addvertexsingle()
    {
        final Graph<?, ?> l_graph1 = new SparseGraph<>();
        final Graph<?, ?> l_graph2 = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( i ->
                    new CAddVertexSingle().execute(
                        null,
                        false,
                        Stream.of( i, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                        Collections.emptyList(),
                        Collections.emptyList()
                    ) );

        Assert.assertArrayEquals( l_graph1.getVertices().toArray(), IntStream.range( 0, 5 ).boxed().toArray() );
        Assert.assertArrayEquals( l_graph2.getVertices().toArray(), IntStream.range( 0, 5 ).boxed().toArray() );
    }


    /**
     * test add-vertex multiple
     */
    @Test
    public final void addvertexmultiple()
    {
        final Graph<?, ?> l_graph = new SparseGraph<>();

        new CAddVertexMultiple().execute(
            null,
            false,
            Stream.of( l_graph, "x", "y", "z" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_graph.getVertices().toArray(), Stream.of( "x", "y", "z" ).toArray() );
    }



    /**
     * test add-edge single
     */
    @Test
    public final void addedgesingle()
    {
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        new CAddEdgeSingle().execute(
            null,
            false,
            Stream.of( "xy", 1, 2, l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        new CAddEdgeSingle().execute(
            null,
            false,
            Stream.of( "bar", 4, 5, l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_graph.getEdgeCount(), 2 );
        Assert.assertEquals( (long) l_graph.getEndpoints( "xy" ).getFirst(), 1 );
        Assert.assertEquals( (long) l_graph.getEndpoints( "xy" ).getSecond(), 2 );
        Assert.assertEquals( (long) l_graph.getEndpoints( "bar" ).getFirst(), 4 );
        Assert.assertEquals( (long) l_graph.getEndpoints( "bar" ).getSecond(), 5 );
    }


    /**
     * test add-edge multiple
     */
    @Test
    public final void addedgemultiple()
    {
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addVertex( 1 );
        l_graph.addVertex( 2 );

        new CAddEdgeMultiple().execute(
            null,
            false,
            Stream.of( l_graph, "foo", 1, 1, "bar", 1, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_graph.getEdges().toArray(), Stream.of( "bar", "foo" ).toArray() );
    }



    /**
     * test vertex-count
     */
    @Test
    public final void vertexcount()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        new CVertexCount().execute(
            null,
            false,
            Stream.of( l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), (long) l_graph.getVertexCount() );
    }


    /**
     * test edge-count
     */
    @Test
    public final void edgecount()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        l_graph.addEdge( "a", 0, 1 );
        l_graph.addEdge( "b", 0, 2 );
        l_graph.addEdge( "c", 1, 3 );
        l_graph.addEdge( "d", 3, 4 );


        new CEdgeCount().execute(
            null,
            false,
            Stream.of( l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 4L );
    }


    /**
     * test vertices
     */
    @Test
    public final void vertices()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        new CVertices().execute(
            null,
            false,
            Stream.of( l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), IntStream.range( 0, 5 ).boxed().toArray() );
    }


    /**
     * test edges
     */
    @Test
    public final void edges()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        l_graph.addEdge( "a", 0, 1 );
        l_graph.addEdge( "b", 0, 2 );
        l_graph.addEdge( "c", 1, 3 );
        l_graph.addEdge( "d", 3, 4 );

        new CEdges().execute(
            null,
            false,
            Stream.of( l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "a", "b", "c", "d" ).toArray() );
    }


    /**
     * test adjacency matrix
     */
    @Test
    public final void adjacencymatrix()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        IntStream.range( 1, 7 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        l_graph.addEdge( "x", 1, 1 );
        l_graph.addEdge( "y", 1, 2 );
        l_graph.addEdge( "z", 1, 5 );

        l_graph.addEdge( "xx", 2, 3 );
        l_graph.addEdge( "yy", 2, 5 );

        l_graph.addEdge( "xxx", 3, 4 );

        l_graph.addEdge( "xxxx", 4, 5 );
        l_graph.addEdge( "yyyy", 4, 6 );


        new CAdjacencyMatrix().execute(
            null,
            false,
            Stream.of( 1, l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 1, 2, 3, 4, 5, 6 ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw(), new DenseDoubleMatrix2D( new double[][]{{2, 1, 0, 0, 1, 0}, {1, 0, 1, 0, 1, 0}, {0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 1}, {1, 1, 0, 1, 0, 0}, {0, 0, 0, 1, 0, 0}} ) );
    }


    /**
     * test contains-edge
     */
    @Test
    public final void containsedge()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new SparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        IntStream.range( 1, 5 )
                 .boxed()
                 .forEach( i -> {
                     l_graph1.addVertex( i );
                     l_graph2.addVertex( i );
                 } );

        l_graph1.addEdge( "ooo", 1, 2 );
        l_graph1.addEdge( "xxx", 1, 2 );
        l_graph1.addEdge( "yyy", 2, 3 );

        l_graph2.addEdge( "yyx", 1, 2 );
        l_graph2.addEdge( "aaa", 2, 3 );


        new CContainsEdge().execute(
            null,
            false,
            Stream.of( "yyy", l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertFalse( l_return.get( 1 ).<Boolean>raw() );
    }


    /**
     * test contains-vertex
     */
    @Test
    public final void containsvertex()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new SparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        IntStream.range( 1, 5 )
                 .boxed()
                 .forEach( l_graph1::addVertex );

        IntStream.range( 5, 10 )
                 .boxed()
                 .forEach( l_graph2::addVertex );

        new CContainsVertex().execute(
            null,
            false,
            Stream.of( 5, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertFalse( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertTrue( l_return.get( 1 ).<Boolean>raw() );
    }


    /**
     * test degree multiple
     */
    @Test
    public final void degreemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        IntStream.range( 1, 7 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        l_graph.addEdge( "a", 1, 1 );
        l_graph.addEdge( "b", 1, 2 );
        l_graph.addEdge( "c", 1, 5 );

        l_graph.addEdge( "d", 2, 3 );
        l_graph.addEdge( "e", 2, 5 );

        l_graph.addEdge( "f", 3, 4 );

        l_graph.addEdge( "g", 4, 5 );
        l_graph.addEdge( "h", 4, 6 );


        new CDegreeMultiple().execute(
            null,
            false,
            Stream.concat(
                Stream.of( l_graph ),
                IntStream.range( 1, 7 ).boxed()
            ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 3, 3, 2, 3, 3, 1 ).mapToLong( i -> i ).boxed().toArray() );
    }


    /**
     * test degree single
     */
    @Test
    public final void degreesingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new UndirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new UndirectedSparseGraph<>();

        IntStream.range( 1, 7 )
                 .boxed()
                 .forEach( i -> {
                     l_graph1.addVertex( i );
                     l_graph2.addVertex( i );
                 } );

        l_graph1.addEdge( "x", 1, 1 );
        l_graph1.addEdge( "y", 1, 2 );
        l_graph1.addEdge( "z", 1, 5 );

        l_graph2.addEdge( "x", 1, 1 );
        l_graph2.addEdge( "y", 1, 2 );


        new CDegreeSingle().execute(
            null,
            false,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 3L, 2L ).toArray() );
    }


    /**
     * test distance-path
     */
    @Test
    public final void distancepath()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        IntStream.range( 1, 7 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        l_graph.addEdge( "m", 1, 2 );
        l_graph.addEdge( "n", 2, 3 );
        l_graph.addEdge( "o", 3, 4 );
        l_graph.addEdge( "p", 2, 6 );

        new CDistancePath().execute(
            null,
            false,
            Stream.of( 2, l_graph, 1, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 6D );
    }


    /**
     * test find-edge single
     */
    @Test
    public final void findedgesingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new UndirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new UndirectedSparseGraph<>();

        IntStream.range( 1, 7 )
                 .boxed()
                 .forEach( i -> {
                     l_graph1.addVertex( i );
                     l_graph2.addVertex( i );
                 } );

        l_graph1.addEdge( "search", 1, 2 );
        l_graph1.addEdge( "notsearch", 1, 2 );
        l_graph2.addEdge( "xxx", 1, 2 );

        new CFindEdgeSingle().execute(
            null,
            false,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( "search", "xxx" ).toArray() );
    }


    /**
     * test find-edge multiple
     */
    @Test
    public final void findedgemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        IntStream.range( 1, 7 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        l_graph.addEdge( "edge12", 1, 2 );
        l_graph.addEdge( "edge23", 2, 3 );
        l_graph.addEdge( "edge34", 3, 4 );
        l_graph.addEdge( "edge13", 1, 3 );
        l_graph.addEdge( "edge24", 2, 4 );

        new CFindEdgeMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 2, 2, 3, 3, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( "edge12", "edge23", "edge34" ).toArray() );
    }


    /**
     * test edgelist single
     */
    @Test
    public final void edgelistsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new SparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new SparseMultigraph<>();

        IntStream.range( 1, 7 )
                 .boxed()
                 .forEach( i -> {
                     l_graph1.addVertex( i );
                     l_graph2.addVertex( i );
                 } );

        l_graph1.addEdge( "edgeA1", 1, 2 );
        l_graph1.addEdge( "edgeAA1", 1, 2 );
        l_graph2.addEdge( "edgeA2", 1, 2 );
        l_graph1.addEdge( "edgeB1", 2, 3 );
        l_graph2.addEdge( "edgeB2", 3, 4 );

        new CEdgeListSingle().execute(
            null,
            false,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "edgeAA1", "edgeA1" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "edgeA2" ).toArray() );
    }


    /**
     * test edgelist multiple
     */
    @Test
    public final void edgelistmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseMultigraph<>();

        IntStream.range( 1, 4 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        l_graph.addEdge( "o", 1, 2 );
        l_graph.addEdge( "p", 1, 2 );
        l_graph.addEdge( "q", 2, 3 );
        l_graph.addEdge( "r", 2, 3 );
        l_graph.addEdge( "s", 2, 3 );

        new CEdgeListMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 2, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "p", "o" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "q", "r", "s" ).toArray() );
    }



    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionGraph().invoketest();
    }

}
