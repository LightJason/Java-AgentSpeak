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
import cern.colt.matrix.tobject.ObjectMatrix2D;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.language.CCommon;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * searching direction
 */
public enum ESearchDirection implements ISearchDirection
{
    NEVER
    {
        @Override
        public Stream<DoubleMatrix1D> apply( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current,
                                             @Nonnull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
        {
            return Stream.of(
                walkable( p_grid, p_current, p_walkable, EDirection.NORTH ),
                walkable( p_grid, p_current, p_walkable, EDirection.EAST ),
                walkable( p_grid, p_current, p_walkable, EDirection.SOUTH ),
                walkable( p_grid, p_current, p_walkable, EDirection.WEST )
            ).filter( Pair::getKey ).map( Pair::getValue );
        }

    },

    ALWAYS
    {
        @Override
        public Stream<DoubleMatrix1D> apply( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current,
                                             @Nonnull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
        {
            return Stream.of(
                walkable( p_grid, p_current, p_walkable, EDirection.NORTH ),
                walkable( p_grid, p_current, p_walkable, EDirection.EAST ),
                walkable( p_grid, p_current, p_walkable, EDirection.SOUTH ),
                walkable( p_grid, p_current, p_walkable, EDirection.WEST ),
                walkable( p_grid, p_current, p_walkable, EDirection.NORTHEAST ),
                walkable( p_grid, p_current, p_walkable, EDirection.NORTHWEST ),
                walkable( p_grid, p_current, p_walkable, EDirection.SOUTHEAST ),
                walkable( p_grid, p_current, p_walkable, EDirection.SOUTHWEST )
            ).filter( Pair::getKey ).map( Pair::getValue );
        }

    },

    NOOBSTACLES
    {
        @Override
        public Stream<DoubleMatrix1D> apply( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current,
                                             @Nonnull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
        {
            final Pair<Boolean, DoubleMatrix1D> l_north = walkable( p_grid, p_current, p_walkable, EDirection.NORTH );
            final Pair<Boolean, DoubleMatrix1D> l_east = walkable( p_grid, p_current, p_walkable, EDirection.EAST );
            final Pair<Boolean, DoubleMatrix1D> l_south = walkable( p_grid, p_current, p_walkable, EDirection.SOUTH );
            final Pair<Boolean, DoubleMatrix1D> l_west = walkable( p_grid, p_current, p_walkable, EDirection.WEST );

            return CCommon.streamconcat(
                Stream.of( l_north, l_east, l_south, l_west ),
                l_north.getKey() && l_east.getKey() ? Stream.of( walkable( p_grid, p_current, p_walkable, EDirection.NORTHEAST ) ) : Stream.of(),
                l_north.getKey() && l_west.getKey() ? Stream.of( walkable( p_grid, p_current, p_walkable, EDirection.NORTHWEST ) ) : Stream.of(),
                l_south.getKey() && l_east.getKey() ? Stream.of( walkable( p_grid, p_current, p_walkable, EDirection.SOUTHEAST ) ) : Stream.of(),
                l_south.getKey() && l_west.getKey() ? Stream.of( walkable( p_grid, p_current, p_walkable, EDirection.SOUTHWEST ) ) : Stream.of()
            ).filter( Pair::getKey ).map( Pair::getValue );
        }

    },

    ONEOBSTACLE
    {
        @Override
        public Stream<DoubleMatrix1D> apply( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current,
                                             @Nonnull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
        {
            final Pair<Boolean, DoubleMatrix1D> l_north = walkable( p_grid, p_current, p_walkable, EDirection.NORTH );
            final Pair<Boolean, DoubleMatrix1D> l_east = walkable( p_grid, p_current, p_walkable, EDirection.EAST );
            final Pair<Boolean, DoubleMatrix1D> l_south = walkable( p_grid, p_current, p_walkable, EDirection.SOUTH );
            final Pair<Boolean, DoubleMatrix1D> l_west = walkable( p_grid, p_current, p_walkable, EDirection.WEST );

            return CCommon.streamconcat(
                Stream.of( l_north, l_east, l_south, l_west ),
                l_north.getKey() || l_east.getKey() ? Stream.of( walkable( p_grid, p_current, p_walkable, EDirection.NORTHEAST ) ) : Stream.of(),
                l_north.getKey() || l_west.getKey() ? Stream.of( walkable( p_grid, p_current, p_walkable, EDirection.NORTHWEST ) ) : Stream.of(),
                l_south.getKey() || l_east.getKey() ? Stream.of( walkable( p_grid, p_current, p_walkable, EDirection.SOUTHEAST ) ) : Stream.of(),
                l_south.getKey() || l_west.getKey() ? Stream.of( walkable( p_grid, p_current, p_walkable, EDirection.SOUTHWEST ) ) : Stream.of()
            ).filter( Pair::getKey ).map( Pair::getValue );
        }

    };

    /**
     * check walkable position e.g. by grid size
     *
     * @param p_grid grid
     * @param p_current current position
     * @param p_walkable walkable
     * @param p_direction walkable direction
     * @return pair of walkable and position vector
     */
    protected final Pair<Boolean, DoubleMatrix1D> walkable( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current,
                                                            @Nonnull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable,
                                                            @Nonnull final IDirection p_direction
    )
    {
        final DoubleMatrix1D l_position = p_direction.apply( p_current );
        return new ImmutablePair<>(
            l_position.getQuick( 0 ) >= 0 && l_position.getQuick( 0 ) < p_grid.rows()
            && l_position.getQuick( 1 ) >= 0 && l_position.getQuick( 1 ) < p_grid.columns()
            && p_walkable.apply( p_grid, l_position ),
            l_position
        );
    }
}
