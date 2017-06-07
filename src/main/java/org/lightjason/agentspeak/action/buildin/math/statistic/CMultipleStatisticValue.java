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
 * gets multiple statistic values of a single statistic object.
 * The action returns different statistic values from a
 * single statistic object, the first argument is the statistic
 * object, all other values are string with statistic value names:
 * geometricmean, max, min, count, populationvariance, quadraticmean, secondmoment,
 * standarddeviation, sum, sumlog, sumsquare, variance, mean, kurtiosis
 *
 * @code [SStd|SVar|SMean]  = math/statistic/multiplestatisticvalue(Statistic, "standarddeviation", "variance", "mean" ); @endcode
 */
public final class CMultipleStatisticValue extends IBuildinAction
{

    /**
     * ctor
     */
    public CMultipleStatisticValue()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        if ( CCommon.rawvalueAssignableTo( l_arguments.get( 0 ), SummaryStatistics.class ) )
            l_arguments.stream()
                       .skip( 1 )
                       .map( ITerm::<String>raw )
                       .map( EStatisticValue::from )
                       .mapToDouble( i -> i.value( l_arguments.get( 0 ).<SummaryStatistics>raw() ) )
                       .boxed()
                       .map( CRawTerm::from )
                       .forEach( p_return::add );
        else
            l_arguments.stream()
                       .skip( 1 )
                       .map( ITerm::<String>raw )
                       .map( EStatisticValue::from )
                       .mapToDouble( i -> i.value( l_arguments.get( 0 ).<DescriptiveStatistics>raw() ) )
                       .boxed()
                       .map( CRawTerm::from )
                       .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
