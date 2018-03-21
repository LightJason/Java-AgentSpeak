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

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.jet.math.tdouble.DoubleMult;
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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * calculates the largest eigenvector with perron-frobenius theorem.
 * The action calculates the largest eigenvector of a sequared matrix
 * based on the perron-frobenius theorem, the calculation is \f$ E_{t+1} = M \cdot E_t \f$,
 * the action uses on the first argument the number of iterations and all other argumentes
 * are squared matrices, the returning arguments are the eigenvector for each matrix, the
 * action never fails
 *
 * {@code [E1|E2|E3] = math/blas/matrix/perronfrobenius(5, M1, M2, M3);}
 * @see https://en.wikipedia.org/wiki/Perron%E2%80%93Frobenius_theorem
 */
public final class CPerronFrobenius extends IAlgebra
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4686274043894517802L;

    /**
     * ctor
     */
    public CPerronFrobenius()
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
        final Random l_random = ThreadLocalRandom.current();
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        // create eigenvectors
        final List<DoubleMatrix1D> l_eigenvector = IntStream
            .range( 0, l_arguments.size() - 1 )
            .parallel()
            .boxed()
            .map( i -> new double[l_arguments.get( i ).<DoubleMatrix2D>raw().rows()] )
            .map( DenseDoubleMatrix1D::new )
            .peek( i -> IntStream.range( 0, Long.valueOf( i.size() ).intValue() ).forEach( j -> i.setQuick( j, l_random.nextDouble() ) ) )
            .collect( Collectors.toList() );

        // run iteration
        IntStream.range( 0, l_arguments.get( 0 ).<Number>raw().intValue() )
                 .forEach( i -> IntStream
                     .range( 0, l_arguments.size() )
                     .boxed()
                     .parallel()
                     .forEach( j ->
                     {
                         l_eigenvector.get( j ).assign( DENSEALGEBRA.mult( l_arguments.get( j + 1 ).<DoubleMatrix2D>raw(), l_eigenvector.get( j ) ) );
                         l_eigenvector.get( j ).assign( DoubleMult.div( DENSEALGEBRA.norm2( l_eigenvector.get( j ) ) ) );
                     } ) );

        l_eigenvector.stream()
                     .map( CRawTerm::of )
                     .forEach( p_return::add );

        return CFuzzyValue.of( true );
    }
}
