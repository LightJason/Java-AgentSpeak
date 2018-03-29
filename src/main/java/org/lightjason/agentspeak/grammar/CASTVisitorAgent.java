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
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;

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
 * default abstract-syntax-tree (AST) visitor for agent scripts
 *
 * @note methods are implemented twice agent and plan-bundle, because both use equal
 * AgentSpeak(L) grammer, but AntLR visitor does not support inheritance by the grammar definition
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public final class CASTVisitorAgent extends AbstractParseTreeVisitor<Object> implements IASTVisitorAgent
{
    /**
     * logger
     */
    private static final Logger LOGGER = CCommon.logger( IASTVisitorAgent.class );
    /**
     * initial goal
     */
    private ILiteral m_initialgoal;
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
    public CASTVisitorAgent( @Nonnull final Set<IAction> p_actions, @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming )
    {
        m_lambdastream = p_lambdastreaming;
        m_actions = p_actions.stream().collect( Collectors.toMap( i -> i.name(), i -> i ) );
        LOGGER.info( MessageFormat.format( "create parser with actions : {0}", m_actions.keySet() ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- agent rules -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitAgent( final AgentParser.AgentContext p_context )
    {
        m_initialgoal = Objects.isNull( p_context.INITIALGOAL() )
                        ? null
                        : CLiteral.of(
                            p_context.INITIALGOAL().getText().replace( "!", "" ).replace( ".", "" )
                        );


        p_context.belief()
                 .stream()
                 .map( i -> (ILiteral) this.visit( i ) )
                 .forEach( i -> m_initialbeliefs.add( i ) );

        /*
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
        */

        p_context.plan()
                 .stream()
                 .flatMap( i -> (Stream<IPlan>) this.visit( i ) )
                 .forEach( i -> m_plans.add( i ) );

        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- AgentSpeak(L++) rules -------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitBelief( final AgentParser.BeliefContext p_context )
    {
        return this.visit( p_context.literal() );
    }

    @Override
    public final Object visitLogicrule( final AgentParser.LogicruleContext p_context )
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
    public final Object visitPlan( final AgentParser.PlanContext p_context )
    {
        return CAgentSpeak.plan(
            this,
            p_context.ANNOTATION(),
            p_context.PLANTRIGGER(),
            p_context.literal(),
            p_context.plandefinition()
        );
    }

    @Override
    public final Object visitPlandefinition( final AgentParser.PlandefinitionContext p_context )
    {
        return CAgentSpeak.plandefinition( this, p_context.expression(), p_context.body() );
    }

    @Override
    public final Object visitBody( final AgentParser.BodyContext p_context )
    {
        return CAgentSpeak.repair( this, p_context.repair_formula() );
    }

    @Override
    public final Object visitRepair_formula( final AgentParser.Repair_formulaContext p_context )
    {
        return CAgentSpeak.repairformula( this, p_context.body_formula() );
    }

    @Override
    public final Object visitBlock_formula( final AgentParser.Block_formulaContext p_context )
    {
        return CAgentSpeak.blockformular( this, p_context.body(), p_context.body_formula() );
    }

    @Override
    public Object visitBody_formula( final AgentParser.Body_formulaContext p_context )
    {
        return CAgentSpeak.bodyformular(
            this,
            p_context.ternary_operation(),
            p_context.belief_action(),
            p_context.expression(),
            p_context.deconstruct_expression(),
            p_context.assignment_expression(),
            p_context.unary_expression(),
            p_context.test_action(),
            p_context.achievement_goal_action(),
            p_context.unification(),
            p_context.lambda()
        );
    }




    @Override
    public final Object visitUnification( final AgentParser.UnificationContext p_context )
    {
        return CAgentSpeak.unification(
            this,
            p_context.AT(),
            p_context.literal(),
            p_context.unification_constraint()
        );
    }

    @Override
    public final Object visitUnification_constraint( final AgentParser.Unification_constraintContext p_context )
    {
        return CAgentSpeak.unificationconstraint( this, p_context.variable(), p_context.expression() );
    }




    @Override
    public final Object visitAchievement_goal_action( final AgentParser.Achievement_goal_actionContext p_context )
    {
        return CAgentSpeak.executeachievementgoal(
            this,
            p_context.DOUBLEEXCLAMATIONMARK(),
            p_context.literal(),
            p_context.variable(),
            p_context.termlist()
        );
    }

    @Override
    public final Object visitTest_action( final AgentParser.Test_actionContext p_context )
    {
        return CAgentSpeak.executetestgoal(
            p_context.DOLLAR(),
            p_context.ATOM()
        );
    }

    @Override
    public final Object visitBelief_action( final AgentParser.Belief_actionContext p_context )
    {
        return CAgentSpeak.executebelief(
            p_context.PLUS(),
            p_context.MINUS(),
            (ILiteral) this.visit( p_context.literal() )
        );
    }

    @Override
    public final Object visitDeconstruct_expression( final AgentParser.Deconstruct_expressionContext p_context )
    {
        return CAgentSpeak.deconstruct(
            this,
            p_context.variablelist().variable(),
            p_context.literal(),
            p_context.variable()
        );
    }




    @Override
    public final Object visitLambda( final AgentParser.LambdaContext p_context )
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
    public final Object visitLambda_initialization( final AgentParser.Lambda_initializationContext p_context )
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
    public final Object visitLambda_element( final AgentParser.Lambda_elementContext p_context )
    {
        return CAgentSpeak.lambdaelement(
            this,
            p_context.NUMBER(),
            p_context.variable()
        );
    }

    @Override
    public final Object visitLambda_return( final AgentParser.Lambda_returnContext p_context )
    {
        return this.visit( p_context.variable() );
    }




    @Override
    public final Object visitExpression( final AgentParser.ExpressionContext p_context )
    {
        return CAgentSpeak.expression(
            this,
            p_context.term(),
            p_context.STRONGNEGATION(),
            p_context.single,
            p_context.binaryoperator,
            p_context.lhs,
            p_context.rhs
        );
    }

    @Override
    public Object visitAssignment_expression( final AgentParser.Assignment_expressionContext p_context )
    {
        // @todo fix exception

        if ( Objects.nonNull( p_context.assignment_expression_singlevariable() ) )
            return this.visit( p_context.assignment_expression_singlevariable() );

        if ( Objects.nonNull( p_context.assignment_expression_multivariable() ) )
            return this.visit( p_context.assignment_expression_multivariable() );

        throw new CSyntaxErrorException();
    }

    @Override
    public final Object visitAssignment_expression_singlevariable( final AgentParser.Assignment_expression_singlevariableContext p_context )
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
    public final Object visitAssignment_expression_multivariable( final AgentParser.Assignment_expression_multivariableContext p_context )
    {
        return CAgentSpeak.multiassignment( this, p_context.variablelist(), p_context.expression() );
    }

    @Override
    public final Object visitUnary_expression( final AgentParser.Unary_expressionContext p_context )
    {
        return CAgentSpeak.executeunary( this, p_context.UNARYOPERATOR(), p_context.variable() );
    }




    @Override
    public final Object visitTernary_operation( final AgentParser.Ternary_operationContext p_context )
    {
        return CAgentSpeak.executeternary(
            this,
            p_context.expression(),
            p_context.ternary_operation_true(),
            p_context.ternary_operation_false()
        );
    }

    @Override
    public final Object visitTernary_operation_true( final AgentParser.Ternary_operation_trueContext p_context )
    {
        return this.visit( p_context.expression() );
    }

    @Override
    public final Object visitTernary_operation_false( final AgentParser.Ternary_operation_falseContext p_context )
    {
        return this.visit( p_context.expression() );
    }




    @Override
    public final Object visitExecute_action( final AgentParser.Execute_actionContext p_context )
    {
        return CAgentSpeak.executeaction( this, p_context.literal(), m_actions );
    }

    @Override
    public final Object visitExecute_rule( final AgentParser.Execute_ruleContext p_context )
    {
        return CAgentSpeak.executerule( this, p_context.literal(), p_context.execute_variable() );
    }

    @Override
    public final Object visitExecute_variable( final AgentParser.Execute_variableContext p_context )
    {
        return CAgentSpeak.passvaribaleliteral( this, p_context.variable(), p_context.termlist() );
    }




    @Override
    public final Object visitTerm( final AgentParser.TermContext p_context )
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
    public final Object visitLiteral( final AgentParser.LiteralContext p_context )
    {
        return CTerm.literal( this, p_context.AT(), p_context.STRONGNEGATION(), p_context.ATOM(), p_context.termlist() );
    }

    @Override
    public final Object visitTermlist( final AgentParser.TermlistContext p_context )
    {
        return CTerm.termlist( this, p_context.term() );
    }

    @Override
    public final Object visitVariable( final AgentParser.VariableContext p_context )
    {
        return CTerm.variable( p_context.AT(), p_context.VARIABLEATOM() );
    }

    @Override
    public final Object visitVariablelist( final AgentParser.VariablelistContext p_context )
    {
        return CAgentSpeak.variablelist( this, p_context.variable() );
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

    @Override
    public final ILiteral initialgoal()
    {
        return m_initialgoal;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
