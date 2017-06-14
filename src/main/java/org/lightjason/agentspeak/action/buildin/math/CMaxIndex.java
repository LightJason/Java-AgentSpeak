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

package org.lightjason.agentspeak.action.buildin.math;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.error.CRuntimeException;
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
 * action for index of maximum.
 * The action takes of the given unflatten input the maximum and
 * returns the index within the unflatten argument list, the
 * action never fails
 *
 * @code MaxIndex = math/maxindex( 5, 6, [7,8, [1,2,3]] ); @endcode
 */
public final class CMaxIndex extends IBuildinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3532214759609690631L;

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
        final List<Double> l_list = CCommon.flatten( p_argument )
                                           .map( ITerm::<Number>raw )
                                           .mapToDouble( Number::doubleValue )
                                           .boxed()
                                           .collect( Collectors.toList() );

        p_return.add(
            CRawTerm.from(
                (long) IntStream.range( 0, l_list.size() - 1 )
                                .parallel()
                                .reduce( ( i, j ) -> l_list.get( i ) < l_list.get( j ) ? j : i )
                                .orElseThrow( () -> new CRuntimeException( p_context ) )
            )
        );

        return CFuzzyValue.from( true );
    }
}
