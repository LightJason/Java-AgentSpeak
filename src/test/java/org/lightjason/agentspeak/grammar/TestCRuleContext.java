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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;


/**
 * test agent parser rule context
 */
@RunWith( DataProviderRunner.class )
public final class TestCRuleContext extends IBaseGrammarTest
{
    /**
     * classes for null-returning rules
     *
     * @return pair of rule-context class and rule method name
     */
    @DataProvider
    public static Object[] manualnullrules()
    {
        return Stream.of(
            generaterule( ManualParser.BeliefContext.class, "visitBelief" ),
            generaterule( ManualParser.PlanContext.class, "visitPlan" ),
            generaterule( ManualParser.PlantriggerContext.class, "visitPlantrigger" ),
            generaterule( ManualParser.PlandefinitionContext.class, "visitPlandefinition" ),
            generaterule( ManualParser.LogicruleContext.class, "visitLogicrule" ),
            generaterule( ManualParser.BodyContext.class, "visitBody" ),
            generaterule( ManualParser.BlockformulaContext.class, "visitBlockformula" ),
            generaterule( ManualParser.ExpressionContext.class, "visitExpression" ),
            generaterule( ManualParser.RepairformulaContext.class, "visitRepairformula" ),
            generaterule( ManualParser.BodyformulaContext.class, "visitBodyformula" ),
            generaterule( ManualParser.BeliefactionContext.class, "visitBeliefaction" ),
            generaterule( ManualParser.TestactionContext.class, "visitTestaction" ),
            generaterule( ManualParser.AchievementgoalContext.class, "visitAchievementgoal" ),
            generaterule( ManualParser.DeconstructexpressionContext.class, "visitDeconstructexpression" ),
            generaterule( ManualParser.AssignmentexpressionContext.class, "visitAssignmentexpression" ),
            generaterule( ManualParser.AssignmentexpressionsinglevariableContext.class, "visitAssignmentexpressionsinglevariable" ),
            generaterule( ManualParser.AssignmentexpressionmultivariableContext.class, "visitAssignmentexpressionmultivariable" ),
            generaterule( ManualParser.UnaryexpressionContext.class, "visitUnaryexpression" ),
            generaterule( ManualParser.TernaryoperationContext.class, "visitTernaryoperation" ),
            generaterule( ManualParser.TernaryoperationtrueContext.class, "visitTernaryoperationtrue" ),
            generaterule( ManualParser.TernaryoperationfalseContext.class, "visitTernaryoperationfalse" ),
            generaterule( ManualParser.UnificationContext.class, "visitUnification" ),
            generaterule( ManualParser.UnificationconstraintContext.class, "visitUnificationconstraint" ),
            generaterule( ManualParser.LambdaContext.class, "visitLambda" ),
            generaterule( ManualParser.LambdastreamContext.class, "visitLambdastream" ),
            generaterule( ManualParser.LambdaelementContext.class, "visitLambdaelement" ),
            generaterule( ManualParser.LambdareturnContext.class, "visitLambdareturn" ),
            generaterule( ManualParser.ExecuteruleContext.class, "visitExecuterule" ),
            generaterule( ManualParser.ExecutevariableContext.class, "visitExecutevariable" ),
            generaterule( ManualParser.ExecuteactionContext.class, "visitExecuteaction" )

        ).toArray();
    }

    /**
     * generate pair of rule context class and checking method name
     *
     * @param p_ruleclass rule class
     * @param p_visitormethod visitor rule name
     * @return pair of class and method name
     */
    private static Pair<Class<?>, String> generaterule( @NonNull final Class<?> p_ruleclass, @Nonnull final String p_visitormethod )
    {
        return new ImmutablePair<>( p_ruleclass, p_visitormethod );
    }

    /**
     * test manual rules which return null
     *
     * @param p_rule pair of rule class and string name
     *
     * @throws NoSuchMethodException visitor method no exists
     * @throws InvocationTargetException invocation error
     * @throws IllegalAccessException access error
     */
    @Test
    @UseDataProvider( "manualnullrules" )
    public void manualnullreturnrules( @Nonnull final Pair<Class<?>, String> p_rule ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        final ManualVisitor<Object> l_visitor = new CASTVisitorManual();

        Assert.assertNull(
            p_rule.getLeft().toString(),
            l_visitor.getClass().getMethod(
                p_rule.getRight(),
                p_rule.getLeft()
            ).invoke(
                    l_visitor,
                    new Object[]{null}
            )
        );
    }

    /**
     * test manual literal rule
     */
    @Test
    public void manualliteral()
    {
        final Object l_literal = new CASTVisitorManual().visitLiteral( new CManualRuleParser().parser( "foo" ).literal() );
        Assert.assertTrue( l_literal instanceof ILiteral );
        Assert.assertEquals( "foo[]", l_literal.toString() );
    }

    /**
     * test agent bodyformula rules
     */
    @Test
    public void agentbodyformular()
    {
        final IASTVisitorAgent l_visitor = new CASTVisitorAgent( null, null );

        final Object l_unify = l_visitor.visitBodyformula( new CAgentRuleParser().parser( ">>foo(X)" ).bodyformula() );
        Assert.assertTrue( l_unify instanceof IExecution );
        Assert.assertEquals( ">>foo[X()]", l_unify.toString() );

        final Object l_ternary = l_visitor.visitBodyformula( new CAgentRuleParser().parser( "X = Y > 3 ? 'foo' : 'bar'" ).bodyformula() );
        Assert.assertTrue( l_ternary instanceof IExecution );
        Assert.assertEquals( "X() = ( Y() > 3.0 ) ? foo : bar", l_ternary.toString() );
    }

    /**
     * test plan-bundle bodyformula rules
     */
    @Test
    public void planbundlebodyformular()
    {
        final IASTVisitorPlanBundle l_visitor = new CASTVisitorPlanBundle( null, null );

        final Object l_unify = l_visitor.visitBodyformula( new CPlanBundleRuleParser().parser( ">>foo(X)" ).bodyformula() );
        Assert.assertTrue( l_unify instanceof IExecution );
        Assert.assertEquals( ">>foo[X()]", l_unify.toString() );

        final Object l_ternary = l_visitor.visitBodyformula( new CPlanBundleRuleParser().parser( "X = Y > 3 ? 'foo' : 'bar'" ).bodyformula() );
        Assert.assertTrue( l_ternary instanceof IExecution );
        Assert.assertEquals( "X() = ( Y() > 3.0 ) ? foo : bar", l_ternary.toString() );
    }

    /**
     * test agent test-action rule
     */
    @Test
    public void agenttestaction()
    {
        final Object l_testaction = new CASTVisitorAgent( null, null )
                                    .visitTestaction( new CAgentRuleParser().parser( "?foo" ).testaction() );
        Assert.assertTrue( l_testaction instanceof IExecution );
        Assert.assertEquals( "?foo", l_testaction.toString() );
    }

    /**
     * test plan-bundle test-action rule
     */
    @Test
    public void planbundletestaction()
    {
        final Object l_testaction = new CASTVisitorPlanBundle( null, null )
                                    .visitTestaction( new CPlanBundleRuleParser().parser( "?foo" ).testaction() );
        Assert.assertTrue( l_testaction instanceof IExecution );
        Assert.assertEquals( "?foo", l_testaction.toString() );
    }

    /**
     * test plan-bundle variable list rule
     */
    @Test
    @SuppressWarnings( "unchecked" )
    public void planbundlevariablelist()
    {
        final Stream<IVariable<?>> l_list = (Stream<IVariable<?>>)new CASTVisitorPlanBundle( null, null )
                                            .visitVariablelist( new CPlanBundleRuleParser().parser( "[X|Y|Z]" ).variablelist() );
        Assert.assertNotNull( l_list );
        Assert.assertArrayEquals(
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
        final Object l_unify = new CASTVisitorPlanBundle( null, null )
                               .visitUnification( new CPlanBundleRuleParser().parser( ">>bar(X)" ).unification() );
        Assert.assertTrue( l_unify instanceof IExecution );
        Assert.assertEquals( ">>bar[X()]", l_unify.toString() );
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
