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

package org.lightjason.agentspeak.action.buildin.math.bit.matrix;

import cern.colt.bitvector.BitMatrix;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.action.buildin.math.blas.EType;
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
import java.util.stream.IntStream;


/**
 * converts the bit matrix to a blas matrix.
 * The action converts the bit matrix to a blas matrix,
 * the last argument can be "dense" or "sparse", all
 * other arguments are bit matrices and the actions
 * never fails
 *
 * @code [A|B] = math/bit/matrix/toblas( BitMatrix1, BitMatrix2, "dense | sparse" ); @endcode
 */
public final class CToBlas extends IBuildinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5682613760524318512L;

    /**
     * ctor
     */
    public CToBlas()
    {
        super( 4 );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        final EType l_type = l_arguments.parallelStream()
                                        .filter( i -> CCommon.rawvalueAssignableTo( i, String.class ) )
                                        .findFirst().map( i -> EType.from( i.<String>raw() ) )
                                        .orElse( EType.SPARSE );

        switch ( l_type )
        {
            case DENSE:
                l_arguments.stream()
                           .filter( i -> CCommon.rawvalueAssignableTo( i, BitMatrix.class ) )
                           .map( ITerm::<BitMatrix>raw )
                           .map( i -> CToBlas.tomatrix( i, new DenseDoubleMatrix2D( i.rows(), i.columns() ) ) )
                           .map( CRawTerm::from )
                           .forEach( p_return::add );

                return CFuzzyValue.from( true );


            case SPARSE:
                l_arguments.stream()
                           .filter( i -> CCommon.rawvalueAssignableTo( i, BitMatrix.class ) )
                           .map( ITerm::<BitMatrix>raw )
                           .map( i -> CToBlas.tomatrix( i, new SparseDoubleMatrix2D( i.rows(), i.columns() ) ) )
                           .map( CRawTerm::from )
                           .forEach( p_return::add );

                return CFuzzyValue.from( true );

            default:

                return CFuzzyValue.from( false );
        }
    }


    /**
     * converts the bit matrix to the blas matrix
     *
     * @param p_source bit matrix
     * @param p_target blas matrix
     * @return blas matrix
     */
    @Nonnull
    private static DoubleMatrix2D tomatrix( @Nonnull final BitMatrix p_source, @Nonnull final DoubleMatrix2D p_target )
    {
        IntStream.range( 0, p_source.rows() )
                 .forEach( r -> IntStream.range( 0, p_source.columns() ).forEach( c -> p_target.setQuick( r, c, p_source.getQuick( r, c ) ? 1 : 0 ) ) );

        return p_target;
    }
}
