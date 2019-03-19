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
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * sets the indexed bit to false within the bit vector.
 * The action gets bit vectors and index positions and
 * in each bit vector the given bit positions are set
 * to false
 *
 * {@code .math/bit/vector/clear( BitVector1, 0, 1, BitVector2, [3, 5] );}
 */
public final class CClear extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8707781446631490810L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CClear.class, "math", "bit", "vector" );

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
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        final int[] l_index = l_arguments.parallelStream()
                                         .filter( i -> CCommon.isssignableto( i, Number.class ) )
                                         .map( ITerm::<Number>raw )
                                         .mapToInt( Number::intValue )
                                         .toArray();

        l_arguments.parallelStream()
                   .filter( i -> CCommon.isssignableto( i, BitVector.class ) )
                   .map( ITerm::<BitVector>raw )
                   .forEach( i -> Arrays.stream( l_index ).forEach( i::clear ) );

        return Stream.of();
    }
}
