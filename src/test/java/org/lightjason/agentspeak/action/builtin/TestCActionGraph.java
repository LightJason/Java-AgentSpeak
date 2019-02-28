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

package org.lightjason.agentspeak.action.builtin;

import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
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
import org.lightjason.agentspeak.action.builtin.graph.CAddEdgeMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CAddEdgeSingle;
import org.lightjason.agentspeak.action.builtin.graph.CAddVertexMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CAddVertexSingle;
import org.lightjason.agentspeak.action.builtin.graph.CAdjacencyMatrix;
import org.lightjason.agentspeak.action.builtin.graph.CContainsEdge;
import org.lightjason.agentspeak.action.builtin.graph.CContainsVertex;
import org.lightjason.agentspeak.action.builtin.graph.CCreate;
import org.lightjason.agentspeak.action.builtin.graph.CDegreeMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CDegreeSingle;
import org.lightjason.agentspeak.action.builtin.graph.CDistancePath;
import org.lightjason.agentspeak.action.builtin.graph.CEdgeCount;
import org.lightjason.agentspeak.action.builtin.graph.CEdgeListMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CEdgeListSingle;
import org.lightjason.agentspeak.action.builtin.graph.CEdges;
import org.lightjason.agentspeak.action.builtin.graph.CEndPointMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CEndPointSingle;
import org.lightjason.agentspeak.action.builtin.graph.CFindEdgeMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CFindEdgeSingle;
import org.lightjason.agentspeak.action.builtin.graph.CInDegreeMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CInDegreeSingle;
import org.lightjason.agentspeak.action.builtin.graph.CInEdgesMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CInEdgesSingle;
import org.lightjason.agentspeak.action.builtin.graph.CIncidentCountMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CIncidentCountSingle;
import org.lightjason.agentspeak.action.builtin.graph.CIncidentVerticesMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CIncidentVerticesSingle;
import org.lightjason.agentspeak.action.builtin.graph.CIsIncidentMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CIsIncidentSingle;
import org.lightjason.agentspeak.action.builtin.graph.CIsNeighborMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CIsNeighborSingle;
import org.lightjason.agentspeak.action.builtin.graph.CIsPredecessorMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CIsPredecessorSingle;
import org.lightjason.agentspeak.action.builtin.graph.CIsSuccessorMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CIsSuccessorSingle;
import org.lightjason.agentspeak.action.builtin.graph.CNeighborsCountMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CNeighborsCountSingle;
import org.lightjason.agentspeak.action.builtin.graph.CNeighborsMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CNeighborsSingle;
import org.lightjason.agentspeak.action.builtin.graph.COppositeMultiple;
import org.lightjason.agentspeak.action.builtin.graph.COppositeSingle;
import org.lightjason.agentspeak.action.builtin.graph.COutDegreeMultiple;
import org.lightjason.agentspeak.action.builtin.graph.COutDegreeSingle;
import org.lightjason.agentspeak.action.builtin.graph.COutEdgesMultiple;
import org.lightjason.agentspeak.action.builtin.graph.COutEdgesSingle;
import org.lightjason.agentspeak.action.builtin.graph.CPredecessorCountMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CPredecessorCountSingle;
import org.lightjason.agentspeak.action.builtin.graph.CRemoveEdgeMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CRemoveEdgeSingle;
import org.lightjason.agentspeak.action.builtin.graph.CRemoveVertexMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CRemoveVertexSingle;
import org.lightjason.agentspeak.action.builtin.graph.CShortestPath;
import org.lightjason.agentspeak.action.builtin.graph.CSpanningTree;
import org.lightjason.agentspeak.action.builtin.graph.CSuccessorCountMultiple;
import org.lightjason.agentspeak.action.builtin.graph.CSuccessorCountSingle;
import org.lightjason.agentspeak.action.builtin.graph.CVertexCount;
import org.lightjason.agentspeak.action.builtin.graph.CVertices;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

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
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "sparse", "SPARSEMULTI", "DIRECTEDSPARSE", "DIRECTEDSPARSEMULTI", "UNDIRECTEDSPARSE", "UNDIRECTEDSPARSEMULTI" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 6, l_return.size() );
        Assert.assertTrue( l_return.stream().map( ITerm::raw ).allMatch( i -> i instanceof Graph<?, ?> ) );
    }


    /**
     * test add-vertex single
     */
    @Test
    public void addvertexsingle()
    {
        final Graph<?, ?> l_graph1 = new SparseGraph<>();
        final Graph<?, ?> l_graph2 = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( i ->
                               new CAddVertexSingle().execute(
                                   false,
                                   IContext.EMPTYPLAN,
                                   Stream.of( i, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                                   Collections.emptyList()
                               ) );

        Assert.assertArrayEquals( IntStream.range( 0, 5 ).boxed().toArray(), l_graph1.getVertices().toArray() );
        Assert.assertArrayEquals( IntStream.range( 0, 5 ).boxed().toArray(), l_graph2.getVertices().toArray() );
    }


    /**
     * test add-vertex multiple
     */
    @Test
    public void addvertexmultiple()
    {
        final Graph<?, ?> l_graph = new SparseGraph<>();

        new CAddVertexMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, "x", "y", "z" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( Stream.of( "x", "y", "z" ).toArray(), l_graph.getVertices().toArray() );
    }



    /**
     * test add-edge single
     */
    @Test
    public void addedgesingle()
    {
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        new CAddEdgeSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "xy", 1, 2, l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        new CAddEdgeSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "bar", 4, 5, l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( 2, l_graph.getEdgeCount() );
        Assert.assertEquals( 1, (long) l_graph.getEndpoints( "xy" ).getFirst() );
        Assert.assertEquals( 2, (long) l_graph.getEndpoints( "xy" ).getSecond() );
        Assert.assertEquals( 4, (long) l_graph.getEndpoints( "bar" ).getFirst() );
        Assert.assertEquals( 5, (long) l_graph.getEndpoints( "bar" ).getSecond() );
    }


    /**
     * test add-edge multiple
     */
    @Test
    public void addedgemultiple()
    {
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        new CAddEdgeMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, "foo", 1, 1, "bar", 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( Stream.of( "bar", "foo" ).toArray(), l_graph.getEdges().toArray() );
    }



    /**
     * test vertex-count
     */
    @Test
    public void vertexcount()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        new CVertexCount().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( (double) l_graph.getVertexCount(), l_return.get( 0 ).<Number>raw() );
    }


    /**
     * test edge-count
     */
    @Test
    public void edgecount()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addEdge( "a", 0, 1 );
        l_graph.addEdge( "b", 0, 2 );
        l_graph.addEdge( "c", 1, 3 );
        l_graph.addEdge( "d", 3, 4 );


        new CEdgeCount().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( 4D, l_return.get( 0 ).<Number>raw() );
    }


    /**
     * test vertices
     */
    @Test
    public void vertices()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        new CVertices().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertArrayEquals( IntStream.range( 0, 5 ).boxed().toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
    }


    /**
     * test edges
     */
    @Test
    public void edges()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addEdge( "a", 0, 1 );
        l_graph.addEdge( "b", 0, 2 );
        l_graph.addEdge( "c", 1, 3 );
        l_graph.addEdge( "d", 3, 4 );

        new CEdges().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "a", "b", "c", "d" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
    }


    /**
     * test adjacency matrix
     */
    @Test
    public void adjacencymatrix()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );


        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 1, 2, 3, 4, 5, 6 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( new DenseDoubleMatrix2D( new double[][]{
            {2, 1, 0, 0, 1, 0}, {1, 0, 1, 0, 1, 0}, {0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 1}, {1, 1, 0, 1, 0, 0}, {0, 0, 0, 1, 0, 0}
        } ), l_return.get( 0 ).raw() );
    }


    /**
     * test contains-edge
     */
    @Test
    public void containsedge()
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
            false, IContext.EMPTYPLAN,
            Stream.of( "yyy", l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertFalse( l_return.get( 1 ).<Boolean>raw() );
    }


    /**
     * test contains-vertex
     */
    @Test
    public void containsvertex()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 5, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertFalse( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertTrue( l_return.get( 1 ).<Boolean>raw() );
    }


    /**
     * test degree multiple
     */
    @Test
    public void degreemultiple()
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
            false, IContext.EMPTYPLAN,
            Stream.concat(
                Stream.of( l_graph ),
                IntStream.range( 1, 7 ).boxed()
            ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( Stream.of( 3D, 3D, 2D, 3D, 3D, 1D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test degree single
     */
    @Test
    public void degreesingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( Stream.of( 3D, 2D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test distance-path
     */
    @Test
    public void distancepath()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        l_graph.addEdge( "ma", 1, 2 );
        l_graph.addEdge( "na", 2, 3 );
        l_graph.addEdge( "oa", 3, 4 );
        l_graph.addEdge( "pa", 2, 6 );

        new CDistancePath().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "defaultweight", 2, l_graph, 1, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( 6D, l_return.get( 0 ).<Number>raw() );
    }

    /**
     * test shortest-path
     */
    @Test
    public void shortestpath()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        l_graph.addEdge( "mb", 1, 2 );
        l_graph.addEdge( "nb", 2, 3 );
        l_graph.addEdge( "ob", 3, 4 );
        l_graph.addEdge( "pb", 2, 6 );

        new CShortestPath().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "defaultweight", 2, l_graph, 1, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( Stream.of( "mb", "nb", "ob" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
    }


    /**
     * test spanning-tree action
     */
    @Test
    public void spanningtree()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        l_graph.addEdge( "spanningtreeedge12", 1, 2 );
        l_graph.addEdge( "spanningtreeedge26", 2, 6 );
        l_graph.addEdge( "spanningtreeedge56", 5, 6 );
        l_graph.addEdge( "spanningtreeedge45", 4, 5 );
        l_graph.addEdge( "spanningtreeedge34", 3, 4 );
        l_graph.addEdge( "spanningtreeedge35", 3, 5 );
        l_graph.addEdge( "spanningtreeedge46", 4, 6 );
        l_graph.addEdge( "spanningtreeedge24", 2, 4 );
        l_graph.addEdge( "spanningtreeedge23", 2, 3 );
        l_graph.addEdge( "spanningtreeedge13", 1, 3 );

        l_graph.addEdge( "spanningtreeedge28", 2, 8 );
        l_graph.addEdge( "spanningtreeedge78", 7, 8 );
        l_graph.addEdge( "spanningtreeedge67", 6, 7 );
        l_graph.addEdge( "spanningtreeedge68", 6, 8 );

        l_graph.addEdge( "spanningtreeedge89", 8, 9 );
        l_graph.addEdge( "spanningtreeedge910", 9, 10 );
        l_graph.addEdge( "spanningtreeedge710", 7, 10 );
        l_graph.addEdge( "spanningtreeedge79", 7, 9 );

        l_graph.addEdge( "spanningtreeedge110", 1, 10 );
        l_graph.addEdge( "spanningtreeedge19", 1, 9 );
        l_graph.addEdge( "spanningtreeedge29", 2, 9 );

        Assert.assertEquals( l_graph.getEdgeCount(), 21 );
        Assert.assertEquals( l_graph.getVertexCount(), 10 );

        new CSpanningTree().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( "spanningtreeedge19", "spanningtreeedge710", "spanningtreeedge68", "spanningtreeedge46", "spanningtreeedge24", "spanningtreeedge89",
                       "spanningtreeedge45", "spanningtreeedge23", "spanningtreeedge67" ).toArray(),
            l_return.get( 0 ).<Graph<Integer, String>>raw().getEdges().toArray()
        );


        final Map<Object, Object> l_weight = new HashMap<>();
        StreamUtils.windowed(
            Stream.of(
                "spanningtreeedge13", 18,
                "spanningtreeedge23", 10,
                "spanningtreeedge34", 3,
                "spanningtreeedge35", 4,
                "spanningtreeedge24", 9,
                "spanningtreeedge46", 5,
                "spanningtreeedge45", 1,
                "spanningtreeedge56", 4,
                "spanningtreeedge26", 7,
                "spanningtreeedge68", 9,
                "spanningtreeedge67", 9,
                "spanningtreeedge28", 8,
                "spanningtreeedge78", 2,
                "spanningtreeedge79", 4,
                "spanningtreeedge710", 6,
                "spanningtreeedge89", 2,
                "spanningtreeedge910", 3,
                "spanningtreeedge110", 9,
                "spanningtreeedge19", 9,
                "spanningtreeedge29", 9,
                "spanningtreeedge12", 8
            ),
            2,
            2
        ).forEach( i -> l_weight.put( i.get( 0 ), i.get( 1 ) ) );

        new CSpanningTree().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_weight, l_graph ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( "spanningtreeedge910", "spanningtreeedge12", "spanningtreeedge56", "spanningtreeedge45", "spanningtreeedge34", "spanningtreeedge89",
                       "spanningtreeedge78", "spanningtreeedge26", "spanningtreeedge28" ).toArray(),
            l_return.get( 1 ).<Graph<Integer, String>>raw().getEdges().toArray()
        );
    }


    /**
     * test find-edge single
     */
    @Test
    public void findedgesingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new UndirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new UndirectedSparseGraph<>();

        l_graph1.addEdge( "search", 1, 2 );
        l_graph1.addEdge( "notsearch", 1, 2 );
        l_graph2.addEdge( "xxx", 1, 2 );

        new CFindEdgeSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( Stream.of( "search", "xxx" ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test find-edge multiple
     */
    @Test
    public void findedgemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        l_graph.addEdge( "edge12", 1, 2 );
        l_graph.addEdge( "edge23", 2, 3 );
        l_graph.addEdge( "edge34", 3, 4 );
        l_graph.addEdge( "edge13", 1, 3 );
        l_graph.addEdge( "edge24", 2, 4 );

        new CFindEdgeMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2, 2, 3, 3, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( Stream.of( "edge12", "edge23", "edge34" ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test edgelist single
     */
    @Test
    public void edgelistsingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "edgeAA1", "edgeA1" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "edgeA2" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new CEdgeListSingle().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "edgeAA1", "edgeA1" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "edgeA2" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test edgelist multiple
     */
    @Test
    public void edgelistmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseMultigraph<>();

        l_graph.addEdge( "o", 1, 2 );
        l_graph.addEdge( "p", 1, 2 );
        l_graph.addEdge( "q", 2, 3 );
        l_graph.addEdge( "r", 2, 3 );
        l_graph.addEdge( "s", 2, 3 );

        new CEdgeListMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2, 2, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "p", "o" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "q", "r", "s" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new CEdgeListMultiple().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2, 2, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "p", "o" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "q", "r", "s" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test endpoint single
     */
    @Test
    public void endpointsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new UndirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new UndirectedSparseGraph<>();

        l_graph1.addEdge( "edgeA", 1, 2 );
        l_graph2.addEdge( "edgeA", 2, 3 );

        new CEndPointSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "edgeA", l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 4, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 1, 2, 2, 3 ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test endpoint multiple
     */
    @Test
    public void endpointmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new UndirectedSparseGraph<>();

        l_graph.addEdge( "edge1", 1, 2 );
        l_graph.addEdge( "edge2", 2, 4 );
        l_graph.addEdge( "edge3", 4, 3 );

        new CEndPointMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, "edge1", "edge3" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 4, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 1, 2, 4, 3 ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test in-degree single
     */
    @Test
    public void indegreesingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2D, 3D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test in-degree multiple
     */
    @Test
    public void indegreemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addEdge( "n", 1, 2 );
        l_graph.addEdge( "m", 3, 2 );
        l_graph.addEdge( "p", 1, 4 );

        new CInDegreeMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 2, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2D, 1D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }

    /**
     * test in-edge
     */
    @Test
    public void inedgessingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new SparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        l_graph1.addEdge( "inedgesingle1", 1, 2 );
        l_graph1.addEdge( "inedgesingle2", 2, 2 );
        l_graph2.addEdge( "inedgesingle3", 1, 2 );

        new CInEdgesSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "inedgesingle1", "inedgesingle2" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "inedgesingle3" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new CInEdgesSingle().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "inedgesingle1", "inedgesingle2" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "inedgesingle3" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test in-edge
     */
    @Test
    public void inedgesmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addEdge( "inedge3", 1, 2 );
        l_graph.addEdge( "inedge4", 2, 2 );
        l_graph.addEdge( "inedge5", 1, 3 );

        new CInEdgesMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 2, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( Stream.of( "inedge3", "inedge4" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "inedge5" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new CInEdgesMultiple().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( l_graph, 2, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( Stream.of( "inedge3", "inedge4" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "inedge5" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test out-degree single
     */
    @Test
    public void outdegreesingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseGraph<>();

        l_graph1.addEdge( "outdegree1", 1, 2 );
        l_graph1.addEdge( "outdegree2", 2, 2 );
        l_graph2.addEdge( "outdegree3", 1, 2 );

        new COutDegreeSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 1D, 0D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test out-degree single
     */
    @Test
    public void outdegreemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseGraph<>();

        l_graph.addEdge( "outdegree4", 1, 2 );
        l_graph.addEdge( "outdegree5", 2, 2 );
        l_graph.addEdge( "outdegree6", 1, 2 );

        new COutDegreeMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 1D, 1D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test incident-count single
     */
    @Test
    public void incidentcountsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new SparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        l_graph1.addEdge( "incident1", 1, 2 );
        l_graph2.addEdge( "incident1", 1, 2 );

        new CIncidentCountSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "incident1", l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2D, 2D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test incident-count single
     */
    @Test
    public void incidentcountmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        l_graph.addEdge( "incident1", 1, 2 );
        l_graph.addEdge( "incident2", 1, 2 );

        new CIncidentCountMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, "incident1", "incident2" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2D, 0D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test incident-vertices single
     */
    @Test
    public void incidentverticessingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new SparseGraph<>();

        l_graph1.addEdge( "incidentsingleA", 2, 1 );
        l_graph2.addEdge( "incidentsingleA", 1, 2 );

        new CIncidentVerticesSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "incidentsingleA", l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 1 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 1, 2 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new CIncidentVerticesSingle().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( "incidentsingleA", l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 1 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 1, 2 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test incident-vertices multiple
     */
    @Test
    public void incidentverticesmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "incidentA", 2, 1 );
        l_graph.addEdge( "incidentB", 3, 2 );
        l_graph.addEdge( "incidentC", 5, 6 );
        l_graph.addEdge( "incidentD", 7, 6 );

        new CIncidentVerticesMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, "incidentA", "incidentB" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 1 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 3, 2 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new CIncidentVerticesMultiple().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( l_graph, "incidentA", "incidentB" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 1 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 3, 2 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test is-successor single
     */
    @Test
    public void issuccessorsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "successor1", 2, 1 );
        l_graph2.addEdge( "successor2", 1, 2 );

        new CIsSuccessorSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( false, true ).toArray(),
            l_return.stream().map( ITerm::<Boolean>raw ).toArray()
        );
    }

    /**
     * test is-successor multiple
     */
    @Test
    public void issuccesormultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "successor1", 2, 1 );
        l_graph.addEdge( "successor2", 3, 1 );

        new CIsSuccessorMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2, 3, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( false, true ).toArray(),
            l_return.stream().map( ITerm::<Boolean>raw ).toArray()
        );
    }


    /**
     * test is-predecessor single
     */
    @Test
    public void ispredecessorsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "predecessor1", 2, 1 );
        l_graph2.addEdge( "predecessor2", 1, 2 );

        new CIsPredecessorSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( true, false ).toArray(),
            l_return.stream().map( ITerm::<Boolean>raw ).toArray()
        );
    }

    /**
     * test is-predecessor multiple
     */
    @Test
    public void ispredecessormultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "predecessor1", 2, 1 );
        l_graph.addEdge( "predecessor2", 3, 1 );

        new CIsPredecessorMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2, 3, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( true, false ).toArray(),
            l_return.stream().map( ITerm::<Boolean>raw ).toArray()
        );
    }


    /**
     * test neighborscount single
     */
    @Test
    public void neighborscountsingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( 2L, 3L ).toArray(),
            l_return.stream().map( ITerm::<Number>raw ).map( Number::longValue ).toArray()
        );
    }


    /**
     * test neighborscount single
     */
    @Test
    public void neighborscountmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "neighborcount1", 2, 1 );
        l_graph.addEdge( "neighborcount2", 3, 1 );
        l_graph.addEdge( "neighborcount3", 5, 3 );
        l_graph.addEdge( "neighborcount4", 4, 1 );

        new CNeighborsCountMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( 3L, 2L ).toArray(),
            l_return.stream().map( ITerm::<Number>raw ).map( Number::longValue ).toArray()
        );
    }


    /**
     * test is-neighbor single
     */
    @Test
    public void isneighborsingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( true, true ).toArray(),
            l_return.stream().map( ITerm::<Boolean>raw ).toArray()
        );
    }


    /**
     * test is-neighbor multiple
     */
    @Test
    public void isneighbormultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "isneighbor10", 2, 1 );
        l_graph.addEdge( "isneighbor20", 3, 1 );
        l_graph.addEdge( "isneighbor10", 2, 1 );
        l_graph.addEdge( "isneighbor20", 3, 1 );
        l_graph.addEdge( "isneighbor30", 4, 1 );

        new CIsNeighborMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2, 3, 4, 3, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( true, false, false ).toArray(),
            l_return.stream().map( ITerm::<Boolean>raw ).toArray()
        );
    }


    /**
     * test neigbors single
     */
    @Test
    public void neigborssingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 3 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 2, 3, 4 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new CNeighborsSingle().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 3 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 2, 3, 4 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test neigbors multiple
     */
    @Test
    public void neigborsmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "neighbors1", 2, 1 );
        l_graph.addEdge( "neighbors2", 3, 1 );
        l_graph.addEdge( "neighbors1", 2, 1 );
        l_graph.addEdge( "neighbors2", 3, 1 );
        l_graph.addEdge( "neighbors3", 4, 1 );

        new CNeighborsMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 3, 4 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 1 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new CNeighborsMultiple().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 3, 4 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 1 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test is-incident single
     */
    @Test
    public void isincidentsingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 1, "isincident2", l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( true, true ).toArray(),
            l_return.stream().map( ITerm::raw ).toArray()
        );
    }


    /**
     * test is-incident multiple
     */
    @Test
    public void isincidentmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "isincident10", 2, 1 );
        l_graph.addEdge( "isincident20", 3, 1 );
        l_graph.addEdge( "isincident10", 2, 1 );
        l_graph.addEdge( "isincident20", 3, 1 );
        l_graph.addEdge( "isincident30", 4, 1 );

        new CIsIncidentMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, "isincident10", 2, "isincident20" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( true, false ).toArray(),
            l_return.stream().map( ITerm::raw ).toArray()
        );
    }

    /**
     * test opposite single
     */
    @Test
    public void oppositesingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "opposite", 2, 1 );
        l_graph2.addEdge( "opposite", 3, 1 );

        new COppositeSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, "opposite", l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 3 ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test opposite multiple
     */
    @Test
    public void oppositemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "opposite1", 2, 1 );
        l_graph.addEdge( "opposite2", 3, 4 );

        new COppositeMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, "opposite1", 3, "opposite2" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 2, 4 ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test outedges single
     */
    @Test
    public void outedgessingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "outedgesingle1", 1, 2 );
        l_graph1.addEdge( "outedgesingle2", 1, 3 );
        l_graph2.addEdge( "outedgesingle3", 1, 4 );
        l_graph2.addEdge( "outedgesingle4", 1, 5 );

        new COutEdgesSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "outedgesingle2", "outedgesingle1" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "outedgesingle4", "outedgesingle3" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new COutEdgesSingle().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "outedgesingle2", "outedgesingle1" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "outedgesingle4", "outedgesingle3" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test outedges multiple
     */
    @Test
    public void outedgemultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "outedgemultiple1", 1, 2 );
        l_graph.addEdge( "outedgemultiple2", 1, 3 );
        l_graph.addEdge( "outedgemultiple3", 2, 4 );
        l_graph.addEdge( "outedgemultiple4", 2, 5 );

        new COutEdgesMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "outedgemultiple2", "outedgemultiple1" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "outedgemultiple4", "outedgemultiple3" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );


        l_return.clear();

        new COutEdgesMultiple().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "outedgemultiple2", "outedgemultiple1" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "outedgemultiple4", "outedgemultiple3" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).raw().getClass() );
    }


    /**
     * test predecessor count single
     */
    @Test
    public void predecessorcountsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph1 = new DirectedSparseMultigraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseMultigraph<>();

        l_graph1.addEdge( "predecessorcountsingle1", 1, 2 );
        l_graph1.addEdge( "predecessorcountsingle2", 3, 1 );
        l_graph2.addEdge( "predecessorcountsingle3", 2, 1 );
        l_graph2.addEdge( "predecessorcountsingle4", 5, 1 );

        new CPredecessorCountSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 1D, 2D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test predecessor count multiple
     */
    @Test
    public void predecessorcountmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseMultigraph<>();

        l_graph.addEdge( "predecessorcountmultiple1", 1, 2 );
        l_graph.addEdge( "predecessorcountmultiple2", 1, 3 );
        l_graph.addEdge( "predecessorcountmultiple3", 4, 2 );
        l_graph.addEdge( "predecessorcountmultiple4", 5, 1 );

        new CPredecessorCountMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 1D, 2D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test remove vertex multiple
     */
    @Test
    public void vertexremovemultiple()
    {
        final Graph<Integer, String> l_graph = new DirectedSparseGraph<>();

        l_graph.addVertex( 5 );
        l_graph.addVertex( 3 );
        l_graph.addEdge( "vertexremovemultiple", 1, 2 );

        new CRemoveVertexMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 3, 5 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );


        Assert.assertEquals( 0, l_graph.getEdgeCount() );
        Assert.assertArrayEquals( Stream.of( 2 ).toArray(), l_graph.getVertices().toArray() );
    }


    /**
     * test remove vertex single
     */
    @Test
    public void vertextremovesingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 5, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( 0, l_graph1.getEdgeCount() );
        Assert.assertEquals( 1, l_graph2.getEdgeCount() );

        Assert.assertArrayEquals( Stream.of( 3, 7 ).toArray(), l_graph1.getVertices().toArray() );
        Assert.assertArrayEquals( Stream.of( 1, 7 ).toArray(), l_graph2.getVertices().toArray() );
    }


    /**
     * test successor count single
     */
    @Test
    public void successorcountsingle()
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
            false, IContext.EMPTYPLAN,
            Stream.of( 1, l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( Stream.of( 3D, 2D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test successor count multiple
     */
    @Test
    public void successorcountmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Graph<Integer, String> l_graph = new DirectedSparseGraph<>();

        l_graph.addEdge( "successorcountmultiple1", 1, 5 );
        l_graph.addEdge( "successorcountmultiple2", 1, 7 );
        l_graph.addEdge( "successorcountmultiple3", 1, 3 );
        l_graph.addEdge( "successorcountmultiple4", 3, 5 );

        new CSuccessorCountMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, 1, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( Stream.of( 3D, 1D ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test remove edge single
     */
    @Test
    public void removeedgesingle()
    {
        final Graph<Integer, String> l_graph1 = new DirectedSparseGraph<>();
        final Graph<Integer, String> l_graph2 = new DirectedSparseGraph<>();

        l_graph1.addEdge( "removeedgesingle1", 1, 5 );
        l_graph1.addEdge( "removeedgesingle2", 1, 7 );
        l_graph1.addEdge( "removeedgesingle3", 1, 3 );

        l_graph2.addEdge( "removeedgesingle1", 1, 2 );
        l_graph2.addEdge( "removeedgesingle5", 1, 9 );

        new CRemoveEdgeSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "removeedgesingle1", l_graph1, l_graph2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( Stream.of( "removeedgesingle2", "removeedgesingle3" ).toArray(), l_graph1.getEdges().toArray() );
        Assert.assertArrayEquals( Stream.of( "removeedgesingle5" ).toArray(), l_graph2.getEdges().toArray() );
    }


    /**
     * test remove edge multiple
     */
    @Test
    public void removeedgemultiple()
    {
        final Graph<Integer, String> l_graph = new DirectedSparseGraph<>();

        l_graph.addEdge( "removeedgesingle1", 1, 5 );
        l_graph.addEdge( "removeedgesingle2", 1, 7 );
        l_graph.addEdge( "removeedgesingle3", 1, 3 );
        l_graph.addEdge( "removeedgesingle4", 5, 3 );
        l_graph.addEdge( "removeedgesingle5", 5, 7 );

        new CRemoveEdgeMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_graph, "removeedgesingle2", "removeedgesingle5" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( Stream.of( "removeedgesingle1", "removeedgesingle4", "removeedgesingle3" ).toArray(), l_graph.getEdges().toArray() );
    }

}
