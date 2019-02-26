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

package org.lightjason.agentspeak.action.builtin.blas.matrix;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix2D;
import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.builtin.blas.IBaseAlgebra;
import org.lightjason.agentspeak.common.IPath;
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
 * solver of matrix-equation.
 * The action solve the equation \f$ A \cdot X = B \f$
 * for each input tuple, \f$ A \f$ is the first matrix argument
 * within the tuple and \f$ B \f$ the second, which can be a
 * matrix or vector, for each tuple the action returns \f$ X \f$
 *
 * {@code [R1|R2] = .math/blas/matrix( Matrix1, Matrix2, [Matrix3, Vector1] );}
 */
public final class CSolve extends IBaseAlgebra
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2024863045333250337L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CSolve.class, "math", "blas", "matrix" );

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
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        StreamUtils.windowed(
            CCommon.flatten( p_argument ),
            2
        )
                   .map( i -> DENSEALGEBRA.solve( i.get( 0 ).<DoubleMatrix2D>raw(), CSolve.result( i.get( 1 ) ) ) )
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }

    /**
     * creates a matrix of the input term
     *
     * @param p_term term with vector or matrix
     * @return matrix
     */
    @Nonnull
    private static DoubleMatrix2D result( @Nonnull final ITerm p_term )
    {
        if ( CCommon.isssignableto( p_term, DoubleMatrix2D.class ) )
            return p_term.raw();

        final DoubleMatrix2D l_result = new DenseDoubleMatrix2D( Long.valueOf( p_term.<DoubleMatrix1D>raw().size() ).intValue(), 1 );
        l_result.viewColumn( 0 ).assign( p_term.<DoubleMatrix1D>raw() );
        return l_result;
    }
}
