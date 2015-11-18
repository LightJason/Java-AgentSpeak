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

package lightjason.parser;

import lightjason.agent.plan.IPlan;
import lightjason.language.ILiteral;

import java.util.Map;
import java.util.Set;


/**
 * visitor interface of the abstract-syntax-tree (AST)
 */
public interface IAgentSpeakVisitor extends lightjason.JasonVisitor<Object>
{

    /**
     * returns the initial beliefs
     *
     * @return set with beliefs
     */
    public Set<ILiteral> getInitialBeliefs();

    /**
     * returns the initial goal
     */
    public ILiteral getInitialGoal();

    /**
     * returns the plans which are triggered by add-goal event
     *
     * @return goal plans
     */
    public Map<String, Set<IPlan>> getGoalAddPlans();

    /**
     * returns the plans which are triggered by delete-goal event
     *
     * @return goal plans
     */
    public Map<String, Set<IPlan>> getGoalDeletePlans();

    /**
     * returns the plans which are triggered by add-belief event
     *
     * @return belief plans
     */
    public Map<String, Set<IPlan>> getBeliefAddPlans();

    /**
     * returns the plans which are triggered by delete-belief event
     *
     * @return belief plans
     */
    public Map<String, Set<IPlan>> getBeliefDeletePlans();

    /**
     * returns the plans which are triggered by change-belief event
     *
     * @return belief plans
     */
    public Map<String, Set<IPlan>> getBeliefChangePlans();

}
