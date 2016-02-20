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
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;

import java.util.List;
import java.util.Set;


/**
 * internal execution interface
 */
public interface IExecution
{

    /**
     * defines a plan-body operation
     * @param p_context current execution context
     * @param p_parallel parallel execution
     * @param p_argument parameter of the action
     * @param p_return return values
     * @param p_annotation annotation    @return fuzzy boolean
     */
    IFuzzyValue<Boolean> execute( final IContext<?> p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                  final List<ITerm> p_annotation
    );

    /**
     * returns the scoring value of the execution structure
     *
     * @param p_aggregate aggregation function
     * @param p_agent agent for which calculates the score value
     * @return score value
     */
    double score( final IAggregation p_aggregate, final IAgent p_agent );

    /**
     * returns a map with all used variables
     *
     * @return variable map
     *
     * @warning must create an individual / local set, because
     * variables will be instantiate locally, so variables must be cloned
     */
    Set<IVariable<?>> getVariables();
}
