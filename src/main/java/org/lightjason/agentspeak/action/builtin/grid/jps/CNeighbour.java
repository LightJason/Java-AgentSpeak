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
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import cern.jet.math.tdouble.DoubleFunctions;
import com.codepoetics.protonpack.functions.TriFunction;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * neighbour generator
 */
public final class CNeighbour implements INeighbour
{
    /**
     * diagonal obstacle avoiding
     */
    private final ESearchDirection m_diagonal;
    /**
     * walkable function
     */
    private final BiFunction<ObjectMatrix2D, Number[], Boolean> m_walkable;

    /**
     * ctor
     */
    public CNeighbour()
    {
        this(
            ESearchDirection.NEVER,
            ( g, p ) -> p[0].intValue() >= 0 && p[0].intValue() < g.rows()
                        && p[1].intValue() >= 0 && p[1].intValue() < g.columns()
                        && Objects.nonNull( g.getQuick( p[0].intValue(), p[1].intValue() ) ) );
    }

    /**
     * ctor
     *
     * @param p_walkable walkable function
     */
    public CNeighbour( @NonNull final ESearchDirection p_diagonal, @NonNull final BiFunction<ObjectMatrix2D, Number[], Boolean> p_walkable )
    {
        m_diagonal = p_diagonal;
        m_walkable = p_walkable;
    }

    @Override
    public Stream<DoubleMatrix1D> apply( @NonNull final ObjectMatrix2D p_grid, @NonNull final DoubleMatrix1D p_current )
    {
        final HashSet<DoubleMatrix1D> l_neigbours = new HashSet<>();

        // searching in th 4 directions (north, south, west, east)




        final boolean l_north = m_walkable.apply( p_grid, EDirection.NORTH.apply( p_current )  );
        final boolean l_east = m_walkable.apply( p_grid, EDirection.EAST.apply( p_current )  );
        final boolean l_south = m_walkable.apply( p_grid, EDirection.SOUTH.apply( p_current )  );
        final boolean l_west = m_walkable.apply( p_grid, EDirection.WEST.apply( p_current )  );


        if ( l_north )
            l_neigbours.add( p_current.copy().assign( new DenseDoubleMatrix1D( new double[]{ 0, 1 } ), DoubleFunctions.minus ) );

        if ( l_south )
            l_neigbours.add( p_current.copy().assign( new DenseDoubleMatrix1D( new double[]{ 0, 1 } ), DoubleFunctions.plus ) );

        if ( l_west )
            l_neigbours.add( p_current.copy().assign( new DenseDoubleMatrix1D( new double[]{ 1, 0 } ), DoubleFunctions.minus ) );

        if ( l_east )
            l_neigbours.add( p_current.copy().assign( new DenseDoubleMatrix1D( new double[]{ 1, 0 } ), DoubleFunctions.plus ) );




        return l_neigbours.stream();
    }


    private Stream<DoubleMatrix1D> checkdirection( @NonNull final ObjectMatrix2D p_grid, @NonNull final DoubleMatrix1D p_current, @NonNull EDirection... p_direction )
    {
        return Arrays.stream( p_direction )
                     .map( i -> i.apply( p_current ) )
                     .filter( i -> m_walkable.apply( p_grid, i ) )
                     .map( i -> p_current.copy().assign( new double[]{ i[0].doubleValue(), i[1].doubleValue() } ) );
    }

}
