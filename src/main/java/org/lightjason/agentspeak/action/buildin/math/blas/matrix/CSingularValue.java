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

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.linalg.SingularValueDecomposition;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
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
 * creates the singular value decomposition of a matrix.
 * For each input matrix the singular value decompisition is
 * called and the values, and the two matrixes (left / right)
 * are returned, the action never fails
 *
 * @code [Values1|U1|V1|Values2|U2|V2] = blas/matrix/singularvalue(Matrix1, Matrix2); @endcode
 * @see https://en.wikipedia.org/wiki/Singular_value_decomposition
 */
public final class CSingularValue extends IBuildinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3316611747769800455L;

    /**
     * ctor
     */
    public CSingularValue()
    {
        super( 4 );
    }

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
        CCommon.flatten( p_argument )
               .map( ITerm::<DoubleMatrix2D>raw )
               .map( SingularValueDecomposition::new )
               .forEach( i -> {
                   p_return.add( CRawTerm.from( new DenseDoubleMatrix1D( i.getSingularValues() ) ) );
                   p_return.add( CRawTerm.from( i.getU() ) );
                   p_return.add( CRawTerm.from( i.getV() ) );
               } );

        return CFuzzyValue.from( true );
    }
}
