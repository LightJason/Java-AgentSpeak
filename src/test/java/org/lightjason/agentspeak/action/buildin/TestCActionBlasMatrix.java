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

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import com.codepoetics.protonpack.StreamUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;

import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CColumn;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CColumns;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CCopy;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CCreate;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CDimension;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CRow;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CRows;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CNonZero;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CCondition;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CDeterminant;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CTwoNorm;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.COneNorm;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CMatrixNorm;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CInfinityNorm;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CRank;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CPower;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CSet;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CToList;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CSolve;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CSubMatrix;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CSingularValue;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CSum;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CTranspose;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CTrace;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CAssign;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CGet;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CParse;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CInvert;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CEigen;
import org.lightjason.agentspeak.action.buildin.math.blas.matrix.CGraphLaplacian;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * test math blas matrix functions
 */
@RunWith( DataProviderRunner.class )
public class TestCActionBlasMatrix extends IBaseTest
{

    /**
     * testing matrix
     * @note static because of usage in data-provider
     */
    private static DoubleMatrix2D s_matrix = new DenseDoubleMatrix2D( new double[][]{{2, 6}, {3, 8}} );

    /**
     * testing matrix
     * @note static because of usage in data-provider
     */
    private static DoubleMatrix2D s_matrix1 = new DenseDoubleMatrix2D( new double[][]{{2, 2}, {3, 1}} );

    /**
     * testing matrix
     */
    private DoubleMatrix2D m_matrix = new DenseDoubleMatrix2D( 2, 2 );

    /**
     * testing symmetric matrix
     */
    private DoubleMatrix2D m_matrix1 = new DenseDoubleMatrix2D( new double[][]{{1, 7}, {7, 4}} );

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generator()
    {
        return testcase(

                Stream.of( s_matrix, s_matrix1 ),

                Stream.of(
                        CColumns.class,
                        CDimension.class,
                        CRows.class,
                        CNonZero.class,
                        CCondition.class,
                        CDeterminant.class,
                        CTwoNorm.class,
                        COneNorm.class,
                        CMatrixNorm.class,
                        CInfinityNorm.class,
                        CRank.class,
                        CSum.class,
                        CTrace.class
                ),
                Stream.of( 2L, 2L ),
                Stream.of( 2L, 2L, 2L, 2L ),
                Stream.of( 2L, 2L ),
                Stream.of( 4L, 4L ),
                Stream.of( 56.48229533707794, 4.265564437074639 ),
                Stream.of( -2.000000000000001, -4.0 ),
                Stream.of( 10.628480167651258, 4.130648586880581 ),
                Stream.of( 14.0000, 5.0000 ),
                Stream.of( 10.63014581273465, 4.242640687119285 ),
                Stream.of( 11.0000, 4.0000 ),
                Stream.of( 2L, 2L ),
                Stream.of( s_matrix.zSum(), s_matrix1.zSum() ),
                Stream.of( 10.0, 3.0 )

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
     * test all input actions
     *
     * @note I guess CDeterminant results should be (2.0, 4.0).
     * @throws IllegalAccessException is thrown on instantiation error
     * @throws InstantiationException is thrown on instantiation error
     */
    @Test
    @UseDataProvider( "generator" )
    public final void action( final Triple<List<ITerm>, Class<? extends IAction>, Stream<Object>> p_input )
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
                Stream.of( 2, 2, "dense" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        Assert.assertEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().size(), 4 );
        Assert.assertEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().rows(), 2 );
        Assert.assertEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().columns(), 2 );
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
                Stream.of( 1, s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix1D );
        Assert.assertEquals( l_return.get( 0 ).<DoubleMatrix1D>raw().size(), 2 );

        final DoubleMatrix1D l_vector = l_return.get( 0 ).raw();

        Assert.assertEquals( l_vector.get( 0 ), 2, 0 );
        Assert.assertEquals( l_vector.get( 1 ), 1, 0 );
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
                Stream.of( 1, s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix1D );
        Assert.assertEquals( l_return.get( 0 ).<DoubleMatrix1D>raw().size(), 2 );

        final DoubleMatrix1D l_vector = l_return.get( 0 ).raw();

        Assert.assertEquals( l_vector.get( 0 ), 3, 0 );
        Assert.assertEquals( l_vector.get( 1 ), 1, 0 );
    }

    /**
     * test power
     */
    @Test
    public final void power()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{10.0, 6.0}, {9.0, 7.0}};

        new CPower().execute(
                null,
                false,
                Stream.of( 2, s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        Assert.assertEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().size(), 4 );

        final DoubleMatrix2D l_matrix = l_return.get( 0 ).raw();
        Assert.assertArrayEquals( l_matrix.toArray(), l_result );

    }

    /**
     * test set
     */
    @Test
    public final void set()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSet().execute(
                null,
                false,
                Stream.of( 0, 1, 6.0, s_matrix ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertEquals( s_matrix.get( 0, 1 ), 6, 0 );
    }

    /**
     * test toList
     */
    @Test
    public final void tolist()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToList().execute(
                null,
                false,
                Stream.of( s_matrix ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List );

        final List<Number> l_tolist = l_return.get( 0 ).raw();
        Assert.assertArrayEquals( l_tolist.toArray(), Stream.of( 2.0, 6.0, 3.0, 8.0 ).collect( Collectors.toList() ).toArray() );
    }

    /**
     * test transpose
     */
    @Test
    public final void transpose()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{2.0, 3.0}, {2.0, 1.0}};

        new CTranspose().execute(
                null,
                false,
                Stream.of( s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );

        final DoubleMatrix2D l_matrix = l_return.get( 0 ).raw();
        Assert.assertArrayEquals( l_matrix.toArray(), l_result );
    }

    /**
     * test submatrix
     */
    @Test
    public final void submatrix()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{2.0, 2.0}};

        new CSubMatrix().execute(
                null,
                false,
                Stream.of( 0, 0, 0, 1, s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );

        final DoubleMatrix2D l_matrix = l_return.get( 0 ).raw();
        Assert.assertArrayEquals( l_matrix.toArray(), l_result );
    }

    /**
     * test solve
     * @note it can be {{1.0, -5.0},{0.0, 2.0}}
     */
    @Test
    public final void solve()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{1.0, -4.999999999999998}, {0.0, 1.9999999999999993}};

        new CSolve().execute(
                null,
                false,
                Stream.of( s_matrix, s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );

        final DoubleMatrix2D l_matrix = l_return.get( 0 ).raw();
        Assert.assertArrayEquals( l_matrix.toArray(), l_result );
    }

    /**
     * test assign
     */
    @Test
    public final void assign()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{2.0, 2.0}, {2.0, 2.0}};

        new CAssign().execute(
                null,
                false,
                Stream.of( 2, m_matrix ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertArrayEquals( m_matrix.toArray(), l_result );

        new CAssign().execute(
                null,
                false,
                Stream.of( s_matrix1, m_matrix ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertArrayEquals( s_matrix1.toArray(), m_matrix.toArray() );
    }

    /**
     * test get
     */
    @Test
    public final void get()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CGet().execute(
                null,
                false,
                Stream.of( s_matrix1, 0, 1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Double );
        Assert.assertEquals( l_return.get( 0 ).<Double>raw(), 2, 0 );
    }

    /**
     * test parse
     */
    @Test
    public final void parse()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{1.0, 2.0}, {3.0, 4.0}};

        new CParse().execute(
                null,
                false,
                Stream.of( "1,2;3,4", "dense" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().toArray(), l_result );
    }

    /**
     * test invert
     * @note I guess it should be {{-0.25, 0.5},{0.75, -0.5}}
     */
    @Test
    public final void invert()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{-0.24999999999999994, 0.5}, {0.7499999999999999, -0.4999999999999999}};

        new CInvert().execute(
                null,
                false,
                Stream.of( s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().toArray(), l_result );
    }

    /**
     * test eigen
     */
    @Test
    public final void eigen()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_result = {{0.7071067811865475, -0.565685424949238}, {0.7071067811865475, 0.8485281374238569}};

        new CEigen().execute(
                null,
                false,
                Stream.of( s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DenseDoubleMatrix1D );
        Assert.assertArrayEquals( l_return.get( 0 ).<DenseDoubleMatrix1D>raw().toArray(), Stream.of( 4, -1 ).mapToDouble( i -> i ).toArray(), 0 );

        Assert.assertTrue( l_return.get( 1 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals( l_return.get( 1 ).<DoubleMatrix2D>raw().toArray(), l_result );
    }

    /**
     * test singularvalue
     */
    @Test
    public final void singularvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Double[][] l_matrixu = {{0.6618025632357398, 0.7496781758158658}, {0.7496781758158657, -0.66180256323574}};
        final Double[][] l_matrixv = {{0.864910093118595, -0.501926818193233}, {0.501926818193233, 0.864910093118595}};

        new CSingularValue().execute(
                null,
                false,
                Stream.of( s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 3 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DenseDoubleMatrix1D );
        Assert.assertArrayEquals( l_return.get( 0 ).<DenseDoubleMatrix1D>raw().toArray(),
                Stream.of(  4.130648586880581, 0.9683709267122022 ).mapToDouble( i -> i ).toArray(), 0 );

        Assert.assertTrue( l_return.get( 1 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals( l_return.get( 1 ).<DoubleMatrix2D>raw().toArray(), l_matrixu );

        Assert.assertTrue( l_return.get( 2 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals( l_return.get( 2 ).<DoubleMatrix2D>raw().toArray(), l_matrixv );
    }

    /**
     * test copy
     * @bug CCopy results empty list
     */
    @Test
    @Ignore
    public final void copy()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCopy().execute(
                null,
                false,
                Stream.of( s_matrix, s_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( s_matrix, s_matrix1 ).toArray() );
    }

    /**
     * test graphlaplacian
     * @bug row sums is zero
     */
    @Ignore
    @Test
    public final void graphlaplacian()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CGraphLaplacian().execute(
                null,
                false,
                Stream.of( m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        final DoubleMatrix2D l_matrix = l_return.get( 0 ).raw();

        Assert.assertEquals( l_matrix.getQuick( 0, 0 ) + l_matrix.getQuick( 0, 1 ), 0, 0 );
        Assert.assertEquals( l_matrix.getQuick( 1, 0 ) + l_matrix.getQuick( 1, 1 ), 0, 0 );
    }

    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionBlasMatrix().invoketest();
    }
}
