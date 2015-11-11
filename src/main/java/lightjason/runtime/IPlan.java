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

package lightjason.runtime;

/**
 * interface of plan
 */
public interface IPlan
{

    /**
     * returns the name of the plan
     * which matchs also the goal
     * definition
     *
     * @return name
     */
    public String getName();

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
     * returns a significance value
     * which matchs the plan of other
     * possible plans
     *
     * @return value in [0,1]
     */
    public float getSignificance();

}
