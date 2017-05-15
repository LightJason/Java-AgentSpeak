/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.math.statistic;

import org.lightjason.agentspeak.language.ITerm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action to define a exponential fitness-proportinate-selection mechanism.
 * The action defines a fitness-proportinate-selection based on a linear
 * distribution, the first and second argument must be a list, the first
 * list contains elements, the second list contains numeric values for defining
 * the weights
 *
 * @code S = math/statistic/linearselection( ["a","b","c","d"], [0.5, 0.7, 0.9, 3], RationalFactor ); @endcode
 * @see https://en.wikipedia.org/wiki/Fitness_proportionate_selection
 */
public final class CLinearSelection extends ISelection
{

    @Override
    protected final List<Double> weight( final List<?> p_items, final Stream<Double> p_values, final List<ITerm> p_argument )
    {
        return p_values.collect( Collectors.toList() );
    }

}
