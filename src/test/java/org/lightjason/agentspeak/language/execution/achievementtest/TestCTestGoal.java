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

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IInspector;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.IPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.execution.passing.CPassVariableLiteral;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;


/**
 * test for test-goal
 */
public final class TestCTestGoal extends IBaseTest
{
    /**
     * agent
     */
    private IAgent<?> m_agent;

    /**
     * init
     *
     * @throws Exception on any error
     */
    @Before
    public void init() throws Exception
    {
        m_agent = new CAgentGenerator().generatesingle();
    }

    /**
     * test goal-literal
     */
    @Test
    public void goalliteral()
    {
        Assume.assumeNotNull( m_agent );

        execute(
            new CAchievementGoalLiteral( CLiteral.of( "foo" ), false ),
            false,
            Collections.emptyList(),
            Collections.emptyList(),
            m_agent
        );

        m_agent.inspect( new IInspector()
        {
            @Override
            public void inspectsleeping( final long p_value )
            {}

            @Override
            public void inspectcycletime( final long p_value )
            {}

            @Override
            public void inspectbelief( final Stream<ILiteral> p_value )
            {}

            @Override
            public void inspectplans( @Nonnull final Stream<IPlanStatistic> p_value )
            {}

            @Override
            public void inspectrules( @Nonnull final Stream<IRule> p_value )
            {}

            @Override
            public void inspectrunningplans( @Nonnull final Stream<ILiteral> p_value )
            {}

            @Override
            public void inspectstorage( @Nonnull final Stream<? extends Map.Entry<String, ?>> p_value )
            {}

            @Override
            public void inspectpendingtrigger( @Nonnull final Stream<ITrigger> p_value )
            {
                Assert.assertEquals( "foo", p_value.findFirst().get().literal().functor() );
            }
        } );
    }

    /**
     * test goal-variable
     */
    @Test
    public void goalvariable()
    {
        Assume.assumeNotNull( m_agent );

        final IVariable<?> l_var = new CVariable<>( "Var" ).set( "bar" );

        execute(
            new CAchievementGoalVariable( new CPassVariableLiteral( l_var ) ),
            false,
            Collections.emptyList(),
            Collections.emptyList(),
            m_agent,
            l_var
        );

        m_agent.inspect( new IInspector()
        {
            @Override
            public void inspectsleeping( final long p_value )
            {}

            @Override
            public void inspectcycletime( final long p_value )
            {}

            @Override
            public void inspectbelief( final Stream<ILiteral> p_value )
            {}

            @Override
            public void inspectplans( @Nonnull final Stream<IPlanStatistic> p_value )
            {}

            @Override
            public void inspectrules( @Nonnull final Stream<IRule> p_value )
            {}

            @Override
            public void inspectrunningplans( @Nonnull final Stream<ILiteral> p_value )
            {}

            @Override
            public void inspectstorage( @Nonnull final Stream<? extends Map.Entry<String, ?>> p_value )
            {}

            @Override
            public void inspectpendingtrigger( @Nonnull final Stream<ITrigger> p_value )
            {
                Assert.assertEquals( "bar", p_value.findFirst().get().literal().functor() );
            }
        } );
    }

}
