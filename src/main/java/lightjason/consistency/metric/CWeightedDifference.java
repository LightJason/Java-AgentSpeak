/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.consistency.metric;


import lightjason.agent.IAgent;
import lightjason.common.CPath;
import lightjason.language.ILiteral;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * calculates the distance with respect
 * to size of union and intersection of beliefbases.
 */
@SuppressWarnings( "serial" )
public final class CWeightedDifference extends IBaseMetric
{

    /**
     * ctor
     *
     * @param p_paths for reading agent value
     */
    public CWeightedDifference( final CPath... p_paths )
    {
        super( p_paths );
    }

    /**
     * ctor
     *
     * @param p_paths collection of path
     */
    public CWeightedDifference( final Collection<CPath> p_paths )
    {
        super( p_paths );
    }

    @Override
    public final double calculate( final IAgent p_first, final IAgent p_second )
    {
        // build filter
        final CPath[] l_filter = m_paths.isEmpty() ? null : m_paths.toArray( new CPath[m_paths.size()] );

        // count elements
        final Stream<ILiteral> l_union = Stream.concat(
                p_first.getBeliefBase().parallelStream( l_filter ),
                p_second.getBeliefBase().parallelStream( l_filter )
        );

        final double l_unionsize = l_union.count();
        final double l_set1 = p_first.getBeliefBase().parallelStream( l_filter ).count();
        final double l_set2 = p_second.getBeliefBase().parallelStream( l_filter ).count();

        final Set<ILiteral> l_inter = p_first.getBeliefBase().parallelStream( l_filter ).collect( Collectors.toSet() );
        l_inter.retainAll( p_second.getBeliefBase().parallelStream( l_filter ).collect( Collectors.toSet() ) );
        final double l_intersectionsize = l_union.distinct().count();


        // return distance
        return new Double( ( l_unionsize - l_set1 + l_unionsize - l_set2 ) * l_unionsize / l_intersectionsize );
    }
}
