/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

import org.lightjason.agentspeak.action.buildin.math.blas.IAlgebra;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * returns a submatrix
 */
public final class CSubMatrix extends IAlgebra
{
    /**
     * ctor
     */
    public CSubMatrix()
    {
        super( 4 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 5;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first argument must be a term with a matrix object,
        // second begin row index, third end row index
        // forth begin column index, fifth end column index
        p_return.add( CRawTerm.from(
            ALGEBRA.subMatrix(
                CCommon.raw( p_argument.get( 0 ) ),
                CCommon.<Number, ITerm>raw( p_argument.get( 1 ) ).intValue(),
                CCommon.<Number, ITerm>raw( p_argument.get( 2 ) ).intValue(),
                CCommon.<Number, ITerm>raw( p_argument.get( 3 ) ).intValue(),
                CCommon.<Number, ITerm>raw( p_argument.get( 4 ) ).intValue()
            )
        ) );

        return CFuzzyValue.from( true );
    }
}
