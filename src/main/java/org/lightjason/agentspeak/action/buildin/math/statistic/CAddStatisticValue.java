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
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * action to push a values to the statistic object.
 * The actions adds the given argument number values to each
 * statistic objects within the arguments, the ordering of values and
 * statistic object is free, each value will be added to each
 * statistic object and the action fails on a wrong input, only
 * number and statistic objects are allowed
 *
 * @code math/statistic/addstatisticvalue( StatisticObject1, [1,2,3, StatisticObject2], 1,5,8, StatisticObject3 ); @endcode
 */
public final class CAddStatisticValue extends IBuildinAction
{

    /**
     * ctor
     */
    public CAddStatisticValue()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        final double[] l_values = l_arguments.parallelStream()
                                           .filter( i -> CCommon.rawvalueAssignableTo( i, Number.class ) )
                                           .map( ITerm::<Number>raw )
                                           .mapToDouble( Number::doubleValue ).toArray();

        return CFuzzyValue.from(
            l_arguments.parallelStream()
                   .filter( i -> CCommon.rawvalueAssignableTo( i, StatisticalSummary.class ) )
                   .allMatch( i -> {

                       if ( CCommon.rawvalueAssignableTo( i, SummaryStatistics.class ) )
                       {
                           Arrays.stream( l_values ).forEach( j -> i.<SummaryStatistics>raw().addValue( j ) );
                           return true;
                       }

                       if ( CCommon.rawvalueAssignableTo( i, DescriptiveStatistics.class ) )
                       {
                           Arrays.stream( l_values ).forEach( j -> i.<DescriptiveStatistics>raw().addValue( j ) );
                           return true;
                       }

                       return false;
                   } )
        );

    }

}
