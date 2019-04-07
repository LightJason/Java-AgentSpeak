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
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;


/**
 * test agent parser rule context
 */
@RunWith( DataProviderRunner.class )
public final class TestCManualRuleContext extends IBaseTest
{
    /**
     * classes for null-returning rules
     *
     * @return pair of rule-context class and rule method name
     */
    @DataProvider
    public static Object[] nullrules()
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
            generaterule( ManualParser.LambdareturnContext.class, "visitLambdareturn" )
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
     * test rules which return null
     *
     * @param p_rule pair of rule class and string name
     *
     * @throws NoSuchMethodException visitor method no exists
     * @throws InvocationTargetException invocation error
     * @throws IllegalAccessException access error
     */
    @Test
    @UseDataProvider( "nullrules" )
    public void nullreturnrules( @Nonnull final Pair<Class<?>, String> p_rule ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
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

}
