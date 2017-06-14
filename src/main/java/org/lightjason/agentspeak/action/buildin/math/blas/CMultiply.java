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

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;


/**
 * defines matrix- / vector-products.
 * The action multiplies tupel-wise all unflatten arguments,
 * the action fails iif the multiply cannot executed e.g. on wrong
 * input
 *
 * @code [M1|M2|M3] = math/blas/multiply( Vector1, Vector2, [[Matrix1, Matrix2], Matrix3, Vector3] ); @endcode
 */
public final class CMultiply extends IAlgebra
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7399930315943440254L;

    /**
     * ctor
     */
    public CMultiply()
    {
        super( 4 );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        return CFuzzyValue.from(
            StreamUtils.windowed(
                CCommon.flatten( p_argument ),
                2,
                2
            ).parallel().allMatch( i -> {

                if ( ( CCommon.rawvalueAssignableTo( i.get( 0 ), DoubleMatrix1D.class ) )
                     && ( CCommon.rawvalueAssignableTo( i.get( 1 ), DoubleMatrix1D.class ) ) )
                    return CMultiply.<DoubleMatrix1D, DoubleMatrix1D>apply(
                        i.get( 0 ), i.get( 1 ),
                        ( u, v ) -> ALGEBRA.multOuter( u, v, null ),
                        p_return
                    );

                if ( ( CCommon.rawvalueAssignableTo( i.get( 0 ), DoubleMatrix2D.class ) )
                     && ( CCommon.rawvalueAssignableTo( i.get( 1 ), DoubleMatrix2D.class ) ) )
                    return CMultiply.<DoubleMatrix2D, DoubleMatrix2D>apply(
                        i.get( 0 ), i.get( 1 ),
                        ALGEBRA::mult,
                        p_return
                    );

                if ( ( CCommon.rawvalueAssignableTo( i.get( 0 ), DoubleMatrix2D.class ) )
                     && ( CCommon.rawvalueAssignableTo( i.get( 1 ), DoubleMatrix1D.class ) ) )
                    return CMultiply.<DoubleMatrix2D, DoubleMatrix1D>apply(
                        i.get( 0 ), i.get( 1 ),
                        ALGEBRA::mult,
                        p_return
                    );

                return ( ( CCommon.rawvalueAssignableTo( i.get( 0 ), DoubleMatrix1D.class ) )
                         && ( CCommon.rawvalueAssignableTo( i.get( 1 ), DoubleMatrix2D.class ) ) )
                       && CMultiply.<DoubleMatrix1D, DoubleMatrix2D>apply(
                    i.get( 0 ), i.get( 1 ),
                    ( u, v ) -> ALGEBRA.mult( v, u ),
                    p_return
                );

            } )
        );
    }

    /**
     * apply method
     *
     * @param p_left first element
     * @param p_right second element
     * @param p_function function for the two elements
     * @param p_return return list
     * @tparam U first argument type
     * @tparam V second argument type
     * @return successful executed
     */
    private static <U, V> boolean apply( final ITerm p_left, final ITerm p_right, final BiFunction<U, V, ?> p_function, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( p_function.apply( p_left.<U>raw(), p_right.<V>raw() ) ) );
        return true;
    }


}
