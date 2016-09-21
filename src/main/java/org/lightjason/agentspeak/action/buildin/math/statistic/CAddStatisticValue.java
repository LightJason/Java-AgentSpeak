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
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * action to push a value to the statistic object
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
        return 2;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final StatisticalSummary l_statistic = p_argument.get( 0 ).toAny();

        if ( l_statistic instanceof SummaryStatistics )
            return this.push( (SummaryStatistics) l_statistic, p_argument.subList( 1, p_argument.size() ) );

        if ( l_statistic instanceof DescriptiveStatistics )
            return this.push( (DescriptiveStatistics) l_statistic, p_argument.subList( 1, p_argument.size() ) );

        return CFuzzyValue.from( false );
    }

    /**
     * push values to the statistics
     *
     * @param p_statistic statistic object
     * @param p_value values
     * @return boolean result
     */
    private IFuzzyValue<Boolean> push( final SummaryStatistics p_statistic, final List<ITerm> p_value )
    {
        CCommon.flatList( p_value ).forEach( i -> p_statistic.addValue( i.<Number>toAny().doubleValue() ) );
        return CFuzzyValue.from( true );
    }

    /**
     * push values to the statistics
     *
     * @param p_statistic statistic object
     * @param p_value values
     * @return boolean result
     */
    private IFuzzyValue<Boolean> push( final DescriptiveStatistics p_statistic, final List<ITerm> p_value )
    {
        CCommon.flatList( p_value ).forEach( i -> p_statistic.addValue( i.<Number>toAny().doubleValue() ) );
        return CFuzzyValue.from( true );
    }
}
