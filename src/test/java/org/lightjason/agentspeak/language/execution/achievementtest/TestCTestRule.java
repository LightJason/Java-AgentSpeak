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
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.execution.passing.CPassVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Collections;
import java.util.Objects;


/**
 * test for test-goal
 */
public final class TestCTestRule extends IBaseTest
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
     * test rule
     */
    @Test
    public void ruletest()
    {
        Assumptions.assumeTrue( Objects.nonNull( m_agent ) );

        m_agent.rules().put( CPath.of( "foo" ), IRule.EMPTY );

        Assertions.assertTrue(
            execute(
                new CTestRule( CPath.of( "foo" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                m_agent
            )
        );

        Assertions.assertFalse(
            execute(
                new CTestRule( CPath.of( "xxx" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                m_agent
            )
        );
    }

    /**
     * test rule-test tostring
     */
    @Test
    public void testruletostring()
    {
        Assertions.assertEquals( "?$foobar", new CTestRule( CPath.of( "foobar" ) ).toString() );
    }

    /**
     * test equal and hashcode
     */
    @Test
    public void equalshashcode()
    {
        Assertions.assertEquals(
            new CAchievementRuleLiteral( CLiteral.of( "bar" ) ),
            new CAchievementRuleLiteral( CLiteral.of( "bar" ) )
        );
        Assertions.assertNotEquals(
            new CAchievementRuleLiteral( CLiteral.of( "bar" ) ),
            new CAchievementRuleLiteral( CLiteral.of( "foo" ) )
        );


        Assertions.assertEquals(
            new CAchievementRuleVariable( new CPassVariable( new CVariable<>( "FOO" ) ) ),
            new CAchievementRuleVariable( new CPassVariable( new CVariable<>( "FOO" ) ) )
        );
        Assertions.assertNotEquals(
            new CAchievementRuleVariable( new CPassVariable( new CVariable<>( "FOO" ) ) ),
            new CAchievementRuleVariable( new CPassVariable( new CVariable<>( "BAR" ) ) )
        );
    }
}
