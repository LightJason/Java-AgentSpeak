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

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.SparseDoubleMatrix2D;
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
import org.lightjason.agentspeak.action.blas.matrix.CAssign;
import org.lightjason.agentspeak.action.blas.matrix.CColumn;
import org.lightjason.agentspeak.action.blas.matrix.CColumnSum;
import org.lightjason.agentspeak.action.blas.matrix.CColumns;
import org.lightjason.agentspeak.action.blas.matrix.CCondition;
import org.lightjason.agentspeak.action.blas.matrix.CCopy;
import org.lightjason.agentspeak.action.blas.matrix.CCreate;
import org.lightjason.agentspeak.action.blas.matrix.CDeterminant;
import org.lightjason.agentspeak.action.blas.matrix.CDiagonal;
import org.lightjason.agentspeak.action.blas.matrix.CDimension;
import org.lightjason.agentspeak.action.blas.matrix.CEigen;
import org.lightjason.agentspeak.action.blas.matrix.CGet;
import org.lightjason.agentspeak.action.blas.matrix.CGraphLaplacian;
import org.lightjason.agentspeak.action.blas.matrix.CIdentity;
import org.lightjason.agentspeak.action.blas.matrix.CInfinityNorm;
import org.lightjason.agentspeak.action.blas.matrix.CInvert;
import org.lightjason.agentspeak.action.blas.matrix.CMatrixNorm;
import org.lightjason.agentspeak.action.blas.matrix.CNonZero;
import org.lightjason.agentspeak.action.blas.matrix.CNormalizedGraphLaplacian;
import org.lightjason.agentspeak.action.blas.matrix.COneNorm;
import org.lightjason.agentspeak.action.blas.matrix.CParse;
import org.lightjason.agentspeak.action.blas.matrix.CPower;
import org.lightjason.agentspeak.action.blas.matrix.CRank;
import org.lightjason.agentspeak.action.blas.matrix.CRow;
import org.lightjason.agentspeak.action.blas.matrix.CRowSum;
import org.lightjason.agentspeak.action.blas.matrix.CRows;
import org.lightjason.agentspeak.action.blas.matrix.CSet;
import org.lightjason.agentspeak.action.blas.matrix.CSingularValue;
import org.lightjason.agentspeak.action.blas.matrix.CSolve;
import org.lightjason.agentspeak.action.blas.matrix.CSubMatrix;
import org.lightjason.agentspeak.action.blas.matrix.CSum;
import org.lightjason.agentspeak.action.blas.matrix.CToList;
import org.lightjason.agentspeak.action.blas.matrix.CTrace;
import org.lightjason.agentspeak.action.blas.matrix.CTranspose;
import org.lightjason.agentspeak.action.blas.matrix.CTwoNorm;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * test math blas matrix functions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionMathBlasMatrix extends IBaseTest
{

    /**
     * testing matrix
     * @note static because of usage in data-provider
     */
    private static final DoubleMatrix2D MATRIX1 = new DenseDoubleMatrix2D( new double[][]{{2, 6}, {3, 8}} );

    /**
     * testing matrix
     * @note static because of usage in data-provider
     */
    private static final DoubleMatrix2D MATRIX2 = new DenseDoubleMatrix2D( new double[][]{{2, 2}, {3, 1}} );

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
                Stream.of( 2D, 2D ),
                Stream.of( 2D, 2D, 2D, 2D ),
                Stream.of( 2D, 2D ),
                Stream.of( 4D, 4D ),
                Stream.of( 56.48229533707812, 4.265564437074639 ),
                Stream.of( -2.000000000000001, -4.0 ),
                Stream.of( 10.628480167651258, 4.130648586880582 ),
                Stream.of( 14.0000, 5.0000 ),
                Stream.of( 10.63014581273465, 4.242640687119285 ),
                Stream.of( 11.0000, 4.0000 ),
                Stream.of( 2D, 2D ),
                Stream.of( MATRIX1.zSum(), MATRIX2.zSum() ),
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
     * @param p_input tripel of input data, actions and results
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
            false,
            IContext.EMPTYPLAN,
            Stream.of( 2, 2, "dense" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        Assert.assertEquals( 4, l_return.get( 0 ).<DoubleMatrix2D>raw().size() );
        Assert.assertEquals( 2, l_return.get( 0 ).<DoubleMatrix2D>raw().rows() );
        Assert.assertEquals( 2, l_return.get( 0 ).<DoubleMatrix2D>raw().columns() );
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

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix1D );
        Assert.assertEquals( 2, l_return.get( 0 ).<DoubleMatrix1D>raw().size() );

        Assert.assertEquals( 2, l_return.get( 0 ).<DoubleMatrix1D>raw().get( 0 ), 0 );
        Assert.assertEquals( 1, l_return.get( 0 ).<DoubleMatrix1D>raw().get( 1 ), 0 );
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

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix1D );
        Assert.assertEquals( 2, l_return.get( 0 ).<DoubleMatrix1D>raw().size() );

        Assert.assertEquals( 3, l_return.get( 0 ).<DoubleMatrix1D>raw().get( 0 ), 0 );
        Assert.assertEquals( 1, l_return.get( 0 ).<DoubleMatrix1D>raw().get( 1 ), 0 );
    }

    /**
     * test power
     */
    @Test
    public void power()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CPower().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 2, MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        Assert.assertEquals( 4, l_return.get( 0 ).<DoubleMatrix2D>raw().size() );

        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{10.0, 6.0}, {9.0, 7.0}} ).toArray(),
            l_return.get( 0 ).<DoubleMatrix2D>raw().toArray()
        );

    }

    /**
     * test set
     */
    @Test
    public void set()
    {
        new CSet().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 0, 1, 6.0, MATRIX1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( 6, MATRIX1.get( 0, 1 ), 0 );
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
            Stream.of( MATRIX1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List );

        Assert.assertArrayEquals(
            Stream.of( 2.0, 6.0, 3.0, 8.0 ).collect( Collectors.toList() ).toArray(),
            l_return.get( 0 ).<List<?>>raw().toArray()
        );
    }

    /**
     * test transpose
     */
    @Test
    public void transpose()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CTranspose().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );

        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{2.0, 3.0}, {2.0, 1.0}} ).toArray(),
            l_return.get( 0 ).<DoubleMatrix2D>raw().toArray()
        );
    }

    /**
     * test submatrix
     */
    @Test
    public void submatrix()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSubMatrix().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 0, 0, 0, 1, MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );

        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{2.0, 2.0}} ).toArray(),
            l_return.get( 0 ).<DoubleMatrix2D>raw().toArray()
        );
    }

    /**
     * test solve
     */
    @Test
    public void solve()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSolve().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX1, MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );

        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{1.0, -4.999999999999998}, {0.0, 1.9999999999999993}} ).toArray(),
            l_return.get( 0 ).<DoubleMatrix2D>raw().toArray()
        );
    }

    /**
     * test assign scalar
     */
    @Test
    public void assignscalar()
    {
        final DoubleMatrix2D l_matrix = new DenseDoubleMatrix2D( 2, 2 );

        new CAssign().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 2, l_matrix ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( new double[][]{{2.0, 2.0}, {2.0, 2.0}}, l_matrix.toArray() );
    }

    /**
     * test assign matrix
     */
    @Test
    public void assignmatrix()
    {
        final DoubleMatrix2D l_matrix = new DenseDoubleMatrix2D( 2, 2 );

        new CAssign().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX2, l_matrix ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertArrayEquals( l_matrix.toArray(), MATRIX2.toArray() );
    }

    /**
     * test get
     */
    @Test
    public void get()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CGet().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX2, 0, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Double );
        Assert.assertEquals( 2, l_return.get( 0 ).<Double>raw(), 0 );
    }

    /**
     * test parse
     */
    @Test
    public void parse()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CParse().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "1,2;3,4", "dense" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{1.0, 2.0}, {3.0, 4.0}} ).toArray(),
            l_return.get( 0 ).<DoubleMatrix2D>raw().toArray()
        );
    }

    /**
     * test invert
     */
    @Test
    public void invert()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInvert().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{-0.24999999999999994, 0.5}, {0.7499999999999999, -0.4999999999999999}} ).toArray(),
            l_return.get( 0 ).<DoubleMatrix2D>raw().toArray()
        );
    }

    /**
     * test eigen
     */
    @Test
    public void eigen()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CEigen().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DenseDoubleMatrix1D );
        Assert.assertArrayEquals( l_return.get( 0 ).<DenseDoubleMatrix1D>raw().toArray(), Stream.of( 4, -1 ).mapToDouble( i -> i ).toArray(), 0 );

        Assert.assertTrue( l_return.get( 1 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{0.7071067811865475, -0.565685424949238}, {0.7071067811865475, 0.8485281374238569}} ).toArray(),
            l_return.get( 1 ).<DoubleMatrix2D>raw().toArray()
        );
    }

    /**
     * test singularvalue
     */
    @Test
    public void singularvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSingularValue().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DenseDoubleMatrix1D );
        Assert.assertArrayEquals(
            l_return.get( 0 ).<DenseDoubleMatrix1D>raw().toArray(),
            Stream.of(  4.130648586880582, 0.9683709267122025 ).mapToDouble( i -> i ).toArray(), 0 );

        Assert.assertTrue( l_return.get( 1 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{-0.6618025632357403, -0.7496781758158657}, {-0.7496781758158659, 0.66180256323574}} ).toArray(),
            l_return.get( 1 ).<DoubleMatrix2D>raw().toArray()
        );

        Assert.assertTrue( l_return.get( 2 ).raw() instanceof DoubleMatrix2D );
        Assert.assertArrayEquals(
            new DenseDoubleMatrix2D( new double[][]{{-0.8649100931185951, 0.5019268181932333}, {-0.5019268181932333, -0.8649100931185951}} ).toArray(),
            l_return.get( 2 ).<DoubleMatrix2D>raw().toArray()
        );
    }

    /**
     * test copy
     */
    @Test
    public void copy()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCopy().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( MATRIX1, MATRIX2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertArrayEquals( Stream.of( MATRIX1, MATRIX2 ).toArray(), l_return.stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test graph-laplacian
     */
    @Test
    public void graphlaplacian()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CGraphLaplacian().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                    new SparseDoubleMatrix2D( new double[][]{
                        {0, 1, 0, 0, 1, 0},
                        {1, 0, 1, 0, 1, 0},
                        {0, 1, 0, 1, 0, 0},
                        {0, 0, 1, 0, 1, 1},
                        {1, 1, 0, 1, 0, 0},
                        {0, 0, 0, 1, 0, 0}
                    } )
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        final DoubleMatrix2D l_result = l_return.get( 0 ).raw();

        IntStream.range( 0, l_result.rows() )
                 .boxed()
                 .map( l_result::viewRow )
                 .mapToDouble( DoubleMatrix1D::zSum )
                 .forEach( i -> Assert.assertEquals( 0, i, 0 ) );

        IntStream.range( 0, l_result.columns() )
                 .boxed()
                 .map( l_result::viewColumn )
                 .mapToDouble( DoubleMatrix1D::zSum )
                 .forEach( i -> Assert.assertEquals( 0, i, 0 ) );
    }


    /**
     * test normalized graph-laplacian
     */
    @Test
    public void normalizedgraphlaplacian()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CNormalizedGraphLaplacian().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                new SparseDoubleMatrix2D( new double[][]{
                    {0, 1, 0, 0, 1, 0},
                    {1, 0, 1, 0, 1, 0},
                    {0, 1, 0, 1, 0, 0},
                    {0, 0, 1, 0, 1, 1},
                    {1, 1, 0, 1, 0, 0},
                    {0, 0, 0, 1, 0, 0}
                } )
            ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        final DoubleMatrix2D l_result = l_return.get( 0 ).raw();

        IntStream.range( 0, l_result.rows() ).boxed().forEach( i -> Assert.assertEquals( 1, l_result.getQuick( i, i ), 0 ) );
        IntStream.range( 0, l_result.rows() )
                 .boxed()
                 .map( l_result::viewRow )
                 .mapToDouble( DoubleMatrix1D::zSum )
                 .forEach( i -> Assert.assertEquals( 0, i, 1e-10 ) );
    }

    /**
     * test row sum
     */
    @Test
    public void rowsum()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRowSum().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                new SparseDoubleMatrix2D( new double[][]{
                    {1, 0, 0, 0, 0, 0},
                    {1, 2, 0, 0, 0, 0},
                    {1, 2, 3, 0, 0, 0},
                    {1, 2, 3, 4, 0, 0},
                    {1, 2, 3, 4, 5, 0},
                    {1, 2, 3, -1, -2, -3}
                } )
            ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertArrayEquals(
            Stream.of( 1D, 3D, 6D, 10D, 15D, 0D ).toArray(),
            Arrays.stream( l_return.get( 0 ).<DoubleMatrix1D>raw().toArray() ).boxed().toArray()
        );
    }


    /**
     * test column sum
     */
    @Test
    public void columsum()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CColumnSum().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                new SparseDoubleMatrix2D( new double[][]{
                    {1, 0, 0, 0, 0, 0},
                    {1, 2, 0, 0, 0, 0},
                    {1, 2, 3, 0, 0, 0},
                    {1, 2, 3, 4, 0, 0},
                    {1, 2, 3, 4, 5, 0},
                    {1, 2, 3, -1, -2, -3}
                } )
            ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertArrayEquals(
            Stream.of( 6D, 10D, 12D, 7D, 3D, -3D ).toArray(),
            Arrays.stream( l_return.get( 0 ).<DoubleMatrix1D>raw().toArray() ).boxed().toArray()
        );
    }


    /**
     * test identity
     */
    @Test
    public void identity()
    {
        final int l_size = Math.abs( new Random().nextInt( 98 ) + 2 );
        final List<ITerm> l_return = new ArrayList<>();

        new CIdentity().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( l_size ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        final DoubleMatrix2D l_result = l_return.get( 0 ).raw();

        Assert.assertTrue(
            IntStream.range( 0, l_result.rows() )
                 .boxed()
                 .flatMap( i -> IntStream.range( 0, l_result.columns() )
                                         .boxed()
                                         .map( j -> i.equals( j ) ? l_result.getQuick( i, j ) == 1D : l_result.getQuick( i, j ) == 0D )
                 )
            .allMatch( i -> i )
        );
    }


    /**
     * test diagonal
     */
    @Test
    public void diagonal()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final double[] l_data = new double[]{1, 3, 5, 11};

        new CDiagonal().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                new DenseDoubleMatrix1D( l_data )
            ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        final DoubleMatrix2D l_result = l_return.get( 0 ).raw();

        Assert.assertArrayEquals(
            IntStream.range( 0, l_result.rows() )
                     .boxed()
                     .map( i -> l_result.getQuick( i, i ) )
                     .toArray(),
            Arrays.stream( l_data ).boxed().toArray()
        );

        IntStream.range( 0, l_result.rows() )
                 .forEach( i -> IntStream.range( 0, l_result.columns() )
                                         .filter( j -> i != j )
                                         .forEach( j -> Assert.assertEquals( 0, l_result.getQuick( i, j ), 0 ) ) );
    }

}
