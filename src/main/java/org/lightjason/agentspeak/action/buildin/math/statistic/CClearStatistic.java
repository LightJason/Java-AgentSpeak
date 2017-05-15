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
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * action to clears the statistic.
 * The actions clears statistic objects, so
 * all arguments must be statistic objects
 *
 * @code math/statistic/clearstatistic( Statistic1, [Statistic2, [Statistic3]] ); @endcode
 */
public final class CClearStatistic extends IBuildinAction
{

    /**
     * ctor
     */
    public CClearStatistic()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        return CFuzzyValue.from(
            CCommon.flatcollection( p_argument )
                   .parallel()
                   .allMatch( i -> {

                       if ( CCommon.rawvalueAssignableTo( i, SummaryStatistics.class ) )
                            return CClearStatistic.apply( i.<SummaryStatistics>raw() );

                       return CCommon.rawvalueAssignableTo( i, DescriptiveStatistics.class ) && CClearStatistic.apply(
                            i.<DescriptiveStatistics>raw() );

                   } )
        );
    }

    /**
     * clear a summary statistic
     *
     * @param p_statistic statistic object
     * @return successful clear
     */
    private static boolean apply( final SummaryStatistics p_statistic )
    {
        p_statistic.clear();
        return true;
    }

    /**
     * clear a descriptive statistic
     *
     * @param p_statistic statistic object
     * @return successful clear
     */
    private static boolean apply( final DescriptiveStatistics p_statistic )
    {
        p_statistic.clear();
        return true;
    }

}
