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
import lightjason.common.CCommon;
import lightjason.error.CIllegalStateException;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics;

import java.util.List;


/**
 * action to create a summary statistic
 */
public final class CCreateStatistic extends IBuildinAction
{

    /**
     * ctor
     */
    public CCreateStatistic()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 0;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        p_return.add( CRawTerm.from(
            ( p_argument.size() == 0
              ? EType.SUMMARY
              : EType.valueOf( lightjason.language.CCommon.<String, ITerm>getRawValue( p_argument.get( 0 ) ).trim().toUpperCase() )
            ).generate( p_parallel )
        ) );

        return CFuzzyValue.from( true );
    }


    /**
     * enume statistic type
     */
    private enum EType
    {
        SUMMARY,
        DESCRIPTIVE;

        /**
         * returns the statistic object
         *
         * @param p_parallel parallel-safe
         * @return statistic object
         */
        public final StatisticalSummary generate( final Boolean p_parallel )
        {
            switch ( this )
            {
                case SUMMARY:
                    return p_parallel
                           ? new SynchronizedSummaryStatistics()
                           : new SummaryStatistics();

                case DESCRIPTIVE:
                    return p_parallel
                           ? new SynchronizedDescriptiveStatistics()
                           : new DescriptiveStatistics();

                default:
                    throw new CIllegalStateException( CCommon.getLanguageString( this, "unknown", this ) );
            }
        }
    }
}
