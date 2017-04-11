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

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.graph.CAddEdge;
import org.lightjason.agentspeak.action.buildin.graph.CAddVertex;
import org.lightjason.agentspeak.action.buildin.graph.CCreate;
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
     * test add-vertex
     */
    @Test
    public final void addvertex()
    {
        final Graph<?, ?> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( i ->
                    new CAddVertex().execute(
                        null,
                        false,
                        Stream.of( i, l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
                        Collections.emptyList(),
                        Collections.emptyList()
                    ) );

        Assert.assertEquals( l_graph.getVertexCount(), 5 );
    }


    /**
     * test add-edge
     */
    @Test
    public final void addedge()
    {
        final Graph<Integer, String> l_graph = new SparseGraph<>();

        IntStream.range( 0, 5 )
                 .boxed()
                 .forEach( l_graph::addVertex );

        new CAddEdge().execute(
            null,
            false,
            Stream.of( "foo", 1, 2, l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        new CAddEdge().execute(
            null,
            false,
            Stream.of( "bar", 4, 5, l_graph ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_graph.getEdgeCount(), 2 );
        Assert.assertEquals( (long) l_graph.getEndpoints( "foo" ).getFirst(), 1 );
        Assert.assertEquals( (long) l_graph.getEndpoints( "foo" ).getSecond(), 2 );
        Assert.assertEquals( (long) l_graph.getEndpoints( "bar" ).getFirst(), 4 );
        Assert.assertEquals( (long) l_graph.getEndpoints( "bar" ).getSecond(), 5 );
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
