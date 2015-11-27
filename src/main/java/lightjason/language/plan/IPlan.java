/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.plan;

import lightjason.agent.IAction;
import lightjason.language.event.IEvent;

import java.util.List;


/**
 * interface of plan
 */
public interface IPlan
{

    /**
     * returns the trigger event
     *
     * @return trigger event
     */
    public IEvent<?> getTrigger();

    /**
     * checks the context of the plan
     * and return if the plan can be
     * executed
     *
     * @return true iif the plan can be executed
     */
    public boolean isExecutable();

    /**
     * runs the plan and returns the result
     *
     * @return execution state
     */
    public EExecutionState execute();

    /**
     * returns the current state of the plan
     *
     * @return current / last execution state
     */
    public EExecutionState getState();

    /**
     * returns the number of executions
     *
     * @return number
     */
    public long getNumberOfRuns();

    /**
     * returns the number of fail runs
     *
     * @return number
     */
    public long getNumberOfFailRuns();

    /**
     * returns the number of successful runs
     *
     * @return number
     */
    public long getNumberOfSuccessfulRuns();

    /**
     * returns the list of actions
     *
     * @return list with actions
     */
    public List<IAction> getActions();

}
