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
import lightjason.action.IAction;
import lightjason.agent.IAgent;

import java.util.stream.Stream;


/**
 * aggregation interface of score values
 */
public interface IAggregation
{

    /**
     * calculates the aggregated score value
     * of an agent and a set of actions
     *
     * @param p_agent agent
     * @param p_score set with actions
     * @return aggregated score of a body item
     */
    double evaluate( final IAgent p_agent, final Multiset<IAction> p_score );

    /**
     * calculates the full aggregated score value depends
     * of a list of single score values
     *
     * @param p_values list with single score values
     * @return full aggregated score value of a plan
     */
    double evaluate( final Stream<Double> p_values );

    /**
     * returns a cost value on errors e.g. non-existing
     *
     * @return error costs
     */
    double error();

}
