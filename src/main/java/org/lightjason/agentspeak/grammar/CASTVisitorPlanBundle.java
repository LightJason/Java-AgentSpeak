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
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.grammar.builder.CAgentSpeak;
import org.lightjason.agentspeak.grammar.builder.CTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
     * set with lambda-streaming structure
     */
    private final Set<ILambdaStreaming<?>> m_lambdastream;

    /**
     * ctor
     *
     * @param p_actions set with actions
     * @param p_lambdastreaming lambda streaming
     */
    public CASTVisitorPlanBundle( @Nonnull final Set<IAction> p_actions, @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming )
    {
        m_lambdastream = p_lambdastreaming;
        m_actions = p_actions.stream().collect( Collectors.toMap( i -> i.name(), i -> i ) );
        LOGGER.info( MessageFormat.format( "create parser with actions : {0}", m_actions.keySet() ) );
    }


    // --- plan bundle rules -----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitPlanbundle( final PlanBundleParser.PlanbundleContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- AgentSpeak(L++) rules -------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitBelief( final PlanBundleParser.BeliefContext p_context )
    {
        return this.visit( p_context.literal() );
    }

    @Override
    public final Object visitLogicrule( final PlanBundleParser.LogicruleContext p_context )
    {
        // @todo add body

        final ILiteral l_literal = (ILiteral) this.visit( p_context.literal() );
        m_rules.put(
            l_literal.fqnfunctor(),
            CAgentSpeak.rule(
                l_literal,
                Objects.isNull( p_context.ANNOTATION() )
                ? Stream.empty()
                : p_context.ANNOTATION().stream()
            )
        );
        return null;
    }

    @Override
    public final Object visitPlan( final PlanBundleParser.PlanContext p_context )
    {
        return CAgentSpeak.plan(
            this,
            p_context.ANNOTATION(),
            p_context.PLANTRIGGER(),
            p_context.literal(),
            p_context.expression(),
            p_context.body()
        );
    }

    @Override
    public final Object visitBody( final PlanBundleParser.BodyContext p_context )
    {
        // @todo ckeck return

        return CAgentSpeak.repair( this, p_context.repair_formula() );
    }

    @Override
    public final Object visitRepair_formula( final PlanBundleParser.Repair_formulaContext p_context )
    {
        // @todo check

        return CAgentSpeak.repairformula( this, p_context.body_formula(), p_context.repair_formula() );
    }

    @Override
    public final Object visitBlock_formula( final PlanBundleParser.Block_formulaContext p_context )
    {
        return CAgentSpeak.blockformular( this, p_context.body(), p_context.body_formula() );
    }

    //Checkstyle:OFF:NPathComplexity
    @Override
    public final Object visitBody_formula( final PlanBundleParser.Body_formulaContext p_context )
    {
        if ( Objects.nonNull( p_context.ternary_operation() ) )
            return this.visit( p_context.ternary_operation() );

        if ( Objects.nonNull( p_context.belief_action() ) )
            return this.visit( p_context.belief_action() );

        if ( Objects.nonNull( p_context.expression() ) )
            return this.visit( p_context.expression() );

        if ( Objects.nonNull( p_context.deconstruct_expression() ) )
            return this.visit( p_context.deconstruct_expression() );

        if ( Objects.nonNull( p_context.assignment_expression() ) )
            return this.visit( p_context.assignment_expression() );

        if ( Objects.nonNull( p_context.unary_expression() ) )
            return this.visit( p_context.unary_expression() );

        if ( Objects.nonNull( p_context.test_action() ) )
            return this.visit( p_context.test_action() );

        if ( Objects.nonNull( p_context.achievement_goal_action() ) )
            return this.visit( p_context.achievement_goal_action() );

        if ( Objects.nonNull( p_context.unification() ) )
            return this.visit( p_context.unification() );

        if ( Objects.nonNull( p_context.lambda() ) )
            return this.visit( p_context.lambda() );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "termunknown", p_context.getText() ) );
    }
    //Checkstyle:ON:NPathComplexity




    @Override
    public final Object visitUnification( final PlanBundleParser.UnificationContext p_context )
    {
        return CAgentSpeak.unification(
            this,
            p_context.AT(),
            p_context.literal(),
            p_context.unification_constraint()
        );
    }

    @Override
    public final Object visitUnification_constraint( final PlanBundleParser.Unification_constraintContext p_context )
    {
        return CAgentSpeak.unificationconstraint( this, p_context.variable(), p_context.expression() );
    }




    @Override
    public final Object visitAchievement_goal_action( final PlanBundleParser.Achievement_goal_actionContext p_context )
    {
        return CAgentSpeak.achievementgoal(
            this,
            p_context.DOUBLEEXCLAMATIONMARK(),
            p_context.literal(),
            p_context.variable(),
            p_context.termlist()
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
        return CAgentSpeak.beliefaction(
            p_context.PLUS(),
            p_context.MINUS(),
            (ILiteral) this.visit( p_context.literal() )
        );
    }

    @Override
    public final Object visitDeconstruct_expression( final PlanBundleParser.Deconstruct_expressionContext p_context )
    {
        return CAgentSpeak.deconstruct(
            this,
            p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visit( i ) ),
            (ITerm) this.visit( p_context.literal() ),
            (ITerm) this.visit( p_context.variable() )
        );
    }




    @Override
    public final Object visitLambda( final PlanBundleParser.LambdaContext p_context )
    {
        return CAgentSpeak.lambda(
            this,
            Objects.nonNull( p_context.AT() ),
            p_context.lambda_initialization(),
            p_context.lambda_return(),
            p_context.block_formula()
        );
    }

    @Override
    public final Object visitLambda_initialization( final PlanBundleParser.Lambda_initializationContext p_context )
    {
        return CAgentSpeak.lambdainitialization(
            this,
            m_lambdastream,
            p_context.HASH(),
            p_context.NUMBER(),
            p_context.variable(),
            p_context.lambda_element()
        );
    }

    @Override
    public final Object visitLambda_element( final PlanBundleParser.Lambda_elementContext p_context )
    {
        return CAgentSpeak.lambdaelement(
            this,
            p_context.NUMBER(),
            p_context.variable()
        );
    }

    @Override
    public final Object visitLambda_return( final PlanBundleParser.Lambda_returnContext p_context )
    {
        return this.visit( p_context.variable() );
    }




    @Override
    public final Object visitExpression( final PlanBundleParser.ExpressionContext p_context )
    {
        return null;
    }

    @Override
    public final Object visitAssignment_expression( final PlanBundleParser.Assignment_expressionContext p_context )
    {
        // @todo fix exception

        if ( Objects.nonNull( p_context.assignment_expression_singlevariable() ) )
            return this.visit( p_context.assignment_expression_singlevariable() );

        if ( Objects.nonNull( p_context.assignment_expression_multivariable() ) )
            return this.visit( p_context.assignment_expression_multivariable() );

        throw new CSyntaxErrorException();
    }

    @Override
    public final Object visitAssignment_expression_singlevariable( final PlanBundleParser.Assignment_expression_singlevariableContext p_context )
    {
        return CAgentSpeak.singleassignment(
            this,
            p_context.variable(),
            p_context.ASSIGNOPERATOR(),
            p_context.ternary_operation(),
            p_context.expression()
        );
    }

    @Override
    public final Object visitAssignment_expression_multivariable( final PlanBundleParser.Assignment_expression_multivariableContext p_context )
    {
        return CAgentSpeak.multiassignment( this, p_context.variablelist(), p_context.expression() );
    }

    @Override
    public final Object visitUnary_expression( final PlanBundleParser.Unary_expressionContext p_context )
    {
        return CAgentSpeak.unary( this, p_context.UNARYOPERATOR(), p_context.variable() );
    }




    @Override
    public final Object visitTernary_operation( final PlanBundleParser.Ternary_operationContext p_context )
    {
        return CAgentSpeak.ternary(
            this,
            p_context.expression(),
            p_context.ternary_operation_true(),
            p_context.ternary_operation_false()
        );
    }

    @Override
    public final Object visitTernary_operation_true( final PlanBundleParser.Ternary_operation_trueContext p_context )
    {
        return this.visit( p_context.expression() );
    }

    @Override
    public final Object visitTernary_operation_false( final PlanBundleParser.Ternary_operation_falseContext p_context )
    {
        return this.visit( p_context.expression() );
    }




    @Override
    public final Object visitExecute_action( final PlanBundleParser.Execute_actionContext p_context )
    {
        return CAgentSpeak.action( this, p_context.literal(), m_actions );
    }

    @Override
    public final Object visitExecute_rule( final PlanBundleParser.Execute_ruleContext p_context )
    {
        return null;
    }

    @Override
    public final Object visitExecute_variable( final PlanBundleParser.Execute_variableContext p_context )
    {
        return CAgentSpeak.passvaribaleliteral( this, p_context.variable(), p_context.termlist() );
    }




    @Override
    public final Object visitTerm( final PlanBundleParser.TermContext p_context )
    {
        return CTerm.term(
            this,

            p_context.STRING(),
            p_context.NUMBER(),
            p_context.LOGICALVALUE(),

            p_context.execute_action(),
            p_context.execute_rule(),
            p_context.execute_variable(),

            p_context.variable(),
            p_context.literal()
        );
    }

    @Override
    public final Object visitLiteral( final PlanBundleParser.LiteralContext p_context )
    {
        return CTerm.literal( this, p_context.AT(), p_context.STRONGNEGATION(), p_context.ATOM(), p_context.termlist() );
    }

    @Override
    public final Object visitTermlist( final PlanBundleParser.TermlistContext p_context )
    {
        return CTerm.termlist( this, p_context.term() );
    }

    @Override
    public final Object visitVariable( final PlanBundleParser.VariableContext p_context )
    {
        return CTerm.variable( p_context.AT(), p_context.VARIABLEATOM() );
    }


    @Override
    public final Object visitVariablelist( final PlanBundleParser.VariablelistContext p_context )
    {
        return p_context.variable().stream().map( i -> this.visit( i ) );
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
