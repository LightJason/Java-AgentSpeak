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
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for agent parser
 */
public final class TestCAgentParser extends IBaseGrammarTest
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
        final Map<ILiteral, IPlan> l_plans = parsemultipleplans(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!mainsuccess <- success. +!mainfail <- fail."
        ).collect( Collectors.toMap( i -> i.trigger().literal(), i -> i ) );

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
        final Map<ILiteral, IPlan> l_plans = parsemultipleplans(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!mainsuccess <- fail << fail << success. +!mainfail <- fail << fail."
        ).collect( Collectors.toMap( i -> i.trigger().literal(), i -> i ) );

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
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!mainsuccess <- [X|Y] =.. foo(123)."
        );

        final IVariable<?> l_xvar = new CVariable<>( "X" );
        final IVariable<?> l_yvar = new CVariable<>( "Y" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_xvar, l_yvar ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals( "foo", l_xvar.raw() );
        Assert.assertTrue( l_yvar.toString(), ( l_yvar.raw() instanceof List<?> ) && ( l_yvar.<List<?>>raw().size() == 1 ) );
        Assert.assertEquals( 123.0, l_yvar.<List<?>>raw().get( 0 ) );
    }

    /**
     * test number expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void numberexpression() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!calculate <- R = 3 * 2 + 5 + 1 - 2 * ( 3 + 1 ) + 2 * 2 ** 3."
        );

        final IVariable<?> l_resultvar = new CVariable<>( "R" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_resultvar ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals( 20.0, l_resultvar.<Number>raw() );
    }

    /**
     * test number expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void numbervariableexpression() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!calculate <- W = A * 2 - B * ( 3 + C ) + 2 ** D."
        );

        final Random l_random = new Random();

        final IVariable<?> l_avar = new CVariable<>( "A" ).set( l_random.nextDouble() );
        final IVariable<?> l_bvar = new CVariable<>( "B" ).set( l_random.nextInt( 40 ) );
        final IVariable<?> l_cvar = new CVariable<>( "C" ).set( l_random.nextInt( 30 ) );
        final IVariable<?> l_dvar = new CVariable<>( "D" ).set( l_random.nextInt( 20 ) );
        final IVariable<?> l_result = new CVariable<>( "W" ).set( l_random.nextInt( 10 ) );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute(
                false,
                new CLocalContext( l_result, l_avar, l_bvar, l_cvar, l_dvar ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertEquals(
            l_avar.<Number>raw().doubleValue() * 2 - l_bvar.<Number>raw().doubleValue() * ( 3 + l_cvar.<Number>raw().doubleValue() )
            + Math.pow( 2, l_dvar.<Number>raw().doubleValue() ),
            l_result.<Number>raw().doubleValue(),
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
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!calculate <- O = pi * euler + gravity."
        );

        final IVariable<?> l_result = new CVariable<>( "O" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_result ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals(
            CCommon.NUMERICCONSTANT.get( "pi" ) * CCommon.NUMERICCONSTANT.get( "euler" ) + CCommon.NUMERICCONSTANT.get( "gravity" ),
            l_result.<Number>raw().doubleValue(),
            0.00000001
        );
    }

    /**
     * test ternary operator true case
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void ternarytrue() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!calculate <- P = 3 < 5 ? euler : pi."
        );

        final IVariable<?> l_result = new CVariable<>( "P" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_result ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals( CCommon.NUMERICCONSTANT.get( "euler" ), l_result.<Number>raw().doubleValue(), 0.00000001 );
    }

    /**
     * test ternary operator true case
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void ternaryfalse() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!calculate <- N = 5 < 3 ? boltzmann : lightspeed."
        );

        final IVariable<?> l_result = new CVariable<>( "N" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_result ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals( CCommon.NUMERICCONSTANT.get( "lightspeed" ), l_result.<Number>raw().doubleValue(), 0.00000001 );
    }


    /**
     * test multiple items in a plan
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void multipleplanitems() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!items <- N = 'test'; P = 5; C = fail."
        );

        final IVariable<?> l_nvar = new CVariable<>( "N" );
        final IVariable<?> l_pvar = new CVariable<>( "P" );
        final IVariable<?> l_cvar = new CVariable<>( "C" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_nvar, l_pvar, l_cvar ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals( "test", l_nvar.raw() );
        Assert.assertEquals( 5.0, l_pvar.<Number>raw() );
        Assert.assertEquals( false, l_cvar.raw() );
    }

    /**
     * test plan description
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void plandescription() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "@description('a long plan description') +!description <- success."
        );

        Assert.assertEquals( "a long plan description", l_plan.description() );
    }

    /**
     * test multiple plan execution
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void multipleplanexecution() throws Exception
    {
        final IPlan[] l_plans = parsemultipleplans(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!multi(X) : X > 5 <- Y = X + 3 : X <= 5 <- Y = X * 3."
        ).toArray( IPlan[]::new );

        final IVariable<?> l_var = new CVariable<>( "Y" );

        Assert.assertArrayEquals(
            Stream.of(
                11.0,
                6.0
            ).toArray(),

            Stream.of(
                new CVariable<>( "X" ).set( 8 ),
                new CVariable<>( "X" ).set( 2 )
            )
                  .map( i -> new CLocalContext( i, l_var ) )
                  .map( i -> Arrays.stream( l_plans )
                                   .filter( j -> j.condition( i ).value() )
                                   .map( j -> j.execute( false, i, Collections.emptyList(), Collections.emptyList() ) )
                                   .filter( IFuzzyValue::value )
                                   .findFirst()
                                   .map( j -> l_var )
                                   .get()
                  )
                  .map( i -> l_var.raw() )
                  .toArray()
        );
    }

    /**
     * test plan annotation
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void annotation() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "@parallel @atomic @constant(StringValue,'abcd') @constant(NumberValue,12345) @tag('foo') @tag('bar') @description('description text') +!annotation <- success."
        );

        Assert.assertEquals( "description text", property( "m_description", l_plan ) );
        Assert.assertTrue( l_plan.toString(), property( "m_atomic", l_plan ) );
        Assert.assertTrue( l_plan.toString(), property( "m_parallel", l_plan ) );
        Assert.assertTrue( l_plan.toString(), l_plan.variables().parallel().anyMatch( i -> "StringValue".equals( i.functor() ) ) );
        Assert.assertTrue( l_plan.toString(), l_plan.variables().parallel().anyMatch( i -> "NumberValue".equals( i.functor() ) ) );
        Assert.assertArrayEquals( Stream.of( "foo", "bar" ).toArray(), l_plan.tags().toArray() );
    }

    /**
     * test term-value list
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public final void termlist() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!list <- L = ['a', 1, true]."
        );

        final IVariable<?> l_var = new CVariable<>( "L" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute(
                false,
                new CLocalContext( l_var ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertTrue( l_var.toString(), l_var.raw() instanceof List<?> );
        Assert.assertEquals( 3, l_var.<List<?>>raw().size() );
        Assert.assertEquals( "a", l_var.<List<?>>raw().get( 0 ) );
        Assert.assertEquals( 1.0, l_var.<List<?>>raw().get( 1 ) );
        Assert.assertEquals( true, l_var.<List<?>>raw().get( 2 ) );
    }
}
