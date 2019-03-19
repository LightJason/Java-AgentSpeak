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
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * converts the bit matrix into a bit vector.
 * The action converts each bit matrix argument
 * into a bit vector with the size of the matrix,
 * the bit within the vector are row-wise copied
 * of the matrix
 *
 * {@code [V1|V2] = .math/bit/matrix/tovector( Matrix1, Matrix2 );}
 */
public final class CToVector extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8873596018924393696L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CToVector.class, "math", "bit", "matrix" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        CCommon.flatten( p_argument )
               .map( ITerm::<BitMatrix>raw )
               .map( CToVector::transform )
               .map( CRawTerm::of )
               .forEach( p_return::add );

        return Stream.of();
    }

    /**
     * transforms the matrix into the bit vector
     *
     * @param p_matrix matrix
     * @return vector
     */
    @Nonnull
    private static BitVector transform( @Nonnull final BitMatrix p_matrix )
    {
        final BitVector l_result = new BitVector( p_matrix.size() );

        IntStream.range( 0, p_matrix.rows() )
                 .boxed()
                 .forEach( i -> IntStream.range( 0, p_matrix.columns() )
                                         .boxed()
                                         .forEach( j -> l_result.putQuick( ( i + 1 ) * j, p_matrix.getQuick( j, i ) ) ) );

        return l_result;
    }
}
