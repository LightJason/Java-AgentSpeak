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

package org.lightjason.agentspeak.action.builtin.math.statistic;

import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * create a (set) of simple random values.
 * Returns single or a list of random (uniform distributed)
 * values, all arguments are the number of returning
 * values, if the argument number is 1 a single random
 * value is returned, is the number greater than 1 a list
 * of values is returned
 *
 * {@code [V1|L1|V2|L2] = .math/statistic/randomsimple( 1, 5, 1, 10 );}
 */
public final class CRandomSimple extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5755982505026599387L;

    /**
     * ctor
     */
    public CRandomSimple()
    {
        super( 3 );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        (
            p_argument.size() == 0
            ? Stream.of( 1 )
            : CCommon.flatten( p_argument )
               .map( ITerm::<Number>raw )
               .map( Number::intValue )
        ).map( i -> i == 1
                    ? Math.random()
                    : p_parallel
                      ? Collections.synchronizedList(
                        IntStream.range( 0, i ).mapToDouble( j -> Math.random() ).boxed().collect( Collectors.toList() )
                      )
                      : IntStream.range( 0, i ).mapToDouble( j -> Math.random() ).boxed().collect( Collectors.toList() )
               )
               .map( CRawTerm::of )
               .forEach( p_return::add );

        return Stream.of();
    }

}
