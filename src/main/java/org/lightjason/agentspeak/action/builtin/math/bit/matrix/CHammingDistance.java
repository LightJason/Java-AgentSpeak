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

package org.lightjason.agentspeak.action.builtin.math.bit.matrix;

import cern.colt.matrix.tbit.BitMatrix;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
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
 * calculates the hamming distance.
 * The action calculates between bit matrices,
 * the distance will be calculated between the first
 * and all other arguments, the action never fails
 *
 * {@code [A|B] = math/bit/matrix/hammingdistance( Matrix1, Matrix2, Matrix3 );}
 * @see https://en.wikipedia.org/wiki/Hamming_distance
 */
public final class CHammingDistance extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 8167680292117761543L;

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
        final List<BitMatrix> l_arguments = CCommon.flatten( p_argument ).map( ITerm::<BitMatrix>raw ).collect( Collectors.toList() );
        if ( l_arguments.size() < 2 )
            return CFuzzyValue.of( false );

        l_arguments.stream()
                   .skip( 1 )
                   .map( BitMatrix::copy )
                   .peek( i -> i.xor( l_arguments.get( 0 ) ) )
                   .mapToDouble( BitMatrix::cardinality )
                   .boxed()
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return CFuzzyValue.of( true );
    }
}
