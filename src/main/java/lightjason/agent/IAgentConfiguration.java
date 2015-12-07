/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.agent;

import lightjason.common.CPath;
import lightjason.grammar.IAgentVisitor;

import java.util.Set;


/**
 * interface to define the agent configuration
 */
public interface IAgentConfiguration
{

    /**
     * returns a set with agent actions
     *
     * @return action set
     */
    public Set<IAction> getActions();


    /**
     * returns the parser definition of
     * the AgentSpeak(L) syntax
     *
     * @return parser visitor
     */
    public IAgentVisitor getParser();

    /**
     * returns an unique name of the agent
     *
     * @return path with agent name
     */
    public CPath getName();

}
