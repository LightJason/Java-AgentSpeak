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


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.collection.multimap.CAsMap;
import org.lightjason.agentspeak.action.buildin.collection.multimap.CCreate;
import org.lightjason.agentspeak.action.buildin.collection.multimap.CKeys;
import org.lightjason.agentspeak.action.buildin.collection.multimap.CPut;
import org.lightjason.agentspeak.action.buildin.collection.multimap.CValues;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

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
    public final void create()
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
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Multimap<?, ?> );
        Assert.assertTrue( l_return.get( 0 ).<Multimap<?, ?>>raw().isEmpty() );
    }

    /**
     * test synchronized create
     */
    @Test
    public final void createsynchronized()
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
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Multimaps.synchronizedSetMultimap( HashMultimap.create() ).getClass() );
    }


    /**
     * test key & values
     */
    @Test
    public final void keysvalues()
    {
        final Multimap<Object, Object> l_map = HashMultimap.create();
        l_map.put( "a", 1 );
        l_map.put( "a", 2 );
        l_map.put( "a", 3 );
        l_map.put( "b", 1 );
        l_map.put( "c", 10 );

        final List<ITerm> l_return = new ArrayList<>();


        new CKeys().execute(
            null,
            false,
            Stream.of( l_map ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        new CValues().execute(
            null,
            false,
            Stream.of( l_map ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "a", "b", "c" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 1, 2, 3, 1, 10 ).toArray() );
    }

    /**
     * test as-map
     */
    @Test
    public final void asmap()
    {
        final Multimap<Integer, String> l_map = HashMultimap.create();
        final List<ITerm> l_return = new ArrayList<>();

        final Random l_random = new Random();
        IntStream.range( 0, 5 ).map( i -> l_random.nextInt( 8 ) )
                 .forEach( i -> l_map.put( i, RandomStringUtils.random( 10, "abcdefghijklmnop" ) ) );

        new CAsMap().execute(
            null,
            false,
            Stream.of( l_map ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Map<?, ?> );
        Assert.assertArrayEquals( l_return.get( 0 ).<Map<?, ?>>raw().keySet().toArray(), l_map.keySet().toArray() );

        Assert.assertArrayEquals(
            CCommon.flatstream( l_return.get( 0 ).<Map<?, ?>>raw().values().stream().map( CRawTerm::from ) ).map( ITerm::raw ).toArray(),
            l_map.values().toArray()
        );
    }


    /**
     * test put
     */
    @Test
    public final void put()
    {
        final Multimap<Integer, String> l_map = HashMultimap.create();

        new CPut().execute(
            null,
            false,
            Stream.of( l_map, 1, "foo", 2, "blub", 3, "xxx", 3, "yyy" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_map.size(), 4 );
        Assert.assertArrayEquals( l_map.keySet().toArray(), Stream.of( 1, 2, 3 ).toArray() );
        Assert.assertArrayEquals( l_map.values().toArray(), Stream.of( "foo", "blub", "yyy", "xxx" ).toArray() );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionCollectionMultimap().invoketest();
    }

}
