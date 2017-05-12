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

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.collection.CClear;
import org.lightjason.agentspeak.action.buildin.collection.CIsEmpty;
import org.lightjason.agentspeak.action.buildin.collection.CSize;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test collection action
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionCollection extends IBaseTest
{

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.of(

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( new Object() ) ).collect( Collectors.toList() ),
                new int[]{0}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( new ArrayList<>() ) ).collect( Collectors.toList() ),
                new int[]{0}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( Stream.of( 1, "test" ).collect( Collectors.toList() ) ) ).collect( Collectors.toList() ),
                new int[]{2}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( new AbstractMap.SimpleEntry<>( "a", 1 ) ) ).collect( Collectors.toList() ),
                new int[]{2}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( Stream.of( 1, 1, "test" ).collect( Collectors.toSet() ) ) ).collect( Collectors.toList() ),
                new int[]{2}
            ),

            new ImmutablePair<>(
                Stream.of(
                    CRawTerm.from( Stream.of( "abcd", "xyz", 12, 12 ).collect( Collectors.toSet() ) ),
                    CRawTerm.from( Stream.of( 1, 2, 3, 3, 4, 4 ).collect( Collectors.toList() )  )
                ).collect( Collectors.toList() ),
                new int[]{3, 6}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from(
                    StreamUtils.windowed( Stream.of( 1, 2, 3, 4 ), 2 ).collect( Collectors.toMap( i -> i.get( 0 ), i -> i.get( 1 ) ) )
                ) ).collect( Collectors.toList() ),
                new int[]{3}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from(
                    StreamUtils.windowed( Stream.of( 1, 2, 3, 4 ), 2 ).collect( Collectors.toMap( i -> i.get( 0 ), i -> i.get( 1 ) ) )
                ) ).collect( Collectors.toList() ),
                new int[]{3}
            )

        ).toArray();
    }

    /**
     * test size
     */
    @Test
    @UseDataProvider( "generate" )
    public final void size( final Pair<List<ITerm>, int[]> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSize().execute(
            null,
            false,
            p_input.getLeft(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            MessageFormat.format( "elements {0}", p_input.getLeft() ),
            l_return.stream().map( ITerm::<Number>raw ).mapToInt( Number::intValue ).toArray(),
            p_input.getRight()
        );
    }


    /**
     * test empty
     */
    @Test
    public final void empty()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CIsEmpty().execute(
            null,
            false,
            Stream.of( new ArrayList<>(), HashMultimap.create(), new HashMap<>(), Stream.of( "1", 2 ).collect( Collectors.toList() ), new Object() )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 5 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::<Boolean>raw ).toArray(), Stream.of( true, true, true, false, false ).toArray() );
    }


    /**
     * test clear
     */
    @Test
    public final void clear()
    {
        final List<Integer> l_list = IntStream.range( 0, 10 ).boxed().collect( Collectors.toList() );
        final Set<Integer> l_set = IntStream.range( 10, 20 ).boxed().collect( Collectors.toSet() );
        final Map<Integer, Integer> l_map = StreamUtils.windowed( IntStream.range( 100, 120 ).boxed(), 2 )
                                                       .collect( Collectors.toMap( i -> i.get( 0 ), i -> i.get( 1 ) ) );

        final Multimap<Integer, Integer> l_multimap = HashMultimap.create();
        IntStream.range( 0, 5 ).forEach( i -> IntStream.range( i, i + 5 ).forEach( j -> l_map.put( i, j ) ) );

        new CClear().execute(
            null,
            false,
            Stream.of( l_list, l_set, l_map, l_multimap ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertTrue( l_list.isEmpty() );
        Assert.assertTrue( l_set.isEmpty() );
        Assert.assertTrue( l_map.isEmpty() );
    }


    /**
     * test call
     *
     * @param p_args command line arguments
     */
    @SuppressWarnings( "unchecked" )
    public static void main( final String[] p_args )
    {
        new TestCActionCollection().invoketest();
    }
}
