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

import cern.colt.bitvector.BitMatrix;
import cern.colt.bitvector.BitVector;
import com.codepoetics.protonpack.StreamUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CCreate;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CAnd;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CNAnd;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CNot;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CRow;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CColumn;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CToVector;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CTrueCount;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CFalseCount;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CBoolValue;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CColumns;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CRows;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CCopy;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CDimension;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CHammingDistance;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CNumericValue;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CXor;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CSize;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.COr;


import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * test math bit matrix functions
 */
@RunWith( DataProviderRunner.class )
public class TestCActionBitMatrix extends IBaseTest
{
    private static BitMatrix s_matrix = new BitMatrix( 2, 2 );
    private static BitMatrix s_matrix1 = new BitMatrix( 2, 2 );
    /**
     * initialize
     */
    @Before
    public void initialize()
    {
        s_matrix.put( 0, 1, false );
        s_matrix.put( 1, 0, false );
        s_matrix.put( 1, 1, true );
        s_matrix.put( 0, 0, true );

        s_matrix1.put( 0, 1, true );
        s_matrix1.put( 1, 0, true );
        s_matrix1.put( 1, 1, true );
        s_matrix1.put( 0, 0, false );
    }

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] singleinputgenerator()
    {
        return testcase(

                Stream.of( s_matrix1 ),

                Stream.of(
                        CColumns.class,
                        CFalseCount.class,
                        CDimension.class,
                        CCopy.class,
                        CTrueCount.class,
                        CSize.class,
                        CNot.class,
                        CRows.class
                ),
                Stream.of( 2L ),
                Stream.of( 1L ),
                Stream.of( 2L, 2L ),
                Stream.of( s_matrix1 ),
                Stream.of( 3L ),
                Stream.of( 4 ),
                Stream.of( 2L ),
                Stream.of()

        ).toArray();
    }

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] multipleinputgenerator()
    {
        return testcase(

                Stream.of( s_matrix, s_matrix1 ),

                Stream.of(
                        COr.class,
                        CAnd.class,
                        CXor.class,
                        CNAnd.class,
                        CHammingDistance.class
                ),
                Stream.of(),
                Stream.of(),
                Stream.of(),
                Stream.of(),
                Stream.of( 3L )

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
     * test all single-input actions
     *
     * @throws IllegalAccessException is thrown on instantiation error
     * @throws InstantiationException is thrown on instantiation error
     */
    @Test
    @UseDataProvider( "singleinputgenerator" )
    public final void singleinputaction( final Triple<List<ITerm>, Class<? extends IAction>, Stream<Object>> p_input )
            throws IllegalAccessException, InstantiationException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_input.getMiddle().newInstance().execute(
                null,
                false,
                p_input.getLeft(),
                l_return,
                Collections.emptyList()
        );

        Assert.assertArrayEquals(
                l_return.stream().map( ITerm::raw ).toArray(),
                p_input.getRight().toArray()
        );
    }

    /**
     * test all multiple-input actions
     *
     * @throws IllegalAccessException is thrown on instantiation error
     * @throws InstantiationException is thrown on instantiation error
     */
    @Test
    @UseDataProvider( "multipleinputgenerator" )
    public final void multipleinputaction( final Triple<List<ITerm>, Class<? extends IAction>, Stream<Object>> p_input )
            throws IllegalAccessException, InstantiationException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_input.getMiddle().newInstance().execute(
                null,
                false,
                p_input.getLeft(),
                l_return,
                Collections.emptyList()
        );

        Assert.assertArrayEquals(
                l_return.stream().map( ITerm::raw ).toArray(),
                p_input.getRight().toArray()
        );

    }

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
                Stream.of( 2, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<BitMatrix>raw(), new BitMatrix( 2, 2 ) );
    }

    /**
     * test toBitVector
     */
    @Test
    public final void tobitvector()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToVector().execute(
                null,
                false,
                Stream.of( new BitMatrix( 2, 2 ) ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<BitMatrix>raw(), new BitMatrix( 2, 2 ).toBitVector() );
    }

    /**
     * test boolean value
     */
    @Test
    public final void boolValue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBoolValue().execute(
                null,
                false,
                Stream.of( s_matrix1, 0, 0 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Boolean>raw(), false );

    }

    /**
     * test column
     */
    @Test
    public final void column()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CColumn().execute(
                null,
                false,
                Stream.of( 2, new BitMatrix( 2, 3 ) ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<BitMatrix>raw(), new BitVector( 3 ) );
    }

    /**
     * test row
     */
    @Test
    public final void row()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRow().execute(
                null,
                false,
                Stream.of( 2, new BitMatrix( 2, 2 ) ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<BitMatrix>raw(), new BitVector( 2 ) );
    }

    /**
     * test numericvalue
     */
    @Test
    public final void numericvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CNumericValue().execute(
                null,
                false,
                Stream.of( s_matrix, 1, 0 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<BitMatrix>raw(), 0L );
    }




    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionBitMatrix().invoketest();
    }
}
