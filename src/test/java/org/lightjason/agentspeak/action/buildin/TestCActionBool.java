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
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
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
public final class TestCActionBool extends IBaseTest
{

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.concat(

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
                Stream.of( false ),
                Stream.of( true ),
                Stream.of( false ),
                Stream.of( true ),
                Stream.of( true ),
                Stream.of( false, true, false, true, true, false ),
                Stream.of( 3L ),
                Stream.of( 3L )
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
                Stream.of( true ),
                Stream.of( true ),
                Stream.of( true ),
                Stream.of( true ),
                Stream.of( false ),
                Stream.of( false, false ),
                Stream.of( 2L ),
                Stream.of( 0L )
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
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    private static Stream<Object> testcase( final Stream<Object> p_input, final Stream<Class<?>> p_classes, final Stream<Object>... p_classresult )
    {
        final List<ITerm> l_input = p_input.map( CRawTerm::from ).collect( Collectors.toList() );

        return StreamUtils.zip(
            p_classes,
            Arrays.stream( p_classresult ),
            ( i, j ) -> new ImmutableTriple<>( l_input, i, j )
        );
    }

    /**
     * generic test-case
     *
     * @param p_input test-case data
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     */
    @Test
    @UseDataProvider( "generate" )
    public final void execute( final Triple<List<ITerm>, Class<? extends IAction>, Stream<Object>> p_input )
    throws IllegalAccessException, InstantiationException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_input.getMiddle().newInstance().execute(
            null,
            false,
            p_input.getLeft(),
            l_return
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::raw ).toArray(),
            p_input.getRight().toArray()
        );
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
            l_return
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
            l_return
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
            l_return
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
            l_return
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
        new TestCActionBool().invoketest();
    }
}
