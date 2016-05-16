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

package lightjason.action.buildin.math.statistic;

import lightjason.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

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
    public final int getMinimalArgumentNumber()
    {
        return 2;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final StatisticalSummary l_statistic = CCommon.<StatisticalSummary, ITerm>getRawValue( p_argument.get( 0 ) );

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
        CCommon.flatList( p_value ).stream().forEach( i -> p_statistic.addValue( CCommon.<Number, ITerm>getRawValue( i ).doubleValue() ) );
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
        CCommon.flatList( p_value ).stream().forEach( i -> p_statistic.addValue( CCommon.<Number, ITerm>getRawValue( i ).doubleValue() ) );
        return CFuzzyValue.from( true );
    }
}
