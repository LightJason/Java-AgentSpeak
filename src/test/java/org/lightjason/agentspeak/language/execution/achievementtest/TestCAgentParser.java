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

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.grammar.CParserAgent;
import org.lightjason.agentspeak.grammar.IASTVisitorAgent;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * test for agent grammar elements
 */
public final class TestCAgentParser extends IBaseTest
{
    /**
     * test belief
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void belief() throws Exception
    {
        final IASTVisitorAgent l_parser = new CParserAgent( Collections.emptySet(), Collections.emptySet() )
                                            .parse( IOUtils.toInputStream(  "foo(123). bar('test').",  "UTF-8" ) );

        final List<ILiteral> l_beliefs = new ArrayList<>( l_parser.initialbeliefs() );

        Assert.assertEquals( 2, l_beliefs.size() );
        Assert.assertEquals( CLiteral.of( "foo", CRawTerm.of( 123.0 ) ), l_beliefs.get( 0 ) );
        Assert.assertEquals( CLiteral.of( "bar", CRawTerm.of( "test" ) ), l_beliefs.get( 1 ) );
    }

    /**
     * test initial-goal
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void initialgoal() throws Exception
    {
        Assert.assertEquals(
            CLiteral.of( "main" ),
            new CParserAgent( Collections.emptySet(), Collections.emptySet() )
                .parse( IOUtils.toInputStream(  "!main.",  "UTF-8" ) ).initialgoal()
        );
    }

    /**
     * test success and fail plan
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void successfailplan() throws Exception
    {
        final Map<ILiteral, IPlan> l_plans = new CParserAgent( Collections.emptySet(), Collections.emptySet() )
                                                .parse( IOUtils.toInputStream(  "+!mainsuccess <- #success. +!mainfail <- #fail.", "UTF-8" ) )
                                                .plans()
                                                .stream()
                                                .collect( Collectors.toMap( i -> i.trigger().literal(), i -> i ) );

        Assert.assertEquals( 2, l_plans.size() );

        Assert.assertTrue(
            l_plans.get( CLiteral.of( "mainsuccess" ) ).toString(),
            l_plans.get( CLiteral.of( "mainsuccess" ) )
                   .execute( false, IContext.EMPTYPLAN, Collections.emptyList(), Collections.emptyList() )
                   .value()
        );

        Assert.assertFalse(
            l_plans.get( CLiteral.of( "mainfail" ) ).toString(),
            l_plans.get( CLiteral.of( "mainfail" ) )
                   .execute( false, IContext.EMPTYPLAN, Collections.emptyList(), Collections.emptyList() )
                   .value()
        );
    }

    /**
     * test repair-chain
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void repair() throws Exception
    {
        final Map<ILiteral, IPlan> l_plans = new CParserAgent( Collections.emptySet(), Collections.emptySet() )
                                                .parse( IOUtils.toInputStream(  "+!mainsuccess <- #fail << #fail << #success. +!mainfail <- #fail << #fail.", "UTF-8" ) )
                                                .plans()
                                                .stream()
                                                .collect( Collectors.toMap( i -> i.trigger().literal(), i -> i ) );

        Assert.assertEquals( 2, l_plans.size() );

        Assert.assertTrue(
            l_plans.get( CLiteral.of( "mainsuccess" ) ).toString(),
            l_plans.get( CLiteral.of( "mainsuccess" ) )
                   .execute( false, IContext.EMPTYPLAN, Collections.emptyList(), Collections.emptyList() )
                   .value()
        );

        Assert.assertFalse(
            l_plans.get( CLiteral.of( "mainfail" ) ).toString(),
            l_plans.get( CLiteral.of( "mainfail" ) )
                   .execute( false, IContext.EMPTYPLAN, Collections.emptyList(), Collections.emptyList() )
                   .value()
        );
    }
}
