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

package lightjason.agentspeak.configuration;

import lightjason.agentspeak.agent.fuzzy.IFuzzy;
import lightjason.agentspeak.beliefbase.IBeliefBaseUpdate;
import lightjason.agentspeak.beliefbase.IView;
import lightjason.agentspeak.language.ILiteral;
import lightjason.agentspeak.language.execution.IVariableBuilder;
import lightjason.agentspeak.language.execution.action.unify.IUnifier;
import lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import lightjason.agentspeak.language.score.IAggregation;

import java.util.Collection;


/**
 * interface to define the agent configuration
 */
public interface IAgentConfiguration extends IConfiguration
{

    /**
     * returns a beliefbase of the agent
     *
     * @return root view
     */
    IView getBeliefbase();

    /**
     * returns the initial goal
     *
     * @return initial goal literal
     */
    ITrigger getInitialGoal();

    /**
     * returns the aggregate function
     * of the plan scoring
     *
     * @return aggregate function
     */
    IAggregation getAggregate();

    /**
     * returns the unifier function
     *
     * @return unifier
     */
    IUnifier getUnifier();

    /**
     * returns the variable builder
     *
     * @return builder
     */
    IVariableBuilder getVariableBuilder();

    /**
     * returns the fuzzy operator
     *
     * @return operator object
     */
    IFuzzy<Boolean> getFuzzy();

    /**
     * returns the initial beliefs
     *
     * @return collection of initial beliefs
     */
    Collection<ILiteral> getInitialBeliefs();

    /**
     * returns beliefbase update
     *
     * @return beliefbase update or null
     */
    IBeliefBaseUpdate getBeliefbaseUpdate();

}
