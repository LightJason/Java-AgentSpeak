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

package org.lightjason.agentspeak.action.buildin.math.statistic;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Collections;
import java.util.List;
import java.util.Random;
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
 * @code [V1|L1|V2|L2] = math/statistic/randomsimple( 1, 5, 1, 10 ); @endcode
 */
public final class CRandomSimple extends IBuildinAction
{
    /**
     * random instance
     */
    private final Random m_random = new Random();

    /**
     * ctor
     */
    public CRandomSimple()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 0;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        (
            p_argument.size() == 0
            ? Stream.of( 1 )
            : CCommon.flatcollection( p_argument )
               .map( ITerm::<Number>raw )
               .map( Number::intValue )
        ).map( i -> i == 1
                    ? m_random.nextDouble()
                    : p_parallel
                      ? Collections.synchronizedList(
                        IntStream.range( 0, i ).mapToDouble( j -> m_random.nextDouble() ).boxed().collect( Collectors.toList() )
                      )
                      : IntStream.range( 0, i ).mapToDouble( j -> m_random.nextDouble() ).boxed().collect( Collectors.toList() )
               )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

}
