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

package org.lightjason.agentspeak.configuration;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.fuzzy.IFuzzy;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.action.unify.IUnifier;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.instantiable.plan.IInjection;

import java.util.Collection;


/**
 * interface to define the agent configuration
 *
 * @tparam T agent type
 */
public interface IAgentConfiguration<T extends IAgent<?>> extends IConfiguration
{

    /**
     * returns a beliefbase of the agent
     *
     * @return root view
     */
    IView<T> beliefbase();

    /**
     * returns the initial goal
     *
     * @return initial goal literal
     */
    ITrigger initialgoal();

    /**
     * returns the aggregate function
     * of the plan scoring
     *
     * @return aggregate function
     */
    IInjection aggregation();

    /**
     * returns the unifier function
     *
     * @return unifier
     */
    IUnifier unifier();

    /**
     * returns the variable builder
     *
     * @return builder
     */
    IVariableBuilder variablebuilder();

    /**
     * returns the fuzzy operator
     *
     * @return operator object
     */
    IFuzzy<Boolean, T> fuzzy();

    /**
     * returns the initial beliefs
     *
     * @return collection of initial beliefs
     */
    Collection<ILiteral> initialbeliefs();

}
