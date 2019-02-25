/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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

package org.lightjason.agentspeak.language.execution.achievementtest;

import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * achievement-goal action based on a literal
 */
public final class CAchievementGoalLiteral extends IAchievementGoal<ILiteral>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8534258655371085840L;

    /**
     * ctor
     *
     * @param p_type value of the achievment-goal
     */
    public CAchievementGoalLiteral( @Nonnull final ILiteral p_type )
    {
        super( p_type, false );
    }

    /**
     * ctor
     *
     * @param p_type value of the achievment-goal
     * @param p_immediately immediately execution
     */
    public CAchievementGoalLiteral( @Nonnull final ILiteral p_type, final boolean p_immediately )
    {
        super( p_type, p_immediately );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}{1}", m_immediately ? "!!" : "!", m_value );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        return p_context.agent().trigger( ITrigger.EType.ADDGOAL.builddefault( m_value.allocate( p_context ) ), m_immediately );
    }

}
