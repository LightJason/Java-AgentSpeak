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
 * filtering for all beliefs
 */
public final class CBelief extends IBaseFilter
{
    /**
     * ctor
     *
     * @param p_paths path list
     */
    public CBelief( final IPath... p_paths )
    {
        super( p_paths );
    }

    /**
     * ctor
     *
     * @param p_paths path collection
     */
    public CBelief( final Collection<IPath> p_paths )
    {
        super( p_paths );
    }

    @Override
    public final Stream<ILiteral> filter( final IAgent p_agent )
    {
        return p_agent.getBeliefBase().stream( m_paths.isEmpty() ? null : m_paths.toArray( new IPath[m_paths.size()] ) );
    }
}
