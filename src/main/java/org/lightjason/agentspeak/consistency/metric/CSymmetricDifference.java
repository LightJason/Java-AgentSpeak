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

package org.lightjason.agentspeak.consistency.metric;

import org.lightjason.agentspeak.language.ITerm;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * metric on collections returns the size of symmetric difference
 *
 * @see http://mathworld.wolfram.com/SymmetricDifference.html
 */
public final class CSymmetricDifference implements IMetric
{

    @Override
    public final Double apply( final Stream<? extends ITerm> p_first, final Stream<? extends ITerm> p_second )
    {
        final Collection<ITerm> l_first = p_first.collect( Collectors.toCollection( HashSet<ITerm>::new ) );
        final Collection<ITerm> l_second = p_second.collect( Collectors.toCollection( HashSet<ITerm>::new ) );

        return (double) Stream.concat( l_first.stream(), l_second.stream() )
                              .sorted()
                              .distinct()
                              .parallel()
                              .filter( i -> !( l_first.contains( i ) && ( l_second.contains( i ) ) ) )
                              .count();
    }

}
