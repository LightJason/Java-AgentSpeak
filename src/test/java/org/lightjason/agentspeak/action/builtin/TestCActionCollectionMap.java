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

import com.codepoetics.protonpack.StreamUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.collection.map.CCreate;
import org.lightjason.agentspeak.action.builtin.collection.map.CGetMultiple;
import org.lightjason.agentspeak.action.builtin.collection.map.CGetSingle;
import org.lightjason.agentspeak.action.builtin.collection.map.CKeys;
import org.lightjason.agentspeak.action.builtin.collection.map.CPutMultiple;
import org.lightjason.agentspeak.action.builtin.collection.map.CPutMultipleIfAbsent;
import org.lightjason.agentspeak.action.builtin.collection.map.CPutSingle;
import org.lightjason.agentspeak.action.builtin.collection.map.CPutSingleIfAbsent;
import org.lightjason.agentspeak.action.builtin.collection.map.CRemove;
import org.lightjason.agentspeak.action.builtin.collection.map.CValues;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

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
public final class TestCActionCollectionMap extends IBaseTest
{

    /**
     * test map create
     */
    @Test
    public final void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Collections.emptyList(),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).<Map<?, ?>>raw().isEmpty() );


        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(  "a", 1, "b", 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertFalse( l_return.get( 1 ).<Map<?, ?>>raw().isEmpty() );

        Assert.assertArrayEquals( l_return.get( 1 ).<Map<?, ?>>raw().keySet().toArray(), Stream.of( "a", "b" ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<Map<?, ?>>raw().values().toArray(), Stream.of( 1, 2 ).toArray() );


        new CCreate().execute(
            true, IContext.EMPTYPLAN,
            Collections.emptyList(),
            l_return
        );

        Assert.assertEquals( l_return.size(), 3 );
        Assert.assertTrue( l_return.get( 2 ).<Map<?, ?>>raw() instanceof ConcurrentHashMap<?, ?> );
    }


    /**
     * test map keys
     */
    @Test
    public final void keysvalues()
    {
        final Map<?, ?> l_map = StreamUtils.zip(
            Stream.of( "foo", "bar", "yyy", "xxx" ),
            Stream.of( 1, 2, 3, 4 ),
            AbstractMap.SimpleImmutableEntry::new
        ).collect( Collectors.toMap( AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue ) );


        final List<ITerm> l_return = new ArrayList<>();

        new CKeys().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( CRawTerm.of( l_map ) ).collect( Collectors.toList() ),
            l_return
        );

        new CValues().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( CRawTerm.of( l_map ) ).collect( Collectors.toList() ),
            l_return
        );


        Assert.assertEquals( 2, l_return.size() );

        Assert.assertEquals( 4, l_return.get( 0 ).<List<?>>raw().size() );
        Assert.assertEquals( 4, l_return.get( 1 ).<List<?>>raw().size() );

        Assert.assertArrayEquals( Stream.of( "bar", "foo", "xxx", "yyy" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 2, 1, 3, 4 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
    }


    /**
     * test map putsingle
     */
    @Test
    public final void putsingle()
    {
        final Map<?, ?> l_map = new HashMap<>();

        new CPutSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "v", 1, l_map ).map( CRawTerm::of ).collect( Collectors.toList() ),

            Collections.emptyList()
        );

        Assert.assertEquals( 1, l_map.size() );
        Assert.assertArrayEquals( Stream.of( "v" ).toArray(), l_map.keySet().toArray() );
        Assert.assertArrayEquals( Stream.of( 1 ).toArray(), l_map.values().toArray() );




        new CPutSingleIfAbsent().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "v", 666, l_map ).map( CRawTerm::of ).collect( Collectors.toList() ),

            Collections.emptyList()
        );

        Assert.assertEquals( 1, l_map.size() );
        Assert.assertArrayEquals( Stream.of( "v" ).toArray(), l_map.keySet().toArray() );
        Assert.assertArrayEquals( Stream.of( 1 ).toArray(), l_map.values().toArray() );
    }


    /**
     * test multiple put
     */
    @Test
    public final void putmultiple()
    {
        final Map<?, ?> l_map = new HashMap<>();

        new CPutMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map, "xx", 2, "yyy", 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( l_map.size(), 2 );
        Assert.assertArrayEquals( l_map.keySet().toArray(), Stream.of( "xx", "yyy" ).toArray() );
        Assert.assertArrayEquals( l_map.values().toArray(), Stream.of( 2, 3 ).toArray() );


        new CPutMultipleIfAbsent().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map, "xx", 100, "zz", 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( 3, l_map.size() );
        Assert.assertArrayEquals( Stream.of( "xx", "zz", "yyy" ).toArray(), l_map.keySet().toArray() );
        Assert.assertArrayEquals( Stream.of( 2, 4, 3 ).toArray(), l_map.values().toArray() );
    }


    /**
     * test remove
     */
    @Test
    public final void remove()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Map<Object, Object> l_map = new HashMap<>();

        l_map.put( "a", 1 );
        l_map.put( "y", 2 );
        l_map.put( "z", 3 );

        new CRemove().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map, "a", "z" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_map.size() );
        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( 1, 3 ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test get-multiple
     */
    @Test
    public final void getmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Map<Object, Object> l_map = new HashMap<>();

        l_map.put( "i", 1 );
        l_map.put( "j", 2 );
        l_map.put( "k", 3 );

        new CGetMultiple().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map, "i", "j", "o" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertEquals( 1, l_return.get( 0 ).<Number>raw() );
        Assert.assertEquals( 2, l_return.get( 1 ).<Number>raw() );
        Assert.assertNull( l_return.get( 2 ).<Number>raw() );
    }


    /**
     * test get-single
     */
    @Test
    public final void getsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        final Map<Object, Object> l_map1 = new HashMap<>();
        l_map1.put( "g", 123 );

        final Map<Object, Object> l_map2 = new HashMap<>();
        l_map2.put( "g", "text" );

        new CGetSingle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "g", l_map1, l_map2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( 123, l_return.get( 0 ).<Number>raw() );
        Assert.assertEquals( "text", l_return.get( 1 ).raw() );
    }

}
