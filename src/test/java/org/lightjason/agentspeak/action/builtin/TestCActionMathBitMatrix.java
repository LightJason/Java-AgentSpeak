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

import cern.colt.matrix.tbit.BitMatrix;
import cern.colt.matrix.tbit.BitVector;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
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
import org.lightjason.agentspeak.action.bit.matrix.CAnd;
import org.lightjason.agentspeak.action.bit.matrix.CBoolValue;
import org.lightjason.agentspeak.action.bit.matrix.CColumn;
import org.lightjason.agentspeak.action.bit.matrix.CColumns;
import org.lightjason.agentspeak.action.bit.matrix.CCopy;
import org.lightjason.agentspeak.action.bit.matrix.CCreate;
import org.lightjason.agentspeak.action.bit.matrix.CDimension;
import org.lightjason.agentspeak.action.bit.matrix.CFalseCount;
import org.lightjason.agentspeak.action.bit.matrix.CHammingDistance;
import org.lightjason.agentspeak.action.bit.matrix.CNAnd;
import org.lightjason.agentspeak.action.bit.matrix.CNot;
import org.lightjason.agentspeak.action.bit.matrix.CNumericValue;
import org.lightjason.agentspeak.action.bit.matrix.COr;
import org.lightjason.agentspeak.action.bit.matrix.CRow;
import org.lightjason.agentspeak.action.bit.matrix.CRows;
import org.lightjason.agentspeak.action.bit.matrix.CSize;
import org.lightjason.agentspeak.action.bit.matrix.CToBlas;
import org.lightjason.agentspeak.action.bit.matrix.CToVector;
import org.lightjason.agentspeak.action.bit.matrix.CTrueCount;
import org.lightjason.agentspeak.action.bit.matrix.CXor;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * test math bit matrix functions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionMathBitMatrix extends IBaseTest
{
    /**
     * testing matrix
     * @note static because of usage in data-provider and test-initialize
     */
    private static final BitMatrix MATRIX1 = new BitMatrix( 2, 2 );
    /**
     * testing matrix
     * @note static because of usage in data-provider and test-initialize
     */
    private static final BitMatrix MATRIX2 = new BitMatrix( 2, 2 );


    /**
     * initialize
     */
    @Before
    public void initialize()
    {
        MATRIX1.put( 0, 1, false );
        MATRIX1.put( 1, 0, false );
        MATRIX1.put( 1, 1, true );
        MATRIX1.put( 0, 0, true );

        MATRIX2.put( 0, 1, true );
        MATRIX2.put( 1, 0, true );
        MATRIX2.put( 1, 1, true );
        MATRIX2.put( 0, 0, false );
    }

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generator()
    {
        return testcase(

                Stream.of( MATRIX1, MATRIX2 ),

                Stream.of(
                        CColumns.class,
                        CFalseCount.class,
                        CDimension.class,
                        CCopy.class,
                        CTrueCount.class,
                        CSize.class,
                        CRows.class,
                        CNot.class,
                        COr.class,
                        CAnd.class,
                        CXor.class,
                        CNAnd.class,
                        CHammingDistance.class
                ),
                Stream.of( 2D, 2D ),
                Stream.of( 2D, 1D ),
                Stream.of( 2D, 2D, 2D, 2D ),
                Stream.of( MATRIX1, MATRIX2 ),
                Stream.of( 2D, 3D ),
                Stream.of( 4, 4 ),
                Stream.of( 2D, 2D ),
                Stream.of(),
                Stream.of(),
                Stream.of(),
                Stream.of(),
                Stream.of(),
                Stream.of( 3D )

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
     * test all input actions
     *
     * @param p_input tripel with input, actions classes and results
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
            false,
            IContext.EMPTYPLAN,
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
            Stream.of( 2, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        assertTrue( l_return.get( 0 ).raw() instanceof BitMatrix );
        Assert.assertEquals( 4, l_return.get( 0 ).<BitMatrix>raw().size() );
        Assert.assertEquals( 2, l_return.get( 0 ).<BitMatrix>raw().rows() );
        Assert.assertEquals( 2, l_return.get( 0 ).<BitMatrix>raw().columns() );
    }

    /**
     * test toBitVector
     */
    @Test
    public void tobitvector()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToVector().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof BitVector );
        Assert.assertEquals( l_return.get( 0 ).<BitVector>raw().size(), 4 );

        final BitVector l_bitvector = l_return.get( 0 ).raw();

        assertTrue( l_bitvector.get( 0 ) );
        assertTrue( l_bitvector.get( 1 ) );
        assertTrue( l_bitvector.get( 2 ) );
        Assert.assertFalse( l_bitvector.get( 3 ) );
    }

    /**
     * test column
     */
    @Test
    public void column()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CColumn().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof BitVector );
        Assert.assertEquals( l_return.get( 0 ).<BitVector>raw().size(), 2 );

        final BitVector l_bitvector = l_return.get( 0 ).raw();

        assertTrue( l_bitvector.get( 0 ) );
        assertTrue( l_bitvector.get( 1 ) );
    }

    /**
     * test row
     */
    @Test
    public void row()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof BitVector );
        Assert.assertEquals( l_return.get( 0 ).<BitVector>raw().size(), 2 );

        final BitVector l_bitvector = l_return.get( 0 ).raw();

        assertTrue( l_bitvector.get( 0 ) );
        assertTrue( l_bitvector.get( 1 ) );
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
            Stream.of( MATRIX1, 1, 0 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 0D, l_return.get( 0 ).<Number>raw() );
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
            Stream.of( MATRIX2, 0, 0 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( false, l_return.get( 0 ).<Boolean>raw() );
    }

     /**
     * test toblas
     */
    @Test
    public void toblas()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{0.0, 1.0}, {1.0, 1.0}};

        new CToBlas().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );

        final DoubleMatrix2D l_blas = l_return.get( 0 ).raw();

        Assert.assertEquals( 4, l_blas.size() );
        Assert.assertArrayEquals( l_result, l_blas.toArray() );
    }

}
