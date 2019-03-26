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

package org.lightjason.agentspeak.action;

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
import org.lightjason.agentspeak.action.listsettuple.CListSetClear;
import org.lightjason.agentspeak.action.listsettuple.CListSetIsEmpty;
import org.lightjason.agentspeak.action.listsettuple.CListSetSize;
import org.lightjason.agentspeak.action.map.CMapClear;
import org.lightjason.agentspeak.action.map.CMapIsEmpty;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
                Stream.of( CRawTerm.of( new Object() ) ).collect( Collectors.toList() ),
                new int[]{0}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.of( new ArrayList<>() ) ).collect( Collectors.toList() ),
                new int[]{0}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.of( Stream.of( 1, "test" ).collect( Collectors.toList() ) ) ).collect( Collectors.toList() ),
                new int[]{2}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.of( new AbstractMap.SimpleEntry<>( "a", 1 ) ) ).collect( Collectors.toList() ),
                new int[]{0}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.of( Stream.of( 1, 1, "test" ).collect( Collectors.toSet() ) ) ).collect( Collectors.toList() ),
                new int[]{2}
            ),

            new ImmutablePair<>(
                Stream.of(
                    CRawTerm.of( Stream.of( "abcd", "xyz", 12, 12 ).collect( Collectors.toSet() ) ),
                    CRawTerm.of( Stream.of( 1, 2, 3, 3, 4, 4 ).collect( Collectors.toList() )  )
                ).collect( Collectors.toList() ),
                new int[]{3, 6}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.of(
                    StreamUtils.windowed( Stream.of( 1, 2, 3, 4 ), 2 ).collect( Collectors.toMap( i -> i.get( 0 ), i -> i.get( 1 ) ) )
                ) ).collect( Collectors.toList() ),
                new int[]{0}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.of(
                    StreamUtils.windowed( Stream.of( 1, 2, 3, 4 ), 2 ).collect( Collectors.toMap( i -> i.get( 0 ), i -> i.get( 1 ) ) )
                ) ).collect( Collectors.toList() ),
                new int[]{0}
            )

        ).toArray();
    }

    /**
     * test size
     *
     * @param p_input pair input data term and results
     */
    @Test
    @UseDataProvider( "generate" )
    public void size( final Pair<List<ITerm>, int[]> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CListSetSize().execute(
            false, IContext.EMPTYPLAN,
            p_input.getLeft(),
            l_return
        );

        Assert.assertArrayEquals(
            MessageFormat.format( "elements {0}", p_input.getLeft() ),
            p_input.getRight(),
            l_return.stream().map( ITerm::<Number>raw ).mapToInt( Number::intValue ).toArray()
        );
    }


    /**
     * test empty map
     */
    @Test
    public void emptymap()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMapIsEmpty().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( new ArrayList<>(), new HashSet<>(), HashMultimap.create(), new HashMap<>(), Stream.of( "1", 2 ).collect( Collectors.toList() ), new Object() )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 6, l_return.size() );
        Assert.assertArrayEquals( Stream.of( false, false, true, true, false, false ).toArray(), l_return.stream().map( ITerm::<Boolean>raw ).toArray() );
    }

    /**
     * test empty list set
     */
    @Test
    public void emptylistset()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CListSetIsEmpty().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( new ArrayList<>(), new HashSet<>(), HashMultimap.create(), new HashMap<>(), Stream.of( "1", 2 ).collect( Collectors.toList() ), new Object() )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 6, l_return.size() );
        Assert.assertArrayEquals( Stream.of( true, true, false, false, false, false ).toArray(), l_return.stream().map( ITerm::<Boolean>raw ).toArray() );
    }


    /**
     * test clear map
     */
    @Test
    public void clearmap()
    {
        final Map<Integer, Integer> l_map = StreamUtils.windowed( IntStream.range( 100, 120 ).boxed(), 2 )
                                                       .collect( Collectors.toMap( i -> i.get( 0 ), i -> i.get( 1 ) ) );
        final Multimap<Integer, Integer> l_multimap = HashMultimap.create();
        IntStream.range( 0, 5 ).forEach( i -> IntStream.range( i, i + 5 ).forEach( j -> l_map.put( i, j ) ) );

        new CMapClear().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_map, l_multimap ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertTrue( l_map.isEmpty() );
        Assert.assertTrue( l_multimap.isEmpty() );

    }

    /**
     * test clear
     */
    @Test
    public void clearlistset()
    {
        final List<Integer> l_list = IntStream.range( 0, 10 ).boxed().collect( Collectors.toList() );
        final Set<Integer> l_set = IntStream.range( 10, 20 ).boxed().collect( Collectors.toSet() );

        new CListSetClear().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_list, l_set ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertTrue( l_list.isEmpty() );
        Assert.assertTrue( l_set.isEmpty() );
    }
}
