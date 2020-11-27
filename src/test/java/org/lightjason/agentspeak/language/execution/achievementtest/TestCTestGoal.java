/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IInspector;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.CPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.IPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.execution.passing.CPassVariableLiteral;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @BeforeEach
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
        Assumptions.assumeTrue( Objects.nonNull( m_agent ) );

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
                Assertions.assertEquals( "foo", p_value.findFirst().get().literal().functor() );
            }
        } );
    }

    /**
     * test goal-variable
     */
    @Test
    public void goalvariable()
    {
        Assumptions.assumeTrue( Objects.nonNull( m_agent ) );

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
                Assertions.assertEquals( "bar", p_value.findFirst().get().literal().functor() );
            }
        } );
    }

    /**
     * tets test-goal
     *
     * @throws Exception on agent execution
     */
    @Test
    public void testgoal() throws Exception
    {
        Assumptions.assumeTrue( Objects.nonNull( m_agent ) );

        final ITrigger l_trigger = CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foobar" ) );
        m_agent.plans().put( l_trigger, CPlanStatistic.of( new IPlan()
        {
            @Nonnull
            @Override
            public ITrigger trigger()
            {
                return l_trigger;
            }

            @Override
            public boolean condition( @Nonnull final IContext p_context )
            {
                return true;
            }

            @Override
            public ILiteral literal()
            {
                return l_trigger.literal();
            }

            @Nonnull
            @Override
            public IContext instantiate( @Nonnull final IAgent<?> p_agent, @Nonnull final Stream<IVariable<?>> p_variable )
            {
                return CCommon.instantiate( this, p_agent, p_variable );
            }

            @Nonnull
            @Override
            public String description()
            {
                return "";
            }

            @Nonnull
            @Override
            public Stream<String> tags()
            {
                return Stream.empty();
            }

            @Nonnull
            @Override
            public Stream<IVariable<?>> variabledescription()
            {
                return Stream.empty();
            }

            @Nonnull
            @Override
            public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                   @Nonnull final List<ITerm> p_return )
            {
                return Stream.empty();
            }

            @Nonnull
            @Override
            public Stream<IVariable<?>> variables()
            {
                return Stream.empty();
            }
        } ) );
        m_agent.trigger( l_trigger );
        m_agent.call();


        Assertions.assertTrue(
            execute(
                new CTestGoal( l_trigger.literal().fqnfunctor() ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                m_agent
            )
        );

        Assertions.assertFalse(
            execute(
                new CTestGoal( CPath.of( "xxx" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                m_agent
            )
        );
    }

}
