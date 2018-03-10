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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.grammar.builder.CAgentSpeak;
import org.lightjason.agentspeak.grammar.builder.CRaw;
import org.lightjason.agentspeak.grammar.builder.CTerm;
import org.lightjason.agentspeak.language.CLiteral;
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
import org.lightjason.agentspeak.language.execution.action.achievement_test.CAchievementRuleLiteral;
import org.lightjason.agentspeak.language.execution.action.achievement_test.CAchievementRuleVariable;
import org.lightjason.agentspeak.language.execution.action.achievement_test.CTestGoal;
import org.lightjason.agentspeak.language.execution.action.achievement_test.CTestRule;
import org.lightjason.agentspeak.language.execution.action.unify.CDefaultUnify;
import org.lightjason.agentspeak.language.execution.action.unify.CExpressionUnify;
import org.lightjason.agentspeak.language.execution.action.unify.CVariableUnify;
import org.lightjason.agentspeak.language.execution.expression.CAtom;
import org.lightjason.agentspeak.language.execution.expression.CProxyReturnExpression;
import org.lightjason.agentspeak.language.execution.expression.EOperator;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.execution.expression.logical.CUnary;
import org.lightjason.agentspeak.language.execution.expression.numerical.CAdditive;
import org.lightjason.agentspeak.language.execution.expression.numerical.CComparable;
import org.lightjason.agentspeak.language.execution.expression.numerical.CMultiplicative;
import org.lightjason.agentspeak.language.execution.expression.numerical.CPower;
import org.lightjason.agentspeak.language.execution.expression.numerical.CRelational;
import org.lightjason.agentspeak.language.execution.expressionbinary.COperatorAssign;
import org.lightjason.agentspeak.language.execution.expressionunary.CDecrement;
import org.lightjason.agentspeak.language.execution.expressionunary.CIncrement;
import org.lightjason.agentspeak.language.instantiable.plan.CPlan;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.annotation.CAtomAnnotation;
import org.lightjason.agentspeak.language.instantiable.plan.annotation.CValueAnnotation;
import org.lightjason.agentspeak.language.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.instantiable.rule.CRule;
import org.lightjason.agentspeak.language.instantiable.rule.CRulePlaceholder;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.CMutexVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.CVariableEvaluate;
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
     * ctor
     *
     * @param p_actions set with actions
     */
    public CASTVisitorAgent( @Nonnull final Set<IAction> p_actions )
    {
        m_actions = p_actions.stream().collect( Collectors.toMap( i -> i.name(), i -> i ) );
        LOGGER.info( MessageFormat.format( "create parser with actions & rules : {0} / {1}", m_actions.keySet(), m_rules.keySet() ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- agent rules -----------------------------------------------------------------------------------------------------------------------------------------



    @Override
    public Object visitAgent( final AgentParser.AgentContext p_context )
    {
        m_initialgoal = Objects.isNull( p_context.INITIALGOAL() )
                        ? null
                        : CLiteral.from(
                            p_context.INITIALGOAL().getText().replace( "!", "" ).replace( ".", "" )
                        );

        p_context.belief()
                 .stream()
                 .map( i -> this.visit( i ) )
                 .map( i -> (ILiteral) i )
                 .forEach( i -> m_initialbeliefs.add( i ) );

        p_context.plan()
                 .stream()
                 .map( i -> this.visit( i ) )
                 .map( i -> (IPlan) i )
                 .forEach( i -> m_plans.add( i ) );


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

        LOGGER.info( MessageFormat.format( "parsed rules: {0}", m_rules.values() ) );
         */

        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- AgentSpeak(L) rules ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitLogicrule( final AgentParser.LogicruleContext p_context )
    {
        CAgentSpeak.rule(
            (ILiteral) this.visit( p_context.literal() ),
            Objects.isNull( p_context.ANNOTATION() )
            ? Stream.empty()
            : p_context.ANNOTATION().stream()
        );


        final ILiteral l_literal = (ILiteral) this.visitLiteral( p_context.literal() );
        return p_context.logicalruledefinition().stream()
                        .map( i -> new CRule( (ILiteral) l_literal.deepcopy(), (List<IExecution>) this.visitLogicalruledefinition( i ) ) )
                        .collect( Collectors.toList() );
    }

    @Override
    public final Object visitPlan( final AgentParser.PlanContext p_context )
    {
        CAgentSpeak.plan(
            p_context.PLANTRIGGER(),

            Objects.isNull( p_context.ANNOTATION() )
            ? Stream.empty()
            : p_context.ANNOTATION().stream()
        );


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
    public final Object visitPlandefinition( final AgentParser.PlandefinitionContext p_context )
    {
        return new ImmutablePair<IExpression, List<IExecution>>(
            Objects.isNull( p_context.expression() )
            ? IExpression.EMPTY
            : (IExpression) this.visitExpression( p_context.expression() ),
            (List<IExecution>) this.visitBody( p_context.body() )
        );
    }

    @Override
    public final Object visitBody( final AgentParser.BodyContext p_context )
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
    public final Object visitRepair_formula( final AgentParser.Repair_formulaContext p_context )
    {
        return Stream.concat(
            Stream.of(
                this.visit( p_context.body_formula() )
            ).map( i -> (IExecution) i ),
            (Stream<IExecution>) this.visit( p_context.repair_formula() )
        );
    }

    @Override
    public final Object visitUnification( final AgentParser.UnificationContext p_context )
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
    public final Object visitUnification_constraint( final AgentParser.Unification_constraintContext p_context )
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
    public final Object visitBlock_formula( final AgentParser.Block_formulaContext p_context )
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
    public final Object visitLambda( final AgentParser.LambdaContext p_context )
    {
        if ( Objects.nonNull( p_context.lambda_return() ) )
            return new CLambdaExpression(
                p_context.AT() != null,
                (IExecution) this.visitLambda_initialization( p_context.lambda_initialization() ),
                (IVariable<?>) this.visitVariable( p_context.variable() ),
                (IVariable<?>) this.visitLambda_return( p_context.lambda_return() ),
                (List<IExecution>) this.visitBlock_formula( p_context.block_formula() )
            );

        return new CLambdaExpression(
            p_context.AT() != null,
            (IExecution) this.visitLambda_initialization( p_context.lambda_initialization() ),
            (IVariable<?>) this.visitVariable( p_context.variable() ),
            (List<IExecution>) this.visitBlock_formula( p_context.block_formula() )
        );
    }

    @Override
    public final Object visitLambda_initialization( final AgentParser.Lambda_initializationContext p_context )
    {
        if ( Objects.nonNull( p_context.variable() ) )
            return new CRawAction<>( this.visitVariable( p_context.variable() ) );

        if ( Objects.nonNull( p_context.literal() ) )
            return new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "lambdainitialization", p_context.getText() ) );
    }

    @Override
    public final Object visitLambda_return( final AgentParser.Lambda_returnContext p_context )
    {
        return this.visitVariable( p_context.variable() );
    }

    @Override
    public final Object visitAssignment_expression( final AgentParser.Assignment_expressionContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitAssignment_expression_singlevariable( final AgentParser.Assignment_expression_singlevariableContext p_context )
    {


        return new CSingleAssignment<>(
            (IVariable<?>) this.visitVariable( p_context.variable() ),
            (IExecution) this.visitExecutable_term( p_context.executable_term() )
        );
    }

    @Override
    public final Object visitAssignment_expression_multivariable( final AgentParser.Assignment_expression_multivariableContext p_context )
    {
        return new CMultiAssignment<>(
            p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) )
                     .collect( Collectors.toList() ),
            (IExecution) this.visitExecutable_term( p_context.executable_term() )
        );
    }

    @Override
    public final Object visitUnary_expression( final AgentParser.Unary_expressionContext p_context )
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
    public final Object visitBinary_expression( final AgentParser.Binary_expressionContext p_context )
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
    public final Object visitAchievement_goal_action( final AgentParser.Achievement_goal_actionContext p_context )
    {
        if ( Objects.nonNull( p_context.literal() ) )
            return new CAchievementGoalLiteral( (ILiteral) this.visitLiteral( p_context.literal() ), Objects.nonNull( p_context.DOUBLEEXCLAMATIONMARK() ) );

        if ( Objects.nonNull( p_context.variable_evaluate() ) )
            return new CAchievementGoalVariable(
                (IVariableEvaluate) this.visitVariable_evaluate( p_context.variable_evaluate() ),
                Objects.nonNull( p_context.DOUBLEEXCLAMATIONMARK() )
            );

        throw new CIllegalArgumentException( CCommon.languagestring( this, "achievmentgoal", p_context.getText() ) );
    }

    @Override
    public final Object visitTernary_operation( final AgentParser.Ternary_operationContext p_context )
    {
        return new CTernaryOperation(
            (IExpression) this.visitExpression( p_context.expression() ),
            (IExecution) this.visitTernary_operation_true( p_context.ternary_operation_true() ),
            (IExecution) this.visitTernary_operation_false( p_context.ternary_operation_false() )
        );
    }

    @Override
    public final Object visitTernary_operation_true( final AgentParser.Ternary_operation_trueContext p_context )
    {
        return this.visitExecutable_term( p_context.executable_term() );
    }

    @Override
    public final Object visitTernary_operation_false( final AgentParser.Ternary_operation_falseContext p_context )
    {
        return this.visitExecutable_term( p_context.executable_term() );
    }

    @Override
    public final Object visitTest_action( final AgentParser.Test_actionContext p_context )
    {
        // dollar sign is used to recognize a rule
        return Objects.nonNull( p_context.DOLLAR() )
               ? new CTestRule( CPath.from( (String) this.visitAtom( p_context.atom() ) ) )
               : new CTestGoal( CPath.from( (String) this.visitAtom( p_context.atom() ) ) );
    }

    @Override
    public final Object visitBelief_action( final AgentParser.Belief_actionContext p_context )
    {
        if ( Objects.nonNull( p_context.PLUS() ) )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.ADD );

        if ( Objects.nonNull( p_context.MINUS() ) )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.DELETE );

        throw new CIllegalArgumentException( CCommon.languagestring( this, "beliefaction", p_context.getText() ) );
    }

    @Override
    public final Object visitDeconstruct_expression( final AgentParser.Deconstruct_expressionContext p_context )
    {
        return new CDeconstruct<>(
            p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) ).collect( Collectors.toList() ),
            (ITerm) ( Objects.nonNull( p_context.literal() ) ? this.visitLiteral( p_context.literal() ) : this.visitVariable( p_context.variable() ) )
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitLiteral( final AgentParser.LiteralContext p_context )
    {
        return CTerm.literal(
            p_context.AT(),
            p_context.STRONGNEGATION(),
            p_context.ATOM(),
            (Collection<ITerm>) this.visitTermlist( p_context.termlist() )
        );
    }

    @Override
    public final Object visitTerm( final AgentParser.TermContext p_context )
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
    public final Object visitTermlist( final AgentParser.TermlistContext p_context )
    {
        return ( Objects.isNull( p_context ) ) || ( p_context.isEmpty() )
               ? Collections.emptyList()
               : CTerm.termlist( this, p_context.term().stream() );
    }

    @Override
    public final Object visitVariablelist( final AgentParser.VariablelistContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- raw rules -------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    public final Object visitVariable( final AgentParser.VariableContext p_context )
    {
        return CTerm.variable( p_context.AT(), p_context.VARIABLEATOM() );
    }

/*

    @Override
    public final Object visitExpression( final AgentParser.ExpressionContext p_context )
    {
        // bracket expression
        if ( Objects.nonNull( p_context.expression_bracket() ) )
            return this.visitExpression_bracket( p_context.expression_bracket() );

        // or-expression
        return org.lightjason.agentspeak.grammar.CCommon.createLogicalBinaryExpression(
            EOperator.OR,
            (IExpression) this.visitExpression_logical_and( p_context.expression_logical_and() ),
            Objects.nonNull( p_context.expression() )
            ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect( Collectors.toList() )
            : Collections.emptyList()
        );
    }

    @Override
    public final Object visitExpression_logical_and( final AgentParser.Expression_logical_andContext p_context )
    {
        return org.lightjason.agentspeak.grammar.CCommon.createLogicalBinaryExpression(
            EOperator.AND,
            (IExpression) this.visitExpression_logical_xor( p_context.expression_logical_xor() ),
            Objects.nonNull( p_context.expression() )
            ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect( Collectors.toList() )
            : Collections.emptyList()
        );
    }

    @Override
    public final Object visitExpression_logical_xor( final AgentParser.Expression_logical_xorContext p_context )
    {
        if ( Objects.nonNull( p_context.expression_logical_element() ) )
            return org.lightjason.agentspeak.grammar.CCommon.createLogicalBinaryExpression(
                EOperator.XOR,
                (IExpression) this.visitExpression_logical_element( p_context.expression_logical_element() ),
                Objects.nonNull( p_context.expression() )
                ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect( Collectors.toList() )
                : Collections.emptyList()
            );

        if ( Objects.nonNull( p_context.expression_logical_negation() ) )
            return this.visitExpression_logical_negation( p_context.expression_logical_negation() );

        if ( Objects.nonNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric( p_context.expression_numeric() );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "logicallefthandside", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_logical_negation( final AgentParser.Expression_logical_negationContext p_context )
    {
        return new CUnary( EOperator.NEGATION, (IExpression) this.visitExpression( p_context.expression() ) );
    }

    @Override
    public final Object visitExpression_logical_element( final AgentParser.Expression_logical_elementContext p_context )
    {
        if ( Objects.nonNull( p_context.LOGICALVALUE() ) )
            return new CAtom( logicalvalue( p_context.LOGICALVALUE().getText() ) );

        if ( Objects.nonNull( p_context.variable() ) )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( Objects.nonNull( p_context.unification() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitUnification( p_context.unification() ) );

        if ( Objects.nonNull( p_context.executable_action() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_action( p_context.executable_action() ) );

        if ( Objects.nonNull( p_context.executable_rule() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_rule( p_context.executable_rule() ) );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "logicalelement", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric( final AgentParser.Expression_numericContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_relation( p_context.expression_numeric_relation() );

        if ( Objects.nonNull( p_context.EQUAL() ) )
            return new CComparable(
                EOperator.EQUAL,
                (IExpression) this.visitExpression_numeric_relation( p_context.expression_numeric_relation() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.NOTEQUAL() ) )
            return new CComparable(
                EOperator.NOTEQUAL,
                (IExpression) this.visitExpression_numeric_relation( p_context.expression_numeric_relation() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "compareoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_relation( final AgentParser.Expression_numeric_relationContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_additive( p_context.expression_numeric_additive() );

        if ( Objects.nonNull( p_context.GREATER() ) )
            return new CRelational(
                EOperator.GREATER,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.GREATEREQUAL() ) )
            return new CRelational(
                EOperator.GREATEREQUAL,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.LESS() ) )
            return new CRelational(
                EOperator.LESS,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.LESSEQUAL() ) )
            return new CRelational(
                EOperator.LESSEQUAL,
                (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "relationaloperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_additive( final AgentParser.Expression_numeric_additiveContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() );

        if ( Objects.nonNull( p_context.PLUS() ) )
            return new CAdditive(
                EOperator.PLUS,
                (IExpression) this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.MINUS() ) )
            return new CAdditive(
                EOperator.MINUS,
                (IExpression) this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "additiveoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_multiplicative( final AgentParser.Expression_numeric_multiplicativeContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_power( p_context.expression_numeric_power() );

        if ( Objects.nonNull( p_context.MULTIPLY() ) )
            return new CMultiplicative(
                EOperator.MULTIPLY,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.SLASH() ) )
            return new CMultiplicative(
                EOperator.DIVIDE,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( Objects.nonNull( p_context.MODULO() ) )
            return new CMultiplicative(
                EOperator.MODULO,
                (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "multiplicativeoperator", p_context.getText() ) );
    }

    @Override
    public final Object visitExpression_numeric_power( final AgentParser.Expression_numeric_powerContext p_context )
    {
        if ( Objects.isNull( p_context.expression_numeric() ) )
            return this.visitExpression_numeric_element( p_context.expression_numeric_element() );

        return new CPower(
            EOperator.POWER,
            (IExpression) this.visitExpression_numeric_element( p_context.expression_numeric_element() ),
            (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
        );
    }

    @Override
    public final Object visitExpression_numeric_element( final AgentParser.Expression_numeric_elementContext p_context )
    {
        if ( Objects.nonNull( p_context.number() ) )
            return new CAtom( this.visitNumber( p_context.number() ) );

        if ( Objects.nonNull( p_context.variable() ) )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( Objects.nonNull( p_context.executable_action() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_action( p_context.executable_action() ) );

        if ( Objects.nonNull( p_context.executable_rule() ) )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_rule( p_context.executable_rule() ) );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "numericelement", p_context.getText() ) );
    }

    @Override
    public final Object visitExecutable_action( final AgentParser.Executable_actionContext p_context )
    {
        return new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public final Object visitExecutable_rule( final AgentParser.Executable_ruleContext p_context )
    {
        if ( Objects.nonNull( p_context.literal() ) )
            return new CAchievementRuleLiteral( (ILiteral) this.visitLiteral( p_context.literal() ) );

        if ( Objects.nonNull( p_context.variable_evaluate() ) )
            return new CAchievementRuleVariable( (IVariableEvaluate) this.visitVariable_evaluate( p_context.variable_evaluate() ) );

        throw new CSyntaxErrorException( CCommon.languagestring( this, "executablerule", p_context.getText() ) );
    }

    @Override
    public final Object visitVariable_evaluate( final AgentParser.Variable_evaluateContext p_context )
    {
        return new CVariableEvaluate(
            (IVariable<?>) this.visitVariable( p_context.variable() ),
            (List<ITerm>) this.visitTermlist( p_context.termlist() )
        );
    }

*/

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- helper ----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * create a rule placeholder object
     *
     * @param p_context logical rule context
     * @return placeholder rule
     */
    protected final Object visitLogicrulePlaceHolder( final AgentParser.LogicruleContext p_context )
    {
        return new CRulePlaceholder( (ILiteral) this.visitLiteral( p_context.literal() ) );
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
