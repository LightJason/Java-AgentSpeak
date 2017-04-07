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
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.action.buildin.bool.CAllMatch;
import org.lightjason.agentspeak.action.buildin.bool.CAnd;
import org.lightjason.agentspeak.action.buildin.bool.CAnyMatch;
import org.lightjason.agentspeak.action.buildin.bool.CCountFalse;
import org.lightjason.agentspeak.action.buildin.bool.CCountTrue;
import org.lightjason.agentspeak.action.buildin.bool.CEqual;
import org.lightjason.agentspeak.action.buildin.bool.CNot;
import org.lightjason.agentspeak.action.buildin.bool.CNotEqual;
import org.lightjason.agentspeak.action.buildin.bool.COr;
import org.lightjason.agentspeak.action.buildin.bool.CXor;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test for boolean actions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionBool
{

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.of(

            // -- first test-case ---
            testcase(

                // input data
                Stream.of( true, false, true, false, false, true ),

                // testing classes / test-methods
                Stream.of(
                    CAllMatch.class,
                    CAnyMatch.class,
                    CAnd.class,
                    COr.class,
                    CXor.class,
                    CNot.class,
                    CCountTrue.class,
                    CCountFalse.class
                ),

                // results for each class test
                Stream.of(

                    Stream.of( false ),
                    Stream.of( true ),
                    Stream.of( false ),
                    Stream.of( true ),
                    Stream.of( true ),
                    Stream.of( false, true, false, true, true, false ),
                    Stream.of( 3L ),
                    Stream.of( 3L )
                )
            ),



            // --- second test-case ---
            testcase(

                // input data
                Stream.of( true, true ),

                // testing classes / test-methods
                Stream.of(
                    CAllMatch.class,
                    CAnyMatch.class,
                    CAnd.class,
                    COr.class,
                    CXor.class,
                    CNot.class,
                    CCountTrue.class,
                    CCountFalse.class

                ),

                // results for each class test
                Stream.of(
                    Stream.of( true ),
                    Stream.of( true ),
                    Stream.of( true ),
                    Stream.of( true ),
                    Stream.of( false ),
                    Stream.of( false, false ),
                    Stream.of( 2L ),
                    Stream.of( 0L )
                )
            )

        ).toArray();
    }

    /**
     * method to generate test-cases
     *
     * @param p_input input data
     * @param p_classes matching test-classes / test-cases
     * @param p_classresult result for each class
     * @return test-object
     */
    private static Object testcase( final Stream<Object> p_input, final Stream<Class<?>> p_classes, final Stream<Stream<Object>> p_classresult )
    {
        return new ImmutablePair<>(
                                    p_input.collect( Collectors.toList() ),

                                    StreamUtils.zip(
                                        p_classes,
                                        p_classresult.map( i -> i.collect( Collectors.toList() ) ),
                                        ( i, j ) -> new ImmutablePair<Object, Collection<Object>>( i, j )
                                    ).collect( Collectors.toMap( i -> i.left, i -> i.right ) )
        );
    }

    /**
     * test all-match
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void allmatch( final Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>> p_input )
    {
        final Collection<Boolean> l_expected = p_input.getRight().get( CAllMatch.class );
        Assume.assumeNotNull( l_expected );

        final List<ITerm> l_return = new ArrayList<>();

        new CAllMatch().execute(
            null,
            false,
            p_input.getLeft().stream().map( CRawTerm::from ).map( CRawTerm::<ITerm>raw ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), l_expected.size() );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), l_expected.toArray() );
    }


    /**
     * test any-match
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void anymatch( final Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>> p_input )
    {
        final Collection<Boolean> l_expected = p_input.getRight().get( CAnyMatch.class );
        Assume.assumeNotNull( l_expected );

        final List<ITerm> l_return = new ArrayList<>();

        new CAnyMatch().execute(
            null,
            false,
            p_input.getLeft().stream().map( CRawTerm::from ).map( CRawTerm::<ITerm>raw ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), l_expected.size() );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), l_expected.toArray() );
    }


    /**
     * test and
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void and( final Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>> p_input )
    {
        final Collection<Boolean> l_expected = p_input.getRight().get( CAnd.class );
        Assume.assumeNotNull( l_expected );

        final List<ITerm> l_return = new ArrayList<>();

        new CAnd().execute(
            null,
            false,
            p_input.getLeft().stream().map( CRawTerm::from ).map( CRawTerm::<ITerm>raw ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), l_expected.size() );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), l_expected.toArray() );
    }


    /**
     * test or
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void or( final Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>> p_input )
    {
        final Collection<Boolean> l_expected = p_input.getRight().get( COr.class );
        Assume.assumeNotNull( l_expected );

        final List<ITerm> l_return = new ArrayList<>();

        new COr().execute(
            null,
            false,
            p_input.getLeft().stream().map( CRawTerm::from ).map( CRawTerm::<ITerm>raw ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), l_expected.size() );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), l_expected.toArray() );
    }


    /**
     * test xor
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void xor( final Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>> p_input )
    {
        final Collection<Boolean> l_expected = p_input.getRight().get( CXor.class );
        Assume.assumeNotNull( l_expected );

        final List<ITerm> l_return = new ArrayList<>();

        new CXor().execute(
            null,
            false,
            p_input.getLeft().stream().map( CRawTerm::from ).map( CRawTerm::<ITerm>raw ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), l_expected.size() );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), l_expected.toArray() );
    }


    /**
     * test not operation
     */
    @Test
    @UseDataProvider( "generate" )
    public final void not( final Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>> p_input )
    {
        final Collection<Boolean> l_expected = p_input.getRight().get( CNot.class );
        Assume.assumeNotNull( l_expected );

        final List<ITerm> l_return = new ArrayList<>();

        new CNot().execute(
            null,
            false,
            p_input.getLeft().stream().map( CRawTerm::from ).map( CRawTerm::<ITerm>raw ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), l_expected.size() );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), l_expected.toArray() );
    }


    /**
     * test count-true
     */
    @Test
    @UseDataProvider( "generate" )
    public final void counttrue( final Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>> p_input )
    {
        final Collection<Boolean> l_expected = p_input.getRight().get( CCountTrue.class );
        Assume.assumeNotNull( l_expected );

        final List<ITerm> l_return = new ArrayList<>();

        new CCountTrue().execute(
            null,
            false,
            p_input.getLeft().stream().map( CRawTerm::from ).map( CRawTerm::<ITerm>raw ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), l_expected.size() );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), l_expected.toArray() );
    }


    /**
     * test count-true
     */
    @Test
    @UseDataProvider( "generate" )
    public final void countfalse( final Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>> p_input )
    {
        final Collection<Boolean> l_expected = p_input.getRight().get( CCountFalse.class );
        Assume.assumeNotNull( l_expected );

        final List<ITerm> l_return = new ArrayList<>();

        new CCountFalse().execute(
            null,
            false,
            p_input.getLeft().stream().map( CRawTerm::from ).map( CRawTerm::<ITerm>raw ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), l_expected.size() );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), l_expected.toArray() );
    }


    /**
     * test equal
     */
    @Test
    public final void equal()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CEqual().execute(
            null,
            false,
            Stream.of( l_return, l_return, new Object() ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertFalse( l_return.get( 1 ).<Boolean>raw() );


        final List<Integer> l_list1 = IntStream.range( 0, 5 ).boxed().collect( Collectors.toList() );
        final List<Integer> l_list2 = IntStream.range( 0, 5 ).boxed().collect( Collectors.toList() );

        new CEqual().execute(
            null,
            false,
            Stream.of( l_list1, l_list2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 3 );
        Assert.assertTrue( l_return.get( 2 ).<Boolean>raw() );


        final Map<Integer, Integer> l_map1 = new HashMap<>();
        l_map1.put( 1, 2 );
        final Map<Integer, Integer> l_map2 = new HashMap<>();
        l_map2.put( 1, 1 );

        new CEqual().execute(
            null,
            false,
            Stream.of( l_map1, l_map2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertFalse( l_return.get( 3 ).<Boolean>raw() );
    }

    /**
     * test not-equal
     */
    @Test
    public final void notequal()
    {
        final Object l_object = new Object();
        final List<ITerm> l_return = new ArrayList<>();

        new CNotEqual().execute(
            null,
            false,
            Stream.of( l_object, l_object, new Object() ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertFalse( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertTrue( l_return.get( 1 ).<Boolean>raw() );
    }


    /**
     * main test call
     *
     * @param p_args arguments
     */
    @SuppressWarnings( "unchecked" )
    public static void main( final String[] p_args )
    {
        final TestCActionBool l_test = new TestCActionBool();

        l_test.equal();
        l_test.notequal();

        Arrays.stream( TestCActionBool.generate() )
              .map( i -> (Pair<Collection<Boolean>, Map<Class<?>, Collection<Boolean>>>) i )
              .forEach( i -> {
                  try
                  {
                      l_test.allmatch( i );
                      l_test.anymatch( i );
                      l_test.and( i );
                      l_test.or( i );
                      l_test.xor( i );
                      l_test.not( i );
                      l_test.counttrue( i );
                      l_test.countfalse( i );
                  }
                  catch ( final AssumptionViolatedException l_ignored )
                  {
                      System.out.println( l_ignored.getMessage() );
                  }
              } );
    }
}
