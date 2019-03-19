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

import cern.colt.matrix.tbit.BitVector;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
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
import org.lightjason.agentspeak.action.bit.vector.CAnd;
import org.lightjason.agentspeak.action.bit.vector.CBoolValue;
import org.lightjason.agentspeak.action.bit.vector.CClear;
import org.lightjason.agentspeak.action.bit.vector.CCopy;
import org.lightjason.agentspeak.action.bit.vector.CCreate;
import org.lightjason.agentspeak.action.bit.vector.CFalseCount;
import org.lightjason.agentspeak.action.bit.vector.CHammingDistance;
import org.lightjason.agentspeak.action.bit.vector.CNAnd;
import org.lightjason.agentspeak.action.bit.vector.CNot;
import org.lightjason.agentspeak.action.bit.vector.CNumericValue;
import org.lightjason.agentspeak.action.bit.vector.COr;
import org.lightjason.agentspeak.action.bit.vector.CRange;
import org.lightjason.agentspeak.action.bit.vector.CSet;
import org.lightjason.agentspeak.action.bit.vector.CSize;
import org.lightjason.agentspeak.action.bit.vector.CToBlas;
import org.lightjason.agentspeak.action.bit.vector.CToList;
import org.lightjason.agentspeak.action.bit.vector.CTrueCount;
import org.lightjason.agentspeak.action.bit.vector.CXor;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for bit vector actions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionMathBitVector extends IBaseTest
{
    /**
     * testing vector
     * @note static because of usage in data-provider and test-initialize
     */
    private static final BitVector VECTOR1 = new BitVector( 3 );
    /**
     * testing matrix
     * @note static because of usage in data-provider and test-initialize
     */
    private static final BitVector VECTOR2 = new BitVector( 3 );


    /**
     * initialize
     */
    @Before
    public void initialize()
    {
        VECTOR1.put( 0, true );
        VECTOR1.put( 1, false );
        VECTOR1.put( 2, false );

        VECTOR2.put( 0, false );
        VECTOR2.put( 1, false );
        VECTOR2.put( 2, true );
    }

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generator()
    {
        return testcase(

                Stream.of( VECTOR1, VECTOR2 ),

                Stream.of(
                        CFalseCount.class,
                        CCopy.class,
                        CTrueCount.class,
                        CSize.class,
                        CNot.class,
                        COr.class,
                        CAnd.class,
                        CNAnd.class,
                        CHammingDistance.class,
                        CXor.class
                ),
                Stream.of( 2D, 2D ),
                Stream.of( VECTOR1, VECTOR2 ),
                Stream.of( 1D, 1D ),
                Stream.of( 3, 3 ),
                Stream.of(),
                Stream.of(),
                Stream.of(),
                Stream.of(),
                Stream.of( 2D ),
                Stream.of()

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
        final List<ITerm> l_input = p_input.map( CRawTerm::of ).collect( Collectors.toList() );

        return StreamUtils.zip(
                p_classes,
                Arrays.stream( p_classresult ),
            ( i, j ) -> new ImmutableTriple<>( l_input, i, j )
        );
    }


    /**
     * test all single-input actions
     *
     * @param p_input tripel of input data, actions classes and results
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     * @throws NoSuchMethodException is thrwon on instantiation error
     * @throws InvocationTargetException is thrwon on instantiation error
     */
    @Test
    @UseDataProvider( "generator" )
    public void action( final Triple<List<ITerm>, Class<? extends IAction>, Stream<Object>> p_input )
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_input.getMiddle().getConstructor().newInstance().execute(
            false, IContext.EMPTYPLAN,
            p_input.getLeft(),
            l_return
        );

        Assert.assertArrayEquals(
                p_input.getMiddle().toGenericString(),
                p_input.getRight().toArray(),
                l_return.stream().map( ITerm::raw ).toArray()
        );
    }

    /**
     * test create
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof BitVector );
        Assert.assertEquals( 3, l_return.get( 0 ).<BitVector>raw().size() );
    }

    /**
     * test boolean value
     */
    @Test
    public void boolValue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBoolValue().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( VECTOR2, 0 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( false, l_return.get( 0 ).<Boolean>raw() );
    }

    /**
     * test set
     */
    @Test
    public void set()
    {
        new CSet().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( VECTOR2, true, 0, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertTrue( VECTOR2.get( 0 ) );
        Assert.assertTrue( VECTOR2.get( 1 ) );
    }

    /**
     * test clear
     */
    @Test
    public void clear()
    {
        new CClear().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( VECTOR2, 0 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertFalse( VECTOR2.get( 0 ) );
    }

    /**
     * test range
     */
    @Test
    public void range()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRange().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( VECTOR2, 0, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( VECTOR2, l_return.get( 0 ).<BitVector>raw() );
    }

    /**
     * test numericvalue
     */
    @Test
    public void numericvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CNumericValue().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( VECTOR1, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 0D, l_return.get( 0 ).<Number>raw() );
    }

    /**
     * test toList
     */
    @Test
    public void tolist()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToList().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( VECTOR1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertArrayEquals( Stream.of( 1D, 0D, 0D ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
    }

    /**
     * test toblas
     */
    @Test
    public void toblas()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToBlas().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( VECTOR2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix1D );
        Assert.assertArrayEquals( Stream.of( 0, 0, 1 ).mapToDouble( i -> i ).toArray(), l_return.get( 0 ).<DoubleMatrix1D>raw().toArray(), 0 );
    }

}
