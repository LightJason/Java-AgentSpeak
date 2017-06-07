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
import org.lightjason.agentspeak.action.buildin.math.blas.IAlgebra;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * returns a submatrix.
 * The first four arguments defines the index values of the
 * submatrix ( from/to row, from/to column ) and each matrix
 * object that starts at the fiveth position will
 *
 * @code [M1|M2] = math/blas/matrix/submatrix( FromRow, ToRow, FromColumn, ToColumn, Matrix1, [ Matrix2 ] );  @endcode
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
    public final int minimalArgumentNumber()
    {
        return 5;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        l_arguments.stream()
                   .skip( 4 )
                   .map( i -> ALGEBRA.subMatrix(
                       i.<DoubleMatrix2D>raw(),
                       l_arguments.get( 0 ).<Number>raw().intValue(),
                       l_arguments.get( 1 ).<Number>raw().intValue(),
                       l_arguments.get( 2 ).<Number>raw().intValue(),
                       l_arguments.get( 3 ).<Number>raw().intValue()
                   ) )
                   .map( CRawTerm::from )
                   .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
