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

package org.lightjason.agentspeak.action.bit.vector;

import cern.colt.matrix.tbit.BitVector;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.SparseDoubleMatrix1D;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.action.bit.EBlasType;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * converts the bit vector to a blas vector.
 * The action converts the bit vector to a blas vector,
 * the last argument can be "dense" or "sparse", all
 * other arguments are bit vectors and the actions
 * never fails
 *
 * {@code [A|B] = .math/bit/vector/toblas( BitVector1, BitVector2, "dense | sparse" );}
 */
public final class CToBlas extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -400958933428905695L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CToBlas.class, "math", "bit", "vector" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        final EBlasType l_type = l_arguments.parallelStream()
                                            .filter( i -> CCommon.isssignableto( i, String.class ) )
                                            .findFirst().map( i -> EBlasType.of( i.<String>raw() ) )
                                            .orElse( EBlasType.SPARSE );

        switch ( l_type )
        {
            case DENSE:
                l_arguments.stream()
                           .filter( i -> CCommon.isssignableto( i, BitVector.class ) )
                           .map( ITerm::<BitVector>raw )
                           .map( i -> IntStream.range( 0, i.size() ).boxed().mapToDouble( j -> i.getQuick( j ) ? 1 : 0 ).toArray() )
                           .map( DenseDoubleMatrix1D::new )
                           .map( CRawTerm::of )
                           .forEach( p_return::add );

                return Stream.of();


            case SPARSE:
                l_arguments.stream()
                           .filter( i -> CCommon.isssignableto( i, BitVector.class ) )
                           .map( ITerm::<BitVector>raw )
                           .map( i -> IntStream.range( 0, i.size() ).boxed().mapToDouble( j -> i.getQuick( j ) ? 1 : 0 ).toArray() )
                           .map( SparseDoubleMatrix1D::new )
                           .map( CRawTerm::of )
                           .forEach( p_return::add );

                return Stream.of();

            default:
                throw new CExecutionIllegealArgumentException(
                    p_context,
                    org.lightjason.agentspeak.common.CCommon.languagestring( this, "unknownargument", l_type )
                );
        }
    }
}
