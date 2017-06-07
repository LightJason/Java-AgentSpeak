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

package org.lightjason.agentspeak.language.instantiable.plan;

import com.google.common.collect.Multiset;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IAgent;

import java.util.stream.Stream;


/**
 * aggregation interface of score values
 * @deprecated obsolet
 */
@Deprecated
public interface IInjection
{

    /**
     * empty aggreation class
     */
    IInjection EMPTY = new IInjection()
    {
        @Override
        public final double evaluate( final IAgent<?> p_agent, final Multiset<IAction> p_score )
        {
            return 0;
        }

        @Override
        public final double evaluate( final Stream<Double> p_values )
        {
            return 0;
        }

        @Override
        public final double error()
        {
            return 0;
        }
    };


    /**
     * calculates the aggregated score value
     * of an agent and a set of actions
     *
     * @param p_agent agent
     * @param p_score set with actions
     * @return aggregated score of a body item
     */
    double evaluate( final IAgent<?> p_agent, final Multiset<IAction> p_score );

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
