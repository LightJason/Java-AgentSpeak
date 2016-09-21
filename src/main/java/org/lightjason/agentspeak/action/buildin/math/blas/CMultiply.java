/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.math.blas;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.AbstractMatrix;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * defines matrix- / vector-products
 */
public final class CMultiply extends IAlgebra
{
    /**
     * ctor
     */
    public CMultiply()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first & second argument are matrix or vector elements
        final AbstractMatrix l_first = p_argument.get( 0 ).toAny();
        final AbstractMatrix l_second = p_argument.get( 1 ).toAny();

        if ( ( l_first instanceof DoubleMatrix1D ) && ( l_second instanceof DoubleMatrix1D ) )
            return this.multiplyVectorVector( (DoubleMatrix1D) l_first, (DoubleMatrix1D) l_second, p_return );

        if ( ( l_first instanceof DoubleMatrix2D ) && ( l_second instanceof DoubleMatrix2D ) )
            return this.multiplyVectorVector( (DoubleMatrix2D) l_first, (DoubleMatrix2D) l_second, p_return );

        if ( ( l_first instanceof DoubleMatrix2D ) && ( l_second instanceof DoubleMatrix1D ) )
            return this.multiplyMatrixVector( (DoubleMatrix2D) l_first, (DoubleMatrix1D) l_second, p_return );

        if ( ( l_first instanceof DoubleMatrix1D ) && ( l_second instanceof DoubleMatrix2D ) )
            return this.multiplyMatrixVector( (DoubleMatrix2D) l_second, (DoubleMatrix1D) l_first, p_return );

        return CFuzzyValue.from( false );
    }

    /**
     * outer-vector product
     *
     * @param p_first vector
     * @param p_second vector
     * @param p_return return matrix
     * @return fuzzy boolean
     */
    private IFuzzyValue<Boolean> multiplyVectorVector( final DoubleMatrix1D p_first, final DoubleMatrix1D p_second, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( ALGEBRA.multOuter( p_first, p_second, null ) ) );
        return CFuzzyValue.from( true );
    }

    /**
     * matrix-vector product
     *
     * @param p_first matrix
     * @param p_second vector
     * @param p_return return vector
     * @return fuzzy boolean
     */
    private IFuzzyValue<Boolean> multiplyMatrixVector( final DoubleMatrix2D p_first, final DoubleMatrix1D p_second, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( ALGEBRA.mult( p_first, p_second ) ) );
        return CFuzzyValue.from( true );
    }

    /**
     * matrix-matrix product
     *
     * @param p_first matrix
     * @param p_second matrix
     * @param p_return return matrix
     * @return fuzzy boolean
     */
    private IFuzzyValue<Boolean> multiplyVectorVector( final DoubleMatrix2D p_first, final DoubleMatrix2D p_second, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( ALGEBRA.mult( p_first, p_second ) ) );
        return CFuzzyValue.from( true );
    }

}
