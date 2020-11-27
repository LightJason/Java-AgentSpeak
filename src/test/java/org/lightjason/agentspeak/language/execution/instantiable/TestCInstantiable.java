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

package org.lightjason.agentspeak.language.execution.instantiable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.CPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.IPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Collections;
import java.util.stream.Stream;


/**
 * test all instantiable structure
 */
public final class TestCInstantiable extends IBaseTest
{
    /**
     * test empty trigger
     */
    @Test
    public void emptytrigger()
    {
        Assertions.assertEquals( 0, ITrigger.EMPTY.compareTo( ITrigger.EMPTY ) );
        Assertions.assertEquals( -1, ITrigger.EMPTY.compareTo( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "xxx" ) ) ) );
        Assertions.assertEquals( 0, ITrigger.EMPTY.structurehash() );
        Assertions.assertFalse( ITrigger.EMPTY.hasShallowcopywithoutsuffix() );
        Assertions.assertTrue( ITrigger.EMPTY.type().sequence().isEmpty() );
        Assertions.assertEquals( ITrigger.EType.EMPTY, ITrigger.EMPTY.type() );
        Assertions.assertEquals( ILiteral.EMPTY, ITrigger.EMPTY.literal() );
        Assertions.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopy() );
        Assertions.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopysuffix() );
        Assertions.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopywithoutsuffix() );
    }

    /**
     * test empty rule
     */
    @Test
    public void emptyrule()
    {
        Assertions.assertEquals( ILiteral.EMPTY, IRule.EMPTY.identifier() );
        Assertions.assertEquals( ILiteral.EMPTY, IRule.EMPTY.literal() );
        Assertions.assertTrue( IRule.EMPTY.description().isEmpty() );
        Assertions.assertEquals( IContext.EMPTYRULE, IRule.EMPTY.instantiate( IAgent.EMPTY, Stream.empty() ) );
        Assertions.assertEquals( 0, IRule.EMPTY.variables().count() );
        Assertions.assertEquals( 0, IRule.EMPTY.variabledescription().count() );
        Assertions.assertEquals( 0, IRule.EMPTY.tags().count() );
        execute(
            IRule.EMPTY,
            false,
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    /**
     * test empty plan
     */
    @Test
    public void emptyplan()
    {
        Assertions.assertEquals( ITrigger.EMPTY, IPlan.EMPTY.trigger() );
        Assertions.assertTrue( IPlan.EMPTY.condition( IContext.EMPTYPLAN ) );
        Assertions.assertEquals( ILiteral.EMPTY, IPlan.EMPTY.literal() );
        Assertions.assertEquals( IContext.EMPTYPLAN, IPlan.EMPTY.instantiate( IAgent.EMPTY, Stream.empty() ) );
        Assertions.assertTrue( IPlan.EMPTY.description().isEmpty() );
        Assertions.assertEquals( 0, IPlan.EMPTY.tags().count() );
        Assertions.assertEquals( 0, IPlan.EMPTY.variabledescription().count() );
        Assertions.assertEquals( 0, IPlan.EMPTY.variables().count() );
        Assertions.assertEquals(
            0,
            IPlan.EMPTY.execute(
                false,
                IContext.EMPTYPLAN,
                Collections.emptyList(),
                Collections.emptyList()
            ).count()
        );
    }

    /**
     * test plan statistic
     */
    @Test
    public void statistic()
    {
        final IPlanStatistic l_statistic = CPlanStatistic.of( IPlan.EMPTY );

        Assertions.assertTrue( l_statistic.toString().startsWith( "successful [0], fail [0]:" ) );
        Assertions.assertEquals( 0L, l_statistic.count() );
        Assertions.assertEquals( 0L, l_statistic.fail() );
        Assertions.assertEquals( 0L, l_statistic.successful() );
        Assertions.assertEquals( l_statistic, l_statistic );
        Assertions.assertEquals( l_statistic, IPlan.EMPTY );
    }
}
