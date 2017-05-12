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

package org.lightjason.agentspeak.action.buildin;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import com.codepoetics.protonpack.StreamUtils;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
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
import org.lightjason.agentspeak.action.buildin.graph.CEndPointMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CEndPointSingle;
import org.lightjason.agentspeak.action.buildin.graph.CFindEdgeMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CFindEdgeSingle;
import org.lightjason.agentspeak.action.buildin.graph.CInDegreeMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CInDegreeSingle;
import org.lightjason.agentspeak.action.buildin.graph.CInEdgesMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CInEdgesSingle;
import org.lightjason.agentspeak.action.buildin.graph.CIncidentCountMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CIncidentCountSingle;
import org.lightjason.agentspeak.action.buildin.graph.CIncidentVerticesMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CIncidentVerticesSingle;
import org.lightjason.agentspeak.action.buildin.graph.CIsIncidentMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CIsIncidentSingle;
import org.lightjason.agentspeak.action.buildin.graph.CIsNeighborMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CIsNeighborSingle;
import org.lightjason.agentspeak.action.buildin.graph.CIsPredecessorMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CIsPredecessorSingle;
import org.lightjason.agentspeak.action.buildin.graph.CIsSuccessorMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CIsSuccessorSingle;
import org.lightjason.agentspeak.action.buildin.graph.CNeighborsCountMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CNeighborsCountSingle;
import org.lightjason.agentspeak.action.buildin.graph.CNeighborsMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CNeighborsSingle;
import org.lightjason.agentspeak.action.buildin.graph.COppositeMultiple;
import org.lightjason.agentspeak.action.buildin.graph.COppositeSingle;
import org.lightjason.agentspeak.action.buildin.graph.COutDegreeMultiple;
import org.lightjason.agentspeak.action.buildin.graph.COutDegreeSingle;
import org.lightjason.agentspeak.action.buildin.graph.COutEdgesMultiple;
import org.lightjason.agentspeak.action.buildin.graph.COutEdgesSingle;
import org.lightjason.agentspeak.action.buildin.graph.CPredecessorCountMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CPredecessorCountSingle;
import org.lightjason.agentspeak.action.buildin.graph.CRemoveEdgeMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CRemoveEdgeSingle;
import org.lightjason.agentspeak.action.buildin.graph.CRemoveVertexMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CRemoveVertexSingle;
import org.lightjason.agentspeak.action.buildin.graph.CShortestPath;
import org.lightjason.agentspeak.action.buildin.graph.CSpanningTree;
import org.lightjason.agentspeak.action.buildin.graph.CSuccessorCountMultiple;
import org.lightjason.agentspeak.action.buildin.graph.CSuccessorCountSingle;
import org.lightjason.agentspeak.action.buildin.graph.CVertexCount;
import org.lightjason.agentspeak.action.buildin.graph.CVertices;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Assert.assertEquals( l_return.get( 0 ).raw(), new DenseDoubleMatrix2D( new double[][]{
            {2, 1, 0, 0, 1, 0}, {1, 0, 1, 0, 1, 0}, {0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 1}, {1, 1, 0, 1, 0, 0}, {0, 0, 0, 1, 0, 0}
        } ) );
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

        l_graph.addEdge( "ma", 1, 2 );
        l_graph.addEdge( "na", 2, 3 );
        l_graph.addEdge( "oa", 3, 4 );
        l_graph.addEdge( "pa", 2, 6 );

        new CDistancePath().execute(
            null,
            false,
            Stream.of( "defaultweight", 2, l_graph, 1, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 6D );
    }

    /**
     * test shortest-path
     */
    @Test
    public final void shortestpath()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        l_graph.addEdge( "mb", 1, 2 );
        l_graph.addEdge( "nb", 2, 3 );
        l_graph.addEdge( "ob", 3, 4 );
        l_graph.addEdge( "pb", 2, 6 );

        new CShortestPath().execute(
            null,
            false,
            Stream.of( "defaultweight", 2, l_graph, 1, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "mb", "nb", "ob" ).toArray() );
    }


    /**
     * test spanning-tree action
     */
    @Test
    public final void spanningtree()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        l_graph.addEdge( "edge12", 1, 2 );
        l_graph.addEdge( "edge26", 2, 6 );
        l_graph.addEdge( "edge56", 5, 6 );
        l_graph.addEdge( "edge45", 4, 5 );
        l_graph.addEdge( "edge34", 3, 4 );
        l_graph.addEdge( "edge35", 3, 5 );
        l_graph.addEdge( "edge46", 4, 6 );
        l_graph.addEdge( "edge24", 2, 4 );
        l_graph.addEdge( "edge23", 2, 3 );
        l_graph.addEdge( "edge13", 1, 3 );

        l_graph.addEdge( "edge28", 2, 8 );
        l_graph.addEdge( "edge78", 7, 8 );
        l_graph.addEdge( "edge67", 6, 7 );
        l_graph.addEdge( "edge68", 6, 8 );

        l_graph.addEdge( "edge89", 8, 9 );
        l_graph.addEdge( "edge910", 9, 10 );
        l_graph.addEdge( "edge710", 7, 10 );
        l_graph.addEdge( "edge79", 7, 9 );

        l_graph.addEdge( "edge110", 1, 10 );
        l_graph.addEdge( "edge19", 1, 9 );
        l_graph.addEdge( "edge29", 2, 9 );

        Assert.assertEquals( l_graph.getEdgeCount(), 21 );
        Assert.assertEquals( l_graph.getVertexCount(), 10 );

        new CSpanningTree().execute(
            null,
            false,
            Stream.of( l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.get( 0 ).<Graph<Integer,String>>raw().getEdges().toArray(),
            Stream.of( "edge56", "edge45", "edge34", "edge110", "edge28", "edge26", "edge710", "edge29", "edge910" ).toArray()
        );


        final Map<Object, Object> l_weight = new HashMap<>();
        StreamUtils.windowed(
            Stream.of(
                "edge13", 18,
                "edge23", 10,
                "edge34", 3,
                "edge35", 4,
                "edge24", 9,
                "edge46", 5,
                "edge45", 1,
                "edge56", 4,
                "edge26", 7,
                "edge68", 9,
                "edge67", 9,
                "edge28", 8,
                "edge78", 2,
                "edge79", 4,
                "edge710", 6,
                "edge89", 2,
                "edge910", 3,
                "edge110", 9,
                "edge19", 9,
                "edge29", 9,
                "edge12", 8
            ),
            2,
            2
        ).forEach( i -> l_weight.put( i.get( 0 ), i.get( 1 ) ) );

        new CSpanningTree().execute(
            null,
            false,
            Stream.of( l_weight, l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.get( 1 ).<Graph<Integer,String>>raw().getEdges().toArray(),
            Stream.of( "edge12", "edge56", "edge45", "edge34", "edge78", "edge89", "edge28", "edge26", "edge910" ).toArray()
        );
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


        l_return.clear();

        new CEdgeListSingle().execute(
            null,
            true,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "edgeAA1", "edgeA1" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "edgeA2" ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test edgelist multiple
     */
    @Test
    public final void edgelistmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseMultigraph<>();

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


        l_return.clear();

        new CEdgeListMultiple().execute(
            null,
            true,
            Stream.of( l_graph, 1, 2, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "p", "o" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "q", "r", "s" ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test endpoint single
     */
    @Test
    public final void endpointsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new UndirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new UndirectedSparseGraph<>();

        l_graph1.addEdge( "edgeA", 1, 2 );
        l_graph2.addEdge( "edgeA", 2, 3 );

        new CEndPointSingle().execute(
            null,
            false,
            Stream.of( "edgeA", l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 1, 2, 2, 3 ).toArray() );
    }


    /**
     * test endpoint multiple
     */
    @Test
    public final void endpointmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        l_graph.addEdge( "edge1", 1, 2 );
        l_graph.addEdge( "edge2", 2, 4 );
        l_graph.addEdge( "edge3", 4, 3 );

        new CEndPointMultiple().execute(
            null,
            false,
            Stream.of( l_graph, "edge1", "edge3" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 1, 2, 4, 3 ).toArray() );
    }


    /**
     * test in-degree single
     */
    @Test
    public final void indegreesingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new SparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        l_graph1.addEdge( "i", 1, 2 );
        l_graph1.addEdge( "j", 3, 2 );
        l_graph2.addEdge( "k", 4, 2 );
        l_graph2.addEdge( "l", 6, 2 );
        l_graph2.addEdge( "m", 8, 2 );

        new CInDegreeSingle().execute(
            null,
            false,
            Stream.of( 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 2L, 3L ).toArray() );
    }


    /**
     * test in-degree multiple
     */
    @Test
    public final void indegreemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addEdge( "n", 1, 2 );
        l_graph.addEdge( "m", 3, 2 );
        l_graph.addEdge( "p", 1, 4 );

        new CInDegreeMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 2L, 1L ).toArray() );
    }

    /**
     * test in-edge
     */
    @Test
    public final void inedgessingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new SparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        l_graph1.addEdge( "inedgesingle1", 1, 2 );
        l_graph1.addEdge( "inedgesingle2", 2, 2 );
        l_graph2.addEdge( "inedgesingle3", 1, 2 );

        new CInEdgesSingle().execute(
            null,
            false,
            Stream.of( 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "inedgesingle1", "inedgesingle2" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "inedgesingle3" ).toArray() );


        l_return.clear();

        new CInEdgesSingle().execute(
            null,
            true,
            Stream.of( 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "inedgesingle1", "inedgesingle2" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "inedgesingle3" ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test in-edge
     */
    @Test
    public final void inedgesmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addEdge( "inedge3", 1, 2 );
        l_graph.addEdge( "inedge4", 2, 2 );
        l_graph.addEdge( "inedge5", 1, 3 );

        new CInEdgesMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "inedge3", "inedge4" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "inedge5" ).toArray() );


        l_return.clear();

        new CInEdgesMultiple().execute(
            null,
            true,
            Stream.of( l_graph, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "inedge3", "inedge4" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "inedge5" ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test out-degree single
     */
    @Test
    public final void outdegreesingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseGraph<>();

        l_graph1.addEdge( "outdegree1", 1, 2 );
        l_graph1.addEdge( "outdegree2", 2, 2 );
        l_graph2.addEdge( "outdegree3", 1, 2 );

        new COutDegreeSingle().execute(
            null,
            false,
            Stream.of( 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 1L, 0L ).toArray() );
    }


    /**
     * test out-degree single
     */
    @Test
    public final void outdegreemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseGraph<>();

        l_graph.addEdge( "outdegree4", 1, 2 );
        l_graph.addEdge( "outdegree5", 2, 2 );
        l_graph.addEdge( "outdegree6", 1, 2 );

        new COutDegreeMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 1L, 1L ).toArray() );
    }


    /**
     * test incident-count single
     */
    @Test
    public final void incidentcountsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new SparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        l_graph1.addEdge( "incident1", 1, 2 );
        l_graph2.addEdge( "incident1", 1, 2 );

        new CIncidentCountSingle().execute(
            null,
            false,
            Stream.of( "incident1", l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 2L, 2L ).toArray() );
    }


    /**
     * test incident-count single
     */
    @Test
    public final void incidentcountmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addEdge( "incident1", 1, 2 );
        l_graph.addEdge( "incident2", 1, 2 );

        new CIncidentCountMultiple().execute(
            null,
            false,
            Stream.of( l_graph, "incident1", "incident2" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 2L, 0L ).toArray() );
    }


    /**
     * test incident-vertices single
     */
    @Test
    public final void incidentverticessingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        l_graph1.addEdge( "incidentsingleA", 2, 1 );
        l_graph2.addEdge( "incidentsingleA", 1, 2 );

        new CIncidentVerticesSingle().execute(
            null,
            false,
            Stream.of( "incidentsingleA", l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2, 1 ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 1, 2 ).toArray() );


        l_return.clear();

        new CIncidentVerticesSingle().execute(
            null,
            true,
            Stream.of( "incidentsingleA", l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2, 1 ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 1, 2 ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test incident-vertices multiple
     */
    @Test
    public final void incidentverticesmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "incidentA", 2, 1 );
        l_graph.addEdge( "incidentB", 3, 2 );
        l_graph.addEdge( "incidentC", 5, 6 );
        l_graph.addEdge( "incidentD", 7, 6 );

        new CIncidentVerticesMultiple().execute(
            null,
            false,
            Stream.of( l_graph, "incidentA", "incidentB" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2, 1 ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 3, 2 ).toArray() );


        l_return.clear();

        new CIncidentVerticesMultiple().execute(
            null,
            true,
            Stream.of( l_graph, "incidentA", "incidentB" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2, 1 ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 3, 2 ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test is-successor single
     */
    @Test
    public final void issuccessorsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "successor1", 2, 1 );
        l_graph2.addEdge( "successor2", 1, 2 );

        new CIsSuccessorSingle().execute(
            null,
            false,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Boolean>raw ).toArray(),
            Stream.of( false, true ).toArray()
        );
    }

    /**
     * test is-successor multiple
     */
    @Test
    public final void issuccesormultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "successor1", 2, 1 );
        l_graph.addEdge( "successor2", 3, 1 );

        new CIsSuccessorMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 2, 3, 1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Boolean>raw ).toArray(),
            Stream.of( false, true ).toArray()
        );
    }


    /**
     * test is-predecessor single
     */
    @Test
    public final void ispredecessorsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "predecessor1", 2, 1 );
        l_graph2.addEdge( "predecessor2", 1, 2 );

        new CIsPredecessorSingle().execute(
            null,
            false,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Boolean>raw ).toArray(),
            Stream.of( true, false ).toArray()
        );
    }

    /**
     * test is-predecessor multiple
     */
    @Test
    public final void ispredecessormultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "predecessor1", 2, 1 );
        l_graph.addEdge( "predecessor2", 3, 1 );

        new CIsPredecessorMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 2, 3, 1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Boolean>raw ).toArray(),
            Stream.of( true, false ).toArray()
        );
    }


    /**
     * test neighborscount single
     */
    @Test
    public final void neighborscountsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "neighborcount1", 2, 1 );
        l_graph1.addEdge( "neighborcount2", 3, 1 );
        l_graph2.addEdge( "neighborcount1", 2, 1 );
        l_graph2.addEdge( "neighborcount2", 3, 1 );
        l_graph2.addEdge( "neighborcount3", 4, 1 );

        new CNeighborsCountSingle().execute(
            null,
            false,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Number>raw ).map( Number::longValue ).toArray(),
            Stream.of( 2L, 3L ).toArray()
        );
    }


    /**
     * test neighborscount single
     */
    @Test
    public final void neighborscountmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "neighborcount1", 2, 1 );
        l_graph.addEdge( "neighborcount2", 3, 1 );
        l_graph.addEdge( "neighborcount3", 5, 3 );
        l_graph.addEdge( "neighborcount4", 4, 1 );

        new CNeighborsCountMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Number>raw ).map( Number::longValue ).toArray(),
            Stream.of( 3L, 2L ).toArray()
        );
    }


    /**
     * test is-neighbor single
     */
    @Test
    public final void isneighborsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "isneighbor1", 2, 1 );
        l_graph1.addEdge( "isneighbor2", 3, 1 );
        l_graph2.addEdge( "isneighbor1", 2, 1 );
        l_graph2.addEdge( "isneighbor2", 3, 1 );
        l_graph2.addEdge( "isneighbor3", 4, 1 );

        new CIsNeighborSingle().execute(
            null,
            false,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Boolean>raw ).toArray(),
            Stream.of( true, true ).toArray()
        );
    }


    /**
     * test is-neighbor multiple
     */
    @Test
    public final void isneighbormultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "isneighbor10", 2, 1 );
        l_graph.addEdge( "isneighbor20", 3, 1 );
        l_graph.addEdge( "isneighbor10", 2, 1 );
        l_graph.addEdge( "isneighbor20", 3, 1 );
        l_graph.addEdge( "isneighbor30", 4, 1 );

        new CIsNeighborMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 2, 3, 4, 3, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Boolean>raw ).toArray(),
            Stream.of( true, false, false ).toArray()
        );
    }


    /**
     * test neigbors single
     */
    @Test
    public final void neigborssingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "isneighbor1", 2, 1 );
        l_graph1.addEdge( "isneighbor2", 3, 1 );
        l_graph2.addEdge( "isneighbor1", 2, 1 );
        l_graph2.addEdge( "isneighbor2", 3, 1 );
        l_graph2.addEdge( "isneighbor3", 4, 1 );

        new CNeighborsSingle().execute(
            null,
            false,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2, 3 ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 2, 3, 4 ).toArray() );


        l_return.clear();

        new CNeighborsSingle().execute(
            null,
            true,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2, 3 ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 2, 3, 4 ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test neigbors multiple
     */
    @Test
    public final void neigborsmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "neighbors1", 2, 1 );
        l_graph.addEdge( "neighbors2", 3, 1 );
        l_graph.addEdge( "neighbors1", 2, 1 );
        l_graph.addEdge( "neighbors2", 3, 1 );
        l_graph.addEdge( "neighbors3", 4, 1 );

        new CNeighborsMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2, 3, 4 ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 1 ).toArray() );


        l_return.clear();

        new CNeighborsMultiple().execute(
            null,
            true,
            Stream.of( l_graph, 1, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2, 3, 4 ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 1 ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test is-incident single
     */
    @Test
    public final void isincidentsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "isincident1", 2, 1 );
        l_graph1.addEdge( "isincident2", 3, 1 );
        l_graph2.addEdge( "isincident1", 2, 1 );
        l_graph2.addEdge( "isincident2", 3, 1 );
        l_graph2.addEdge( "isincident3", 4, 1 );

        new CIsIncidentSingle().execute(
            null,
            false,
            Stream.of( 1, "isincident2", l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::raw ).toArray(),
            Stream.of( true, true ).toArray()
        );
    }


    /**
     * test is-incident multiple
     */
    @Test
    public final void isincidentmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "isincident10", 2, 1 );
        l_graph.addEdge( "isincident20", 3, 1 );
        l_graph.addEdge( "isincident10", 2, 1 );
        l_graph.addEdge( "isincident20", 3, 1 );
        l_graph.addEdge( "isincident30", 4, 1 );

        new CIsIncidentMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, "isincident10", 2, "isincident20" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::raw ).toArray(),
            Stream.of( true, false ).toArray()
        );
    }

    /**
     * test opposite single
     */
    @Test
    public final void oppositesingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "opposite", 2, 1 );
        l_graph2.addEdge( "opposite", 3, 1 );

        new COppositeSingle().execute(
            null,
            false,
            Stream.of( 1, "opposite", l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 2, 3 ).toArray() );
    }


    /**
     * test opposite multiple
     */
    @Test
    public final void oppositemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "opposite1", 2, 1 );
        l_graph.addEdge( "opposite2", 3, 4 );

        new COppositeMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, "opposite1", 3, "opposite2" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 2, 4 ).toArray() );
    }


    /**
     * test outedges single
     */
    @Test
    public final void outedgessingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "outedgesingle1", 1, 2 );
        l_graph1.addEdge( "outedgesingle2", 1, 3 );
        l_graph2.addEdge( "outedgesingle3", 1, 4 );
        l_graph2.addEdge( "outedgesingle4", 1, 5 );

        new COutEdgesSingle().execute(
            null,
            false,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "outedgesingle2", "outedgesingle1" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "outedgesingle4", "outedgesingle3" ).toArray() );


        l_return.clear();

        new COutEdgesSingle().execute(
            null,
            true,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "outedgesingle2", "outedgesingle1" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "outedgesingle4", "outedgesingle3" ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test outedges multiple
     */
    @Test
    public final void outedgemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "outedgemultiple1", 1, 2 );
        l_graph.addEdge( "outedgemultiple2", 1, 3 );
        l_graph.addEdge( "outedgemultiple3", 2, 4 );
        l_graph.addEdge( "outedgemultiple4", 2, 5 );

        new COutEdgesMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "outedgemultiple2", "outedgemultiple1" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "outedgemultiple4", "outedgemultiple3" ).toArray() );


        l_return.clear();

        new COutEdgesMultiple().execute(
            null,
            true,
            Stream.of( l_graph, 1, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "outedgemultiple2", "outedgemultiple1" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( "outedgemultiple4", "outedgemultiple3" ).toArray() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
        Assert.assertEquals( l_return.get( 1 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test predecessor count single
     */
    @Test
    public final void predecessorcountsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "predecessorcountsingle1", 1, 2 );
        l_graph1.addEdge( "predecessorcountsingle2", 3, 1 );
        l_graph2.addEdge( "predecessorcountsingle3", 2, 1 );
        l_graph2.addEdge( "predecessorcountsingle4", 5, 1 );

        new CPredecessorCountSingle().execute(
            null,
            false,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 1L, 2L ).toArray() );
    }


    /**
     * test predecessor count multiple
     */
    @Test
    public final void predecessorcountmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "predecessorcountmultiple1", 1, 2 );
        l_graph.addEdge( "predecessorcountmultiple2", 1, 3 );
        l_graph.addEdge( "predecessorcountmultiple3", 4, 2 );
        l_graph.addEdge( "predecessorcountmultiple4", 5, 1 );

        new CPredecessorCountMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 1L, 2L ).toArray() );
    }


    /**
     * test remove vertex multiple
     */
    @Test
    public final void vertexremovemultiple()
    {
        final Graph<Integer, String> l_graph = new DirectedSparseGraph<>();

        l_graph.addVertex( 5 );
        l_graph.addVertex( 3 );
        l_graph.addEdge( "vertexremovemultiple", 1, 2 );

        new CRemoveVertexMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 3, 5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );


        Assert.assertEquals( l_graph.getEdgeCount(), 0 );
        Assert.assertArrayEquals( l_graph.getVertices().toArray(), Stream.of( 2 ).toArray() );
    }


    /**
     * test remove vertex single
     */
    @Test
    public final void vertextremovesingle()
    {
        final Graph<Integer, String> l_graph1 = new DirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseGraph<>();

        l_graph1.addVertex( 5 );
        l_graph1.addVertex( 3 );
        l_graph1.addEdge( "vertexremovesingle", 5, 7 );
        l_graph2.addVertex( 5 );
        l_graph2.addVertex( 7 );
        l_graph2.addEdge( "vertexremovesingle", 7, 1 );

        new CRemoveVertexSingle().execute(
            null,
            false,
            Stream.of( 5, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_graph1.getEdgeCount(), 0 );
        Assert.assertEquals( l_graph2.getEdgeCount(), 1 );

        Assert.assertArrayEquals( l_graph1.getVertices().toArray(), Stream.of( 3, 7 ).toArray() );
        Assert.assertArrayEquals( l_graph2.getVertices().toArray(), Stream.of( 1, 7 ).toArray() );
    }


    /**
     * test successor count single
     */
    @Test
    public final void successorcountsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        final Graph<Integer, String> l_graph1 = new DirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseGraph<>();

        l_graph1.addEdge( "successorcountsingle1", 1, 5 );
        l_graph1.addEdge( "successorcountsingle2", 1, 7 );
        l_graph1.addEdge( "successorcountsingle3", 1, 3 );

        l_graph2.addEdge( "successorcountsingle4", 1, 2 );
        l_graph2.addEdge( "successorcountsingle5", 1, 9 );

        new CSuccessorCountSingle().execute(
            null,
            false,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 3L, 2L ).toArray() );
    }


    /**
     * test successor count multiple
     */
    @Test
    public final void successorcountmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseGraph<>();

        l_graph.addEdge( "successorcountmultiple1", 1, 5 );
        l_graph.addEdge( "successorcountmultiple2", 1, 7 );
        l_graph.addEdge( "successorcountmultiple3", 1, 3 );
        l_graph.addEdge( "successorcountmultiple4", 3, 5 );

        new CSuccessorCountMultiple().execute(
            null,
            false,
            Stream.of( l_graph, 1, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 3L, 1L ).toArray() );
    }


    /**
     * test remove edge single
     */
    @Test
    public final void removeedgesingle()
    {
        final Graph<Integer, String> l_graph1 = new DirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseGraph<>();

        l_graph1.addEdge( "removeedgesingle1", 1, 5 );
        l_graph1.addEdge( "removeedgesingle2", 1, 7 );
        l_graph1.addEdge( "removeedgesingle3", 1, 3 );

        l_graph2.addEdge( "removeedgesingle1", 1, 2 );
        l_graph2.addEdge( "removeedgesingle5", 1, 9 );

        new CRemoveEdgeSingle().execute(
            null,
            false,
            Stream.of( "removeedgesingle1", l_graph1, l_graph2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_graph1.getEdges().toArray(), Stream.of( "removeedgesingle2", "removeedgesingle3" ).toArray() );
        Assert.assertArrayEquals( l_graph2.getEdges().toArray(), Stream.of( "removeedgesingle5" ).toArray() );
    }


    /**
     * test remove edge multiple
     */
    @Test
    public final void removeedgemultiple()
    {
        final Graph<Integer, String> l_graph = new DirectedSparseGraph<>();

        l_graph.addEdge( "removeedgesingle1", 1, 5 );
        l_graph.addEdge( "removeedgesingle2", 1, 7 );
        l_graph.addEdge( "removeedgesingle3", 1, 3 );
        l_graph.addEdge( "removeedgesingle4", 5, 3 );
        l_graph.addEdge( "removeedgesingle5", 5, 7 );

        new CRemoveEdgeMultiple().execute(
            null,
            false,
            Stream.of( l_graph, "removeedgesingle2", "removeedgesingle5" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_graph.getEdges().toArray(), Stream.of( "removeedgesingle1", "removeedgesingle4", "removeedgesingle3" ).toArray() );
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
