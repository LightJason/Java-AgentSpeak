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

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import org.lightjason.agentspeak.action.buildin.math.blas.EType;
import org.lightjason.agentspeak.action.buildin.math.blas.IAlgebra;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.IntStream;


/**
 * returns the column-sum of a matrix.
 * The action returns the column-sum of all matrix objects,
 * a string value defines a sparse or dense resulting vector,
 * the action never fails
 *
 * @code
    [S1|S2] = math/blas/matrix/columnsum( Matrix1, Matrix2 );
    [S1|S2] = math/blas/matrix/columnsum( Matrix1, Matrix2, "sparse" );
 * @endcode
 */
public final class CColumnSum extends IAlgebra
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final EType l_type = CCommon.flatcollection( p_argument )
                                    .parallel()
                                    .filter( i -> CCommon.rawvalueAssignableTo( i, String.class ) )
                                    .findFirst()
                                    .map( ITerm::<String>raw )
                                    .map( EType::from )
                                    .orElseGet( () -> EType.DENSE );

        CCommon.flatcollection( p_argument )
               .filter( i -> CCommon.rawvalueAssignableTo( i, DoubleMatrix2D.class ) )
               .map( ITerm::<DoubleMatrix2D>raw )
               .map( i -> IntStream.range( 0, i.columns() ).boxed().map( i::viewColumn ).mapToDouble( DoubleMatrix1D::zSum ).toArray() )
               .map( i -> generate( i, l_type ) )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

    /**
     * generates a vector
     *
     * @param p_value values
     * @param p_type type
     * @return vector
     */
    private static DoubleMatrix1D generate( final double[] p_value, final EType p_type )
    {
        switch ( p_type )
        {
            case SPARSE: return new SparseDoubleMatrix1D( p_value );
            default : return new DenseDoubleMatrix1D( p_value );
        }
    }

}
