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
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * gets a single statistic value of multiple statistic objects.
 * The action returns for each statistic object the statistic value,
 * the first argument is a string with the statitic value name (geometricmean, max,
 * min, count, populationvariance, quadraticmean, secondmoment,
 * standarddeviation, sum, sumlog, sumsquare, variance, mean, kurtiosis)
 * all other arguments are statistic objects
 *
 * @code [V1|V2|V3] = math/statistic/getstatisticvalue( "mean|min|max|...", Statistic1, [Statistic2, Statistic3] ); @endcode
 */
public final class CSingleStatisticValue extends IBuildinAction
{

    /**
     * ctor
     */
    public CSingleStatisticValue()
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
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        final EStatisticValue l_value = EStatisticValue.from( l_arguments.get( 0 ).<String>raw() );

        l_arguments.stream()
                   .skip( 1 )
                   .mapToDouble( i -> {

                       if ( CCommon.rawvalueAssignableTo( i, SummaryStatistics.class ) )
                           return l_value.value( i.<SummaryStatistics>raw() );

                       return l_value.value( i.<DescriptiveStatistics>raw() );

                   } )
                   .boxed()
                   .map( CRawTerm::from )
                   .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

}
