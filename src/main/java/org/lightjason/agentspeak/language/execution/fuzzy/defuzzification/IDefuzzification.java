/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.fuzzy.defuzzification;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;


/**
 * defuzzification interface
 *
 * @tparam T fuzzy type
 * @tparam S agent type
 * @see https://en.wikipedia.org/wiki/Defuzzification
 */
public interface IDefuzzification<T, S extends IAgent<?>>
{

    /**
     * runs the defuzzyification algorithm
     *
     * @param p_value fuzzy value
     * @return native value
     */
    T defuzzify( final IFuzzyValue<T> p_value );

    /**
     * update of the internal defuzzification
     * structure on the agent-cycle
     *
     * @param p_agent agent object
     * @return agent reference
     */
    S update( final S p_agent );

}
