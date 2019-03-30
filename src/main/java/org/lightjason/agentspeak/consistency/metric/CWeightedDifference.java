/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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
    public Double apply( final Stream<? extends ITerm> p_first, final Stream<? extends ITerm> p_second )
    {
        final Collection<ITerm> l_first = p_first.collect( Collectors.toCollection( HashSet::new ) );
        final Collection<ITerm> l_second = p_second.collect( Collectors.toCollection( HashSet::new ) );

        // element aggregation
        final double l_union = Stream.concat( l_first.stream(), l_second.stream() ).count();
        final Set<? extends ITerm> l_intersection = new HashSet<>( l_first );
        l_intersection.retainAll( l_second );

        // return distance
        return ( 2.0 * l_union
                 - l_first.size()
                 - l_second.size()
               )
               * l_union
               / l_intersection.size();
    }

}
