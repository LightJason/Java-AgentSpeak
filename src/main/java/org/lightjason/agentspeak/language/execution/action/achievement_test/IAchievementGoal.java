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

package org.lightjason.agentspeak.language.execution.action.achievement_test;

import com.google.common.collect.ImmutableMultiset;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.action.IBaseExecution;


/**
 * abstract achievement-goal class for goal execution
 */
abstract class IAchievementGoal<T extends ITerm> extends IBaseExecution<T>
{
    /**
     * flag to run immediately
     */
    protected final boolean m_immediately;

    /**
     * ctor
     *
     * @param p_type value of the achievment-goal
     * @param p_immediately immediately execution
     */
    protected IAchievementGoal( final T p_type, final boolean p_immediately )
    {
        super( p_type );
        m_immediately = p_immediately;
    }

    @Override
    public double score( final IAgent<?> p_agent )
    {
        return p_agent.getAggregation().evaluate( p_agent, ImmutableMultiset.of() );
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IExecution ) && ( this.hashCode() == p_object.hashCode() );
    }

}
