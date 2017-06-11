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

package org.lightjason.agentspeak.action.buildin.math.blas;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;


/**
 * elementweise vector / matrix operation.
 * The action calculates elementwise different
 * operations (plus, plus-absolute, minus, multiply, divide),
 * all arguments are triples of matrix-operator-matrix|scalar,
 * the action fails on assigning problems
 * @code [M1|M2|M3] = math/blas/elementwise( Matrix1, "+", 5, Matrix2, "|+|", Matrix3, Matrix4, "-", 3, [Matrix5, "*", 0.5], [Matrix6, "/", 100]); @endcode
 *
 */
public class CElementWise extends IBuildinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2655464156364927632L;

    @Override
    public final int minimalArgumentNumber()
    {
        return 3;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        return CFuzzyValue.from(
                StreamUtils.windowed(
                CCommon.flatcollection( p_argument ),
            3,
                3
            ).allMatch( i -> {

                switch ( i.get( 1 ).<String>raw().trim() )
                {
                    case "+" :
                        return CElementWise.apply( i.get( 0 ), i.get( 2 ), Functions.plus, ( n, m ) -> n + m, p_return );

                    case "|+|" :
                        return CElementWise.apply( i.get( 0 ), i.get( 2 ),  Functions.plusAbs, ( n, m ) -> Math.abs( n + m ), p_return );

                    case "-" :
                        return CElementWise.apply( i.get( 0 ), i.get( 2 ), Functions.minus, ( n, m ) -> n - m, p_return );

                    case "*" :
                        return CElementWise.apply( i.get( 0 ), i.get( 2 ), Functions.mult, ( n, m ) -> n * m, p_return );

                    case "/" :
                        return CElementWise.apply( i.get( 0 ), i.get( 2 ), Functions.div, ( n, m ) -> n / m, p_return );

                    default:
                        return false;
                }

            } )
        );
    }


    /**
     * elementwise assign
     *
     * @param p_left left term argument (matrix argument)
     * @param p_right matrix or scalar value argument
     * @param p_matrixfunction function for matrix-matrix operation
     * @param p_scalarfunction scalar function for value
     * @param p_return return list
     * @return successful executed
     * @note DoubleMatrix1D and DoubleMatrix2D does not use an equal
     * super class for defining the assign method, so code must be
     * created twice for each type
     */
    private static boolean apply( final ITerm p_left, final ITerm p_right,
                                  final DoubleDoubleFunction p_matrixfunction, final BiFunction<Double, Double, Double> p_scalarfunction,
                                  final List<ITerm> p_return )
    {
        // operation for matrix
        if ( CCommon.rawvalueAssignableTo( p_left, DoubleMatrix2D.class ) )
        {
            final DoubleMatrix2D l_assign = p_left.<DoubleMatrix2D>raw().copy();

            if ( CCommon.rawvalueAssignableTo( p_right, DoubleMatrix2D.class ) )
            {
                l_assign.assign( p_right.<DoubleMatrix2D>raw(), p_matrixfunction );
                p_return.add( CRawTerm.from( l_assign ) );
                return true;
            }

            if ( CCommon.rawvalueAssignableTo( p_right, Number.class ) )
            {
                l_assign.assign( ( i ) -> p_scalarfunction.apply( i, p_right.<Number>raw().doubleValue() ) );
                p_return.add( CRawTerm.from( l_assign ) );
                return true;
            }
        }

        // operation for vector
        if ( CCommon.rawvalueAssignableTo( p_left, DoubleMatrix1D.class ) )
        {
            final DoubleMatrix1D l_assign = p_left.<DoubleMatrix1D>raw().copy();

            if ( CCommon.rawvalueAssignableTo( p_right, DoubleMatrix1D.class ) )
            {
                l_assign.assign( p_right.<DoubleMatrix1D>raw(), p_matrixfunction );
                p_return.add( CRawTerm.from( l_assign ) );
                return true;
            }

            if ( CCommon.rawvalueAssignableTo( p_right, Number.class ) )
            {
                l_assign.assign( ( i ) -> p_scalarfunction.apply( i, p_right.<Number>raw().doubleValue() ) );
                p_return.add( CRawTerm.from( l_assign ) );
                return true;
            }
        }

        return false;
    }



}
