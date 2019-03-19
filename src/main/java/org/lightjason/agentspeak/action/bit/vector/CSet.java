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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * sets bit position by index and value.
 * The first argument is the bit vector, the second
 * argument is a boolean value or number value (0 = false),
 * all other values are index positions, each index bit
 * within the bit vector will be set to the given value
 *
 * {@code .math/bit/vector/set( BitVector, true, 1, [3, 7]);}
 */
public final class CSet extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 6269327474104817444L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CSet.class, "math", "bit", "vector" );

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
        return 3;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        final boolean l_value = CCommon.isssignableto( l_arguments.get( 1 ), Number.class )
                                ? l_arguments.get( 1 ).<Number>raw().intValue() != 0
                                : l_arguments.get( 1 ).<Boolean>raw();

        l_arguments.stream()
                   .skip( 2 )
                   .map( ITerm::<Number>raw )
                   .mapToInt( Number::intValue )
                   .boxed()
                   .forEach( i -> l_arguments.get( 0 ).<BitVector>raw().put( i, l_value ) );

        return Stream.of();
    }
}
