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

import org.apache.commons.math3.distribution.NormalDistribution;
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
import java.util.stream.Stream;


/**
 * test for statistics actions
 */
public class TestCActionStatistics extends IBaseTest
{
    /**
     * testing statistic
     * @note static because of usage in test-initialize
     */
    private static SummaryStatistics s_statistics = new SummaryStatistics();

    /**
     * testing statistic
     * @note static because of usage in test-initialize
     */
    private static SummaryStatistics s_statistics1 = new SummaryStatistics();


    /**
     * initialize
     */
    @Before
    public void initialize()
    {
        s_statistics.addValue( 2 );
        s_statistics.addValue( 5 );
        s_statistics.addValue( 3 );
    }

    /**
     * test create statistics
     */
    @Test
    public final void createstatistics()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreateStatistic().execute(
                null,
                false,
                Stream.of( "summary" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof SummaryStatistics );
    }

    /**
     * test clear
     */
    @Test
    public final void clear()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CClearStatistic().execute(
                null,
                false,
                Stream.of( s_statistics1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( s_statistics1.getSum(), 0, 0 );
        Assert.assertEquals( l_return.size(), 0 );
    }

    /**
     * test create distribution
     */
    @Test
    public final void createdistribution()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreateDistribution().execute(
                null,
                false,
                Stream.of( "normal", 20, 10 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof NormalDistribution );
    }

    /**
     * test add statistics value
     */
    @Test
    public final void addstatisticvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAddStatisticValue().execute(
                null,
                false,
                Stream.of( s_statistics1, 1, 2, 3 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertEquals( s_statistics1.getSum(), 6, 0 );
    }

    /**
     * test multiple statistics value
     */
    @Test
    public final void multiplestatisticvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMultipleStatisticValue().execute(
                null,
                false,
                Stream.of( s_statistics, "sum", "variance", "mean" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 3 );
        Assert.assertEquals( s_statistics.getSum(), l_return.get( 0 ).raw(), 0 );
        Assert.assertEquals( s_statistics.getVariance(), l_return.get( 1 ).raw(), 0 );
        Assert.assertEquals( s_statistics.getMean(), l_return.get( 2 ).raw(), 0 );
    }

    /**
     * test add random sample
     */
    @Test
    public final void randomsample()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRandomSample().execute(
                null,
                false,
                Stream.of( new NormalDistribution(), 3 ).map( CRawTerm::from  ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List );
        Assert.assertEquals( l_return.get( 0 ).<List>raw().size(), 3 );
    }

    /**
     * test random simple
     */
    @Test
    public final void randomsimple()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRandomSimple().execute(
                null,
                false,
                Stream.of( 5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List );
        Assert.assertEquals( l_return.get( 0 ).<List>raw().size(), 5 );
    }

    /**
     * test single statistics value
     */
    @Test
    public final void singlestatisticvalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSingleStatisticValue().execute(
                null,
                false,
                Stream.of( "sum", s_statistics, s_statistics1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertEquals( s_statistics.getSum(), l_return.get( 0 ).raw(), 0 );
        Assert.assertEquals( s_statistics1.getSum(), l_return.get( 1 ).raw(), 0 );
    }

    /**
     * test exponential selection
     */
    @Test
    public final void exponentialselection()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CExponentialSelection().execute(
                null,
                false,
                Stream.of( Stream.of( "a", "b", "c" ).collect( Collectors.toList() ), Stream.of( 0.5, 0.7, 0.9 ).collect( Collectors.toList() ), 2 )
                        .map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).<String>raw() instanceof String );
    }

    /**
     * test linear silection
     */
    @Test
    public final void linearselection()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CLinearSelection().execute(
                null,
                false,
                Stream.of( Stream.of( "a", "b", "c" ).collect( Collectors.toList() ), Stream.of( 0.5, 0.7, 0.9 ).collect( Collectors.toList() ), 2 )
                        .map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).<String>raw() instanceof String );
    }

    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionStatistics().invoketest();
    }
}
