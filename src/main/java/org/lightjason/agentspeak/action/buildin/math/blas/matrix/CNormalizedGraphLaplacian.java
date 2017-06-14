/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.math.blas.matrix;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import org.lightjason.agentspeak.action.buildin.math.blas.IAlgebra;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.IntStream;


/**
 * creates the normalized graph laplacian.
 * For each input adjacency matrix, the normalized graph
 * ´laplacian is calculated and returned, the action never fails
 *
 * @code [L1|L2] = math/blas/matrix/normalizedgraphlaplacian( AdjacencyMatrix1, AdjacencyMatrix2 ); @endcode
 * @see https://en.wikipedia.org/wiki/Laplacian_matrix
 */
public final class CNormalizedGraphLaplacian extends IAlgebra
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3481859239102399848L;

    /**
     * ctor
     */
    public CNormalizedGraphLaplacian()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        CCommon.flatten( p_argument )
               .map( ITerm::<DoubleMatrix2D>raw )
               .map( i -> {
                   final DoubleMatrix2D l_degree = DoubleFactory2D
                             .sparse
                             .diagonal( new DenseDoubleMatrix1D( IntStream.range( 0, i.rows() ).mapToDouble( j -> i.viewRow( j ).cardinality() ).toArray() ) );

                   return ALGEBRA.mult( ALGEBRA.inverse( l_degree ), l_degree.assign( i, ( n, m ) -> n - m ) );
               } )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
