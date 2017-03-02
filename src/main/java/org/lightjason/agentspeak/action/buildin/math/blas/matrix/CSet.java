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

package org.lightjason.agentspeak.action.buildin.math.blas.matrix;

import cern.colt.matrix.DoubleMatrix2D;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * set a single element of a matrix.
 * The action sets a value into the matrix, the
 * first argument is the row index, second argument
 * the column index, the third value, the new value,
 * all other arguments are matrix objects
 *
 * @code math/blas/matrix/set(2,2, 0.33, Matrix1, [Matrix2, Matrix3] ); @endcode
 */
public final class CSet extends IBuildinAction
{
    /**
     * ctor
     */
    public CSet()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 4;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        l_arguments.stream()
                   .skip( 3 )
                   .parallel()
                   .map( ITerm::<DoubleMatrix2D>raw )
                   .forEach( i -> i.setQuick(
                       l_arguments.get( 0 ).<Number>raw().intValue(),
                       l_arguments.get( 1 ).<Number>raw().intValue(),
                       l_arguments.get( 2 ).<Number>raw().doubleValue()
                   ) );

        return CFuzzyValue.from( true );
    }
}
