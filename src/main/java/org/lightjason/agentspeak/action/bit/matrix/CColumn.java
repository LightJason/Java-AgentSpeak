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

package org.lightjason.agentspeak.action.bit.matrix;

import cern.colt.matrix.tbit.BitMatrix;
import cern.colt.matrix.tbit.BitVector;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;


/**
 * returns a single column of a bit matrix.
 * The action returns a column of a bit matrix as vector,
 * the first argument is the index of the column, all
 * other a matrix objects, the action never fails
 *
 * {@code [V1|V2] = .math/bit/matrix/column(3, Matrix1, [Matrix2]);}
 */
public final class CColumn extends IBaseRowColumn
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -6865043145578397469L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CColumn.class, "math", "bit", "matrix" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnull
    @Override
    protected BitVector extract( @Nonnull final BitMatrix p_matrix, final int p_index )
    {
        final BitVector l_result = new BitVector( p_matrix.rows() );
        IntStream.range( 0, p_matrix.rows() )
                 .boxed()
                 .forEach( i -> l_result.putQuick( i, p_matrix.getQuick( p_index, i ) ) );

        return l_result;
    }

}
