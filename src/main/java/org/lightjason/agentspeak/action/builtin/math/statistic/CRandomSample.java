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

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * create a (set) of random values.
 * The action creates a set or single random value
 * based on a distirbution, the first argument
 * is the distirbution and all other arguments defines
 * the size of the samples
 *
 * {@code [R1|R2] = math/statistic/randomsample( Distribution, 1, 5 );}
 */
public final class CRandomSample extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1823216973305374276L;

    /**
     * ctor
     */
    public CRandomSample()
    {
        super( 3 );
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
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        (
            l_arguments.size() < 2
            ? Stream.of( 1 )
            : l_arguments.stream()
                       .skip( 1 )
                       .map( ITerm::<Number>raw )
        )
            .mapToInt( Number::intValue )
            .mapToObj( i -> CRandomSample.samples( l_arguments.get( 0 ).<AbstractRealDistribution>raw(), i, p_parallel ) )
            .forEach( p_return::add );

        return Stream.of();
    }

    /**
     * creates the sample structure
     *
     * @param p_distribution distribution object
     * @param p_size size of the returned values
     * @param p_parallel parallel flag
     * @return term with data
     */
    @Nonnull
    private static ITerm samples( @Nonnull final AbstractRealDistribution p_distribution, final int p_size, final boolean p_parallel )
    {
        if ( p_size < 2 )
            return CRawTerm.of( p_distribution.sample() );

        final List<Double> l_list = Arrays.stream( p_distribution.sample( p_size ) ).boxed().collect( Collectors.toList() );
        return CRawTerm.of( p_parallel ? Collections.synchronizedList( l_list ) : l_list );

    }

}
