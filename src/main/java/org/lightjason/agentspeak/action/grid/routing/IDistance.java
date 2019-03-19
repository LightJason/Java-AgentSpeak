/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.grid.routing;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.function.BiFunction;


/**
 * distance and heuristic algorithm interface of routing distances
 */
public interface IDistance extends BiFunction<DoubleMatrix1D, DoubleMatrix1D, Number>
{

    /**
     * heurisitc distance approximation
     *
     * @param p_value1 first value
     * @param p_value2 second value
     * @return distance
     */
    Number heuristic( @NonNull final DoubleMatrix1D p_value1, @NonNull final DoubleMatrix1D p_value2 );

}
