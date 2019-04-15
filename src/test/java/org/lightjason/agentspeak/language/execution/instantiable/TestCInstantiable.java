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

import org.junit.Assert;
import org.junit.Test;
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
        Assert.assertEquals( 0, ITrigger.EMPTY.compareTo( ITrigger.EMPTY ) );
        Assert.assertEquals( -1, ITrigger.EMPTY.compareTo( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "xxx" ) ) ) );
        Assert.assertEquals( 0, ITrigger.EMPTY.structurehash() );
        Assert.assertFalse( ITrigger.EMPTY.hasShallowcopywithoutsuffix() );
        Assert.assertTrue( ITrigger.EMPTY.type().sequence().isEmpty() );
        Assert.assertEquals( ITrigger.EType.EMPTY, ITrigger.EMPTY.type() );
        Assert.assertEquals( ILiteral.EMPTY, ITrigger.EMPTY.literal() );
        Assert.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopy() );
        Assert.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopysuffix() );
        Assert.assertEquals( ITrigger.EMPTY, ITrigger.EMPTY.shallowcopywithoutsuffix() );
    }

    /**
     * test empty rule
     */
    @Test
    public void emptyrule()
    {
        Assert.assertEquals( ILiteral.EMPTY, IRule.EMPTY.identifier() );
        Assert.assertEquals( ILiteral.EMPTY, IRule.EMPTY.literal() );
        Assert.assertTrue( IRule.EMPTY.description().isEmpty() );
        Assert.assertEquals( IContext.EMPTYRULE, IRule.EMPTY.instantiate( IAgent.EMPTY, Stream.empty() ) );
        Assert.assertEquals( 0, IRule.EMPTY.variables().count() );
        Assert.assertEquals( 0, IRule.EMPTY.variabledescription().count() );
        Assert.assertEquals( 0, IRule.EMPTY.tags().count() );
        execute(
            IRule.EMPTY,
            false,
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    /**
     * test plan statistic
     */
    @Test
    public void statistic()
    {
        final IPlanStatistic l_statistic = CPlanStatistic.of( IPlan.EMPTY );

        Assert.assertTrue( l_statistic.toString().startsWith( "successful [0], fail [0]:" ) );
        Assert.assertEquals( 0L, l_statistic.count() );
        Assert.assertEquals( 0L, l_statistic.fail() );
        Assert.assertEquals( 0L, l_statistic.successful() );
        Assert.assertEquals( l_statistic, l_statistic );
        Assert.assertEquals( l_statistic, IPlan.EMPTY );
    }
}
