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
import lightjason.common.IPath;

import java.util.Collection;
import java.util.stream.Stream;


/**
 * metric on collections returns the size of symmetric difference
 */
@SuppressWarnings( "serial" )
public final class CSymmetricDifference extends IBaseMetric
{

    /**
     * ctor
     *
     * @param p_paths for reading agent value
     */
    public CSymmetricDifference( final IPath... p_paths )
    {
        super( p_paths );
    }

    /**
     * ctor
     *
     * @param p_paths collection of path
     */
    public CSymmetricDifference( final Collection<IPath> p_paths )
    {
        super( p_paths );
    }

    @Override
    public final double calculate( final IAgent p_first, final IAgent p_second )
    {
        // build filter
        final IPath[] l_filter = m_paths.isEmpty() ? null : m_paths.toArray( new IPath[m_paths.size()] );

        // element aggregation and return distance
        return 2.0 * Stream.concat( p_first.getBeliefBase().stream( l_filter ), p_second.getBeliefBase().stream( l_filter ) ).sorted().distinct().count()
               - p_first.getBeliefBase().stream( l_filter ).count()
               - p_second.getBeliefBase().stream( l_filter ).sorted().count();
    }

}
