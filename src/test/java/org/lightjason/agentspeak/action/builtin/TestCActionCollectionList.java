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

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.collection.list.CAdd;
import org.lightjason.agentspeak.action.builtin.collection.list.CComplement;
import org.lightjason.agentspeak.action.builtin.collection.list.CCreate;
import org.lightjason.agentspeak.action.builtin.collection.list.CFlat;
import org.lightjason.agentspeak.action.builtin.collection.list.CFlatConcat;
import org.lightjason.agentspeak.action.builtin.collection.list.CGet;
import org.lightjason.agentspeak.action.builtin.collection.list.CRange;
import org.lightjason.agentspeak.action.builtin.collection.list.CRemove;
import org.lightjason.agentspeak.action.builtin.collection.list.CReverse;
import org.lightjason.agentspeak.action.builtin.collection.list.CSet;
import org.lightjason.agentspeak.action.builtin.collection.list.CSubList;
import org.lightjason.agentspeak.action.builtin.collection.list.CSymmetricDifference;
import org.lightjason.agentspeak.action.builtin.collection.list.CUnique;
import org.lightjason.agentspeak.action.builtin.collection.list.CZip;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test collection list
 */
public final class TestCActionCollectionList extends IBaseTest
{

    /**
     * test create empty list
     */
    @Test
    public void createempty()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CCreate(),
                false,
                Collections.emptyList(),
                l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertTrue( l_return.get( 0 ).<List<?>>raw().isEmpty() );
    }


    /**
     * test create empty synchronized list
     */
    @Test
    public void createemptysynchronize()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CCreate(),
                    true,
                    Collections.emptyList(),
                    l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertTrue( l_return.get( 0 ).<List<?>>raw().isEmpty() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 0 ).raw().getClass() );
    }


    /**
     * test create non-empty list
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CCreate(),
                    false,
                    Stream.of( "a", 1, "b", true ).map( CRawTerm::of ).collect( Collectors.toList() ),
                    l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertEquals( 4, l_return.get( 0 ).<List<?>>raw().size() );

        final List<?> l_list = l_return.get( 0 ).raw();

        Assert.assertEquals( "a", l_list.get( 0 ) );
        Assert.assertEquals( 1, l_list.get( 1 ) );
        Assert.assertEquals( "b", l_list.get( 2 ) );
        Assert.assertEquals( true, l_list.get( 3 ) );
    }


    /**
     * test complement action
     */
    @Test
    public void complement()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CComplement(),
                    false,
                    Stream.of(
                        CRawTerm.of( Stream.of( "a", "b", 1, 2 ).collect( Collectors.toList() ) ),
                        CRawTerm.of( Stream.of( "x", "y", 4, "a", 5, 1 ).collect( Collectors.toList() ) )
                    ).collect( Collectors.toList() ),
                    l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( CCommon.isssignableto( l_return.get( 0 ), List.class ) );
        Assert.assertEquals( "b", l_return.get( 0 ).<List<?>>raw().get( 0 ) );
        Assert.assertEquals( 2, l_return.get( 0 ).<List<?>>raw().get( 1 ) );
    }


    /**
     * test get action
     */
    @Test
    public void get()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = Stream.of( "a", 1, "b", true, "foobar", 56.78 ).collect( Collectors.toList() );

        Assert.assertTrue(
            execute(
                new CGet(),
                false,
                Stream.of( CRawTerm.of( l_list ), CRawTerm.of( 1 ), CRawTerm.of( 4 ), CRawTerm.of( 5 ) ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertEquals( 1, l_return.get( 0 ).<Number>raw() );
        Assert.assertEquals( "foobar", l_return.get( 1 ).raw() );
        Assert.assertEquals( 56.78, l_return.get( 2 ).<Number>raw() );
    }

    /**
     * test reverse action
     */
    @Test
    public void reverse()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = IntStream.range( 0, 10 ).mapToObj( i -> Math.random() ).collect( Collectors.toList() );

        Assert.assertTrue(
            execute(
                new CReverse(),
                false,
                Stream.of( CRawTerm.of( l_list ) ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertArrayEquals( Lists.reverse( l_list ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test remove action
     */
    @Test
    public void remove()
    {
        final Random l_random = new Random();

        final List<?> l_elements = IntStream.range( 0, l_random.nextInt( 100 ) + 1 ).map( i -> l_random.nextInt() ).boxed().collect( Collectors.toList() );
        final List<?> l_list = new ArrayList<>( l_elements );
        final List<Integer> l_index = IntStream.range( 0, l_list.size() / 3 )
                                               .map( i -> l_random.nextInt( l_list.size() ) )
                                               .boxed().distinct().collect( Collectors.toList() );

        final int l_startsize = l_list.size();
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CRemove(),
                false,
                Stream.concat(
                    Stream.of( l_list ),
                    l_index.stream()
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( l_list.size() + l_index.size(), l_startsize );
        Assert.assertTrue(
            l_index.parallelStream()
                   .map( l_elements::get )
                   .allMatch( i -> l_return.parallelStream().map( ITerm::<Number>raw ).anyMatch( j -> j.equals( i ) ) )
        );
    }


    /**
     * test set action
     */
    @Test
    public void set()
    {
        final List<?> l_list1 = Stream.of( "" ).collect( Collectors.toList() );
        final List<?> l_list2 = Stream.of( "abc", 123, true ).collect( Collectors.toList() );

        Assert.assertTrue(
            execute(
                new CSet(),
                false,
                Stream.of( CRawTerm.of( 0 ), CRawTerm.of( "xxx" ), CRawTerm.of( l_list1 ), CRawTerm.of( l_list2 ) ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assert.assertEquals( 1, l_list1.size() );
        Assert.assertEquals( "xxx", l_list1.get( 0 ) );

        Assert.assertEquals( 3, l_list2.size() );
        Assert.assertEquals( "xxx", l_list2.get( 0 ) );
    }


    /**
     * test add action
     */
    @Test
    public void add()
    {
        final List<?> l_list = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CAdd(),
                false,
                Stream.of( CRawTerm.of( "xyz" ), CRawTerm.of( l_list ) ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assert.assertEquals( 1, l_list.size() );
        Assert.assertEquals( "xyz", l_list.get( 0 ) );
    }


    /**
     * test range error
     */
    @Test
    public void rangeerror()
    {
        Assert.assertFalse(
            execute(
                new CRange(),
                false,
                Stream.of().map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );
    }


    /**
     * test range
     */
    @Test
    public void range()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CRange(),
                false,
                Stream.of( 0, 5, 7, 9 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertTrue(
            execute(
                new CRange(),
                true,
                Stream.of( 1, 7 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( l_return.size(), 3 );

        Assert.assertArrayEquals( IntStream.range( 0, 5 ).boxed().toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( IntStream.range( 7, 9 ).boxed().toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );

        Assert.assertArrayEquals( IntStream.range( 1, 7 ).boxed().toArray(), l_return.get( 2 ).<List<?>>raw().toArray() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 2 ).<List<?>>raw().getClass() );
    }

    /**
     * test sublist error
     */
    @Test
    public void sublisterror()
    {
        Assert.assertFalse(
            execute(
                new CSubList(),
                false,
                Stream.of( new ArrayList<>() ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );
    }

    /**
     * test sublist
     */
    @Test
    public void sublist()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CSubList(),
                false,
                Stream.of( Stream.of( "ax", "bx", "c", 1, 2, 3 ).collect( Collectors.toList() ), 0, 2, 2, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertTrue(
            execute(
                new CSubList(),
                true,
                Stream.of( Stream.of( 8, 9, 10 ).collect( Collectors.toList() ), 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( 3, l_return.size() );

        Assert.assertArrayEquals( Stream.of( "ax", "bx" ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( "c", 1 ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
        Assert.assertArrayEquals( Stream.of( 9 ).toArray(), l_return.get( 2 ).<List<?>>raw().toArray() );
    }


    /**
     * test flat action
     */
    @Test
    public void flat()
    {
        final Random l_random = new Random();

        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = IntStream.range( 0, l_random.nextInt( 100 ) + 1 )
                                        .mapToObj( i -> RandomStringUtils.random( l_random.nextInt( 100 ) + 1 ) )
                                        .collect( Collectors.toList() );

        Assert.assertTrue(
            execute(
                new CFlat(),
                false,
                l_list.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( l_list.size(), l_return.size() );
        Assert.assertArrayEquals( l_list.toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test flatconcat action
     */
    @Test
    public void flatconcat()
    {
        final Random l_random = new Random();

        final List<ITerm> l_return = new ArrayList<>();
        final List<?> l_list = IntStream.range( 0, l_random.nextInt( 100 ) + 1 )
                                        .mapToObj( i -> RandomStringUtils.random( l_random.nextInt( 100 ) + 1 ) )
                                        .collect( Collectors.toList() );

        Assert.assertTrue(
            execute(
                new CFlatConcat(),
                false,
                l_list.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), l_list.toArray() );
    }


    /**
     * test zip action error
     */
    @Test
    public void ziperror()
    {
        Assert.assertFalse(
            execute(
                new CZip(),
                false,
                Stream.of( "" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );
    }


    /**
     * test zip action
     */
    @Test
    public void zip()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CZip(),
                false,
                IntStream.range( 0, 6 ).boxed().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( 3, l_return.get( 0 ).<List<?>>raw().size() );

        Assert.assertEquals( 0, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 0 ).getKey() );
        Assert.assertEquals( 3, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 0 ).getValue() );

        Assert.assertEquals( 1, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 1 ).getKey() );
        Assert.assertEquals( 4, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 1 ).getValue() );

        Assert.assertEquals( 2, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 2 ).getKey() );
        Assert.assertEquals( 5, l_return.get( 0 ).<List<AbstractMap.SimpleEntry<?, ?>>>raw().get( 2 ).getValue() );

        Assert.assertTrue(
            execute(
                new CZip(),
                true,
                Stream.of( 1, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).<List<?>>raw().getClass() );

    }


    /**
     * test unique action
     */
    @Test
    public void unique()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CUnique(),
                false,
                Stream.of( 1, 1, 3, 4, 5, 5 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().size(), 4 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 1, 3, 4, 5 ).toArray() );

        new CUnique().execute(
            true, IContext.EMPTYPLAN,
            Stream.of( 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( Collections.synchronizedList( Collections.emptyList() ).getClass(), l_return.get( 1 ).<List<?>>raw().getClass() );
    }

    /**
     * test symmetric difference
     */
    @Test
    public void symmetricdifference()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CSymmetricDifference(),
                false,
                Stream.of( 1, 2, 3, 3, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertArrayEquals( Stream.of( 1, 2, 4 ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
    }

}
