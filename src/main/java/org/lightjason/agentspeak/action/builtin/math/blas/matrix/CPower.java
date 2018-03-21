/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.builtin.math.blas.matrix;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import org.lightjason.agentspeak.action.builtin.math.blas.IAlgebra;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;


/**
 * returns the matrix power.
 * The action creates the matrix power, the
 * first argument is the exponent, all other arguments are
 * input matrix vlaues and the returns the power value for each
 * input matrix
 *
 * {@code [M1|M2|M3] = blas/matrix/power(3, M1, [M2, [M3]]);}
 * @see https://en.wikipedia.org/wiki/Matrix_exponential
 */
public final class CPower extends IAlgebra
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -6606020870832212319L;

    /**
     * ctor
     */
    public CPower()
    {
        super( 4 );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_argument = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        l_argument.stream()
                  .skip( 1 )
                  .map( ITerm::<DoubleMatrix2D>raw )
                  .map( i -> DENSEALGEBRA.pow( i, l_argument.get( 0 ).<Number>raw().intValue() ) )
                  .map( CRawTerm::of )
                  .forEach( p_return::add );

        return CFuzzyValue.of( true );
    }
}
