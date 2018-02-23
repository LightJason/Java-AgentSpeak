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

package org.lightjason.agentspeak.language.execution.action.achievement_test;

import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.language.variable.IVariableEvaluate;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * achievement-goal action based on variables
 */
public final class CAchievementGoalVariable extends IAchievementGoal<IVariableEvaluate>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 4338602284287736836L;

    /**
     * ctor
     *
     * @param p_type value of the achievment-goal
     * @param p_immediately immediately execution
     */
    public CAchievementGoalVariable( @Nonnull final IVariableEvaluate p_type, final boolean p_immediately )
    {
        super( p_type, p_immediately );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}{1}", m_immediately ? "!!" : "!", m_value );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        return p_context.agent().trigger(
            CTrigger.from(
                ITrigger.EType.ADDGOAL,
                m_value.evaluate( p_context )
            ),
            m_immediately
        );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return m_value == null ? Stream.empty() :  m_value.variables();
    }
}
