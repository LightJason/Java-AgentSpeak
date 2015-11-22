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

package lightjason.agent.plan;

import lightjason.agent.event.IEvent;
import lightjason.agent.score.IAgentActionScore;


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
     * returns the score of the plan
     *
     * @return cost
     */
    public double getScore();

    /**
     * sets the score sum
     *
     * @param p_score score sum
     */
    public void setScore( final IAgentActionScore p_score );

    /**
     * returns the number of executions
     */
    public long getNumberOfRuns();

    /**
     * returns the number of fail runs
     */
    public long getNumberOfFailRuns();

}
