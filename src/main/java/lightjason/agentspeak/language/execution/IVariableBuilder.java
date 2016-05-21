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


package lightjason.agentspeak.language.execution;

import lightjason.agentspeak.agent.IAgent;
import lightjason.agentspeak.language.instantiable.IInstantiable;
import lightjason.agentspeak.language.variable.IVariable;

import java.util.stream.Stream;


/**
 * interface for a variable builder which is
 * called on each plan / rule execution
 */
public interface IVariableBuilder
{

    /**
     * returns a set of variables / constants
     * which are pushed to the plan / rule
     * execution
     *
     * @param p_agent agent which is run
     * @param p_runningcontext plan or rule
     * @return variable stream
     *
     * @warning returning variable can be manipulate direct by the agent and generation must be thread-safe
     */
    Stream<IVariable<?>> generate( final IAgent<?> p_agent, final IInstantiable p_runningcontext );

}
