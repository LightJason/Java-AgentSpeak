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

package org.lightjason.agentspeak.action.builtin;

import com.codepoetics.protonpack.StreamUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.primes.Primes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.builtin.math.CACos;
import org.lightjason.agentspeak.action.builtin.math.CASin;
import org.lightjason.agentspeak.action.builtin.math.CATan;
import org.lightjason.agentspeak.action.builtin.math.CAbs;
import org.lightjason.agentspeak.action.builtin.math.CAverage;
import org.lightjason.agentspeak.action.builtin.math.CBinomial;
import org.lightjason.agentspeak.action.builtin.math.CCeil;
import org.lightjason.agentspeak.action.builtin.math.CCos;
import org.lightjason.agentspeak.action.builtin.math.CCosh;
import org.lightjason.agentspeak.action.builtin.math.CDegrees;
import org.lightjason.agentspeak.action.builtin.math.CExp;
import org.lightjason.agentspeak.action.builtin.math.CFactorial;
import org.lightjason.agentspeak.action.builtin.math.CFloor;
import org.lightjason.agentspeak.action.builtin.math.CGeometricMean;
import org.lightjason.agentspeak.action.builtin.math.CHarmonicMean;
import org.lightjason.agentspeak.action.builtin.math.CHypot;
import org.lightjason.agentspeak.action.builtin.math.CIsPrime;
import org.lightjason.agentspeak.action.builtin.math.CLog;
import org.lightjason.agentspeak.action.builtin.math.CLog10;
import org.lightjason.agentspeak.action.builtin.math.CMax;
import org.lightjason.agentspeak.action.builtin.math.CMaxIndex;
import org.lightjason.agentspeak.action.builtin.math.CMin;
import org.lightjason.agentspeak.action.builtin.math.CMinIndex;
import org.lightjason.agentspeak.action.builtin.math.CNextPrime;
import org.lightjason.agentspeak.action.builtin.math.CPow;
import org.lightjason.agentspeak.action.builtin.math.CPrimeFactors;
import org.lightjason.agentspeak.action.builtin.math.CRadians;
import org.lightjason.agentspeak.action.builtin.math.CRound;
import org.lightjason.agentspeak.action.builtin.math.CSigmoid;
import org.lightjason.agentspeak.action.builtin.math.CSignum;
import org.lightjason.agentspeak.action.builtin.math.CSin;
import org.lightjason.agentspeak.action.builtin.math.CSinh;
import org.lightjason.agentspeak.action.builtin.math.CSqrt;
import org.lightjason.agentspeak.action.builtin.math.CStirling;
import org.lightjason.agentspeak.action.builtin.math.CSum;
import org.lightjason.agentspeak.action.builtin.math.CTan;
import org.lightjason.agentspeak.action.builtin.math.CTanh;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test math functions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionMath extends IBaseTest
{

    /**
     * data provider generator for single-value tests
     * @return data
     */
    @DataProvider
    public static Object[] singlevaluegenerate()
    {
        return Stream.concat(

            singlevaluetestcase(

                Stream.of( 2.5, 9.1, 111.7, 889.9 ),

                Stream.of(
                    CNextPrime.class
                ),

                i -> (double) Primes.nextPrime( i.intValue() )
            ),

            singlevaluetestcase(

                Stream.of( -2, -6, 4, -1, -5, 3, 49, 30, 6, 5, 1.3, 2.8, 9.7, 1, 8, 180, Math.PI ),

                Stream.of(
                    CAbs.class,
                    CACos.class,
                    CASin.class,
                    CATan.class,
                    CCeil.class,
                    CCos.class,
                    CCosh.class,
                    CDegrees.class,
                    CExp.class,
                    CIsPrime.class,
                    CLog.class,
                    CLog10.class,
                    CFloor.class,
                    CRadians.class,
                    CRound.class,
                    CSignum.class,
                    CSin.class,
                    CSinh.class,
                    CSqrt.class,
                    CTan.class,
                    CTanh.class
                ),

                i -> Math.abs( i.doubleValue() ),
                i -> Math.acos( i.doubleValue() ),
                i -> Math.asin( i.doubleValue() ),
                i -> Math.atan( i.doubleValue() ),
                i -> Math.ceil( i.doubleValue() ),
                i -> Math.cos( i.doubleValue() ),
                i -> Math.cosh( i.doubleValue() ),
                i -> Math.toDegrees( i.doubleValue() ),
                i -> Math.exp( i.doubleValue() ),
                i -> Primes.isPrime( i.intValue() ),
                i -> Math.log( i.doubleValue() ),
                i -> Math.log10( i.doubleValue() ),
                i -> Math.floor( i.doubleValue() ),
                i -> Math.toRadians( i.doubleValue() ),
                i -> Math.round( i.doubleValue() ),
                i -> Math.signum( i.doubleValue() ),
                i -> Math.sin( i.doubleValue() ),
                i -> Math.sinh( i.doubleValue() ),
                i -> Math.sqrt( i.doubleValue() ),
                i -> Math.tan( i.doubleValue() ),
                i -> Math.tanh( i.doubleValue() )
            )

        ).toArray();
    }


    /**
     * data provider generator for aggregation-value tests
     * @return data
     */
    @DataProvider
    public static Object[] aggregationvaluegenerate()
    {
        final Random l_random = new Random();

        return aggregationvaluetestcase(

            IntStream.range( 0, 100 ).boxed().map( i -> l_random.nextGaussian() ),

            Stream.of(
                CAverage.class,
                CSum.class,
                CMin.class,
                CMax.class
            ),

            i -> i.mapToDouble( Number::doubleValue ).average().getAsDouble(),
            i -> i.mapToDouble( Number::doubleValue ).sum(),
            i -> i.mapToDouble( Number::doubleValue ).min().getAsDouble(),
            i -> i.mapToDouble( Number::doubleValue ).max().getAsDouble()

        ).toArray();
    }


    /**
     * create test case
     *
     * @param p_input input data
     * @param p_class action class
     * @param p_result result function
     * @return test-case data
     */
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    private static Stream<Object> singlevaluetestcase( final Stream<Number> p_input, final Stream<Class<? extends IAction>> p_class,
                                                       final Function<Number, ?>... p_result )
    {
        final List<ITerm> l_input = p_input.map( CRawTerm::from ).collect( Collectors.toList() );

        return StreamUtils.zip(
            p_class,
            Arrays.stream( p_result ),
            ( i, j ) -> new ImmutableTriple<>( l_input, i, j )
        );
    }


    /**
     * create test case
     *
     * @param p_input input data
     * @param p_class action class
     * @param p_result result function
     * @return test-case data
     */
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    private static Stream<Object> aggregationvaluetestcase( final Stream<Number> p_input, final Stream<Class<? extends IAction>> p_class,
                                                            final Function<Stream<Number>, ?>... p_result )
    {
        final List<ITerm> l_input = p_input.map( CRawTerm::from ).collect( Collectors.toList() );

        return StreamUtils.zip(
            p_class,
            Arrays.stream( p_result ),
            ( i, j ) -> new ImmutableTriple<>( l_input, i, j )
        );
    }


    /**
     * test all aggregation-value actions
     *
     * @param p_input test data
     *
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     * @throws NoSuchMethodException is thrwon on instantiation error
     * @throws InvocationTargetException is thrwon on instantiation error
     */
    @Test
    @UseDataProvider( "aggregationvaluegenerate" )
    public final void aggregationvalueaction( final Triple<List<ITerm>, Class<? extends IAction>, Function<Stream<Number>, ?>> p_input )
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_input.getMiddle().getConstructor().newInstance().execute(
            false, IContext.EMPTYPLAN,
            p_input.getLeft(),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).raw(), p_input.getRight().apply( p_input.getLeft().stream().map( ITerm::<Number>raw ) ) );
    }


    /**
     * test all single-value actions
     *
     * @param p_input test data
     *
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     * @throws NoSuchMethodException is thrwon on instantiation error
     * @throws InvocationTargetException is thrwon on instantiation error
     */
    @Test
    @UseDataProvider( "singlevaluegenerate" )
    public final void singlevalueaction( final Triple<List<ITerm>, Class<? extends IAction>, Function<Number, ?>> p_input )
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
            l_return.stream().map( ITerm::raw ).toArray(),
            p_input.getLeft().stream().map( ITerm::<Number>raw ).map( p_input.getRight() ).toArray()
        );
    }


    /**
     * test binomial
     */
    @Test
    public final void binomial()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBinomial().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 49, 30, 6, 5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 18851684897584L );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 6L );
    }


    /**
     * test factorial
     */
    @Test
    public final void factorial()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CFactorial().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 5, 1, 2, 3, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 120L );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 1L );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 2L );
        Assert.assertEquals( l_return.get( 3 ).<Number>raw(), 6L );
        Assert.assertEquals( l_return.get( 4 ).<Number>raw(), 24L );
    }


    /**
     * test primefactors
     */
    @Test
    public final void primefactors()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CPrimeFactors().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 8, 120 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2D, 2D, 2D ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 2D, 2D, 2D, 3D, 5D ).toArray() );
    }


    /**
     * test sigmoid
     */
    @Test
    public final void sigmoid()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSigmoid().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 1, 1, 10, 20, 30 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 0.9999546021312976 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 0.9999999979388463 );
        Assert.assertEquals( l_return.get( 3 ).<Number>raw(), 0.9999999999999065 );
    }


    /**
     * test stirling
     */
    @Test
    public final void stirling()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CStirling().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 3, 2, 8, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals( l_return.stream().map( ITerm::<Number>raw ).toArray(), Stream.of( 3L, 966L ).toArray() );
    }


    /**
     * test power
     */
    @Test
    public final void pow()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CPow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 2, 3, 4, 0.5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::raw ).toArray(),
            Stream.of( 9.0, 16.0, 0.25 ).toArray()
        );
    }


    /**
     * test geometricmean
     */
    @Test
    public final void geometricmean( )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CGeometricMean().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1.05, 1.03, 0.94, 1.02, 1.04 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 1.0152139522031014 );
    }


    /**
     * test harmonicmean
     */
    @Test
    public final void harmonicmean()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CHarmonicMean().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 150, 50 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 75.0 );
    }

    /**
     * test hypot
     */
    @Test
    public final void hypot()
    {
        final Random l_random = new Random();
        final List<Double> l_input = IntStream.range( 0, 100 ).mapToDouble( i -> l_random.nextGaussian() ).boxed().collect( Collectors.toList() );

        final List<ITerm> l_return = new ArrayList<>();

        new CHypot().execute(
            false, IContext.EMPTYPLAN,
            l_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            l_return.stream().map( ITerm::<Number>raw ).toArray(),
            StreamUtils.windowed( l_input.stream(), 2, 2 )
                       .mapToDouble( i -> Math.hypot( i.get( 0 ), i.get( 1 ) ) )
                       .boxed()
                       .toArray()
        );
    }


    /**
     * test max index
     */
    @Test
    public final void maxIndex()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMaxIndex().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 3, 4, 9, 1, 7, 8, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 2D );
    }

    /**
     * test min index
     */
    @Test
    public final void minIndex()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMinIndex().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 3, 4, 9, 1, 7, 8, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 3D );
    }

}
