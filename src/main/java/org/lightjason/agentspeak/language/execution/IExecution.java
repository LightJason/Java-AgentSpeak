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

package org.lightjason.agentspeak.language.execution;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.List;
import java.util.stream.Stream;


/**
 * internal execution interface
 */
public interface IExecution
{

    /**
     * defines a plan-body operation
     *
     * @param p_context current execution context
     * @param p_parallel parallel execution
     * @param p_argument parameter of the action
     * @param p_return return values
     * @param p_annotation annotation    @return fuzzy boolean
     */
    IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                  final List<ITerm> p_annotation
    );

    /**
     * returns the scoring value of the execution structure
     *
     * @param p_agent agent for which calculates the score value
     * @return score value
     */
    double score( final IAgent<?> p_agent );

    /**
     * returns a stream with all used variables
     *
     * @return variable stream (variables will be cloned on instantiation)
     */
    Stream<IVariable<?>> variables();
}
