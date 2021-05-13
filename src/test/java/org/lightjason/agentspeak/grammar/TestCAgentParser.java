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

package org.lightjason.agentspeak.grammar;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.CLambdaStreamingStaticGenerator;
import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;
import org.lightjason.agentspeak.testing.action.CTestMax;
import org.lightjason.agentspeak.testing.action.CTestMin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for agent parser
 */
@Timeout( 60 )
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
        final IASTVisitorAgent l_parser = new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
            .parse( streamfromstring( "foo(123). bar('test')." ) );

        final List<ILiteral> l_beliefs = new ArrayList<>( l_parser.initialbeliefs() );

        Assertions.assertEquals( 2, l_beliefs.size() );
        Assertions.assertEquals( CLiteral.of( "foo", CRawTerm.of( 123.0 ) ), l_beliefs.get( 0 ) );
        Assertions.assertEquals( CLiteral.of( "bar", CRawTerm.of( "test" ) ), l_beliefs.get( 1 ) );
    }

    /**
     * test initial-goal
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void initialgoal() throws Exception
    {
        Assertions.assertEquals(
            ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "main" ) ),
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "nexttower(T, M) :- T--; T = T < 0 ? M - 1 + T : T."
        );

        final IVariable<Object> l_tvar = new CVariable<>( "T" ).set( 0 );
        final IVariable<Object> l_mvar = new CVariable<>( "M" ).set( 3 );


        Assertions.assertTrue(
            execute(
                l_rule,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_tvar,
                l_mvar
            )
        );
        Assertions.assertEquals( 1.0, l_tvar.<Number>raw() );

        l_tvar.set( 3 );
        Assertions.assertTrue(
            execute(
                l_rule,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_tvar,
                l_mvar
            )
        );
        Assertions.assertEquals( 2.0, l_tvar.<Number>raw() );
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
            + " :- X <= 2;  R = 1"
            + " :- X > 2; TA = X - 1; TB = X - 2; $fibonacci(TA,A); $fibonacci(TB,B); R = A+B."
            + "+!fib(X) <- $fibonacci(X, R); .push/value(X, R).",
            new CActionStaticGenerator( Stream.of( l_values ) ),
            ILambdaStreamingGenerator.EMPTY
        ).generatesingle();

        Assertions.assertTrue(
            defuzzify(
                l_agent.trigger(
                    ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "fib", CRawTerm.of( l_fibonacci ) ) ),
                    true
                ),
                l_agent
            )

        );

        Assertions.assertEquals( 2, l_values.value().size() );
        Assertions.assertEquals( l_fibonacci, l_values.value().get( 0 ).<Number>raw() );
        Assertions.assertEquals( fibonacci( l_fibonacci ).doubleValue(), l_values.value().get( 1 ).<Number>raw() );
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
            new CParserAgent( new CActionStaticGenerator( Stream.of( l_values ) ), ILambdaStreamingGenerator.EMPTY ),
            "+!actiontest <- X=5; .push/value(X, [1,2,3], 'test data', inner('foobar'), 555, fail)."
        );

        final IVariable<?> l_xvar = new CVariable<>( "X" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_xvar
            ),
            l_plan.toString()
        );

        Assertions.assertEquals( 5.0, l_xvar.<Number>raw() );
        Assertions.assertEquals( 6, l_values.value().size() );

        Assertions.assertEquals( 5.0, l_values.value().get( 0 ).<Number>raw() );
        Assertions.assertArrayEquals( Stream.of( 1.0, 2.0, 3.0 ).toArray(), l_values.value().get( 1 ).<List<?>>raw().toArray() );
        Assertions.assertEquals( "test data", l_values.value().get( 2 ).raw() );
        Assertions.assertEquals( CLiteral.of( "inner", CRawTerm.of( "foobar" ) ), l_values.value().get( 3 ).raw() );
        Assertions.assertEquals( 555.0, l_values.value().get( 4 ).<Number>raw() );
        Assertions.assertEquals( false, l_values.value().get( 5 ).raw() );
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!mainsuccess <- success. +!mainfail <- fail."
        ).collect( Collectors.toMap( i -> i.trigger().literal(), i -> i ) );

        Assertions.assertEquals( 2, l_plans.size() );

        Assertions.assertTrue(
            execute(
                l_plans.get( CLiteral.of( "mainsuccess" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            ),
            l_plans.get( CLiteral.of( "mainsuccess" ) ).toString()
        );

        Assertions.assertFalse(
            execute(
                l_plans.get( CLiteral.of( "mainfail" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            ),
            l_plans.get( CLiteral.of( "mainfail" ) ).toString()
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!mainsuccess <- fail << fail << success. +!mainfail <- fail << fail."
        ).collect( Collectors.toMap( i -> i.trigger().literal(), i -> i ) );

        Assertions.assertEquals( 2, l_plans.size() );

        Assertions.assertTrue(
            execute(
                l_plans.get( CLiteral.of( "mainsuccess" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            ),
            l_plans.get( CLiteral.of( "mainsuccess" ) ).toString()
        );

        Assertions.assertFalse(
            execute(
                l_plans.get( CLiteral.of( "mainfail" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            ),
            l_plans.get( CLiteral.of( "mainfail" ) ).toString()
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!mainsuccess <- [X|Y] =.. foo(123)."
        );

        final IVariable<?> l_xvar = new CVariable<>( "X" );
        final IVariable<?> l_yvar = new CVariable<>( "Y" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_xvar,
                l_yvar
            ),
            l_plan.toString()
        );

        Assertions.assertEquals( "foo", l_xvar.raw() );
        Assertions.assertTrue( l_yvar.raw() instanceof List<?> && l_yvar.<List<?>>raw().size() == 1, l_yvar.toString() );
        Assertions.assertEquals( 123.0, l_yvar.<List<?>>raw().get( 0 ) );
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- R = 3 * 2 + 5 + 1 - 2 * ( 3 + 1 ) + 2 * 2 ** 3."
        );

        final IVariable<?> l_resultvar = new CVariable<>( "R" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_resultvar
            ),
            l_plan.toString()
        );

        Assertions.assertEquals( 20.0, l_resultvar.<Number>raw() );
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- W = A * 2 - B * ( 3 + C ) + 2 ** D."
        );

        final Random l_random = new Random();

        final IVariable<?> l_avar = new CVariable<>( "A" ).set( l_random.nextDouble() );
        final IVariable<?> l_bvar = new CVariable<>( "B" ).set( l_random.nextInt( 40 ) );
        final IVariable<?> l_cvar = new CVariable<>( "C" ).set( l_random.nextInt( 30 ) );
        final IVariable<?> l_dvar = new CVariable<>( "D" ).set( l_random.nextInt( 20 ) );
        final IVariable<?> l_result = new CVariable<>( "W" ).set( l_random.nextInt( 10 ) );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result,
                l_avar,
                l_bvar,
                l_cvar,
                l_dvar
            ),
            l_plan.toString()
        );

        Assertions.assertEquals(
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- O = pi * euler + gravity."
        );

        final IVariable<?> l_result = new CVariable<>( "O" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result
            ),
            l_plan.toString()
        );

        Assertions.assertEquals(
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
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

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_andtrue,
                l_andfalse,
                l_ortrue,
                l_orfalse,
                l_xortrue,
                l_xorfalse,
                l_notfalse,
                l_nottrue
            ),
            l_plan.toString()
        );

        Assertions.assertTrue( l_andtrue.<Boolean>raw(), l_plan.toString() );
        Assertions.assertFalse( l_andfalse.<Boolean>raw(), l_plan.toString() );

        Assertions.assertTrue( l_ortrue.<Boolean>raw(), l_plan.toString() );
        Assertions.assertFalse( l_orfalse.<Boolean>raw(), l_plan.toString() );

        Assertions.assertTrue( l_xortrue.<Boolean>raw(), l_plan.toString() );
        Assertions.assertFalse( l_xorfalse.<Boolean>raw(), l_plan.toString() );

        Assertions.assertTrue( l_nottrue.<Boolean>raw(), l_plan.toString() );
        Assertions.assertFalse( l_notfalse.<Boolean>raw(), l_plan.toString() );
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
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- R = false || false and not ( true and false ) xor ( 2 < 3 )."
        );

        final IVariable<?> l_result = new CVariable<>( "R" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result
            ),
            l_plan.toString()
        );

        //Checkstyle:OFF:SimplifyBooleanExpression
        //noinspection ConstantConditions
        Assertions.assertEquals( false || false && !( true && false ) ^ ( 2 < 3 ), l_result.raw() );
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- P = 3 < 5 ? euler : pi."
        );

        final IVariable<?> l_result = new CVariable<>( "P" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result
            ),
            l_plan.toString()
        );

        Assertions.assertEquals( CCommon.NUMERICCONSTANT.get( "euler" ), l_result.<Number>raw().doubleValue(), 0.00000001 );
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate(XVal, YVal) <- Res = XVal < YVal ? XVal + YVal - 2 : XVal + 3 - YVal + 4."
        );

        final IVariable<?> l_result = new CVariable<>( "Res" );
        final IVariable<?> l_xvar = new CVariable<>( "XVal" ).set( l_random.nextInt() );
        final IVariable<?> l_yvar = new CVariable<>( "YVal" ).set( l_random.nextInt() );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result,
                l_xvar,
                l_yvar
            ),
            l_plan.toString()
        );

        Assertions.assertEquals(
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- N = 5 < 3 ? boltzmann : lightspeed."
        );

        final IVariable<?> l_result = new CVariable<>( "N" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result
            ),
            l_plan.toString()
        );

        Assertions.assertEquals( CCommon.NUMERICCONSTANT.get( "lightspeed" ), l_result.<Number>raw().doubleValue(), 0.00000001 );
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!items <- N = 'test'; P = 5; C = fail."
        );

        final IVariable<?> l_nvar = new CVariable<>( "N" );
        final IVariable<?> l_pvar = new CVariable<>( "P" );
        final IVariable<?> l_cvar = new CVariable<>( "C" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_nvar,
                l_pvar,
                l_cvar
            ),
            l_plan.toString()
        );

        Assertions.assertEquals( "test", l_nvar.raw() );
        Assertions.assertEquals( 5.0, l_pvar.<Number>raw() );
        Assertions.assertEquals( false, l_cvar.raw() );
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "@description('a long plan description') +!description <- success."
        );

        Assertions.assertEquals( "a long plan description", l_plan.description() );
    }

    /**
     * test multiple plan execute
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void multipleplanexecution() throws Exception
    {
        final IPlan[] l_plans = parsemultipleplans(
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!multi(X) : X > 5 <- Y = X + 3 : X <= 5 <- Y = X * 3."
        ).toArray( IPlan[]::new );

        final IVariable<?> l_var = new CVariable<>( "Y" );

        Assertions.assertArrayEquals(
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
                                   .filter( j -> j.condition( i ) )
                                   .map( j -> execute( j, false, Collections.emptyList(), Collections.emptyList(), i ) )
                                   .filter( j -> j )
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "@parallel @atomic @constant(StringValue,'abcd') @constant(NumberValue,12345) @tag('foo')"
            + "@variable(X,'x value description') @tag('bar') @description('description text') +!annotation(X) <- success."
        );

        Assertions.assertEquals( "description text", property( "m_description", l_plan ) );

        Assertions.assertTrue( IBaseTest.<Boolean>property( "m_atomic", l_plan ), l_plan.toString() );
        Assertions.assertTrue( IBaseTest.<Boolean>property( "m_parallel", l_plan ), l_plan.toString() );

        Assertions.assertTrue(
            l_plan.variables().parallel().anyMatch( i -> ( "StringValue".equals( i.functor() ) ) && ( "abcd".equals( i.raw() ) ) ),
            l_plan.toString()
        );
        Assertions.assertTrue(
            l_plan.variables().parallel().anyMatch( i -> ( "NumberValue".equals( i.functor() ) ) && ( Double.valueOf( 12345 ).equals( i.raw() ) ) ),
            l_plan.toString()
        );

        Assertions.assertArrayEquals( Stream.of( "foo", "bar" ).toArray(), l_plan.tags().toArray() );

        Assertions.assertArrayEquals(
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
            new CParserAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!list <- L = ['a', 1, true]."
        );

        final IVariable<?> l_var = new CVariable<>( "L" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_var
            ),
            l_plan.toString()
        );

        Assertions.assertTrue( l_var.raw() instanceof List<?>, l_var.toString() );
        Assertions.assertEquals( 3, l_var.<List<?>>raw().size() );
        Assertions.assertEquals( "a", l_var.<List<?>>raw().get( 0 ) );
        Assertions.assertEquals( 1.0, l_var.<List<?>>raw().get( 1 ) );
        Assertions.assertEquals( true, l_var.<List<?>>raw().get( 2 ) );
    }

    /**
     * test lambda expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void lambdasingle() throws Exception
    {
        final CCollectValues l_values = new CCollectValues();

        final IPlan l_plan = parsesingleplan(
            new CParserAgent(
                new CActionStaticGenerator( Stream.of( l_values ) ),
                new CLambdaStreamingStaticGenerator( Stream.of( new CTestLambdaStreaming() ) )
            ),
            "+!lambda <- (L) -> I : .push/value(I)."
        );

        final IVariable<?> l_var = new CVariable<>( "L" ).set( Stream.of( 1, 2, 3, 4 ).collect( Collectors.toList() ) );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_var
            ),
            l_plan.toString()
        );

        Assertions.assertArrayEquals( Stream.of( 1, 2, 3, 4 ).toArray(), l_values.value().stream().map( ITerm::raw ).toArray() );
    }

    /**
     * test lambda expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void lambdablock() throws Exception
    {
        final CCollectValues l_values = new CCollectValues();

        final IPlan l_plan = parsesingleplan(
            new CParserAgent(
                new CActionStaticGenerator( Stream.of( l_values ) ),
                new CLambdaStreamingStaticGenerator( Stream.of( new CTestLambdaStreaming() ) )
            ),
            "+!lambda <- (L) -> I : { I *= 10; .push/value(I) }."
        );

        final IVariable<?> l_var = new CVariable<>( "L" ).set( Stream.of( 1, 2, 3, 4 ).collect( Collectors.toList() ) );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_var
            ),
            l_plan.toString()
        );

        Assertions.assertArrayEquals( Stream.of( 10D, 20D, 30D, 40D ).toArray(), l_values.value().stream().map( ITerm::raw ).toArray() );
    }


    /**
     * test of inner action execute
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void inneraction() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserAgent( new CActionStaticGenerator( Stream.of( new CTestMax(), new CTestMin() ) ), ILambdaStreamingGenerator.EMPTY ),
            "+!inner <- L = .test/max( .test/min( 3, 4, 1 ), -8, -6 ) ."
        );

        final IVariable<?> l_var = new CVariable<>( "L" );

        Assertions.assertTrue(
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_var
            ),
            l_plan.toString()
        );

        Assertions.assertEquals( 1.0, l_var.<Number>raw() );
    }

    /**
     * lambda streaming
     */
    private static final class CTestLambdaStreaming implements ILambdaStreaming<Collection<?>>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -8718134128784899343L;

        @NonNull
        @Override
        public Stream<Class<?>> assignable()
        {
            return Stream.of( Collection.class );
        }

        @Override
        public Stream<?> apply( final Collection<?> p_objects )
        {
            return p_objects.stream();
        }
    }
}
