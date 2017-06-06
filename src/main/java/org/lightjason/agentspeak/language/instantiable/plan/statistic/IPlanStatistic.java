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

package org.lightjason.agentspeak.language.instantiable.plan.statistic;

import org.lightjason.agentspeak.language.instantiable.plan.IPlan;


/**
 * plan statistic
 */
public interface IPlanStatistic extends Comparable<IPlanStatistic>
{
    /**
     * plan reference
     *
     * @return plan
     */
    IPlan plan();

    /**
     * returns number of successful execution
     *
     * @return number of executions
     */
    long successful();

    /**
     * returns the ratio of successful execution
     *
     * @return ratio
     */
    double successfulratio();

    /**
     * returns number of failed execution
     *
     * @return number of executions
     */
    long fail();

    /**
     * returns the ratio of failed executions
     *
     * @return ratio
     */
    double failratio();

    /**
     * increments the successful executions
     *
     * @return self reference
     */
    IPlanStatistic incrementsuccessful();

    /**
     * increments the failed executions
     *
     * @return self reference
     */
    IPlanStatistic incrementfail();


}
