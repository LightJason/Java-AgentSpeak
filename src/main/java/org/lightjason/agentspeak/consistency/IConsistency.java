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

package org.lightjason.agentspeak.consistency;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.consistency.filter.IFilter;
import org.lightjason.agentspeak.consistency.metric.IMetric;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;


/**
 * consistency interface
 */
public interface IConsistency extends Callable<IConsistency>
{

    /**
     * returns the consistency value of an object
     *
     * @param p_object object
     * @return value or default value
     */
    double value( final IAgent<?> p_object );

    /**
     * returns statistic data of the consistency values
     *
     * @return statistic
     */
    DescriptiveStatistics statistic();

    /**
     * adds a new object
     *
     * @param p_object new object
     * @return self reference
     */
    IConsistency add( final IAgent<?> p_object );

    /**
     * removes an object
     *
     * @param p_object removing object
     * @return self reference
     */
    IConsistency remove( final IAgent<?> p_object );

    /**
     * clear
     * @return self reference
     */
    IConsistency clear();

    /**
     * returns the used metric
     *
     * @return metric
     */
    IMetric metric();

    /**
     * returns the used metric filter
     *
     * @return filter
     */
    IFilter filter();

    /**
     * stream over all data
     *
     * @return entry with agent and consistency value
     */
    Stream<Map.Entry<IAgent<?>, Double>> stream();

}
