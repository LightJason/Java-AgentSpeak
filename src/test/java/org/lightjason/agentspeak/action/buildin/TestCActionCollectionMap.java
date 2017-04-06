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

import com.codepoetics.protonpack.StreamUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.buildin.collection.map.CCreate;
import org.lightjason.agentspeak.action.buildin.collection.map.CKeys;
import org.lightjason.agentspeak.action.buildin.collection.map.CPut;
import org.lightjason.agentspeak.action.buildin.collection.map.CPutIfAbsent;
import org.lightjason.agentspeak.action.buildin.collection.map.CPutMultiple;
import org.lightjason.agentspeak.action.buildin.collection.map.CValues;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test action map
 */
public final class TestCActionCollectionMap
{

    /**
     * test map create
     */
    @Test
    public final void testcreate()
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
        Assert.assertTrue( l_return.get( 0 ).<Map<?, ?>>raw().isEmpty() );


        new CCreate().execute(
            null,
            false,
            Stream.of(  "a", 1, "b", 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertFalse( l_return.get( 1 ).<Map<?, ?>>raw().isEmpty() );

        Assert.assertArrayEquals( l_return.get( 1 ).<Map<?, ?>>raw().keySet().toArray(), Stream.of( "a", "b" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<Map<?, ?>>raw().values().toArray(), Stream.of( 1, 2 ).toArray() );


        new CCreate().execute(
            null,
            true,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 3 );
        Assert.assertTrue( l_return.get( 2 ).<Map<?, ?>>raw() instanceof ConcurrentHashMap<?, ?> );
    }


    /**
     * test map keys
     */
    @Test
    public final void testkeysvalues()
    {
        final Map<?, ?> l_map = StreamUtils.zip(
            Stream.of( "foo", "bar", "yyy", "xxx" ),
            Stream.of( 1, 2, 3, 4 ),
            AbstractMap.SimpleImmutableEntry::new
        ).collect( Collectors.toMap( AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue ) );


        final List<ITerm> l_return = new ArrayList<>();

        new CKeys().execute(
            null,
            false,
            Stream.of( CRawTerm.from( l_map ) ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        new CValues().execute(
            null,
            false,
            Stream.of( CRawTerm.from( l_map ) ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        Assert.assertEquals( l_return.size(), 2 );

        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().size(), 4 );
        Assert.assertEquals( l_return.get( 1 ).<List<?>>raw().size(), 4 );

        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( "bar", "foo", "xxx", "yyy" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 2, 1, 3, 4 ).toArray() );
    }


    /**
     * test map put
     */
    @Test
    public final void testput()
    {
        final Map<?, ?> l_map = new HashMap<>();

        new CPutMultiple().execute(
            null,
            false,
            Stream.of( "x", 1, l_map ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_map.size(), 1 );
        Assert.assertArrayEquals( l_map.keySet().toArray(), Stream.of( "x" ).toArray() );
        Assert.assertArrayEquals( l_map.values().toArray(), Stream.of( 1 ).toArray() );


        new CPut().execute(
            null,
            false,
            Stream.of( l_map, "xx", 2, "yyy", 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_map.size(), 3 );
        Assert.assertArrayEquals( l_map.keySet().toArray(), Stream.of( "xx", "x", "yyy" ).toArray() );
        Assert.assertArrayEquals( l_map.values().toArray(), Stream.of( 2, 1, 3 ).toArray() );


        new CPutIfAbsent().execute(
            null,
            false,
            Stream.of( l_map, "xx", 100, "zz", 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_map.size(), 4 );
        Assert.assertArrayEquals( l_map.keySet().toArray(), Stream.of( "xx", "zz", "x", "yyy" ).toArray() );
        Assert.assertArrayEquals( l_map.values().toArray(), Stream.of( 2, 4, 1, 3 ).toArray() );
    }


    /**
     * test call
     *
     * @param p_args command-line
     */
    public static void main( final String[] p_args )
    {
        final TestCActionCollectionMap l_test = new TestCActionCollectionMap();

        l_test.testcreate();
        l_test.testkeysvalues();
        l_test.testput();
    }

}
