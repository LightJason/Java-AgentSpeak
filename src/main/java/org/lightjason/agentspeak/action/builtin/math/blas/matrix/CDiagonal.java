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
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import org.lightjason.agentspeak.action.builtin.math.blas.EType;
import org.lightjason.agentspeak.action.builtin.math.blas.IAlgebra;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;


/**
 * returns a diagonal matrix.
 * The action returns for each vector element a
 * matrix which contains the vetcor elements on
 * the diagonal line. A string value indicates
 * a sparse or dense matrix (default sparse),
 * the action fails never
 *
 * {@code
      [D1|D2] = math/blas/matrix/diagonal( Vector1, Vector2 );
      [D3|D4] = math/blas/matrix/diagonal( Vector1, Vector2, "dense" );
 * }
 */
public final class CDiagonal extends IAlgebra
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3604474410543673614L;

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final EType l_type = CCommon.flatten( p_argument )
                                    .parallel()
                                    .filter( i -> CCommon.rawvalueAssignableTo( i, String.class ) )
                                    .findFirst()
                                    .map( ITerm::<String>raw )
                                    .map( EType::of )
                                    .orElse( EType.SPARSE );

        CCommon.flatten( p_argument )
               .filter( i -> CCommon.rawvalueAssignableTo( i, DoubleMatrix1D.class ) )
               .map( ITerm::<DoubleMatrix1D>raw )
               .map( i  -> generate( i, l_type ) )
               .map( CRawTerm::of )
               .forEach( p_return::add );


        return CFuzzyValue.of( true );
    }

    /**
     * generates the diagonal matrix
     *
     * @param p_elements vector with diagonal elements
     * @param p_type type of the matrix
     * @return identity
     */
    @Nonnull
    private static DoubleMatrix2D generate( @Nonnull final DoubleMatrix1D p_elements, @Nonnull final EType p_type )
    {
        switch ( p_type )
        {
            case DENSE: return DoubleFactory2D.dense.diagonal( p_elements );
            default: return DoubleFactory2D.sparse.diagonal( p_elements );
        }
    }
}
