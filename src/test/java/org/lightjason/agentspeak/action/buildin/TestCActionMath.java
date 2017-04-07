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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test math functions
 */
public final class TestCActionMath extends IBaseTest
{

    /**
     * test abs
     */
    @Test
    public final void abs()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAbs().execute(
                null,
                false,
                Stream.of( -2, -6, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 2.0 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 6.0 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 4.0 );
    }

    /**
     * test acos
     */
    @Test
    public final void acos()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CACos().execute(
                null,
                false,
                Stream.of( -1, -5, 3, -3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 3.141592653589793 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), Double.NaN );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), Double.NaN );
        Assert.assertEquals( l_return.get( 3 ).<Number>raw(), Double.NaN );
    }

    /**
     * test asin
     */
    @Test
    public final void asin()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CASin().execute(
                null,
                false,
                Stream.of( -1, -5, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), -1.5707963267948966 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), Double.NaN );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), Double.NaN );
    }

    /**
     * test atan
     */
    @Test
    public final void atan()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CATan().execute(
                null,
                false,
                Stream.of( -1, -5, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), -0.7853981633974483 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), -1.373400766945016 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 1.2490457723982544 );
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
    public final void average()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAverage().execute(
                null,
                false,
                Stream.of( 1, 3, 9, 10, 11, 12 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 7.666666666666667 );
    }

    /**
     * test ceil
     */
    @Test
    public final void ceil()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCeil().execute(
                null,
                false,
                Stream.of( 1.3, 2.8, 9.7 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 2.0 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 3.0 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 10.0 );

    }

    /**
     * test cos
     */
    @Test
    public final void cos()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCos().execute(
                null,
                false,
                Stream.of( 3, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), -0.9899924966004454 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), -0.6536436208636119 );

    }

    /**
     * test cosh
     */
    @Test
    public final void cosh()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCosh().execute(
                null,
                false,
                Stream.of( 3, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 10.067661995777765 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 27.308232836016487 );

    }

    /**
     * test degrees
     */
    @Test
    public final void degrees()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CDegrees().execute(
                null,
                false,
                Stream.of( Math.PI ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 180.0 );

    }

    /**
     * test exponential
     */
    @Test
    public final void exp()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CExp().execute(
                null,
                false,
                Stream.of( 1, 8 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 2.718281828459045 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 2980.9579870417283 );

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
    public final void geometricmean()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CGeometricMean().execute(
                null,
                false,
                Stream.of( 1, 3, 9, 10, 11, 12 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 5.73662264007279 );

    }

    /**
     * test harmonicmean
     */
    @Test
    public final void harmonicmean()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CHarmonicMean().execute(
                null,
                false,
                Stream.of( 1, 3, 9, 10, 11, 12 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 3.4910373200117544 );
    }

    /**
     * test hypot
     * l_return is empty???
     */
    @Test
    public final void hypot()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CHypot().execute(
                null,
                false,
                Stream.of( 1, 2, 3, 4, 5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );

    }

    /**
     * test isprime
     */
    @Test
    public final void isprime()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CIsPrime().execute(
                null,
                false,
                Stream.of( 3, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Boolean>raw(), true );
        Assert.assertEquals( l_return.get( 1 ).<Boolean>raw(), false );

    }


    /**
     * test log
     */
    @Test
    public final void log()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CLog().execute(
                null,
                false,
                Stream.of( 1, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 0.0 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 0.6931471805599453 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 1.0986122886681098 );

    }

    /**
     * test log10
     */
    @Test
    public final void log10()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CLog10().execute(
                null,
                false,
                Stream.of( 1, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 0.0 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 0.3010299956639812 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 0.47712125471966244 );

    }

    /**
     * test max
     */
    @Test
    public final void max()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMax().execute(
                null,
                false,
                Stream.of( 2, 5, 7, 3, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<OptionalDouble>raw(), OptionalDouble.of( 7 ) );

    }

    /**
     * test max index
     */
    @Test
    public final void maxIndex()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMaxIndex().execute(
                null,
                false,
                Stream.of( 5, 6, 7, 8, 1, 2, 3  ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 3L );

    }

    /**
     * test min
     */
    @Test
    public final void min()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMin().execute(
                null,
                false,
                Stream.of( 2, 5, 7, 3, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<OptionalDouble>raw(), OptionalDouble.of( 2 ) );

    }

    /**
     * test min index
     */
    @Test
    public final void minIndex()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMinIndex().execute(
                null,
                false,
                Stream.of( 5, 6, 7, 8, 1, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 4L );
    }

    /**
     * test floor
     */
    @Test
    public final void floor()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CFloor().execute(
                null,
                false,
                Stream.of( 1.3, 2.8, 9.7 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 1.0 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 2.0 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 9.0 );

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
     * test power
     */
    @Test
    public final void power()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CPow().execute(
                null,
                false,
                Stream.of( 2.0, -2.0, 9.0 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 4.0 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 81.0 );

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
    public final void radians()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRadians().execute(
                null,
                false,
                Stream.of( 180 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 3.141592653589793 );

    }

    /**
     * test round
     */
    @Test
    public final void round()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRound().execute(
                null,
                false,
                Stream.of( 5.5, 1.2, 2.7 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 6L );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 1L );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 3L );

    }

    /**
     * test siamoid
     */
    @Test
    public final void siamoid()
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
    public final void signum()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSignum().execute(
                null,
                false,
                Stream.of( -3, 0, 8  ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), -1.0 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 0.0 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 1.0 );

    }

    /**
     * test sin
     */
    @Test
    public final void sin()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSin().execute(
                null,
                false,
                Stream.of( 3, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 0.1411200080598672 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), -0.7568024953079282 );

    }

    /**
     * test sinh
     */
    @Test
    public final void sinh()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSinh().execute(
                null,
                false,
                Stream.of( 3.2, 8, 1.2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 12.245883996565492 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 1490.4788257895502 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 1.5094613554121725 );

    }

    /**
     * test tan
     */
    @Test
    public final void tan()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CTan().execute(
                null,
                false,
                Stream.of( Math.PI, 1.2 * Math.PI ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), -1.2246467991473532E-16 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 0.7265425280053607 );

    }

    /**
     * test tanh
     */
    @Test
    public final void tanh()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CTanh().execute(
                null,
                false,
                Stream.of( 1, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 0.7615941559557649 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 0.9640275800758169 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 0.9950547536867305 );

    }

    /**
     * test sum
     */
    @Test
    public final void sum()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSum().execute(
                null,
                false,
                Stream.of( 3, 4, 1, -5, 3, 4, 10, 12 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 32.0 );

    }

    /**
     * test stirling
     * l_return is empty??
     */
    @Test
    public final void stirling()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CStirling().execute(
                null,
                false,
                Stream.of( 2, 3, 4, 5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );

    }

    /**
     * test sqrt
     */
    @Test
    public final void sqrt()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSqrt().execute(
                null,
                false,
                Stream.of( 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 1.4142135623730951 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 1.7320508075688772 );

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
