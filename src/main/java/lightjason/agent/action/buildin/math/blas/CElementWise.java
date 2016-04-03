/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agent.action.buildin.math.blas;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.AbstractMatrix;
import cern.jet.math.Functions;
import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * elementweise vector / matrix operation
 */
public class CElementWise extends IBuildinAction
{

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 3;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        switch ( CCommon.<String, ITerm>getRawValue( p_argument.get( 1 ) ).trim() )
        {
            case "+":
                this.plus(
                        CCommon.getRawValue( p_argument.get( 0 ) ),
                        CCommon.<Number, ITerm>getRawValue( p_argument.get( 2 ) ).doubleValue()
                );
                return CFuzzyValue.from( true );

            case "-":
                this.minus(
                        CCommon.getRawValue( p_argument.get( 0 ) ),
                        CCommon.<Number, ITerm>getRawValue( p_argument.get( 2 ) ).doubleValue()
                );
                return CFuzzyValue.from( true );

            case "*":
                this.multiply(
                        CCommon.getRawValue( p_argument.get( 0 ) ),
                        CCommon.<Number, ITerm>getRawValue( p_argument.get( 2 ) ).doubleValue()
                );
                return CFuzzyValue.from( true );

            case "/":
                this.divide(
                        CCommon.getRawValue( p_argument.get( 0 ) ),
                        CCommon.<Number, ITerm>getRawValue( p_argument.get( 2 ) ).doubleValue()
                );
                return CFuzzyValue.from( true );


            default:
                return CFuzzyValue.from( false );
        }
    }


    /**
     * plus operator
     *
     * @param p_matrix matrix element
     * @param p_value value element
     */
    private void plus( final AbstractMatrix p_matrix, final double p_value )
    {
        if ( p_matrix instanceof DoubleMatrix1D )
            ( (DoubleMatrix1D) p_matrix ).assign( Functions.plus( p_value ) );
        else
            ( (DoubleMatrix2D) p_matrix ).assign( Functions.plus( p_value ) );
    }


    /**
     * minus operator
     *
     * @param p_matrix matrix element
     * @param p_value value element
     */
    private void minus( final AbstractMatrix p_matrix, final double p_value )
    {
        if ( p_matrix instanceof DoubleMatrix1D )
            ( (DoubleMatrix1D) p_matrix ).assign( Functions.minus( p_value ) );
        else
            ( (DoubleMatrix2D) p_matrix ).assign( Functions.minus( p_value ) );
    }


    /**
     * multiply operator
     *
     * @param p_matrix matrix element
     * @param p_value value element
     */
    private void multiply( final AbstractMatrix p_matrix, final double p_value )
    {
        if ( p_matrix instanceof DoubleMatrix1D )
            ( (DoubleMatrix1D) p_matrix ).assign( Functions.mult( p_value ) );
        else
            ( (DoubleMatrix2D) p_matrix ).assign( Functions.mult( p_value ) );
    }


    /**
     * divide operator
     *
     * @param p_matrix matrix element
     * @param p_value value element
     */
    private void divide( final AbstractMatrix p_matrix, final double p_value )
    {
        if ( p_matrix instanceof DoubleMatrix1D )
            ( (DoubleMatrix1D) p_matrix ).assign( Functions.div( p_value ) );
        else
            ( (DoubleMatrix2D) p_matrix ).assign( Functions.div( p_value ) );
    }

}
