/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agent.action.buildin.math.statistic;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.error.CIllegalStateException;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.List;


/**
 * action to return a statistic value
 */
public final class CGetSummaryStatisticValue extends IBuildinAction
{

    /**
     * ctor
     */
    public CGetSummaryStatisticValue()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 2;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        p_return.add( CRawTerm.from(
                EValueType.valueOf( CCommon.<String, ITerm>getRawValue( p_argument.get( 1 ) ).trim().toUpperCase() )
                          .get( CCommon.<SummaryStatistics, ITerm>getRawValue( p_argument.get( 0 ) ) )
        ) );
        return CBoolean.from( true );
    }



    /**
     * enum of statistic value types
     */
    private enum EValueType
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
        MEAN;

        /**
         * returns a statistic value
         *
         * @param p_statistic statistic object
         * @return value
         */
        public final Number get( final SummaryStatistics p_statistic )
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
                    throw new CIllegalStateException( lightjason.common.CCommon.getLanguageString( this, "unknown" ) );
            }
        }
    }

}
