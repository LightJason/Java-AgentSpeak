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

package lightjason.language.score;

import com.google.common.collect.Multiset;
import lightjason.agent.IAgent;
import lightjason.agent.action.IAction;

import java.util.stream.Stream;


/**
 * zero aggreation
 */
public final class CZeroAggregation implements IAggregation
{
    @Override
    public final double evaluate( final IAgent p_agent, final Multiset<IAction> p_score )
    {
        return 0;
    }

    @Override
    public final double evaluate( final Stream<Double> p_values )
    {
        return 0;
    }
}
