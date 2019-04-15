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

package org.lightjason.agentspeak.language.execution;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.CMutexVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test execution parts
 */
public final class TestCExecution extends IBaseTest
{
    /**
     * test execution variables
     */
    @Test
    public void execution()
    {
        Assert.assertEquals( 0, IExecution.EMPTY.variables().count() );
    }

    /**
     * test empty context
     */
    @Test
    public void emptycontext()
    {
        Assert.assertEquals( IAgent.EMPTY, IContext.EMPTYPLAN.agent() );
        Assert.assertEquals( IAgent.EMPTY, IContext.EMPTYRULE.agent() );

        Assert.assertEquals( IRule.EMPTY, IContext.EMPTYRULE.instance() );
        Assert.assertEquals( IPlan.EMPTY, IContext.EMPTYPLAN.instance() );

        Assert.assertTrue( IContext.EMPTYPLAN.instancevariables().isEmpty() );
        Assert.assertTrue( IContext.EMPTYRULE.instancevariables().isEmpty() );

        Assert.assertEquals( IContext.EMPTYPLAN, IContext.EMPTYPLAN.duplicate() );
        Assert.assertEquals( IContext.EMPTYRULE, IContext.EMPTYRULE.duplicate() );

        Assert.assertEquals( IContext.EMPTYPLAN, IContext.EMPTYPLAN.duplicate( Stream.empty() ) );
        Assert.assertEquals( IContext.EMPTYRULE, IContext.EMPTYRULE.duplicate( Stream.empty() ) );
    }

    /**
     * test context to string
     */
    @Test
    public void contexttostring()
    {
        Assert.assertTrue(
            new CContext(
                IAgent.EMPTY,
                IPlan.EMPTY,
                Collections.emptySet()
            ).toString().startsWith( "org.lightjason.agentspeak.language.execution.CContext@" )
        );
    }

    /**
     * test context hashcode & equals
     */
    @Test
    public void contexthashcode()
    {
        Assert.assertEquals(
            new CContext( IAgent.EMPTY, IPlan.EMPTY, Collections.emptySet() ),
            new CContext( IAgent.EMPTY, IPlan.EMPTY, Collections.emptySet() )
        );

        Assert.assertEquals(
            new CContext( IAgent.EMPTY, IPlan.EMPTY, Stream.of( new CMutexVariable<>( "X" ) ).collect( Collectors.toSet() ) ),
            new CContext( IAgent.EMPTY, IPlan.EMPTY, Stream.of( new CVariable<>( "X" ) ).collect( Collectors.toSet() ) )
        );
    }
}
