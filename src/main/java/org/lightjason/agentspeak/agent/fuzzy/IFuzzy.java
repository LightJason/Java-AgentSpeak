/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.agent.fuzzy;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.fuzzy.defuzzification.IDefuzzification;
import org.lightjason.agentspeak.language.execution.fuzzy.operator.IFuzzyOperator;


/**
 * fuzzy operators
 *
 * @tparam S agent type
 */
public interface IFuzzy<T, S extends IAgent<?>>
{

    /**
     * returns the fuzzy-collector object
     * to collect results
     *
     * @return collector object
     */
    IFuzzyOperator<T> getResultOperator();

    /**
     * returns the defuzzifcator of the agent
     *
     * @return defuzzyification
     */
    IDefuzzification<T, S> getDefuzzyfication();

}
