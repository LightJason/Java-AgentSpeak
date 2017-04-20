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

package org.lightjason.agentspeak.action.buildin.math.statistic;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.lightjason.agentspeak.error.CIllegalStateException;

import java.util.Locale;


/**
 * enum of statistic value types
 */
public enum EStatisticValue
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
    KURTIOSIS;

    /**
     * additional factory
     *
     * @param p_value string
     * @return enum
     */
    public static EStatisticValue from( final String p_value )
    {
        return EStatisticValue.valueOf( p_value.trim().toUpperCase( Locale.ROOT ) );
    }

    /**
     * returns a statistic value
     *
     * @param p_statistic statistic object
     * @return statistic value
     */
    public final double value( final SummaryStatistics p_statistic )
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
     * @return statistic value
     */
    public final double value( final DescriptiveStatistics p_statistic )
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

            default:
                throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "unknown", this ) );
        }
    }
}
