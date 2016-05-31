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

package org.lightjason.agentspeak.beliefbase;

import org.lightjason.agentspeak.agent.IAgent;


/**
 * interface to generate beliefbase updates (perceiving)
 *
 * @tparam T agent type
 */
public interface IBeliefPerceive<T extends IAgent<?>>
{
    /**
     * empty beliefbase update
     */
    IBeliefPerceive<IAgent<?>> EMPTY = new IBeliefPerceive<IAgent<?>>()
    {

        @Override
        public final IAgent<?> perceive( final IAgent<?> p_agent )
        {
            return p_agent;
        }
    };

    /**
     * runs the update process of an agent
     *
     * @param p_agent agent
     * @return agent
     */
    T perceive( final T p_agent );

}
