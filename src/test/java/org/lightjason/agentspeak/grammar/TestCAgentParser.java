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
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
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
    public void belief() throws Exception
    {
        final IASTVisitorAgent l_parser = new CParserAgent( Collections.emptySet(), Collections.emptySet() )
            .parse( streamfromstring( "foo(123). bar('test')." ) );

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
    public void initialgoal() throws Exception
    {
        Assert.assertEquals(
            ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "main" ) ),
            new CParserAgent( Collections.emptySet(), Collections.emptySet() )
                .parse( streamfromstring( "!main." ) ).initialgoal()
        );
    }

    /**
     * test simple rule
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void simplerule() throws Exception
    {
        final IRule l_rule = parsesinglerule(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "nexttower(T, M) :- T--; T = T < 0 ? M - 1 + T : T."
        );

        final IVariable<Object> l_tvar = new CVariable<>( "T" ).set( 0 );
        final IVariable<Object> l_mvar = new CVariable<>( "M" ).set( 3 );
        final IContext l_context = new CLocalContext( l_tvar, l_mvar );

        Assert.assertTrue(
            l_rule.execute(
                false,
                l_context,
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
        Assert.assertEquals( 1.0, l_tvar.<Number>raw() );

        l_tvar.set( 3 );
        Assert.assertTrue(
            l_rule.execute(
                false,
                l_context,
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
        Assert.assertEquals( 2.0, l_tvar.<Number>raw() );
    }

    /**
     * test complex rule
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void complexrule() throws Exception
    {
        final int l_fibonacci = new Random().nextInt( 25 );
        final CCollectValues l_values = new CCollectValues();

        final IAgent<?> l_agent = new CAgentGenerator(
            "fibonacci(X, R)"
            + " :- X <= 2;  R = 1 "
            + " :- X > 2; TA = X - 1; TB = X - 2; $fibonacci(TA,A); $fibonacci(TB,B); R = A+B."
            + "+!fib(X) <- $fibonacci(X, R); .push/value(X, R).",
            Stream.of( l_values ).collect( Collectors.toSet() ),
            Collections.emptySet()
        ).generatesingle();

        Assert.assertTrue(
            l_agent.trigger(
                ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "fib", CRawTerm.of( l_fibonacci ) ) ),
                true
            )
                   .value()
        );

        Assert.assertEquals( 2, l_values.value().size() );
        Assert.assertEquals( l_fibonacci, l_values.value().get( 0 ).<Number>raw() );
        Assert.assertEquals( fibonacci( l_fibonacci ).doubleValue(), l_values.value().get( 1 ).<Number>raw() );
    }

    /**
     * test action call with parameter
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void action() throws Exception
    {
        final CCollectValues l_values = new CCollectValues();

        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Stream.of( l_values ).collect( Collectors.toSet() ), Collections.emptySet() ),
            "+!actiontest <- X=5; .push/value(X, [1,2,3], 'test data', inner('foobar'), 555, fail)."
        );

        final IVariable<?> l_xvar = new CVariable<>( "X" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute(
                false,
                new CLocalContext( l_xvar ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertEquals( 5.0, l_xvar.<Number>raw() );
        Assert.assertEquals( 6, l_values.value().size() );

        Assert.assertEquals( 5.0, l_values.value().get( 0 ).<Number>raw() );
        Assert.assertArrayEquals( Stream.of( 1.0, 2.0, 3.0 ).toArray(), l_values.value().get( 1 ).<List<?>>raw().toArray() );
        Assert.assertEquals( "test data", l_values.value().get( 2 ).raw() );
        Assert.assertEquals( CLiteral.of( "inner", CRawTerm.of( "foobar" ) ), l_values.value().get( 3 ).raw() );
        Assert.assertEquals( 555.0, l_values.value().get( 4 ).<Number>raw() );
        Assert.assertEquals( false, l_values.value().get( 5 ).raw() );
    }

    /**
     * test success and fail plan
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void successfailplan() throws Exception
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
    public void repair() throws Exception
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
    public void deconstructsimple() throws Exception
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
    public void numberexpression() throws Exception
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
    public void numbervariableexpression() throws Exception
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
    public void constantexpression() throws Exception
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
     * test boolean operators
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void booleanoperators() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!calculate <- AndTrue = true && true; AndFalse = true and false; OrTrue = true || false;"
            + "OrFalse = false or false; XorTrue = true xor false; XorFalse = true ^ true; NotFalse = not true; NotTrue = ~false."
        );

        final IVariable<?> l_andtrue = new CVariable<>( "AndTrue" );
        final IVariable<?> l_andfalse = new CVariable<>( "AndFalse" );
        final IVariable<?> l_ortrue = new CVariable<>( "OrTrue" );
        final IVariable<?> l_orfalse = new CVariable<>( "OrFalse" );
        final IVariable<?> l_xortrue = new CVariable<>( "XorTrue" );
        final IVariable<?> l_xorfalse = new CVariable<>( "XorFalse" );
        final IVariable<?> l_notfalse = new CVariable<>( "NotFalse" );
        final IVariable<?> l_nottrue = new CVariable<>( "NotTrue" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute(
                false,
                new CLocalContext( l_andtrue, l_andfalse, l_ortrue, l_orfalse, l_xortrue, l_xorfalse, l_notfalse, l_nottrue ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertTrue( l_plan.toString(), l_andtrue.raw() );
        Assert.assertFalse( l_plan.toString(), l_andfalse.raw() );

        Assert.assertTrue( l_plan.toString(), l_ortrue.raw() );
        Assert.assertFalse( l_plan.toString(), l_orfalse.raw() );

        Assert.assertTrue( l_plan.toString(), l_xortrue.raw() );
        Assert.assertFalse( l_plan.toString(), l_xorfalse.raw() );

        Assert.assertTrue( l_plan.toString(), l_nottrue.raw() );
        Assert.assertFalse( l_plan.toString(), l_notfalse.raw() );
    }

    /**
     * test complex boolean expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void booleanexpression() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( Collections.emptySet(), Collections.emptySet() ),
            "+!calculate <- R = false || false and not ( true and false ) xor ( 2 < 3 )."
        );

        final IVariable<?> l_result = new CVariable<>( "R" );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_result ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        //Checkstyle:OFF:SimplifyBooleanExpression
        Assert.assertEquals( false || false && !( true && false ) ^ ( 2 < 3 ), l_result.raw() );
        //Checkstyle:ON:SimplifyBooleanExpression
    }

    /**
     * test ternary operator true case
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void ternarytrue() throws Exception
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
     * test ternary operator with expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void ternarycalculation() throws Exception
    {
        final Random l_random = new Random();

        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "+!calculate(XVal, YVal) <- Res = XVal < YVal ? XVal + YVal - 2 : XVal + 3 - YVal + 4."
        );

        final IVariable<?> l_result = new CVariable<>( "Res" );
        final IVariable<?> l_xvar = new CVariable<>( "XVal" ).set( l_random.nextInt() );
        final IVariable<?> l_yvar = new CVariable<>( "YVal" ).set( l_random.nextInt() );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute( false, new CLocalContext( l_result, l_xvar, l_yvar ), Collections.emptyList(), Collections.emptyList() ).value()
        );

        Assert.assertEquals(
            l_xvar.<Number>raw().doubleValue() < l_yvar.<Number>raw().doubleValue()
            ? l_xvar.<Number>raw().doubleValue() + l_yvar.<Number>raw().doubleValue() - 2
            : l_xvar.<Number>raw().doubleValue() + 3 - l_yvar.<Number>raw().doubleValue() + 4,

            l_result.<Number>raw()
        );
    }

    /**
     * test ternary operator true case
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void ternaryfalse() throws Exception
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
    public void multipleplanitems() throws Exception
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
    public void plandescription() throws Exception
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
    public void multipleplanexecution() throws Exception
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
    public void annotation() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( Collections.emptySet(), Collections.emptySet() ),
            "@parallel @atomic @constant(StringValue,'abcd') @constant(NumberValue,12345) @tag('foo')"
            + "@variable(X,'x value description') @tag('bar') @description('description text') +!annotation(X) <- success."
        );

        Assert.assertEquals( "description text", property( "m_description", l_plan ) );

        Assert.assertTrue( l_plan.toString(), property( "m_atomic", l_plan ) );
        Assert.assertTrue( l_plan.toString(), property( "m_parallel", l_plan ) );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.variables().parallel().anyMatch( i -> ( "StringValue".equals( i.functor() ) ) && ( "abcd".equals( i.raw() ) ) )
        );
        Assert.assertTrue(
            l_plan.toString(),
            l_plan.variables().parallel().anyMatch( i -> ( "NumberValue".equals( i.functor() ) ) && ( Double.valueOf( 12345 ).equals( i.raw() ) ) )
        );

        Assert.assertArrayEquals( Stream.of( "foo", "bar" ).toArray(), l_plan.tags().toArray() );

        Assert.assertArrayEquals(
            Stream.of( new CVariable<>( "X" ).set( "x value description" ) ).toArray(),
            l_plan.variabledescription().toArray()
        );
    }

    /**
     * test term-value list
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void termlist() throws Exception
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

    /**
     * test lambda expression
     *
     * @throws Exception thrown on stream and parser error
     * @todo add lambda block-command test
     */
    @Test
    public void lambda() throws Exception
    {
        final CCollectValues l_values = new CCollectValues();

        final IPlan l_plan = parsesingleplan(
            new CParserAgent(
                Stream.of( l_values ).collect( Collectors.toSet() ),
                org.lightjason.agentspeak.common.CCommon.lambdastreamingFromPackage().collect( Collectors.toSet() )
            ),
            "+!lambda <- (L) -> I : .push/value(I)."
        );

        final IVariable<?> l_var = new CVariable<>( "L" ).set( Stream.of( 1, 2, 3, 4 ).collect( Collectors.toList() ) );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.execute(
                false,
                new CLocalContext( l_var ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        System.out.println( l_values.value() );
        Assert.assertArrayEquals( Stream.of( 1, 2, 3, 4 ).toArray(), l_values.value().stream().map( ITerm::raw ).toArray() );
    }

    /**
     * test of inner action execution
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void inneraction() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( org.lightjason.agentspeak.common.CCommon.actionsFromPackage().collect( Collectors.toSet() ), Collections.emptySet() ),
            "+!inner <- L = .math/max( .math/min( 3, 4, 1 ), -8, -6 ) ."
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

        Assert.assertEquals( 1.0, l_var.<Number>raw() );
    }
}
