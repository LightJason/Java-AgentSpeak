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

package lightjason.agentspeak.consistency.filter;

import lightjason.agentspeak.agent.IAgent;
import lightjason.agentspeak.common.IPath;
import lightjason.agentspeak.language.ILiteral;

import java.util.Collection;
import java.util.stream.Stream;


/**
 * filtering for all execution plans & beliefs
 */
public final class CAll extends IBaseFilter
{

    /**
     * ctor
     *
     * @param p_paths list of path
     */
    public CAll( final IPath... p_paths )
    {
        super( p_paths );
    }

    /**
     * ctor
     *
     * @param p_paths path collection
     */
    public CAll( final Collection<IPath> p_paths )
    {
        super( p_paths );
    }

    @Override
    public final Stream<ILiteral> filter( final IAgent p_agent )
    {
        return Stream.concat(
            p_agent.getRunningPlans().values().stream(),
            p_agent.getBeliefBase().stream( m_paths.isEmpty() ? null : m_paths.toArray( new IPath[m_paths.size()] ) )
        );
    }
}
