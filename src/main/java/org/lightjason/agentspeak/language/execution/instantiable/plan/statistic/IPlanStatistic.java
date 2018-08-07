/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.instantiable.plan.statistic;

import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.stream.Stream;


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
    @Nonnull
    IPlan plan();

    /**
     * returns the number of executions
     *
     * @return number of executions
     */
    @Nonnegative
    long count();

    /**
     * returns number of successful execution
     *
     * @return number of executions
     */
    @Nonnegative
    long successful();

    /**
     * returns the ratio of successful execution
     *
     * @return ratio
     */
    @Nonnegative
    double successfulratio();

    /**
     * returns number of failed execution
     *
     * @return number of executions
     */
    @Nonnegative
    long fail();

    /**
     * returns the ratio of failed executions
     *
     * @return ratio
     */
    @Nonnegative
    double failratio();

    /**
     * increments the successful executions
     *
     * @return self reference
     */
    @Nonnull
    IPlanStatistic incrementsuccessful();

    /**
     * increments the failed executions
     *
     * @return self reference
     */
    @Nonnull
    IPlanStatistic incrementfail();

    /**
     * returns a stream with variables of
     * the internal data
     *
     * @return variable stream
     */
    @Nonnull
    Stream<IVariable<?>> variables();


}
