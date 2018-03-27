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

package org.lightjason.agentspeak.grammar;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * test for agent parser
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
                                            .parse( streamfromstring(  "foo(123). bar('test')." ) );

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
                .parse( streamfromstring(  "!main." ) ).initialgoal()
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
                                                .parse( streamfromstring(  "+!mainsuccess <- success. +!mainfail <- fail." ) )
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
                                                .parse( streamfromstring(  "+!mainsuccess <- fail << fail << success. +!mainfail <- fail << fail." ) )
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
     * test deconstruct
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void deconstructsimple() throws Exception
    {
        final IPlan l_plan = new CParserAgent( Collections.emptySet(), Collections.emptySet() )
                                .parse( streamfromstring(  "+!mainsuccess <- [X|Y] =.. foo(123)." ) )
                                .plans()
                                .stream()
                                .findFirst()
                                .orElse( IPlan.EMPTY );

        Assert.assertNotEquals( IPlan.EMPTY, l_plan );

        final IVariable<?> l_xvar = new CVariable<>( "X" );
        final IVariable<?> l_yvar = new CVariable<>( "Y" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_xvar, l_yvar ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals( "foo", l_xvar.raw() );
        Assert.assertTrue( l_yvar.toString(), ( l_yvar.raw() instanceof List<?> ) && ( l_yvar.<List<?>>raw().size() == 1 ) );
        Assert.assertEquals( 123.0, l_yvar.<List<Number>>raw().get( 0 ) );
    }

    /**
     * test number expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void numberexpression() throws Exception
    {
        final IPlan l_plan = new CParserAgent( Collections.emptySet(), Collections.emptySet() )
            .parse( streamfromstring(  "+!calculate <- X = 3 * 2 + 5 + 1 - 2 * ( 3 + 1 ) + 2 * 2 ** 3." ) )
            .plans()
            .stream()
            .findFirst()
            .orElse( IPlan.EMPTY );

        Assert.assertNotEquals( IPlan.EMPTY, l_plan );

        final IVariable<?> l_xvar = new CVariable<>( "X" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_xvar ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals( 20.0, l_xvar.<Number>raw() );
    }

    /**
     * test number expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void numbervariableexpression() throws Exception
    {
        final IPlan l_plan = new CParserAgent( Collections.emptySet(), Collections.emptySet() )
            .parse( streamfromstring(  "+!calculate <- X = A * 2 - B * ( 3 + C ) + 2 ** D." ) )
            .plans()
            .stream()
            .findFirst()
            .orElse( IPlan.EMPTY );

        Assert.assertNotEquals( IPlan.EMPTY, l_plan );

        final Random l_random = new Random();

        final IVariable<?> l_avar = new CVariable<>( "A" ).set( l_random.nextDouble() );
        final IVariable<?> l_bvar = new CVariable<>( "B" ).set( l_random.nextInt( 40 ) );
        final IVariable<?> l_cvar = new CVariable<>( "C" ).set( l_random.nextInt( 30 ) );
        final IVariable<?> l_dvar = new CVariable<>( "D" ).set( l_random.nextInt( 20 ) );
        final IVariable<?> l_xvar = new CVariable<>( "X" ).set( l_random.nextInt( 10 ) );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute(
                false,
                new CLocalContext( l_xvar, l_avar, l_bvar, l_cvar, l_dvar ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertEquals(
            l_avar.<Number>raw().doubleValue() * 2 - l_bvar.<Number>raw().doubleValue() * ( 3 + l_cvar.<Number>raw().doubleValue() )
            + Math.pow( 2, l_dvar.<Number>raw().doubleValue() ),
            l_xvar.<Number>raw().doubleValue(),
            0.000001
        );
    }

    /**
     * test number expression with constants
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void constantexpression() throws Exception
    {
        final IPlan l_plan = new CParserAgent( Collections.emptySet(), Collections.emptySet() )
            .parse( streamfromstring(  "+!calculate <- X = pi * euler + gravity." ) )
            .plans()
            .stream()
            .findFirst()
            .orElse( IPlan.EMPTY );

        Assert.assertNotEquals( IPlan.EMPTY, l_plan );

        final IVariable<?> l_xvar = new CVariable<>( "X" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_xvar ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals(
            CCommon.NUMERICCONSTANT.get( "pi" ) * CCommon.NUMERICCONSTANT.get( "euler" ) + CCommon.NUMERICCONSTANT.get( "gravity" ),
            l_xvar.<Number>raw().doubleValue(),
            0.00000001
        );
    }
}
