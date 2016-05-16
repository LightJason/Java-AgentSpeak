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

package lightjason.generator;

import lightjason.agent.IAgent;
import lightjason.agent.IPlanBundle;

import java.util.Set;


/**
 * generator interface to create agents
 */
public interface IAgentGenerator extends IGenerator
{

    /**
     * generates an agent
     *
     * @param p_data any object data
     * @return agent
     *
     * @throws Exception on any error
     */
    IAgent generate( final Object... p_data ) throws Exception;


    /**
     * generates a set of agents
     *
     * @param p_number number of agents within the set
     * @param p_data any object data
     * @return set of agents
     *
     * @throws Exception on any error
     */
    Set<IAgent> generate( final int p_number, final Object... p_data ) throws Exception;

    /**
     * returns the planbundle set
     *
     * @return planbundle set
     *
     * @note defined a thread-safe data structure
     */
    Set<IPlanBundle> getPlanBundles();

}
