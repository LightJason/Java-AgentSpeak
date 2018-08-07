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


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.collection.multimap.CAsMap;
import org.lightjason.agentspeak.action.builtin.collection.multimap.CCreate;
import org.lightjason.agentspeak.action.builtin.collection.multimap.CGetMultiple;
import org.lightjason.agentspeak.action.builtin.collection.multimap.CGetSingle;
import org.lightjason.agentspeak.action.builtin.collection.multimap.CKeys;
import org.lightjason.agentspeak.action.builtin.collection.multimap.CPutMultiple;
import org.lightjason.agentspeak.action.builtin.collection.multimap.CPutSingle;
import org.lightjason.agentspeak.action.builtin.collection.multimap.CValues;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test multimap action
 */
public final class TestCActionCollectionMultimap extends IBaseTest
{

    /**
     * test create
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Collections.emptyList(),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Multimap<?, ?> );
        Assert.assertTrue( l_return.get( 0 ).<Multimap<?, ?>>raw().isEmpty() );
    }

    /**
     * test synchronized create
     */
    @Test
    public void createsynchronized()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            true, IContext.EMPTYPLAN,
            Collections.emptyList(),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( Multimaps.synchronizedSetMultimap( HashMultimap.create() ).getClass(), l_return.get( 0 ).raw().getClass() );
    }


    /**
     * test key and values
     */
    @Test
    public void keysvalues()
    {
        final Multimap<Object, Object> l_map = HashMultimap.create();
        l_map.put( "a", 1 );
        l_map.put( "a", 2 );
        l_map.put( "a", 3 );
        l_map.put( "b", 1 );
        l_map.put( "c", 10 );

        final List<ITerm> l_return = new ArrayList<>();


        new CKeys().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        new CValues().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );


        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "a", "b", "c" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 1, 2, 3, 1, 10 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
    }

    /**
     * test as-map
     */
    @Test
    public void asmap()
    {
        final Multimap<Integer, String> l_map = HashMultimap.create();
        final List<ITerm> l_return = new ArrayList<>();

        final Random l_random = new Random();
        IntStream.range( 0, 5 ).map( i -> l_random.nextInt( 8 ) )
                 .forEach( i -> l_map.put( i, RandomStringUtils.random( 10, "abcdefghijklmnop" ) ) );

        new CAsMap().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Map<?, ?> );
        Assert.assertArrayEquals( l_map.keySet().toArray(), l_return.get( 0 ).<Map<?, ?>>raw().keySet().toArray() );

        Assert.assertArrayEquals(
            l_map.values().toArray(),
            CCommon.flatten( l_return.get( 0 ).<Map<?, ?>>raw().values().stream().map( CRawTerm::of ) ).map( ITerm::raw ).toArray()
        );
    }


    /**
     * test put-single
     */
    @Test
    public void putsingle()
    {
        final Multimap<Integer, String> l_map1 = HashMultimap.create();
        final Multimap<Integer, String> l_map2 = HashMultimap.create();

        new CPutSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, "foo", l_map1, l_map2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( Stream.of( 1 ).toArray(), l_map1.keys().toArray() );
        Assert.assertArrayEquals( Stream.of( 1 ).toArray(), l_map2.keys().toArray() );

        Assert.assertArrayEquals( Stream.of( "foo" ).toArray(), l_map1.values().toArray() );
        Assert.assertArrayEquals( Stream.of( "foo" ).toArray(), l_map1.values().toArray() );
    }


    /**
     * test put-multiple
     */
    @Test
    public void putmultiple()
    {
        final Multimap<Integer, String> l_map = HashMultimap.create();

        new CPutMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map, 1, "xxx", 2, "blub", 3, "xxx", 3, "yyy" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( 4, l_map.size() );
        Assert.assertArrayEquals( Stream.of( 1, 2, 3 ).toArray(), l_map.keySet().toArray() );
        Assert.assertArrayEquals( Stream.of( "xxx", "blub", "yyy", "xxx" ).toArray(), l_map.values().toArray() );
    }


    /**
     * test get-multiple
     */
    @Test
    public void getmultiple()
    {
        final Multimap<Integer, String> l_map = HashMultimap.create();
        final List<ITerm> l_return = new ArrayList<>();

        l_map.put( 1, "yyy" );
        l_map.put( 1, "bar" );
        l_map.put( 2, "test string" );
        l_map.put( 3, "blub" );

        new CGetMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map, 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( l_map.asMap().get( 1 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( l_map.asMap().get( 2 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
    }


    /**
     * test get-single
     */
    @Test
    public void getsingle()
    {
        final Multimap<Integer, String> l_map1 = HashMultimap.create();
        final Multimap<Integer, String> l_map2 = HashMultimap.create();
        final List<ITerm> l_return = new ArrayList<>();

        l_map1.put( 1, "foo" );
        l_map1.put( 2, "bar" );
        l_map2.put( 1, "foobar" );

        new CGetSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, l_map1, l_map2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( "foo" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "foobar" ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
    }

}
