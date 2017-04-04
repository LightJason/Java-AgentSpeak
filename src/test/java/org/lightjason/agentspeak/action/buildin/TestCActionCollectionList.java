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

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.buildin.collection.list.CComplement;
import org.lightjason.agentspeak.action.buildin.collection.list.CCreate;
import org.lightjason.agentspeak.action.buildin.collection.list.CGet;
import org.lightjason.agentspeak.action.buildin.collection.list.CReverse;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test collection list
 */
public final class TestCActionCollectionList
{

    /**
     * test create empty list
     */
    @Test
    public final void testcreateempty()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            null,
            false,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertTrue( l_return.get( 0 ).<List<?>>raw().isEmpty() );
    }


    /**
     * test create empty synchronized list
     */
    @Test
    public final void testcreateemptysynchronize()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            null,
            true,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertTrue( l_return.get( 0 ).<List<?>>raw().isEmpty() );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Collections.synchronizedList( Collections.emptyList() ).getClass() );
    }


    /**
     * test create non-empty list
     */
    @Test
    public final void testcreate()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            null,
            false,
            Stream.of( "a", 1, "b", true ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().size(), 4 );

        final List<?> l_list = l_return.get( 0 ).raw();

        Assert.assertEquals( l_list.get( 0 ), "a" );
        Assert.assertEquals( l_list.get( 1 ), 1 );
        Assert.assertEquals( l_list.get( 2 ), "b" );
        Assert.assertEquals( l_list.get( 3 ), true );
    }


    /**
     * test complement action
     */
    @Test
    public final void testcomplement()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CComplement().execute(
            null,
            false,
            Stream.of(
                CRawTerm.from( Stream.of( "a", "b", 1, 2 ).collect( Collectors.toList() ) ),
                CRawTerm.from( Stream.of( "x", "y", 4, "a", 5, 1 ).collect( Collectors.toList() ) )
            ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( CCommon.rawvalueAssignableTo( l_return.get( 0 ), List.class ) );
        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().get( 0 ), "b" );
        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().get( 1 ), 2 );
    }


    /**
     * test get action
     */
    @Test
    public final void testget()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = Stream.of( "a", 1, "b", true, "foobar", 56.78 ).collect( Collectors.toList() );

        new CGet().execute(
            null,
            false,
            Stream.of( CRawTerm.from( l_list ), CRawTerm.from( 1 ), CRawTerm.from( 4 ), CRawTerm.from( 5 ) ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 3 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 1 );
        Assert.assertEquals( l_return.get( 1 ).raw(), "foobar" );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 56.78 );
    }

    /**
     * test reverse action
     */
    @Test
    public final void testreverse()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = IntStream.range( 0, 10 ).mapToObj( i -> Math.random() ).collect( Collectors.toList() );

        new CReverse().execute(
            null,
            false,
            Stream.of( CRawTerm.from( l_list ) ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Lists.reverse( l_list ).toArray() );
    }



    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        final TestCActionCollectionList l_test = new TestCActionCollectionList();

        l_test.testcreateempty();
        l_test.testcreateemptysynchronize();
        l_test.testcreate();
        l_test.testcomplement();
        l_test.testget();
        l_test.testreverse();

    }

}
