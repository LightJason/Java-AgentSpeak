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

package lightjason.agentspeak.consistency;

import lightjason.agentspeak.agent.IAgent;
import lightjason.agentspeak.consistency.filter.IFilter;
import lightjason.agentspeak.consistency.metric.IMetric;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.concurrent.Callable;


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
    double get( final IAgent<?> p_object );

    /**
     * returns statistic data of the consistency values
     *
     * @return statistic
     */
    DescriptiveStatistics getStatistic();

    /**
     * adds a new object
     *
     * @param p_object new object
     */
    boolean add( final IAgent<?> p_object );

    /**
     * removes an object
     *
     * @param p_object removing object
     */
    boolean remove( final IAgent<?> p_object );

    /**
     * returns the used metric
     *
     * @return metric
     */
    IMetric getMetric();

    /**
     * returns the used metric filter
     *
     * @return filter
     */
    IFilter getFilter();

}
