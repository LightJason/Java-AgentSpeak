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

package org.lightjason.agentspeak.beliefbase.storage;


import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.IBeliefPerceive;


/**
 * default structure of a storage
 */
public abstract class IBaseStorage<N, M, T extends IAgent<?>> implements IStorage<N, M, T>
{
    /**
     * belief perceiver object
     */
    private final IBeliefPerceive<T> m_perceive;

    /**
     * ctor
     */
    @SuppressWarnings( "unchecked" )
    protected IBaseStorage()
    {
        this( (IBeliefPerceive<T>) IBeliefPerceive.EMPTY );
    }

    /**
     * ctor
     *
     * @param p_perceive perceive object
     */
    protected IBaseStorage( final IBeliefPerceive<T> p_perceive )
    {
        m_perceive = p_perceive;
    }


    @Override
    public final T update( final T p_agent )
    {
        return m_perceive.perceive( p_agent );
    }

}
