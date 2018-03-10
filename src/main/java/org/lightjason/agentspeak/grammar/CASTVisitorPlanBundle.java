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

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.grammar.builder.CAgentSpeak;
import org.lightjason.agentspeak.grammar.builder.CTerm;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.action.CBeliefAction;
import org.lightjason.agentspeak.language.execution.action.CDeconstruct;
import org.lightjason.agentspeak.language.execution.action.CLambdaExpression;
import org.lightjason.agentspeak.language.execution.action.CMultiAssignment;
import org.lightjason.agentspeak.language.execution.action.CProxyAction;
import org.lightjason.agentspeak.language.execution.action.CRawAction;
import org.lightjason.agentspeak.language.execution.action.CRepair;
import org.lightjason.agentspeak.language.execution.action.CSingleAssignment;
import org.lightjason.agentspeak.language.execution.action.CTernaryOperation;
import org.lightjason.agentspeak.language.execution.action.achievement_test.CAchievementGoalLiteral;
import org.lightjason.agentspeak.language.execution.action.achievement_test.CAchievementGoalVariable;
import org.lightjason.agentspeak.language.execution.action.unify.CDefaultUnify;
import org.lightjason.agentspeak.language.execution.action.unify.CExpressionUnify;
import org.lightjason.agentspeak.language.execution.action.unify.CVariableUnify;
import org.lightjason.agentspeak.language.execution.expression.EOperator;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.expressionbinary.COperatorAssign;
import org.lightjason.agentspeak.language.execution.expressionunary.CDecrement;
import org.lightjason.agentspeak.language.execution.expressionunary.CIncrement;
import org.lightjason.agentspeak.language.instantiable.plan.CPlan;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.instantiable.rule.CRule;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.language.variable.IVariableEvaluate;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * default abstract-syntax-tree (AST) visitor for plan-bundles scripts
 *
 * @note methods are implemented twice agent and plan-bundle, because both use equal
 * AgentSpeak(L) grammer, but AntLR visitor does not support inheritance by the grammar definition
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public final class CASTVisitorPlanBundle extends AbstractParseTreeVisitor<Object> implements IASTVisitorPlanBundle
{
    /**
     * logger
     */
    private static final Logger LOGGER = CCommon.logger( IASTVisitorAgent.class );
    /**
     * set with initial beliefs
     */
    private final Set<ILiteral> m_initialbeliefs = new LinkedHashSet<>();
    /**
     * map with plans
     */
    private final Set<IPlan> m_plans = new HashSet<>();
    /**
     * map with logical rules
     */
    private final Multimap<IPath, IRule> m_rules = LinkedHashMultimap.create();
    /**
     * map with action definition
     */
    private final Map<IPath, IAction> m_actions;

    /**
     * ctor
     *
     * @param p_actions set with actions
     */
    public CASTVisitorPlanBundle( @Nonnull final Set<IAction> p_actions )
    {
        m_actions = p_actions.stream().collect( Collectors.toMap( i -> i.name(), i -> i ) );
        LOGGER.info( MessageFormat.format( "create parser with actions & rules : {0} / {1}", m_actions.keySet(), m_rules.keySet() ) );
    }


    // --- plan bundle rules -----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitPlanbundle( final PlanBundleParser.PlanbundleContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    // --- AgentSpeak(L) rules ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitBelief( final PlanBundleParser.BeliefContext p_context )
    {
        if ( Objects.isNull( p_context.literal() ) )
            return null;

        m_initialbeliefs.add( (ILiteral) this.visitLiteral( p_context.literal() ) );
        return null;
    }

    @Override
    public final Object visitPlans( final PlanBundleParser.PlansContext p_context )
    {
        if ( Objects.isNull( p_context.plan() ) )
            return null;

        p_context.plan().stream().forEach( i -> ( (List<IPlan>) this.visitPlan( i ) ).stream().forEach( j -> m_plans.add( j ) ) );
        LOGGER.info( MessageFormat.format( "parsed plans: {0}", m_plans ) );
        return null;
    }

    @Override
    public final Object visitLogicrules( final PlanBundleParser.LogicrulesContext p_context )
    {
        // create placeholder objects first and run parsing again to build full-qualified rule objects
        p_context.logicrule().stream()
                 .map( i -> (IRule) this.visitLogicrulePlaceHolder( i ) )
                 .forEach( i -> m_rules.put( i.identifier().fqnfunctor(), i ) );

        final Multimap<IPath, IRule> l_rules = LinkedHashMultimap.create();
        p_context.logicrule().stream()
                 .flatMap( i -> ( (List<IRule>) this.visitLogicrule( i ) ).stream() )
                 .forEach( i -> l_rules.put( i.identifier().fqnfunctor(), i ) );

        // clear rule list and replace placeholder objects
        m_rules.clear();
        l_rules.values().stream()
               .map( i -> i.replaceplaceholder( l_rules ) )
               .forEach( i -> m_rules.put( i.identifier().fqnfunctor(), i ) );

        LOGGER.info( MessageFormat.format( "parsed rules: {0}", m_rules.values() ) );
        return null;
    }

    @Override
    public final Object visitLogicrule( final PlanBundleParser.LogicruleContext p_context )
    {
        final ILiteral l_literal = (ILiteral) this.visitLiteral( p_context.literal() );
        return p_context.logicalruledefinition().stream()
                        .map( i -> new CRule( (ILiteral) l_literal.deepcopy(), (List<IExecution>) this.visitLogicalruledefinition( i ) ) )
                        .collect( Collectors.toList() );
    }

    @Override
    public final Object visitPlan( final PlanBundleParser.PlanContext p_context )
    {
        final Set<IAnnotation<?>> l_annotation = (Set<IAnnotation<?>>) this.visitAnnotations( p_context.annotations() );
        final CTrigger l_trigger = new CTrigger(
            (ITrigger.EType) this.visitPlan_trigger( p_context.plan_trigger() ),
            (ILiteral) this.visitLiteral( p_context.literal() )
        );

        return p_context.plandefinition()
                        .stream()
                        .map( i ->
                        {
                            final Pair<IExpression, List<IExecution>> l_content = (Pair<IExpression, List<IExecution>>) this.visitPlandefinition( i );
                            return new CPlan( l_trigger, l_content.getLeft(), l_content.getRight(), l_annotation );
                        } )
                        .collect( Collectors.toList() );
    }

    @Override
    public final Object visitBody( final PlanBundleParser.BodyContext p_context )
    {
        // filter null values of the body formular, because blank lines adds a null value, body-formula rule return an executable call everytime
        return p_context.body_formula().stream()
                        .filter( i -> Objects.nonNull( i ) )
                        .map( i -> this.visitBody_formula( i ) )
                        .filter( i -> i instanceof IExecution )
                        // expression are encapsulate to get result
                        .map( i -> i instanceof IExpression ? new CRawAction<>( i ) : i )
                        .collect( Collectors.toList() );
    }

    @Override
    public final Object visitBody_formula( final PlanBundleParser.Body_formulaContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitRepair_formula( final PlanBundleParser.Repair_formulaContext p_context )
    {
        // a non-existing repair formula can return any object-item, so convert it
        // to executable structure, because the grammar rule must return an executable item
        if ( Objects.isNull( p_context.repair_formula() ) )
            return this.visitChildren( p_context );


        // if there exists any repair element, build a sequential hierarchie of repair calls
        if ( Objects.nonNull( p_context.executable_term() ) )
            return new CRepair(
                (IExecution) this.visitExecutable_term( p_context.executable_term() ),
                (IExecution) this.visitRepair_formula( p_context.repair_formula() )
            );

        if ( Objects.nonNull( p_context.test_action() ) )
            return new CRepair(
                (IExecution) this.visitTest_action( p_context.test_action() ),
                (IExecution) this.visitRepair_formula( p_context.repair_formula() )
            );

        if ( Objects.nonNull( p_context.achievement_goal_action() ) )
            return new CRepair(
                (IExecution) this.visitAchievement_goal_action( p_context.achievement_goal_action() ),
                (IExecution) this.visitRepair_formula( p_context.repair_formula() )
            );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "repairelement", p_context.getText() ) );
    }

    @Override
    public final Object visitUnification( final PlanBundleParser.UnificationContext p_context )
    {
        final Object l_constraint = this.visitUnification_constraint( p_context.unification_constraint() );

        if ( l_constraint instanceof IExpression )
            return new CExpressionUnify(
                p_context.AT() != null,
                (ILiteral) this.visitLiteral( p_context.literal() ),
                (IExpression) l_constraint
            );

        if ( l_constraint instanceof IVariable<?> )
            return new CVariableUnify(
                p_context.AT() != null,
                (ILiteral) this.visitLiteral( p_context.literal() ),
                (IVariable<?>) l_constraint
            );

        return new CDefaultUnify( p_context.AT() != null, (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public final Object visitUnification_constraint( final PlanBundleParser.Unification_constraintContext p_context )
    {
        if ( Objects.isNull( p_context ) )
            return null;

        if ( Objects.nonNull( p_context.expression() ) )
            return this.visitExpression( p_context.expression() );

        if ( Objects.nonNull( p_context.variable() ) )
            return this.visitVariable( p_context.variable() );

        return null;
    }

    @Override
    public final Object visitBlock_formula( final PlanBundleParser.Block_formulaContext p_context )
    {
        if ( Objects.nonNull( p_context.body_formula() ) )
        {
            final LinkedList<IExecution> l_statement = new LinkedList<>();
            l_statement.add( (IExecution) this.visitBody_formula( p_context.body_formula() ) );
            return l_statement;
        }

        return this.visitBody( p_context.body() );
    }

    @Override
    public final Object visitLambda( final PlanBundleParser.LambdaContext p_context )
    {
        if ( Objects.nonNull( p_context.lambda_return() ) )
            return new CLambdaExpression(
                Objects.nonNull( p_context.AT() ),
                (IExecution) this.visitLambda_initialization( p_context.lambda_initialization() ),
                (IVariable<?>) this.visitVariable( p_context.variable() ),
                (IVariable<?>) this.visitLambda_return( p_context.lambda_return() ),
                (List<IExecution>) this.visitBlock_formula( p_context.block_formula() )
            );

        return new CLambdaExpression(
            Objects.nonNull( p_context.AT() ),
            (IExecution) this.visitLambda_initialization( p_context.lambda_initialization() ),
            (IVariable<?>) this.visitVariable( p_context.variable() ),
            (List<IExecution>) this.visitBlock_formula( p_context.block_formula() )
        );
    }

    @Override
    public final Object visitLambda_initialization( final PlanBundleParser.Lambda_initializationContext p_context )
    {
        if ( Objects.nonNull( p_context.variable() ) )
            return new CRawAction<>( this.visitVariable( p_context.variable() ) );

        if ( Objects.nonNull( p_context.literal() ) )
            return new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "lambdainitialization", p_context.getText() ) );
    }

    @Override
    public final Object visitLambda_return( final PlanBundleParser.Lambda_returnContext p_context )
    {
        return this.visitVariable( p_context.variable() );
    }

    @Override
    public final Object visitAssignment_expression( final PlanBundleParser.Assignment_expressionContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitAssignment_expression_singlevariable( final PlanBundleParser.Assignment_expression_singlevariableContext p_context )
    {
        return new CSingleAssignment<>(
            (IVariable<?>) this.visitVariable( p_context.variable() ),
            (IExecution) this.visitExecutable_term( p_context.executable_term() )
        );
    }

    @Override
    public final Object visitAssignment_expression_multivariable( final PlanBundleParser.Assignment_expression_multivariableContext p_context )
    {
        return new CMultiAssignment<>(
            p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) )
                     .collect( Collectors.toList() ),
            (IExecution) this.visitExecutable_term( p_context.executable_term() )
        );
    }

    @Override
    public final Object visitUnary_expression( final PlanBundleParser.Unary_expressionContext p_context )
    {
        switch ( p_context.UNARYOPERATOR().getText() )
        {
            case "++":
                return new CIncrement<>( (IVariable<Number>) this.visitVariable( p_context.variable() ) );

            case "--":
                return new CDecrement<>( (IVariable<Number>) this.visitVariable( p_context.variable() ) );

            default:
                throw new CIllegalArgumentException( CCommon.languagestring( this, "unaryoperator", p_context.getText() ) );
        }
    }

    @Override
    public final Object visitBinary_expression( final PlanBundleParser.Binary_expressionContext p_context )
    {
        final IVariable<Number> l_lhs = (IVariable<Number>) this.visitVariable( p_context.variable( 0 ) );
        final ITerm l_rhs = p_context.variable().size() == 2
                            ? (IVariable<Number>) this.visitVariable( p_context.variable( 1 ) )
                            : CRawTerm.from( numbervalue( p_context.NUMBER() ) );

        return new COperatorAssign(
            l_lhs, l_rhs, org.lightjason.agentspeak.language.execution.expressionbinary.EOperator.from( p_context.BINARYOPERATOR().getText() )
        );
    }

    @Override
    public final Object visitAchievement_goal_action( final PlanBundleParser.Achievement_goal_actionContext p_context )
    {
        if ( Objects.nonNull( p_context.literal() ) )
            return new CAchievementGoalLiteral( (ILiteral) this.visitLiteral( p_context.literal() ), Objects.nonNull( p_context.DOUBLEEXCLAMATIONMARK() ) );

        if ( Objects.nonNull( p_context.variable_evaluate() ) )
            return new CAchievementGoalVariable(
                (IVariableEvaluate) this.visitVariable_evaluate( p_context.variable_evaluate() ),
                p_context.DOUBLEEXCLAMATIONMARK() != null
            );

        throw new CIllegalArgumentException( CCommon.languagestring( this, "achievmentgoal", p_context.getText() ) );
    }

    @Override
    public final Object visitTernary_operation( final PlanBundleParser.Ternary_operationContext p_context )
    {
        return new CTernaryOperation(
            (IExpression) this.visitExpression( p_context.expression() ),
            (IExecution) this.visitTernary_operation_true( p_context.ternary_operation_true() ),
            (IExecution) this.visitTernary_operation_false( p_context.ternary_operation_false() )
        );
    }

    @Override
    public final Object visitTest_action( final PlanBundleParser.Test_actionContext p_context )
    {
        return CAgentSpeak.testgoal(
            p_context.DOLLAR(),
            p_context.ATOM()
        );
    }

    @Override
    public final Object visitBelief_action( final PlanBundleParser.Belief_actionContext p_context )
    {
        if ( Objects.nonNull( p_context.PLUS() ) )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.ADD );

        if ( Objects.nonNull( p_context.MINUS() ) )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.DELETE );

        throw new CIllegalArgumentException( CCommon.languagestring( this, "beliefaction", p_context.getText() ) );
    }

    @Override
    public final Object visitDeconstruct_expression( final PlanBundleParser.Deconstruct_expressionContext p_context )
    {
        return new CDeconstruct<>(
            p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) ).collect( Collectors.toList() ),
            (ITerm) ( p_context.literal() != null ? this.visitLiteral( p_context.literal() ) : this.visitVariable( p_context.variable() ) )
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitLiteral( final PlanBundleParser.LiteralContext p_context )
    {
        return CTerm.literal(
            p_context.AT(),
            p_context.STRONGNEGATION(),
            p_context.ATOM(),
            (Collection<ITerm>) this.visitTermlist( p_context.termlist() )
        );
    }

    @Override
    public final Object visitVariable( final PlanBundleParser.VariableContext p_context )
    {
        return CTerm.variable( p_context.AT(), p_context.VARIABLEATOM() );
    }

    @Override
    public final Object visitTerm( final PlanBundleParser.TermContext p_context )
    {
        final Object l_terminal = CTerm.termterminals( p_context.STRING(), p_context.NUMBER(), p_context.LOGICALVALUE() );
        if ( Objects.nonNull( l_terminal ) )
            return l_terminal;

        if ( Objects.nonNull( p_context.execute_action() ) )
            return this.visit( p_context.execute_action() );
        if ( Objects.nonNull( p_context.execute_rule() ) )
            return this.visit( p_context.execute_rule() );
        if ( Objects.nonNull( p_context.execute_variable() ) )
            return this.visit( p_context.execute_variable() );

        if ( Objects.nonNull( p_context.literal() ) )
            return this.visit( p_context.literal() );
        if ( Objects.nonNull( p_context.variable() ) )
            return this.visit( p_context.variable() );

        throw new CIllegalArgumentException( CCommon.languagestring( this, "termunknown", p_context.getText() ) );
    }

    @Override
    public final Object visitTermlist( final PlanBundleParser.TermlistContext p_context )
    {
        return ( Objects.isNull( p_context ) ) || ( p_context.isEmpty() )
               ? Collections.emptyList()
               : CTerm.termlist( this, p_context.term().stream() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- raw rules -------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitExpression( final PlanBundleParser.ExpressionContext p_context )
    {
        // bracket expression
        if ( Objects.nonNull( p_context.expression_bracket() ) )
            return this.visitExpression_bracket( p_context.expression_bracket() );

        // or-expression
        return org.lightjason.agentspeak.grammar.CCommon.createLogicalBinaryExpression(
            EOperator.OR,
            (IExpression) this.visitExpression_logical_and( p_context.expression_logical_and() ),
            p_context.expression() != null
            ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect( Collectors.toList() )
            : Collections.emptyList()
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- getter structure ------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public final Set<ILiteral> initialbeliefs()
    {
        return m_initialbeliefs;
    }

    @Nonnull
    @Override
    public final Set<IPlan> plans()
    {
        return m_plans;
    }

    @Nonnull
    @Override
    public final Set<IRule> rules()
    {
        return new HashSet<>( m_rules.values() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------
}
