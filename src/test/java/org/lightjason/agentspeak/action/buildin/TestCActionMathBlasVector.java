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
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
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
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CCopy;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CAssign;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CGet;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CToList;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CSet;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CCreate;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CDotProduct;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CNonZero;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CSum;
import org.lightjason.agentspeak.action.buildin.math.blas.vector.CFromList;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * test math blas vector functions
 */
@RunWith( DataProviderRunner.class )
public class TestCActionMathBlasVector extends IBaseTest
{

    /**
     * testing vector
     * @note static because of usage in data-provider
     */
    private static DoubleMatrix1D s_vector = new DenseDoubleMatrix1D( new double[]{2, 5, 3, 8} );

    /**
     * testing vector
     * @note static because of usage in data-provider
     */
    private static DoubleMatrix1D s_vector1 = new DenseDoubleMatrix1D( new double[]{8, 6, 2, 1} );


    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generator()
    {
        return testcase(

                Stream.of( s_vector, s_vector1 ),

                Stream.of(
                        CNonZero.class,
                        CSum.class,
                        CDotProduct.class
                ),
                Stream.of( 4L, 4L ),
                Stream.of( s_vector.zSum(), s_vector1.zSum() ),
                Stream.of( 60.0 )

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
                l_return
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
                Stream.of( 2, "dense" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix1D );
        Assert.assertEquals( l_return.get( 0 ).<DoubleMatrix1D>raw().size(), 2 );
    }

    /**
     * test set
     */
    @Test
    public final void set()
    {
        final DoubleMatrix1D l_vector = new DenseDoubleMatrix1D( 4 );
        final List<ITerm> l_return = new ArrayList<>();

        new CSet().execute(
                null,
                false,
                Stream.of( 0, 6.0, l_vector ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertEquals( l_vector.get( 0 ), 6, 0 );
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
                Stream.of( s_vector ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List );

        final List<Number> l_tolist = l_return.get( 0 ).raw();
        Assert.assertArrayEquals( l_tolist.toArray(), Stream.of( 2.0, 5.0, 3.0, 8.0 ).collect( Collectors.toList() ).toArray() );
    }

    /**
     * test assign scalar
     */
    @Test
    public final void assignscalar()
    {
        final DoubleMatrix1D l_vector = new DenseDoubleMatrix1D( 4 );
        final List<ITerm> l_return = new ArrayList<>();

        new CAssign().execute(
                null,
                false,
                Stream.of( 2, l_vector ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertArrayEquals( l_vector.toArray(), Stream.of( 2, 2, 2, 2 ).mapToDouble( i -> i ).toArray(), 0 );


    }

    /**
     * test assign vector
     */
    @Test
    public final void assignvector()
    {
        final DoubleMatrix1D l_vector = new DenseDoubleMatrix1D( 4 );
        final List<ITerm> l_return = new ArrayList<>();

        new CAssign().execute(
            null,
            false,
            Stream.of( s_vector1, l_vector ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertArrayEquals( l_vector.toArray(), s_vector1.toArray(), 0 );
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
                Stream.of( s_vector, 0 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Double );
        Assert.assertEquals( l_return.get( 0 ).<Double>raw(), 2, 0 );
    }

    /**
     * test copy
     */
    @Test
    public final void copy()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCopy().execute(
                null,
                false,
                Stream.of( s_vector, s_vector1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( s_vector, s_vector1 ).toArray() );
    }

    /**
     * test fromlist
     */
    @Test
    public final void fromlist()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final DoubleMatrix1D l_result = new DenseDoubleMatrix1D( new double[]{1, 2, 3} );

        new CFromList().execute(
                null,
                false,
                Stream.of( Stream.of( 1, 2, 3 ).collect( Collectors.toList() ), "dense" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix1D );
        Assert.assertArrayEquals( l_return.get( 0 ).<DoubleMatrix1D>raw().toArray(), l_result.toArray(), 0 );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionMathBlasVector().invoketest();
    }
}
