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

package org.lightjason.agentspeak.action.builtin.grid.jps;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import com.codepoetics.protonpack.functions.TriFunction;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Objects;
import java.util.stream.Stream;


/**
 * neighbour generator
 */
public final class CNeighbour implements INeighbour
{
    /**
     * diagonal obstacle avoiding
     */
    private final EDiagonal m_diagonal;
    /**
     * walkable function
     */
    private final TriFunction<ObjectMatrix2D, Number, Number, Boolean> m_walkable;

    /**
     * ctor
     */
    public CNeighbour()
    {
        this(
            EDiagonal.NEVER,
            ( g, r, c ) -> r.intValue() >= 0 && r.intValue() < g.rows()
                           && c.intValue() >= 0 && c.intValue() < g.columns()
                           && Objects.nonNull( g.getQuick( r.intValue(), c.intValue() ) ) );
    }

    /**
     * ctor
     *
     * @param p_walkable walkable function
     */
    public CNeighbour( @NonNull final EDiagonal p_diagonal, @NonNull final TriFunction<ObjectMatrix2D, Number, Number, Boolean> p_walkable )
    {
        m_diagonal = p_diagonal;
        m_walkable = p_walkable;
    }

    @Override
    public Stream<DoubleMatrix1D> apply( @NonNull final ObjectMatrix2D p_grid, @NonNull final DoubleMatrix1D p_current )
    {
        return null;
    }

}
