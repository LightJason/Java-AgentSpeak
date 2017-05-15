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

package org.lightjason.agentspeak.action.buildin.math.bit.vector;

import cern.colt.bitvector.BitVector;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.action.buildin.math.blas.EType;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * converts the bit vector to a blas vector.
 * The action converts the bit vector to a blas vector,
 * the last argument can be "dense" or "sparse", all
 * other arguments are bit vectors and the actions
 * never fails
 *
 * @code [A|B] = math/bit/vector/toblas( BitVector1, BitVector2, "dense | sparse" ); @endcode
 */
public final class CToBlas extends IBuildinAction
{
    /**
     * ctor
     */
    public CToBlas()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        final EType l_type = l_arguments.parallelStream()
                                        .filter( i -> CCommon.rawvalueAssignableTo( i, String.class ) )
                                        .findFirst().map( i -> EType.from( i.<String>raw() ) )
                                        .orElseGet( () -> EType.SPARSE );

        switch ( l_type )
        {
            case DENSE:
                l_arguments.stream()
                           .filter( i -> CCommon.rawvalueAssignableTo( i, BitVector.class ) )
                           .map( ITerm::<BitVector>raw )
                           .map( i -> IntStream.range( 0, i.size() ).boxed().mapToDouble( j -> i.getQuick( j ) ? 1 : 0 ).toArray() )
                           .map( DenseDoubleMatrix1D::new )
                           .map( CRawTerm::from )
                           .forEach( p_return::add );

                return CFuzzyValue.from( true );


            case SPARSE:
                l_arguments.stream()
                           .filter( i -> CCommon.rawvalueAssignableTo( i, BitVector.class ) )
                           .map( ITerm::<BitVector>raw )
                           .map( i -> IntStream.range( 0, i.size() ).boxed().mapToDouble( j -> i.getQuick( j ) ? 1 : 0 ).toArray() )
                           .map( SparseDoubleMatrix1D::new )
                           .map( CRawTerm::from )
                           .forEach( p_return::add );

                return CFuzzyValue.from( true );

            default:

                return CFuzzyValue.from( false );
        }
    }
}
