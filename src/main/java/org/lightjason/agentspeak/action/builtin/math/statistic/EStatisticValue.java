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

package org.lightjason.agentspeak.action.builtin.math.statistic;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.lightjason.agentspeak.error.CEnumConstantNotPresentException;

import javax.annotation.Nonnull;
import java.util.Locale;


/**
 * enum of statistic value types
 */
public enum EStatisticValue implements IStatisticValue
{
    GEOMETRICMEAN
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getGeometricMean();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getGeometricMean();
        }

    },
    MAX
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getMax();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getMax();
        }

    },
    MIN
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getMin();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getMin();
        }

    },
    COUNT
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getN();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getN();
        }

    },
    POPULATIONVARIANCE
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getPopulationVariance();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getPopulationVariance();
        }

    },
    QUADRATICMEAN
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getQuadraticMean();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getQuadraticMean();
        }

    },
    SECONDMOMENT
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getSecondMoment();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            throw new CEnumConstantNotPresentException( this.getClass(), this.toString() );
        }

    },
    STANDARDDEVIATION
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getStandardDeviation();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getStandardDeviation();
        }

    },
    SUM
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getSum();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getSum();
        }

    },
    SUMLOG
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getSumOfLogs();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            throw new CEnumConstantNotPresentException( this.getClass(), this.toString() );
        }

    },
    SUMSQUARE
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getSumsq();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getSumsq();
        }

    },
    VARIANCE
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getVariance();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getVariance();
        }

    },
    MEAN
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            return p_statistic.getMean();
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getMean();
        }

    },
    KURTIOSIS
    {

        @Override
        public double value( @Nonnull final SummaryStatistics p_statistic )
        {
            throw new CEnumConstantNotPresentException( this.getClass(), this.toString() );
        }

        @Override
        public double value( @Nonnull final DescriptiveStatistics p_statistic )
        {
            return p_statistic.getKurtosis();
        }

    };

    /**
     * additional factory
     *
     * @param p_value string
     * @return enum
     */
    @Nonnull
    public static EStatisticValue of( @Nonnull final String p_value )
    {
        return EStatisticValue.valueOf( p_value.trim().toUpperCase( Locale.ROOT ) );
    }

}
