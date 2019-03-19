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

package org.lightjason.agentspeak.action.builtin.grid;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.algo.DenseDoubleAlgebra;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import cern.jet.math.tdouble.DoubleFunctions;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;


/**
 * helper functions for grid structure
 */
public final class CCommon
{

    /**
     * private ctor
     */
    private CCommon()
    {
    }

    /**
     * creates a rotation matrix
     *
     * @param p_alpha angel in radians
     * @return matrix
     *
     * @see https://en.wikipedia.org/wiki/Rotation_matrix
     */
    public static DoubleMatrix2D rotationmatrix2d( final double p_alpha )
    {
        final double l_sin = Math.sin( p_alpha );
        final double l_cos = Math.cos( p_alpha );
        return new DenseDoubleMatrix2D( new double[][]{{l_cos, l_sin}, {-l_sin, l_cos}} );
    }

    /**
     * returns the angel
     *
     * @param p_first first vector
     * @param p_second second vector
     * @return pair of angel in radians and boolean for calulation correctness
     */
    public static Pair<Double, Boolean> angle( @Nonnull final DoubleMatrix1D p_first, @Nonnull final DoubleMatrix1D p_second )
    {
        final double l_first = DenseDoubleAlgebra.DEFAULT.norm2( p_first );
        final double l_second = DenseDoubleAlgebra.DEFAULT.norm2( p_second );

        return ( l_first == 0 ) || ( l_second == 0 )
               ? new ImmutablePair<>( 0.0, false )
               : new ImmutablePair<>( Math.acos( DenseDoubleAlgebra.DEFAULT.mult( p_first, p_second ) / ( Math.sqrt( l_first ) * Math.sqrt( l_second ) ) ), true );
    }


    /**
     * returns the distance between to points
     *
     * @param p_first vector
     * @param p_second vector
     * @return distance
     */
    public static double distance( @Nonnull final DoubleMatrix1D p_first, @Nonnull final DoubleMatrix1D p_second )
    {
        return DenseDoubleAlgebra.DEFAULT.norm2(
            new DenseDoubleMatrix1D( p_second.toArray() )
                .assign( p_first, DoubleFunctions.minus )
        );
    }

}
