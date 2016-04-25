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


package lightjason.language.instantiable;

import lightjason.agent.IAgent;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.variable.IVariable;

import java.util.stream.Stream;


/**
 * interface for (instantiable) plans and logical-rules
 */
public interface IInstantiable extends IExecution
{

    /**
     * creates an individual execution context
     *
     * @param p_agent agent
     * @param p_variable variable stream
     * @return individual context
     */
    IContext instantiate( final IAgent p_agent, final Stream<IVariable<?>> p_variable );

}
