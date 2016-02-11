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

package lightjason.agent.configuration;

import lightjason.beliefbase.IView;
import lightjason.language.ILiteral;
import lightjason.language.execution.IUnifier;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.score.IAggregation;


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
    ILiteral getInitialGoal();

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

}
