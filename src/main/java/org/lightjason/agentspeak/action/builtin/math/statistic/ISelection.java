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

package org.lightjason.agentspeak.action.builtin.math.statistic;

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
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * abstract class for creating a selection one element
 * of a list based on a fitness weight
 */
public abstract class ISelection extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -365949510289020495L;
    /**
     * random instance
     */
    private final Random m_random = new Random();

    /**
     * ctor
     */
    protected ISelection()
    {
        super( 3 );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2 + this.additionalArgumentNumber();
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        // first parameter is a list with elements, which will return by the selection
        // second parameter is a numeric value for each element
        final List<?> l_items = p_argument.get( 0 ).<List<?>>raw().stream()
                                                                  .map( i -> i instanceof ITerm ? ( CCommon.replaceFromContext( p_context, (ITerm) i ) ).raw() : i  )
                                                                  .collect( Collectors.toList() );
        final List<Double> l_weight = this.weight(
            l_items,
            p_argument.get( 1 ).<List<?>>raw().stream()
                                              // list can be contains default Java objects or term objects
                                              .map( i -> i instanceof ITerm ? ( CCommon.replaceFromContext( p_context, (ITerm) i ) ).<Number>raw() : (Number) i )
                                              .map( Number::doubleValue )
                                              .map( Math::abs ),
            p_argument.subList( 2, p_argument.size() )
        );

        if ( ( l_items.isEmpty() ) || ( l_items.size() != l_weight.size() ) )
            return CFuzzyValue.from( false );

        // select a random value and scale with the sum
        double l_random = m_random.nextDouble() * l_weight.stream().mapToDouble( i -> i ).sum();
        for ( int i = 0; i < l_weight.size(); i++ )
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

    /**
     * modifies the weights
     *
     * @param p_items item list
     * @param p_values stream of weights
     * @param p_argument additional arguments
     * @return list with weights
     */
    @Nonnull
    protected abstract List<Double> weight( @Nonnull final List<?> p_items, @Nonnull final Stream<Double> p_values, @Nonnull final List<ITerm> p_argument );

    /**
     * number of additional parameter
     *
     * @return additional number
     */
    protected int additionalArgumentNumber()
    {
        return 0;
    }
}
