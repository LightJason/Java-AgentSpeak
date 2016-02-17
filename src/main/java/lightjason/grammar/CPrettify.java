/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.grammar;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;


/**
 * default abstract-syntax-tree (AST) visitor for plan-bundles and agent script prettifier
 *
 * @note methods are implemented twice agent and plan-bundle, because both use equal
 * AgentSpeak(L) grammer, but AntLR visitor does not support inheritance by the grammar definition
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public class CPrettify extends AbstractParseTreeVisitor<Object> implements IASTPrettifyAgent, IASTPrettifyPlanBundle
{

    @Override
    public Object visitAgent( final AgentParser.AgentContext p_context )
    {
        return null;
    }

    @Override
    public Object visitInitial_beliefs( final AgentParser.Initial_beliefsContext p_context )
    {
        return null;
    }

    @Override
    public Object visitInitial_goal( final AgentParser.Initial_goalContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBelief( final AgentParser.BeliefContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlans( final AgentParser.PlansContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLogicrules( final AgentParser.LogicrulesContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan( final AgentParser.PlanContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlandefinition( final AgentParser.PlandefinitionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLogicrule( final AgentParser.LogicruleContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotations( final AgentParser.AnnotationsContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotation_atom( final AgentParser.Annotation_atomContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotation_literal( final AgentParser.Annotation_literalContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotation_numeric_literal( final AgentParser.Annotation_numeric_literalContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotation_symbolic_literal( final AgentParser.Annotation_symbolic_literalContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan_trigger( final AgentParser.Plan_triggerContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan_goal_trigger( final AgentParser.Plan_goal_triggerContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan_belief_trigger( final AgentParser.Plan_belief_triggerContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBody( final AgentParser.BodyContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBody_formula( final AgentParser.Body_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitRepair_formula( final AgentParser.Repair_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTerm( final AgentParser.TermContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBelief_action( final AgentParser.Belief_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTest_goal_action( final AgentParser.Test_goal_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAchievement_goal_action( final AgentParser.Achievement_goal_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation( final AgentParser.Ternary_operationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation_true( final AgentParser.Ternary_operation_trueContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation_false( final AgentParser.Ternary_operation_falseContext p_context )
    {
        return null;
    }

    @Override
    public Object visitDeconstruct_expression( final AgentParser.Deconstruct_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBarrier( final AgentParser.BarrierContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnification( final AgentParser.UnificationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression( final AgentParser.Assignment_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression_singlevariable( final AgentParser.Assignment_expression_singlevariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression_multivariable( final AgentParser.Assignment_expression_multivariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnary_expression( final AgentParser.Unary_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression( final AgentParser.ExpressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_bracket( final AgentParser.Expression_bracketContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_logical_and( final AgentParser.Expression_logical_andContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_logical_xor( final AgentParser.Expression_logical_xorContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_logical_element( final AgentParser.Expression_logical_elementContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_logical_negation( final AgentParser.Expression_logical_negationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric( final AgentParser.Expression_numericContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_relation( final AgentParser.Expression_numeric_relationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_additive( final AgentParser.Expression_numeric_additiveContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_multiplicative( final AgentParser.Expression_numeric_multiplicativeContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_power( final AgentParser.Expression_numeric_powerContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_element( final AgentParser.Expression_numeric_elementContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBlock_formula( final AgentParser.Block_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda( final AgentParser.LambdaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda_initialization( final AgentParser.Lambda_initializationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda_return( final AgentParser.Lambda_returnContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLiteral( final AgentParser.LiteralContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTermlist( final AgentParser.TermlistContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLiteralset( final AgentParser.LiteralsetContext p_context )
    {
        return null;
    }

    @Override
    public Object visitVariablelist( final AgentParser.VariablelistContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAtom( final AgentParser.AtomContext p_context )
    {
        return null;
    }

    @Override
    public Object visitVariable( final AgentParser.VariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnaryoperator( final AgentParser.UnaryoperatorContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBinaryoperator( final AgentParser.BinaryoperatorContext p_context )
    {
        return null;
    }

    @Override
    public Object visitNumber( final AgentParser.NumberContext p_context )
    {
        return null;
    }

    @Override
    public Object visitFloatnumber( final AgentParser.FloatnumberContext p_context )
    {
        return null;
    }

    @Override
    public Object visitIntegernumber( final AgentParser.IntegernumberContext p_context )
    {
        return null;
    }

    @Override
    public Object visitIntegernumber_positive( final AgentParser.Integernumber_positiveContext p_context )
    {
        return null;
    }

    @Override
    public Object visitIntegernumber_negative( final AgentParser.Integernumber_negativeContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLogicalvalue( final AgentParser.LogicalvalueContext p_context )
    {
        return null;
    }

    @Override
    public Object visitConstant( final AgentParser.ConstantContext p_context )
    {
        return null;
    }

    @Override
    public Object visitString( final AgentParser.StringContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlanbundle( final PlanBundleParser.PlanbundleContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBelief( final PlanBundleParser.BeliefContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlans( final PlanBundleParser.PlansContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLogicrules( final PlanBundleParser.LogicrulesContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan( final PlanBundleParser.PlanContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlandefinition( final PlanBundleParser.PlandefinitionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLogicrule( final PlanBundleParser.LogicruleContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotations( final PlanBundleParser.AnnotationsContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotation_atom( final PlanBundleParser.Annotation_atomContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotation_literal( final PlanBundleParser.Annotation_literalContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotation_numeric_literal( final PlanBundleParser.Annotation_numeric_literalContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAnnotation_symbolic_literal( final PlanBundleParser.Annotation_symbolic_literalContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan_trigger( final PlanBundleParser.Plan_triggerContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan_goal_trigger( final PlanBundleParser.Plan_goal_triggerContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan_belief_trigger( final PlanBundleParser.Plan_belief_triggerContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBody( final PlanBundleParser.BodyContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBody_formula( final PlanBundleParser.Body_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitRepair_formula( final PlanBundleParser.Repair_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTerm( final PlanBundleParser.TermContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBelief_action( final PlanBundleParser.Belief_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTest_goal_action( final PlanBundleParser.Test_goal_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAchievement_goal_action( final PlanBundleParser.Achievement_goal_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation( final PlanBundleParser.Ternary_operationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation_true( final PlanBundleParser.Ternary_operation_trueContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation_false( final PlanBundleParser.Ternary_operation_falseContext p_context )
    {
        return null;
    }

    @Override
    public Object visitDeconstruct_expression( final PlanBundleParser.Deconstruct_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBarrier( final PlanBundleParser.BarrierContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnification( final PlanBundleParser.UnificationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression( final PlanBundleParser.Assignment_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression_singlevariable( final PlanBundleParser.Assignment_expression_singlevariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression_multivariable( final PlanBundleParser.Assignment_expression_multivariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnary_expression( final PlanBundleParser.Unary_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression( final PlanBundleParser.ExpressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_bracket( final PlanBundleParser.Expression_bracketContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_logical_and( final PlanBundleParser.Expression_logical_andContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_logical_xor( final PlanBundleParser.Expression_logical_xorContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_logical_element( final PlanBundleParser.Expression_logical_elementContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_logical_negation( final PlanBundleParser.Expression_logical_negationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric( final PlanBundleParser.Expression_numericContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_relation( final PlanBundleParser.Expression_numeric_relationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_additive( final PlanBundleParser.Expression_numeric_additiveContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_multiplicative( final PlanBundleParser.Expression_numeric_multiplicativeContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_power( final PlanBundleParser.Expression_numeric_powerContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression_numeric_element( final PlanBundleParser.Expression_numeric_elementContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBlock_formula( final PlanBundleParser.Block_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda( final PlanBundleParser.LambdaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda_initialization( final PlanBundleParser.Lambda_initializationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda_return( final PlanBundleParser.Lambda_returnContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLiteral( final PlanBundleParser.LiteralContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTermlist( final PlanBundleParser.TermlistContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLiteralset( final PlanBundleParser.LiteralsetContext p_context )
    {
        return null;
    }

    @Override
    public Object visitVariablelist( final PlanBundleParser.VariablelistContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAtom( final PlanBundleParser.AtomContext p_context )
    {
        return null;
    }

    @Override
    public Object visitVariable( final PlanBundleParser.VariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnaryoperator( final PlanBundleParser.UnaryoperatorContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBinaryoperator( final PlanBundleParser.BinaryoperatorContext p_context )
    {
        return null;
    }

    @Override
    public Object visitNumber( final PlanBundleParser.NumberContext p_context )
    {
        return null;
    }

    @Override
    public Object visitFloatnumber( final PlanBundleParser.FloatnumberContext p_context )
    {
        return null;
    }

    @Override
    public Object visitIntegernumber( final PlanBundleParser.IntegernumberContext p_context )
    {
        return null;
    }

    @Override
    public Object visitIntegernumber_positive( final PlanBundleParser.Integernumber_positiveContext p_context )
    {
        return null;
    }

    @Override
    public Object visitIntegernumber_negative( final PlanBundleParser.Integernumber_negativeContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLogicalvalue( final PlanBundleParser.LogicalvalueContext p_context )
    {
        return null;
    }

    @Override
    public Object visitConstant( final PlanBundleParser.ConstantContext p_context )
    {
        return null;
    }

    @Override
    public Object visitString( final PlanBundleParser.StringContext p_context )
    {
        return null;
    }

}
