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

package org.lightjason.agentspeak.action.buildin.math.statistic;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * action to return a statistic value
 */
public final class CGetStatisticValue extends IBuildinAction
{

    /**
     * ctor
     */
    public CGetStatisticValue()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final StatisticalSummary l_statistic = p_argument.get( 0 ).toAny();
        final EValue l_value = EValue.valueOf( p_argument.get( 1 ).<String>toAny().trim().toUpperCase() );

        if ( l_statistic instanceof SummaryStatistics )
        {
            p_return.add( CRawTerm.from( l_value.get( (SummaryStatistics) l_statistic, p_argument.subList( 2, p_argument.size() ) ) ) );
            return CFuzzyValue.from( true );
        }

        if ( l_statistic instanceof DescriptiveStatistics )
        {
            p_return.add( CRawTerm.from( l_value.get( (DescriptiveStatistics) l_statistic, p_argument.subList( 2, p_argument.size() ) ) ) );
            return CFuzzyValue.from( true );
        }

        return CFuzzyValue.from( false );
    }



    /**
     * enum of statistic value types
     */
    private enum EValue
    {
        GEOMETRICMEAN,
        MAX,
        MIN,
        COUNT,
        POPULATIONVARIANCE,
        QUADRATICMEAN,
        SECONDMOMENT,
        STANDARDDEVIATION,
        SUM,
        SUMLOG,
        SUMSQUARE,
        VARIANCE,
        MEAN,
        KURTIOSIS,
        PERCENTILE;

        /**
         * returns a statistic value
         *
         * @param p_statistic statistic object
         * @param p_argument argument
         * @return value
         */
        public final Number get( final SummaryStatistics p_statistic, final List<ITerm> p_argument )
        {
            switch ( this )
            {
                case GEOMETRICMEAN:
                    return p_statistic.getGeometricMean();

                case MAX:
                    return p_statistic.getMax();

                case MIN:
                    return p_statistic.getMin();

                case COUNT:
                    return p_statistic.getN();

                case POPULATIONVARIANCE:
                    return p_statistic.getPopulationVariance();

                case QUADRATICMEAN:
                    return p_statistic.getQuadraticMean();

                case SECONDMOMENT:
                    return p_statistic.getSecondMoment();

                case STANDARDDEVIATION:
                    return p_statistic.getStandardDeviation();

                case SUM:
                    return p_statistic.getSum();

                case SUMLOG:
                    return p_statistic.getSumOfLogs();

                case SUMSQUARE:
                    return p_statistic.getSumsq();

                case VARIANCE:
                    return p_statistic.getVariance();

                case MEAN:
                    return p_statistic.getMean();

                default:
                    throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "unknown", this ) );
            }
        }

        /**
         * returns a statistic value
         *
         * @param p_statistic statistic object
         * @param p_argument argument
         * @return value
         */
        public final Number get( final DescriptiveStatistics p_statistic, final List<ITerm> p_argument )
        {
            switch ( this )
            {
                case GEOMETRICMEAN:
                    return p_statistic.getGeometricMean();

                case MAX:
                    return p_statistic.getMax();

                case MIN:
                    return p_statistic.getMin();

                case COUNT:
                    return p_statistic.getN();

                case POPULATIONVARIANCE:
                    return p_statistic.getPopulationVariance();

                case QUADRATICMEAN:
                    return p_statistic.getQuadraticMean();

                case STANDARDDEVIATION:
                    return p_statistic.getStandardDeviation();

                case SUM:
                    return p_statistic.getSum();

                case SUMSQUARE:
                    return p_statistic.getSumsq();

                case VARIANCE:
                    return p_statistic.getVariance();

                case MEAN:
                    return p_statistic.getMean();

                case KURTIOSIS:
                    return p_statistic.getKurtosis();

                case PERCENTILE:
                    return p_statistic.getPercentile( p_argument.get( 0 ).<Number>toAny().doubleValue() );

                default:
                    throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "unknown", this ) );
            }
        }
    }

}
