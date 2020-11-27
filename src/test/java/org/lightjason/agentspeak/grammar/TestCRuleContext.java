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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;


/**
 * test agent parser rule context
 */
public final class TestCRuleContext extends IBaseGrammarTest
{
    /**
     * classes for null-returning rules
     *
     * @return pair of rule-context class and rule method name
     */
    public static Stream<Arguments> manualnullrules()
    {
        return Stream.of(
            Arguments.of( ManualParser.BeliefContext.class, "visitBelief" ),
            Arguments.of( ManualParser.PlanContext.class, "visitPlan" ),
            Arguments.of( ManualParser.PlantriggerContext.class, "visitPlantrigger" ),
            Arguments.of( ManualParser.PlandefinitionContext.class, "visitPlandefinition" ),
            Arguments.of( ManualParser.LogicruleContext.class, "visitLogicrule" ),
            Arguments.of( ManualParser.BodyContext.class, "visitBody" ),
            Arguments.of( ManualParser.BlockformulaContext.class, "visitBlockformula" ),
            Arguments.of( ManualParser.ExpressionContext.class, "visitExpression" ),
            Arguments.of( ManualParser.RepairformulaContext.class, "visitRepairformula" ),
            Arguments.of( ManualParser.BodyformulaContext.class, "visitBodyformula" ),
            Arguments.of( ManualParser.BeliefactionContext.class, "visitBeliefaction" ),
            Arguments.of( ManualParser.TestactionContext.class, "visitTestaction" ),
            Arguments.of( ManualParser.AchievementgoalContext.class, "visitAchievementgoal" ),
            Arguments.of( ManualParser.DeconstructexpressionContext.class, "visitDeconstructexpression" ),
            Arguments.of( ManualParser.AssignmentexpressionContext.class, "visitAssignmentexpression" ),
            Arguments.of( ManualParser.AssignmentexpressionsinglevariableContext.class, "visitAssignmentexpressionsinglevariable" ),
            Arguments.of( ManualParser.AssignmentexpressionmultivariableContext.class, "visitAssignmentexpressionmultivariable" ),
            Arguments.of( ManualParser.UnaryexpressionContext.class, "visitUnaryexpression" ),
            Arguments.of( ManualParser.TernaryoperationContext.class, "visitTernaryoperation" ),
            Arguments.of( ManualParser.TernaryoperationtrueContext.class, "visitTernaryoperationtrue" ),
            Arguments.of( ManualParser.TernaryoperationfalseContext.class, "visitTernaryoperationfalse" ),
            Arguments.of( ManualParser.UnificationContext.class, "visitUnification" ),
            Arguments.of( ManualParser.UnificationconstraintContext.class, "visitUnificationconstraint" ),
            Arguments.of( ManualParser.LambdaContext.class, "visitLambda" ),
            Arguments.of( ManualParser.LambdastreamContext.class, "visitLambdastream" ),
            Arguments.of( ManualParser.LambdaelementContext.class, "visitLambdaelement" ),
            Arguments.of( ManualParser.LambdareturnContext.class, "visitLambdareturn" ),
            Arguments.of( ManualParser.ExecuteruleContext.class, "visitExecuterule" ),
            Arguments.of( ManualParser.ExecutevariableContext.class, "visitExecutevariable" ),
            Arguments.of( ManualParser.ExecuteactionContext.class, "visitExecuteaction" )

        );
    }

    /**
     * test manual rules which return null
     *
     * @param p_ruleclass rule class
     * @param p_visitormethod visitor rule name
     *
     * @throws NoSuchMethodException visitor method no exists
     * @throws InvocationTargetException invocation error
     * @throws IllegalAccessException access error
     */
    @ParameterizedTest
    @MethodSource( "manualnullrules" )
    public void manualnullreturnrules( @NonNull final Class<?> p_ruleclass, @Nonnull final String p_visitormethod )
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        final ManualVisitor<Object> l_visitor = new CASTVisitorManual();

        Assertions.assertNull(
            l_visitor.getClass().getMethod(
                p_visitormethod,
                p_ruleclass
            ).invoke(
                    l_visitor,
                    new Object[]{null}
            ),
            p_ruleclass.toString()
        );
    }

    /**
     * test manual literal rule
     */
    @Test
    public void manualliteral()
    {
        final Object l_literal = new CASTVisitorManual().visitLiteral( new CManualRuleParser().parser( "foo" ).literal() );
        Assertions.assertTrue( l_literal instanceof ILiteral );
        Assertions.assertEquals( "foo[]", l_literal.toString() );
    }

    /**
     * test agent bodyformula rules
     */
    @Test
    public void agentbodyformular()
    {
        final IASTVisitorAgent l_visitor = new CASTVisitorAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY );

        final Object l_unify = l_visitor.visitBodyformula( new CAgentRuleParser().parser( ">>foo(X)" ).bodyformula() );
        Assertions.assertTrue( l_unify instanceof IExecution );
        Assertions.assertEquals( ">>foo[X()]", l_unify.toString() );

        final Object l_ternary = l_visitor.visitBodyformula( new CAgentRuleParser().parser( "X = Y > 3 ? 'foo' : 'bar'" ).bodyformula() );
        Assertions.assertTrue( l_ternary instanceof IExecution );
        Assertions.assertEquals( "X() = ( Y() > 3.0 ) ? foo : bar", l_ternary.toString() );
    }

    /**
     * test plan-bundle bodyformula rules
     */
    @Test
    public void planbundlebodyformular()
    {
        final IASTVisitorPlanBundle l_visitor = new CASTVisitorPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY );

        final Object l_unify = l_visitor.visitBodyformula( new CPlanBundleRuleParser().parser( ">>foo(X)" ).bodyformula() );
        Assertions.assertTrue( l_unify instanceof IExecution );
        Assertions.assertEquals( ">>foo[X()]", l_unify.toString() );

        final Object l_ternary = l_visitor.visitBodyformula( new CPlanBundleRuleParser().parser( "X = Y > 3 ? 'foo' : 'bar'" ).bodyformula() );
        Assertions.assertTrue( l_ternary instanceof IExecution );
        Assertions.assertEquals( "X() = ( Y() > 3.0 ) ? foo : bar", l_ternary.toString() );
    }

    /**
     * test agent test-action rule
     */
    @Test
    public void agenttestaction()
    {
        final Object l_testaction = new CASTVisitorAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
                                    .visitTestaction( new CAgentRuleParser().parser( "?foo" ).testaction() );
        Assertions.assertTrue( l_testaction instanceof IExecution );
        Assertions.assertEquals( "?foo", l_testaction.toString() );
    }

    /**
     * test plan-bundle test-action rule
     */
    @Test
    public void planbundletestaction()
    {
        final Object l_testaction = new CASTVisitorPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
                                    .visitTestaction( new CPlanBundleRuleParser().parser( "?foo" ).testaction() );
        Assertions.assertTrue( l_testaction instanceof IExecution );
        Assertions.assertEquals( "?foo", l_testaction.toString() );
    }

    /**
     * test plan-bundle variable list rule
     */
    @Test
    @SuppressWarnings( "unchecked" )
    public void planbundlevariablelist()
    {
        final Stream<IVariable<?>> l_list = (Stream<IVariable<?>>)new CASTVisitorPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
                                            .visitVariablelist( new CPlanBundleRuleParser().parser( "[X|Y|Z]" ).variablelist() );
        Assertions.assertNotNull( l_list );
        Assertions.assertArrayEquals(
            Stream.of( "X()", "Y()", "Z()" ).toArray(),
            l_list.map( Object::toString ).toArray()
        );
    }

    /**
     * test plan-bundle unification rule
     */
    @Test
    public void planbundleunification()
    {
        final Object l_unify = new CASTVisitorPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
                               .visitUnification( new CPlanBundleRuleParser().parser( ">>bar(X)" ).unification() );
        Assertions.assertTrue( l_unify instanceof IExecution );
        Assertions.assertEquals( ">>bar[X()]", l_unify.toString() );
    }

    /**
     * test agent action-execution rule
     */
    @Test
    public void agentexecutionactionwrongarguments()
    {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new CASTVisitorAgent( new CActionStaticGenerator( Stream.of( new IAction()
                {
                    @Override
                    public int minimalArgumentNumber()
                    {
                        return 5;
                    }

                    @Nonnull
                    @Override
                    public IPath name()
                    {
                        return CPath.of( "bar" );
                    }

                    @Nonnull
                    @Override
                    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                           @Nonnull final List<ITerm> p_return
                    )
                    {
                        return Stream.empty();
                    }
                } ) ), ILambdaStreamingGenerator.EMPTY ).visitExecuteaction( new CAgentRuleParser().parser( ".bar" ).executeaction() )
        );
    }

    /**
     * test agent action-execution rule
     */
    @Test
    public void agentexecuteactionnotexist()
    {
        Assertions.assertThrows(
            NoSuchElementException.class,
            () -> new CASTVisitorAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
                .visitExecuteaction( new CAgentRuleParser().parser( ".bar(123)" ).executeaction() )
        );
    }

    /**
     * test agent unification constraint
     */
    @Test
    public void agentunificationconstraint()
    {
        final Object l_unifyexpression = new CASTVisitorAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
            .visitUnificationconstraint( new CAgentRuleParser().parser( ">>(foo(X), X > 1000)" ).unificationconstraint() );

        Assertions.assertTrue( l_unifyexpression instanceof IExecution );

        final Object l_unifyliteral = new CASTVisitorAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
            .visitUnificationconstraint( new CAgentRuleParser().parser( ">>( foo(X), foo(bar(5)) )" ).unificationconstraint() );

        Assertions.assertTrue( l_unifyliteral instanceof IExecution );
    }

    /**
     * test agent lambda-element with range
     */
    @Test
    public void agentlambdaelementrange()
    {
        final IExecution l_range = (IExecution) new CASTVisitorAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
            .visitLambdastream( new CAgentRuleParser().parser( "(#1,5)" ).lambdastream() );

        final List<ITerm> l_return = new ArrayList<>();
        l_range.execute( false, IContext.EMPTYPLAN, Collections.emptyList(), l_return )
               .forEach( i ->
               {
               } );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertArrayEquals(
            Stream.of( 1L, 2L, 3L, 4L ).toArray(),
            l_return.get( 0 ).<Stream<?>>raw().toArray()
        );
    }

    /**
     * test agent lambda-element with range variable
     */
    @Test
    public void agentlambdaelementvariable()
    {
        final List<ITerm> l_return = new ArrayList<>();
        execute(
            (IExecution) new CASTVisitorAgent( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
                .visitLambdastream( new CAgentRuleParser().parser( "(#A,B)" ).lambdastream() ),
            false,
            Collections.emptyList(),
            l_return,
            new CVariable<>( "A", 5 ),
            new CVariable<>( "B", 10 )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertArrayEquals(
            Stream.of( 5L, 6L, 7L, 8L, 9L ).toArray(),
            l_return.get( 0 ).<Stream<?>>raw().toArray()
        );
    }



    /**
     * manual rule testing parser
     */
    private static final class CManualRuleParser extends ITestParser<IASTVisitorManual, ManualLexer, ManualParser>
    {
        @Override
        protected Class<ManualLexer> lexerclass()
        {
            return ManualLexer.class;
        }

        @Override
        protected Class<ManualParser> parserclass()
        {
            return ManualParser.class;
        }
    }

    /**
     * agent rule testing parser
     */
    private static final class CAgentRuleParser extends ITestParser<IASTVisitorAgent, AgentLexer, AgentParser>
    {

        @Override
        protected Class<AgentLexer> lexerclass()
        {
            return AgentLexer.class;
        }

        @Override
        protected Class<AgentParser> parserclass()
        {
            return AgentParser.class;
        }
    }

    /**
     * plan-bundle rule testing parser
     */
    private static final class CPlanBundleRuleParser extends ITestParser<IASTVisitorPlanBundle, PlanBundleLexer, PlanBundleParser>
    {

        @Override
        protected Class<PlanBundleLexer> lexerclass()
        {
            return PlanBundleLexer.class;
        }

        @Override
        protected Class<PlanBundleParser> parserclass()
        {
            return PlanBundleParser.class;
        }
    }
}
