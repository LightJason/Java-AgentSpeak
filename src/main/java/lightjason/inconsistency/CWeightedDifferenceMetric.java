/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.inconsistency;


import lightjason.agent.IAgent;
import lightjason.common.CPath;
import lightjason.language.ILiteral;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Metric implementation for agents. Calculates the distance with respect
 * to size of union and intersection of beliefbases.
 */
@SuppressWarnings( "serial" )
public final class CWeightedDifferenceMetric<T extends IAgent> extends IDefaultMetric<T>
{
    /**
     * ctor
     *
     * @param p_paths path list
     */
    public CWeightedDifferenceMetric( final CPath... p_paths )
    {
        super( p_paths );
    }

    /**
     * ctor
     *
     * @param p_paths collection of paths
     */
    public CWeightedDifferenceMetric( final Collection<CPath> p_paths )
    {
        super( p_paths );
    }

    /**
     * copy-ctor
     *
     * @param p_metric metric
     */
    public CWeightedDifferenceMetric( final IDefaultMetric<T> p_metric )
    {
        super( p_metric );
    }

    @Override
    public final double calculate( final T p_first, final T p_second )
    {
        // equal objects create zero value
        if ( p_first.equals( p_second ) )
            return 0;

        // collect all literals within specified paths
        final Set<ILiteral> l_firstLiterals = new HashSet<>();
        final Set<ILiteral> l_secondLiterals = new HashSet<>();


        // if no path elements are set, we use all
        if ( m_paths.isEmpty() )
        {
            l_firstLiterals.addAll( p_first.getBeliefBase().getLiterals().values() );
            l_secondLiterals.addAll( p_second.getBeliefBase().getLiterals().values() );
        }
        else
            for ( final CPath l_path : m_paths )
            {
                l_firstLiterals.addAll( p_first.getBeliefBase().getLiterals( l_path ).values() );
                l_secondLiterals.addAll( p_second.getBeliefBase().getLiterals( l_path ).values() );
            }

        // get size of union
        final Set<ILiteral> l_set = new HashSet<ILiteral>()
        {{
            addAll( l_firstLiterals );
            addAll( l_secondLiterals );
        }};
        final int l_unionSize = l_set.size();

        // get size of intersection
        l_set.retainAll( l_firstLiterals );
        l_set.retainAll( l_secondLiterals );
        final int l_intersectionSize = l_set.size();

        // return distance
        return new Double(
                ( ( l_unionSize - l_firstLiterals.size() ) +
                  ( l_unionSize - l_secondLiterals.size() ) ) * l_unionSize / l_intersectionSize
        );
    }
}
