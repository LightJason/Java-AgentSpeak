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

package org.lightjason.agentspeak.action.buildin.math.statistic;

import org.lightjason.agentspeak.language.ITerm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action to define a fitness-proportinate-selection mechanism
 *
 * @see https://en.wikipedia.org/wiki/Boltzmann_distribution
 * @see https://en.wikipedia.org/wiki/Log-linear_model
 */
public final class CExponentialProportionateSelection extends ISelection
{

    @Override
    protected final List<Double> weight( final List<?> p_items, final Stream<Double> p_values, final List<ITerm> p_argument, final List<ITerm> p_annotation )
    {
        final double l_demand = p_argument.get( 0 ).<Number>raw().doubleValue();
        return p_values.map( i -> Math.exp( i / l_demand ) ).collect( Collectors.toList() );
    }

    @Override
    protected final int additionalArgumentNumber()
    {
        return 1;
    }
}
