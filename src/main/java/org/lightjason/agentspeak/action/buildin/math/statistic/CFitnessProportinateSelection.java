/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * action to define a fitness-proportinate-selection mechanism
 *
 * @see https://en.wikipedia.org/wiki/Fitness_proportionate_selection
 */
public final class CFitnessProportinateSelection extends IBuildinAction
{
    /**
     * random instance
     */
    private final Random m_random = new Random();

    /**
     * ctor
     */
    public CFitnessProportinateSelection()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                         final List<ITerm> p_annotation
    )
    {
        // first parameter is a list with elements, which will return by the selection
        // second parameter is a numeric value for each element
        final List<?> l_items = p_argument.get( 0 ).raw();
        final List<Double> l_weight = p_argument.get( 1 ).<List<Number>>raw().stream().mapToDouble( Number::doubleValue ).collect( Collectors.toList() );

        if ( ( l_items.isEmpty() ) || ( l_items.size() != l_weight.size() ) )
            return CFuzzyValue.from( false );

        // select a random value and scale with the sum
        double l_random = m_random.nextDouble() * l_weight.stream().mapToDouble( i -> i ).sum();
        for( int i = 0; i < l_weight.size(); i++ )
        {
            l_random -= l_weight.get( i );
            if ( l_random <= 0 )
            {
                p_return.add( CRawTerm.from( l_items.get( i ) ) );
                return CFuzzyValue.from( true );
            }
        }

        // on rounding error return last element
        p_return.add( CRawTerm.from( l_items.get( l_items.size() - 1 ) ) );
        return CFuzzyValue.from( true );
    }
}
