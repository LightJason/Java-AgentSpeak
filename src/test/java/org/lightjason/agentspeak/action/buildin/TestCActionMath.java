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
import org.apache.commons.math3.primes.Primes;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.math.CACos;
import org.lightjason.agentspeak.action.buildin.math.CASin;
import org.lightjason.agentspeak.action.buildin.math.CATan;
import org.lightjason.agentspeak.action.buildin.math.CAbs;
import org.lightjason.agentspeak.action.buildin.math.CAverage;
import org.lightjason.agentspeak.action.buildin.math.CBinomial;
import org.lightjason.agentspeak.action.buildin.math.CCeil;
import org.lightjason.agentspeak.action.buildin.math.CCos;
import org.lightjason.agentspeak.action.buildin.math.CCosh;
import org.lightjason.agentspeak.action.buildin.math.CDegrees;
import org.lightjason.agentspeak.action.buildin.math.CExp;
import org.lightjason.agentspeak.action.buildin.math.CFactorial;
import org.lightjason.agentspeak.action.buildin.math.CFloor;
import org.lightjason.agentspeak.action.buildin.math.CGeometricMean;
import org.lightjason.agentspeak.action.buildin.math.CHarmonicMean;
import org.lightjason.agentspeak.action.buildin.math.CHypot;
import org.lightjason.agentspeak.action.buildin.math.CIsPrime;
import org.lightjason.agentspeak.action.buildin.math.CLog;
import org.lightjason.agentspeak.action.buildin.math.CLog10;
import org.lightjason.agentspeak.action.buildin.math.CMax;
import org.lightjason.agentspeak.action.buildin.math.CMaxIndex;
import org.lightjason.agentspeak.action.buildin.math.CMin;
import org.lightjason.agentspeak.action.buildin.math.CMinIndex;
import org.lightjason.agentspeak.action.buildin.math.CNextPrime;
import org.lightjason.agentspeak.action.buildin.math.CPow;
import org.lightjason.agentspeak.action.buildin.math.CPrimeFactors;
import org.lightjason.agentspeak.action.buildin.math.CRadians;
import org.lightjason.agentspeak.action.buildin.math.CRound;
import org.lightjason.agentspeak.action.buildin.math.CSigmoid;
import org.lightjason.agentspeak.action.buildin.math.CSignum;
import org.lightjason.agentspeak.action.buildin.math.CSin;
import org.lightjason.agentspeak.action.buildin.math.CSinh;
import org.lightjason.agentspeak.action.buildin.math.CSqrt;
import org.lightjason.agentspeak.action.buildin.math.CStirling;
import org.lightjason.agentspeak.action.buildin.math.CSum;
import org.lightjason.agentspeak.action.buildin.math.CTan;
import org.lightjason.agentspeak.action.buildin.math.CTanh;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test math functions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionMath extends IBaseTest
{

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.of( Stream.of( -2, -6, 4, -1, -5, 3, 49, 30, 6, 5, 1.3, 2.8, 9.7, 1, 8, 180 )
                .collect( Collectors.toList() ) ).toArray();
    }


    /**
     * test abs
     */
    @Test
    @UseDataProvider( "generate" )
    public final void abs( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAbs().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.abs( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test acos
     */
    @Test
    @UseDataProvider( "generate" )
    public final void acos( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CACos().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.acos( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }

    /**
     * test asin
     */
    @Test
    @UseDataProvider( "generate" )
    public final void asin( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CASin().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.asin( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }

    /**
     * test atan
     */
    @Test
    @UseDataProvider( "generate" )
    public final void atan( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CATan().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.atan( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }

    /**
     * test binomial
     */
    @Test
    public final void binomial()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBinomial().execute(
                null,
                false,
                Stream.of( 49, 30, 6, 5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 18851684897584L );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 593775L );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 6L );

    }

    /**
     * test binomial error
     */
    @Test
    @Ignore
    public final void binomialerror()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBinomial().execute(
                null,
                false,
                Stream.of( 49, 6, 30, 5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

    }

    /**
     * test average
     */
    @Test
    @UseDataProvider( "generate" )
    public final void average( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAverage().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 17.8625 );

    }

    /**
     * test ceil
     */
    @Test
    @UseDataProvider( "generate" )
    public final void ceil( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCeil().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.ceil( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test cos
     */
    @Test
    @UseDataProvider( "generate" )
    public final void cos( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCos().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.cos( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test cosh
     */
    @Test
    @UseDataProvider( "generate" )
    public final void cosh( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCosh().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.cosh( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test degrees
     */
    @Test
    @UseDataProvider( "generate" )
    public final void degrees( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CDegrees().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.toDegrees( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );


    }

    /**
     * test exponential
     */
    @Test
    @UseDataProvider( "generate" )
    public final void exp( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CExp().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.exp( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }


    /**
     * test factorial
     */
    @Test
    public final void factorial()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CFactorial().execute(
                null,
                false,
                Stream.of( 5, 1, 2, 3, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 120L );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 1L );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 2L );
        Assert.assertEquals( l_return.get( 3 ).<Number>raw(), 6L );
        Assert.assertEquals( l_return.get( 4 ).<Number>raw(), 24L );

    }

    /**
     * test geometricmean
     */
    @Test
    @UseDataProvider( "generate" )
    public final void geometricmean( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CGeometricMean().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 5.7942925477557 );

    }


    /**
     * test harmonicmean
     */
    @Test
    @UseDataProvider( "generate" )
    public final void harmonicmean( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CHarmonicMean().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 10.687351712676081 );
    }

    /**
     * test hypot
     * l_return is empty???
     */
    @Test
    @UseDataProvider( "generate" )
    public final void hypot( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CHypot().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        for ( int i = 0; i < p_input.size() - 1; i++ )
        {
            Assert.assertEquals( l_return.get( i ).<Number>raw(), Math.hypot( p_input.get( i ).doubleValue(),
                                                                        p_input.get( i + 1 ).doubleValue() ) );
        }

    }

    /**
     * test isprime
     */
    @Test
    @UseDataProvider( "generate" )
    public final void isprime( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CIsPrime().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Primes.isPrime( i.intValue() ) ),
                l_return.stream().map( ITerm::<Boolean>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test log
     */
    @Test
    @UseDataProvider( "generate" )
    public final void log( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CLog().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.log( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test log10
     */
    @Test
    @UseDataProvider( "generate" )
    public final void log10( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CLog10().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.log10( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test max
     */
    @Test
    @UseDataProvider( "generate" )
    public final void max( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMax().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<OptionalDouble>raw(), OptionalDouble.of( 180.0 ) );

    }

    /**
     * test max index
     */
    @Test
    @UseDataProvider( "generate" )
    public final void maxIndex( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMaxIndex().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 6L );

    }

    /**
     * test min
     */
    @Test
    @UseDataProvider( "generate" )
    public final void min( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMin().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<OptionalDouble>raw(), OptionalDouble.of( -6.0 ) );

    }

    /**
     * test min index
     */
    @Test
    @UseDataProvider( "generate" )
    public final void minIndex( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMinIndex().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 1L );
    }


    /**
     * test floor
     */
    @Test
    @UseDataProvider( "generate" )
    public final void floor( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CFloor().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.floor( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test nextprime
     */
    @Test
    public final void nextprime()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CNextPrime().execute(
                null,
                false,
                Stream.of( 12, 144, 1096 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 13L );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 149L );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 1097L );

    }

    /**
     * test nextprime
     */
    @Test
    @Ignore
    @UseDataProvider( "generate" )
    public final void nextprime( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CNextPrime().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Primes.nextPrime( i.intValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }


    /**
     * test power
     */
    @Test
    @UseDataProvider( "generate" )
    public final void power( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CPow().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        for ( int i = 1; i < p_input.size(); i++ )
        {
            Assert.assertEquals( l_return.get( i - 1 ).<Number>raw(), Math.pow( p_input.get( i ).doubleValue(), p_input.get( 0 ).doubleValue() ) );
        }
    }

    /**
     * test primefactors
     */
    @Test
    public final void primefactors()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CPrimeFactors().execute(
                null,
                false,
                Stream.of( 8, 120 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( 2L, 2L, 2L ).toArray() );
        Assert.assertArrayEquals( l_return.get( 1 ).<List<?>>raw().toArray(), Stream.of( 2L, 2L, 2L, 3L, 5L ).toArray() );

    }

    /**
     * test radians
     */
    @Test
    @UseDataProvider( "generate" )
    public final void radians( final List<Number> p_input  )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRadians().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.toRadians( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test round
     */
    @Test
    @UseDataProvider( "generate" )
    public final void round( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRound().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.round( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test siamoid
     */
    @Test
    @UseDataProvider( "generate" )
    public final void siamoid( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSigmoid().execute(
                null,
                false,
                Stream.of( 1.0, 1.0, 1.0, 10.0, 20.0, 30.0  ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 0.7310585786300049 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 0.7310585786300049 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 0.7310585786300049 );
        Assert.assertEquals( l_return.get( 3 ).<Number>raw(), 0.2137302715195763 );
        Assert.assertEquals( l_return.get( 4 ).<Number>raw(), 0.11965173462430909 );
        Assert.assertEquals( l_return.get( 5 ).<Number>raw(), 0.08308143571569296 );

    }

    /**
     * test signum
     */
    @Test
    @UseDataProvider( "generate" )
    public final void signum( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSignum().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.signum( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test sin
     */
    @Test
    @UseDataProvider( "generate" )
    public final void sin( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSin().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.sin( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test sinh
     */
    @Test
    @UseDataProvider( "generate" )
    public final void sinh( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSinh().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.sinh( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test tan
     */
    @Test
    @UseDataProvider( "generate" )
    public final void tan( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CTan().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.tan( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test tanh
     */
    @Test
    @UseDataProvider( "generate" )
    public final void tanh( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CTanh().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.tanh( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }

    /**
     * test sum
     */
    @Test
    @UseDataProvider( "generate" )
    public final void sum( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSum().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 285.8 );

    }

    /**
     * test stirling
     * l_return is empty??
     */
    @Test
    @UseDataProvider( "generate" )
    public final void stirling( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CStirling().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );

    }

    /**
     * test sqrt
     */
    @Test
    @UseDataProvider( "generate" )
    public final void sqrt( final List<Number> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSqrt().execute(
                null,
                false,
                p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        StreamUtils.zip(
                p_input.stream().map( i -> Math.sqrt( i.doubleValue() ) ),
                l_return.stream().map( ITerm::<Number>raw ),
                AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );

    }



    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionMath().invoketest();
    }

}
