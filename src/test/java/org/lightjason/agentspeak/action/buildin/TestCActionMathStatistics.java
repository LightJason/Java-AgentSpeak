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

import org.apache.commons.math3.distribution.NakagamiDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.LaplaceDistribution;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.ParetoDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LevyDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.distribution.GumbelDistribution;
import org.apache.commons.math3.distribution.LogisticDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.math.statistic.CCreateDistribution;
import org.lightjason.agentspeak.action.buildin.math.statistic.CCreateStatistic;
import org.lightjason.agentspeak.action.buildin.math.statistic.CClearStatistic;
import org.lightjason.agentspeak.action.buildin.math.statistic.CAddStatisticValue;
import org.lightjason.agentspeak.action.buildin.math.statistic.CRandomSample;
import org.lightjason.agentspeak.action.buildin.math.statistic.CRandomSimple;
import org.lightjason.agentspeak.action.buildin.math.statistic.CMultipleStatisticValue;
import org.lightjason.agentspeak.action.buildin.math.statistic.CSingleStatisticValue;
import org.lightjason.agentspeak.action.buildin.math.statistic.CExponentialSelection;
import org.lightjason.agentspeak.action.buildin.math.statistic.CLinearSelection;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test for statistics actions
 */
public final class TestCActionMathStatistics extends IBaseTest
{
    /**
     * testing summary statistic
     */
    private SummaryStatistics m_summarystatistic;

    /**
     * testing descriptive statistic
     */
    private DescriptiveStatistics m_descriptivestatistic;

    /**
     * initialize
     */
    @Before
    public final void initialize()
    {
        m_summarystatistic = new SummaryStatistics();
        m_descriptivestatistic = new DescriptiveStatistics();

        m_summarystatistic.addValue( 2 );
        m_summarystatistic.addValue( 5 );
        m_summarystatistic.addValue( 3 );
        m_descriptivestatistic.addValue( 3 );
        m_descriptivestatistic.addValue( 4 );
        m_descriptivestatistic.addValue( 5 );
    }

    /**
     * test create statistics
     */
    @Test
    public final void createstatistics()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreateStatistic().execute(
            false, null,
            Stream.of( "summary", "descriptive" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );
        new CCreateStatistic().execute(
            true, null,
            Stream.of( "summary", "descriptive" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof SummaryStatistics );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof DescriptiveStatistics );
        Assert.assertTrue( l_return.get( 2 ).raw() instanceof SummaryStatistics );
        Assert.assertTrue( l_return.get( 3 ).raw() instanceof DescriptiveStatistics );
    }

    /**
     * test clear
     */
    @Test
    public final void clear()
    {
        new CClearStatistic().execute(
            false, null,
            Stream.of( m_summarystatistic, m_descriptivestatistic ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( m_summarystatistic.getSum(), 0, 0 );
        Assert.assertEquals( m_descriptivestatistic.getSum(), 0, 0 );
    }

    /**
     * test create distribution
     */
    @Test
    public final void createdistribution()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreateDistribution().execute(
            false, null,
            Stream.of(
                        "normal", 20, 10,
                        "beta", 20, 10,
                        "cauchy", 10, 20,
                        "CHISQUARE", 10,
                        "EXPONENTIAL", 5,
                        "F", 2, 6,
                        "GAMMA", 6, 9,
                        "GUMBLE", 2, 7,
                        "LAPLACE", 20, 18,
                        "LEVY", 15, 20,
                        "LOGISTIC", 10, 17,
                        "LOGNORMAL", 12, 14,
                        "NAKAGAMI", 20, 18,
                        "PARETO", 20, 10,
                        "T", 10,
                        "TRIANGULAR", 10, 15, 20,
                        "UNIFORM", 10, 25,
                        "WEIBULL", 10, 23
                ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 18 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof NormalDistribution );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof BetaDistribution );
        Assert.assertTrue( l_return.get( 2 ).raw() instanceof CauchyDistribution );
        Assert.assertTrue( l_return.get( 3 ).raw() instanceof ChiSquaredDistribution );
        Assert.assertTrue( l_return.get( 4 ).raw() instanceof ExponentialDistribution );
        Assert.assertTrue( l_return.get( 5 ).raw() instanceof FDistribution );
        Assert.assertTrue( l_return.get( 6 ).raw() instanceof GammaDistribution );
        Assert.assertTrue( l_return.get( 7 ).raw() instanceof GumbelDistribution );
        Assert.assertTrue( l_return.get( 8 ).raw() instanceof LaplaceDistribution );
        Assert.assertTrue( l_return.get( 9 ).raw() instanceof LevyDistribution );
        Assert.assertTrue( l_return.get( 10 ).raw() instanceof LogisticDistribution );
        Assert.assertTrue( l_return.get( 11 ).raw() instanceof LogNormalDistribution );
        Assert.assertTrue( l_return.get( 12 ).raw() instanceof NakagamiDistribution );
        Assert.assertTrue( l_return.get( 13 ).raw() instanceof ParetoDistribution );
        Assert.assertTrue( l_return.get( 14 ).raw() instanceof TDistribution );
        Assert.assertTrue( l_return.get( 15 ).raw() instanceof TriangularDistribution );
        Assert.assertTrue( l_return.get( 16 ).raw() instanceof UniformRealDistribution );
        Assert.assertTrue( l_return.get( 17 ).raw() instanceof WeibullDistribution );
    }

    /**
     * test add statistics value
     */
    @Test
    public final void addstatisticvalue()
    {
        new CAddStatisticValue().execute(
            false, null,
            Stream.of( m_descriptivestatistic, m_summarystatistic, 1, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( m_descriptivestatistic.getN(), 6, 0 );
        Assert.assertEquals( m_summarystatistic.getN(), 6 );
    }

    /**
     * test multiple statistics value of summary
     */
    @Test
    public final void summarymultiplestatisticvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMultipleStatisticValue().execute(
            false, null,
            Stream.of(
                    m_summarystatistic,
                    "variance", "mean",
                    "max", "geometricmean",
                    "populationvariance", "quadraticmean",
                    "secondmoment", "standarddeviation",
                    "sumlog", "sumsquare"
                ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 10 );
        Assert.assertArrayEquals(
            Stream.of(
                m_summarystatistic.getVariance(), m_summarystatistic.getMean(),
                m_summarystatistic.getMax(), m_summarystatistic.getGeometricMean(),
                m_summarystatistic.getPopulationVariance(), m_summarystatistic.getQuadraticMean(),
                m_summarystatistic.getSecondMoment(), m_summarystatistic.getStandardDeviation(),
                m_summarystatistic.getSumOfLogs(), m_summarystatistic.getSumsq()
            ).toArray(),

            l_return.stream().map( ITerm::raw ).toArray()
        );
    }

    /**
     * test multiple statistics value of descriptive
     */
    @Test
    public final void descriptivemultiplestatisticvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMultipleStatisticValue().execute(
            false, null,
            Stream.of(
                    m_descriptivestatistic,
                    "variance", "mean",
                    "max", "geometricmean",
                    "populationvariance", "quadraticmean",
                    "standarddeviation", "sumsquare",
                    "kurtiosis", "count", "sum"
                ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 11 );
        Assert.assertArrayEquals(
                Stream.of(
                        m_descriptivestatistic.getVariance(), m_descriptivestatistic.getMean(),
                        m_descriptivestatistic.getMax(), m_descriptivestatistic.getGeometricMean(),
                        m_descriptivestatistic.getPopulationVariance(), m_descriptivestatistic.getQuadraticMean(),
                        m_descriptivestatistic.getStandardDeviation(), m_descriptivestatistic.getSumsq(),
                        m_descriptivestatistic.getKurtosis(), (double) m_descriptivestatistic.getN(), m_descriptivestatistic.getSum()
                ).toArray(),

                l_return.stream().map( ITerm::raw ).toArray()
        );
    }

    /**
     * test add random sample
     */
    @Test
    public final void randomsample()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRandomSample().execute(
            false, null,
            Stream.of( new NormalDistribution(), 3 ).map( CRawTerm::from  ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List );
        Assert.assertEquals( l_return.get( 0 ).<List<Number>>raw().size(), 3 );
    }

    /**
     * test random simple
     */
    @Test
    public final void randomsimple()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRandomSimple().execute(
            false, null,
            Stream.of( 5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List );
        Assert.assertEquals( l_return.get( 0 ).<List<Number>>raw().size(), 5 );
    }

    /**
     * test single statistics value
     */
    @Test
    public final void singlestatisticvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSingleStatisticValue().execute(
            false, null,
            Stream.of( "min", m_summarystatistic, m_descriptivestatistic ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertEquals( m_summarystatistic.getMin(), l_return.get( 0 ).<Double>raw(), 0 );
        Assert.assertEquals( m_descriptivestatistic.getMin(), l_return.get( 1 ).<Double>raw(), 0 );
    }

    /**
     * test exponential selection with strict parameter
     */
    @Test
    public final void exponentialselectionstrict()
    {
        final List<ITerm> l_return = Collections.synchronizedList( new ArrayList<>() );

        IntStream.range( 0, 5000 )
                 .parallel()
                 .forEach( i ->
                        new CExponentialSelection().execute(
                            false, null,
                            Stream.of( Stream.of( "a", "b" ).collect( Collectors.toList() ), Stream.of( 4.5, 3.5 ).collect( Collectors.toList() ), 1 )
                                        .map( CRawTerm::from ).collect( Collectors.toList() ),
                            l_return
                        ) );


        Assert.assertEquals(
            (double) Collections.frequency( l_return.stream().map( ITerm::raw ).collect( Collectors.toList() ), "a" ) / l_return.size(),
            0.73,
            0.02
        );

        Assert.assertEquals(
            (double) Collections.frequency( l_return.stream().map( ITerm::raw ).collect( Collectors.toList() ), "b" ) / l_return.size(),
            0.27,
            0.02
        );
    }


    /**
     * test exponential selection with lazy parameter
     */
    @Test
    public final void exponentialselectionlazy()
    {
        final List<ITerm> l_return = Collections.synchronizedList( new ArrayList<>() );

        IntStream.range( 0, 6500 )
                 .parallel()
                 .forEach( i ->
                               new CExponentialSelection().execute(
                                   false, null,
                                   Stream.of( Stream.of( "a", "b" ).collect( Collectors.toList() ), Stream.of( 4.5, 3.5 ).collect( Collectors.toList() ), 0.5 )
                                         .map( CRawTerm::from ).collect( Collectors.toList() ),
                                   l_return
                               ) );


        Assert.assertEquals(
            (double) Collections.frequency( l_return.stream().map( ITerm::raw ).collect( Collectors.toList() ), "a" ) / l_return.size(),
            0.73,
            0.2
        );

        Assert.assertEquals(
            (double) Collections.frequency( l_return.stream().map( ITerm::raw ).collect( Collectors.toList() ), "b" ) / l_return.size(),
            0.27,
            0.2
        );
    }

    /**
     * test linear selection
     */
    @Test
    public final void linearselection()
    {
        final List<ITerm> l_return = Collections.synchronizedList( new ArrayList<>() );

        IntStream.range( 0, 6500 )
                 .parallel()
                 .forEach( i ->
                        new CLinearSelection().execute(
                            false, null,
                            Stream.of( Stream.of( "c", "d" ).collect( Collectors.toList() ), Stream.of( 3, 7 ).collect( Collectors.toList() ) )
                                        .map( CRawTerm::from ).collect( Collectors.toList() ),
                            l_return
        ) );

        Assert.assertEquals(
            (double) Collections.frequency( l_return.stream().map( ITerm::raw ).collect( Collectors.toList() ), "c" ) / l_return.size(),
            0.3,
            0.05
        );

        Assert.assertEquals(
            (double) Collections.frequency( l_return.stream().map( ITerm::raw ).collect( Collectors.toList() ), "d" ) / l_return.size(),
            0.7,
            0.05
        );
    }

    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionMathStatistics().invoketest();
    }

}
