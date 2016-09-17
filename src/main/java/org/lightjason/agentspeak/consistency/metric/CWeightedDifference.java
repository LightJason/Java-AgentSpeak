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

package org.lightjason.agentspeak.consistency.metric;

import org.lightjason.agentspeak.language.ILiteral;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * calculates the distance with respect
 * to size of union and intersection of beliefbases.
 */
public final class CWeightedDifference implements IMetric
{

    @Override
    public final double calculate( final Collection<ILiteral> p_first, final Collection<ILiteral> p_second )
    {
        // element aggregation
        final double l_union = Stream.concat( p_first.stream(), p_second.stream() ).count();
        final Set<ILiteral> l_intersection = p_first.stream().collect( Collectors.toSet() );
        l_intersection.retainAll( p_second.stream().collect( Collectors.toSet() ) );

        // return distance
        return ( 2.0 * l_union
                 - p_first.stream().count()
                 - p_second.stream().count()
               )
               * l_union
               / l_intersection.size();
    }
}
