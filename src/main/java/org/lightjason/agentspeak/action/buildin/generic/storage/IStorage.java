/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.action.buildin.generic.storage;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


/**
 * storage default definitions
 */
public abstract class IStorage extends IBuildinAction
{

    /**
     * set with forbidden keys
     */
    protected final Set<String> m_forbidden;

    /**
     * ctor
     */
    protected IStorage()
    {
        super( 3 );
        m_forbidden = Collections.<String>emptySet();
    }

    /**
     * ctor
     *
     * @param p_fordbidden forbidden keys
     */
    protected IStorage( final Collection<String> p_fordbidden )
    {
        super( 3 );
        m_forbidden = new ConcurrentSkipListSet<>( p_fordbidden );
    }

    /**
     * returns the set with forbidden keys
     *
     * @return set with keys
     */
    public final Set<String> getForbiddenKeys()
    {
        return m_forbidden;
    }
}
