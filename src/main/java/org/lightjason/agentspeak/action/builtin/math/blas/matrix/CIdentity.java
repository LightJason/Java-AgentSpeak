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

package org.lightjason.agentspeak.action.builtin.math.blas.matrix;

import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import org.lightjason.agentspeak.action.builtin.math.blas.EType;
import org.lightjason.agentspeak.action.builtin.math.blas.IAlgebra;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;


/**
 * returns the identity matrix.
 * The action returns the identity matrix multiple
 * times, the arguments defines the size of each
 * matrix, a string defines the resulting matrix
 * type dense or space (default sparse)
 *
 * {@code
 * [E1|E2] = .math/blas/matrix/identity( 2, 3 );
 * [E3|E4] = .math/blas/matrix/identity( 2, 3, "dense" );
 * }
 */
public final class CIdentity extends IAlgebra
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4774277157938945200L;

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
        final EType l_type = CCommon.flatten( p_argument )
                                    .parallel()
                                    .filter( i -> CCommon.isssignableto( i, String.class ) )
                                    .findFirst()
                                    .map( ITerm::<String>raw )
                                    .map( EType::of )
                                    .orElse( EType.SPARSE );

        CCommon.flatten( p_argument )
               .filter( i -> CCommon.isssignableto( i, Number.class ) )
               .map( ITerm::<Number>raw )
               .map( Number::intValue )
               .map( i -> generate( i, l_type ) )
               .map( CRawTerm::of )
               .forEach( p_return::add );

        return Stream.of();
    }


    /**
     * generates the identitiy matrix
     *
     * @param p_size size of the matrix
     * @param p_type type of the matrix
     * @return identity
     */
    @Nonnull
    private static DoubleMatrix2D generate( final int p_size, @Nonnull final EType p_type )
    {
        switch ( p_type )
        {
            case DENSE:
                return DoubleFactory2D.dense.identity( p_size );
            default:
                return DoubleFactory2D.sparse.identity( p_size );
        }
    }
}
