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

import lightjason.agent.IAgent;
import lightjason.common.CPath;
import lightjason.language.ILiteral;

import java.util.Map;


/**
 * execution context with local data
 */
public interface IExecutionContext
{

    /**
     * returns the agent of the context
     *
     * @return agent
     */
    public IAgent getAgent();

    /**
     * returns the plan or rule of the context
     *
     * @return plan
     */
    public IPlan getPlan();

    /**
     * returns the current running plans
     */
    public Map<ILiteral, IPlan> getRunningPlans();

    /**
     * returns the variables names and their current value
     *
     * @return variable names and their current value
     */
    public Map<CPath, Object> getVariables();

}
