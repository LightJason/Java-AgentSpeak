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

package lightjason.language.execution;

import lightjason.agent.IAgent;
import lightjason.common.CPath;
import lightjason.language.IVariable;

import java.util.Map;


/**
 * execution context with local data
 */
public interface IContext<T>
{

    /**
     * returns the agent of the context
     *
     * @return agent
     */
    IAgent getAgent();

    /**
     * returns the instance object
     *
     * @return instance object plan or rule
     */
    T getInstance();

    /**
     * returns the variables names and their current value
     *
     * @return variable names and their current value
     */
    Map<CPath, IVariable<?>> getInstanceVariables();

}
