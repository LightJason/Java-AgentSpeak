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

package org.lightjason.agentspeak.action.builtin.grid.routing.star;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.builtin.grid.routing.IBaseRouting;
import org.lightjason.agentspeak.action.builtin.grid.routing.IDistance;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * d-star algorithm
 */
public final class CDStar extends IBaseRouting
{

    /**
     * ctor
     *
     * @param p_distance distance function
     */
    public CDStar( @Nonnull final IDistance p_distance )
    {
        super( p_distance );
    }

    /**
     * ctor
     *
     * @param p_distance distance function
     * @param p_walkable walkable function
     */
    public CDStar( @Nonnull final IDistance p_distance, @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable
    )
    {
        super( p_distance, p_walkable );
    }

    @Override
    public Stream<DoubleMatrix1D> apply( final ObjectMatrix2D p_grid, final DoubleMatrix1D p_start, final DoubleMatrix1D p_end )
    {
        return Stream.of();
    }
}
