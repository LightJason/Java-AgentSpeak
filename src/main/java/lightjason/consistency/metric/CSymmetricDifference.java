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
import java.util.HashSet;
import java.util.Set;


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
    public CSymmetricDifference( final CPath... p_paths )
    {
        super( p_paths );
    }

    /**
     * ctor
     *
     * @param p_paths collection of path
     */
    public CSymmetricDifference( final Collection<CPath> p_paths )
    {
        super( p_paths );
    }

    @Override
    public final double calculate( final IAgent p_first, final IAgent p_second )
    {
        final Set<ILiteral> l_firstLiterals = new HashSet<>();
        final Set<ILiteral> l_secondLiterals = new HashSet<>();

        // if no path elements are set, we use all
        if ( m_paths.isEmpty() )
        {
            l_firstLiterals.addAll( p_first.getBeliefBase().getLiteral() );
            l_secondLiterals.addAll( p_second.getBeliefBase().getLiteral() );
        }
        else
        {
            l_firstLiterals.addAll( p_first.getBeliefBase().getLiteral( m_paths.toArray( new CPath[m_paths.size()] ) ) );
            l_secondLiterals.addAll( p_second.getBeliefBase().getLiteral( m_paths.toArray( new CPath[m_paths.size()] ) ) );
        }


        // get union
        final Set<ILiteral> l_aggregate = new HashSet<ILiteral>()
        {{
            addAll( l_firstLiterals );
            addAll( l_secondLiterals );
        }};

        // difference of contradiction is the sum of difference of contradictions on each belief-base (closed-world-assumption)
        return new Double( ( ( l_aggregate.size() - l_firstLiterals.size() ) + ( l_aggregate.size() - l_secondLiterals.size() ) ) );
    }

}
