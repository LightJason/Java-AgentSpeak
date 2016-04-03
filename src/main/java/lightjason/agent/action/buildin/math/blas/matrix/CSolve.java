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

package lightjason.agent.action.buildin.math.blas.matrix;

import lightjason.agent.action.buildin.math.blas.IAlgebra;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * solver of matrix-equation \f$ A \cdot X = B \f$
 */
public final class CSolve extends IAlgebra
{
    @Override
    public final int getMinimalArgumentNumber()
    {
        return 2;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first argument matrix, second argument result matrix
        p_return.add( CRawTerm.from(
                m_algebra.solve(
                        CCommon.getRawValue( p_return.get( 0 ) ),
                        CCommon.getRawValue( p_return.get( 1 ) )
                )
        ) );
        return CFuzzyValue.from( true );
    }
}
