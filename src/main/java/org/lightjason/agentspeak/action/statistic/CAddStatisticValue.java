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

package org.lightjason.agentspeak.action.statistic;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action to push a values to the statistic object.
 * The actions adds the given argument number values to each
 * statistic objects within the arguments, the ordering of values and
 * statistic object is free, each value will be added to each
 * statistic object, only number and statistic objects are allowed
 *
 * {@code .math/statistic/addstatisticvalue( StatisticObject1, [1,2,3, StatisticObject2], 1,5,8, StatisticObject3 );}
 */
public final class CAddStatisticValue extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -7796632639293108323L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CAddStatisticValue.class, "math", "statistic" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        final double[] l_values = l_arguments.parallelStream()
                                             .filter( i -> CCommon.isssignableto( i, Number.class ) )
                                             .map( ITerm::<Number>raw )
                                             .mapToDouble( Number::doubleValue ).toArray();

        if ( !l_arguments.parallelStream()
                        .filter( i -> CCommon.isssignableto( i, StatisticalSummary.class ) )
                        .allMatch( i ->
                        {

                            if ( CCommon.isssignableto( i, SummaryStatistics.class ) )
                            {
                                Arrays.stream( l_values ).forEach( j -> i.<SummaryStatistics>raw().addValue( j ) );
                                return true;
                            }

                            if ( CCommon.isssignableto( i, DescriptiveStatistics.class ) )
                            {
                                Arrays.stream( l_values ).forEach( j -> i.<DescriptiveStatistics>raw().addValue( j ) );
                                return true;
                            }

                            return false;
                        } ) )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumenterror" )
            );

        return Stream.of();
    }

}
