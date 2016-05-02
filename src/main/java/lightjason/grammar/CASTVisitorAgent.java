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


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lightjason.agent.action.IAction;
import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.common.IPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.error.CSyntaxErrorException;
import lightjason.language.CLiteral;
import lightjason.language.CRawTerm;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.action.CBeliefAction;
import lightjason.language.execution.action.CDeconstruct;
import lightjason.language.execution.action.CLambdaExpression;
import lightjason.language.execution.action.CMultiAssignment;
import lightjason.language.execution.action.CProxyAction;
import lightjason.language.execution.action.CRawAction;
import lightjason.language.execution.action.CRepair;
import lightjason.language.execution.action.CSingleAssignment;
import lightjason.language.execution.action.CTernaryOperation;
import lightjason.language.execution.action.goaltest.CAchievementGoalLiteral;
import lightjason.language.execution.action.goaltest.CAchievementGoalVariable;
import lightjason.language.execution.action.goaltest.CAchievmentRule;
import lightjason.language.execution.action.goaltest.CTestGoal;
import lightjason.language.execution.action.goaltest.CTestRule;
import lightjason.language.execution.action.unify.CDefaultUnify;
import lightjason.language.execution.action.unify.CExpressionUnify;
import lightjason.language.execution.action.unify.CVariableUnify;
import lightjason.language.execution.annotation.CAtomAnnotation;
import lightjason.language.execution.annotation.CNumberAnnotation;
import lightjason.language.execution.annotation.IAnnotation;
import lightjason.language.execution.expression.CAtom;
import lightjason.language.execution.expression.CProxyReturnExpression;
import lightjason.language.execution.expression.EOperator;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.expression.logical.CUnary;
import lightjason.language.execution.expression.numerical.CAdditive;
import lightjason.language.execution.expression.numerical.CComparable;
import lightjason.language.execution.expression.numerical.CMultiplicative;
import lightjason.language.execution.expression.numerical.CPower;
import lightjason.language.execution.expression.numerical.CRelational;
import lightjason.language.execution.unaryoperator.CDecrement;
import lightjason.language.execution.unaryoperator.CIncrement;
import lightjason.language.instantiable.plan.CPlan;
import lightjason.language.instantiable.plan.IPlan;
import lightjason.language.instantiable.plan.trigger.CTrigger;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.instantiable.rule.CRule;
import lightjason.language.instantiable.rule.CRulePlaceholder;
import lightjason.language.instantiable.rule.IRule;
import lightjason.language.variable.CMutexVariable;
import lightjason.language.variable.CVariable;
import lightjason.language.variable.IVariable;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * default abstract-syntax-tree (AST) visitor for agent scripts
 *
 * @note methods are implemented twice agent and plan-bundle, because both use equal
 * AgentSpeak(L) grammer, but AntLR visitor does not support inheritance by the grammar definition
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public class CASTVisitorAgent extends AbstractParseTreeVisitor<Object> implements IASTVisitorAgent
{
    /**
     * logger
     */
    protected final static Logger LOGGER = CCommon.getLogger( CASTVisitorAgent.class );
    /**
     * initial goal
     */
    protected ILiteral m_InitialGoal;
    /**
     * set with initial beliefs
     */
    protected final Set<ILiteral> m_InitialBeliefs = new HashSet<>();
    /**
     * map with plans
     */
    protected final Set<IPlan> m_plans = new HashSet<>();
    /**
     * map with logical rules
     */
    protected final Multimap<IPath, IRule> m_rules = HashMultimap.create();
    /**
     * map with action definition
     */
    protected final Map<IPath, IAction> m_actions;

    /**
     * ctor
     *
     * @param p_actions set with actions
     * @param p_rules set with rules
     */
    public CASTVisitorAgent( final Set<IAction> p_actions, final Set<IRule> p_rules )
    {
        m_actions = p_actions.stream().collect( Collectors.toMap( i -> i.getName(), i -> i ) );
        p_rules.stream().forEach( i -> m_rules.put( i.getIdentifier().getFQNFunctor(), i ) );

        LOGGER.info( MessageFormat.format( "create parser with actions & rules : {0} / {1}", m_actions, m_rules ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- agent rules -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitAgent( final AgentParser.AgentContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public Object visitInitial_beliefs( final AgentParser.Initial_beliefsContext p_context )
    {
        p_context.belief().parallelStream().map( i -> (ILiteral) this.visitBelief( i ) ).forEach( m_InitialBeliefs::add );
        LOGGER.info( MessageFormat.format( "parsed initial beliefs: {0}", m_InitialBeliefs ) );
        return null;
    }



    @Override
    public Object visitInitial_goal( final AgentParser.Initial_goalContext p_context )
    {
        m_InitialGoal = CLiteral.from( (String) this.visitAtom( p_context.atom() ) );
        LOGGER.info( MessageFormat.format( "parsed initial-goal: {0}", m_InitialGoal ) );
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- AgentSpeak(L) rules ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitBelief( final AgentParser.BeliefContext p_context )
    {
        return this.visitLiteral( p_context.literal() );
    }



    @Override
    public Object visitPlans( final AgentParser.PlansContext p_context )
    {
        if ( p_context.plan() == null )
            return null;

        p_context.plan().stream().forEach( i -> ( (List<IPlan>) this.visitPlan( i ) ).stream().forEach( j -> m_plans.add( j ) ) );
        LOGGER.info( MessageFormat.format( "parsed plans: {0}", m_plans ) );
        return null;
    }



    @Override
    public Object visitLogicrules( final AgentParser.LogicrulesContext p_context )
    {
        // create placeholder objects first and run parsing again to build full-qualified rule objects
        p_context.logicrule().stream()
                 .map( i -> (IRule) this.visitLogicrulePlaceHolder( i ) )
                 .forEach( i -> m_rules.put( i.getIdentifier().getFQNFunctor(), i ) );

        final Multimap<IPath, IRule> l_rules = HashMultimap.create();
        p_context.logicrule().stream()
                 .flatMap( i -> ( (List<IRule>) this.visitLogicrule( i ) ).stream() )
                 .forEach( i -> l_rules.put( i.getIdentifier().getFQNFunctor(), i ) );

        // clear rule list and replace placeholder objects
        m_rules.clear();
        l_rules.values().parallelStream()
               .map( i -> i.replaceplaceholder( l_rules ) )
               .forEach( i -> m_rules.put( i.getIdentifier().getFQNFunctor(), i ) );

        LOGGER.info( MessageFormat.format( "parsed rules: {0}", m_rules.values() ) );
        return null;
    }

    @Override
    public Object visitLogicrule( final AgentParser.LogicruleContext p_context )
    {
        final ILiteral l_literal = (ILiteral) this.visitLiteral( p_context.literal() );
        return p_context.logicalruledefinition().stream()
                        .map( i -> new CRule( (ILiteral) l_literal.deepcopy(), (List<IExecution>) this.visitLogicalruledefinition( i ) ) )
                        .collect( Collectors.toList() );
    }

    @Override
    public Object visitLogicalruledefinition( final AgentParser.LogicalruledefinitionContext p_context )
    {
        return this.visitBody( p_context.body() );
    }

    @Override
    public Object visitPlan( final AgentParser.PlanContext p_context )
    {
        final Set<IAnnotation<?>> l_annotation = (Set<IAnnotation<?>>) this.visitAnnotations( p_context.annotations() );
        final CTrigger l_trigger = new CTrigger(
                (ITrigger.EType) this.visitPlan_trigger( p_context.plan_trigger() ),
                ( (ILiteral) this.visitLiteral( p_context.literal() ) )
        );

        return p_context.plandefinition().stream().map( i -> {

            final Pair<IExpression, List<IExecution>> l_content = (Pair<IExpression, List<IExecution>>) this.visitPlandefinition( i );
            return new CPlan( l_trigger, l_content.getLeft(), l_content.getRight(), l_annotation );

        } ).collect( Collectors.toList() );
    }

    @Override
    public Object visitPlandefinition( final AgentParser.PlandefinitionContext p_context )
    {
        return new ImmutablePair<IExpression, List<IExecution>>(
                p_context.expression() == null ? null : (IExpression) this.visitExpression( p_context.expression() ),
                (List<IExecution>) this.visitBody( p_context.body() )
        );
    }

    @Override
    public Object visitAnnotations( final AgentParser.AnnotationsContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_SET;

        final Set<IAnnotation<?>> l_annotation = new HashSet<>();
        if ( p_context.annotation_atom() != null )
            p_context.annotation_atom().stream().map( i -> (IAnnotation<?>) this.visitAnnotation_atom( i ) ).forEach( l_annotation::add );
        if ( p_context.annotation_literal() != null )
            p_context.annotation_literal().stream().map( i -> (IAnnotation<?>) this.visitAnnotation_literal( i ) ).forEach( l_annotation::add );

        return l_annotation.isEmpty() ? Collections.EMPTY_SET : l_annotation;
    }

    @Override
    public Object visitAnnotation_atom( final AgentParser.Annotation_atomContext p_context )
    {
        if ( p_context.ATOMIC() != null )
            return new CAtomAnnotation<>( IAnnotation.EType.ATOMIC );

        if ( p_context.PARALLEL() != null )
            return new CAtomAnnotation<>( IAnnotation.EType.PARALLEL );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "atomannotation", p_context.getText() ) );
    }

    @Override
    public final Object visitAnnotation_literal( final AgentParser.Annotation_literalContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitAnnotation_numeric_literal( final AgentParser.Annotation_numeric_literalContext p_context )
    {
        if ( p_context.FUZZY() != null )
            return new CNumberAnnotation<>( IAnnotation.EType.FUZZY, (Number) this.visitNumber( p_context.number() ) );

        if ( p_context.SCORE() != null )
            return new CNumberAnnotation<>( IAnnotation.EType.SCORE, ( (Number) this.visitNumber( p_context.number() ) ).doubleValue() );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "numberannotation", p_context.getText() ) );
    }

    @Override
    public final Object visitPlan_trigger( final AgentParser.Plan_triggerContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitPlan_goal_trigger( final AgentParser.Plan_goal_triggerContext p_context )
    {
        switch ( p_context.getText() )
        {
            case "+!":
                return ITrigger.EType.ADDGOAL;
            case "-!":
                return ITrigger.EType.DELETEGOAL;

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "goaltrigger", p_context.getText() ) );
        }
    }

    @Override
    public Object visitPlan_belief_trigger( final AgentParser.Plan_belief_triggerContext p_context )
    {
        switch ( p_context.getText() )
        {
            case "+":
                return ITrigger.EType.ADDBELIEF;
            case "-":
                return ITrigger.EType.DELETEBELIEF;

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "belieftrigger", p_context.getText() ) );
        }
    }

    @Override
    public final Object visitBody( final AgentParser.BodyContext p_context )
    {
        // filter null values of the body formular, because blank lines adds a null value, body-formula rule return an executable call everytime
        return p_context.body_formula().stream()
                        .filter( i -> i != null )
                        .map( i -> this.visitBody_formula( i ) )
                        .filter( i -> i instanceof IExecution )
                        // expression are encapsulate to get result
                        .map( i -> i instanceof IExpression ? new CRawAction<>( i ) : i )
                        .collect( Collectors.toList() );
    }

    @Override
    public final Object visitBody_formula( final AgentParser.Body_formulaContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitRepair_formula( final AgentParser.Repair_formulaContext p_context )
    {
        // a non-existing repair formula can return any object-item, so convert it
        // to executable structure, because the grammar rule must return an executable item
        if ( p_context.repair_formula() == null )
            return this.visitChildren( p_context );


        // if there exists any repair element, build a sequential hierarchie of repair calls
        if ( p_context.executable_term() != null )
            return new CRepair(
                    (IExecution) this.visitExecutable_term( p_context.executable_term() ),
                    (IExecution) this.visitRepair_formula( p_context.repair_formula() )
            );

        if ( p_context.test_action() != null )
            return new CRepair(
                    (IExecution) this.visitTest_action( p_context.test_action() ),
                    (IExecution) this.visitRepair_formula( p_context.repair_formula() )
            );

        if ( p_context.achievement_goal_action() != null )
            return new CRepair(
                    (IExecution) this.visitAchievement_goal_action( p_context.achievement_goal_action() ),
                    (IExecution) this.visitRepair_formula( p_context.repair_formula() )
            );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "repairelement", p_context.getText() ) );
    }

    @Override
    public Object visitUnification( final AgentParser.UnificationContext p_context )
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
    public Object visitUnification_constraint( final AgentParser.Unification_constraintContext p_context )
    {
        if ( p_context == null )
            return null;

        if ( p_context.expression() != null )
            return this.visitExpression( p_context.expression() );

        if ( p_context.variable() != null )
            return this.visitVariable( p_context.variable() );

        return null;
    }

    @Override
    public final Object visitBlock_formula( final AgentParser.Block_formulaContext p_context )
    {
        if ( p_context.body_formula() != null )
        {
            final LinkedList<IExecution> l_statement = new LinkedList<>();
            l_statement.add( (IExecution) this.visitBody_formula( p_context.body_formula() ) );
            return l_statement;
        }

        return this.visitBody( p_context.body() );
    }

    @Override
    public Object visitLambda( final AgentParser.LambdaContext p_context )
    {
        if ( p_context.lambda_return() != null )
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
    public Object visitLambda_initialization( final AgentParser.Lambda_initializationContext p_context )
    {
        if ( p_context.variable() != null )
            return new CRawAction<>( this.visitVariable( p_context.variable() ) );

        if ( p_context.literal() != null )
            return new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "lambdainitialization", p_context.getText() ) );
    }

    @Override
    public Object visitLambda_return( final AgentParser.Lambda_returnContext p_context )
    {
        return this.visitVariable( p_context.variable() );
    }

    @Override
    public Object visitExecutable_term( final AgentParser.Executable_termContext p_context )
    {
        if ( p_context.string() != null )
            return new CRawAction<>( this.visitString( p_context.string() ) );
        if ( p_context.number() != null )
            return new CRawAction<>( this.visitNumber( p_context.number() ) );
        if ( p_context.logicalvalue() != null )
            return new CRawAction<>( this.visitLogicalvalue( p_context.logicalvalue() ) );

        if ( p_context.executable_action() != null )
            return this.visitExecutable_action( p_context.executable_action() );
        if ( p_context.executable_rule() != null )
            return this.visitExecutable_rule( p_context.executable_rule() );

        if ( p_context.expression() != null )
            return this.visitExpression( p_context.expression() );
        if ( p_context.ternary_operation() != null )
            return this.visitTernary_operation( p_context.ternary_operation() );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "termunknown", p_context.getText() ) );
    }

    @Override
    public final Object visitAssignment_expression( final AgentParser.Assignment_expressionContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitAssignment_expression_singlevariable( final AgentParser.Assignment_expression_singlevariableContext p_context )
    {
        return new CSingleAssignment<>(
                (IVariable<?>) this.visitVariable( p_context.variable() ),
                (IExecution) this.visitExecutable_term( p_context.executable_term() )
        );
    }

    @Override
    public Object visitAssignment_expression_multivariable( final AgentParser.Assignment_expression_multivariableContext p_context )
    {
        return new CMultiAssignment<>(
                p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) ).collect( Collectors.toList() ),
                (IExecution) this.visitExecutable_term( p_context.executable_term() )
        );
    }

    @Override
    public Object visitUnary_expression( final AgentParser.Unary_expressionContext p_context )
    {
        switch ( p_context.unaryoperator().getText() )
        {
            case "++":
                return new CIncrement<>( (IVariable) this.visitVariable( p_context.variable() ) );

            case "--":
                return new CDecrement<>( (IVariable) this.visitVariable( p_context.variable() ) );

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "unaryoperator", p_context.getText() ) );
        }
    }

    @Override
    public Object visitAchievement_goal_action( final AgentParser.Achievement_goal_actionContext p_context )
    {
        if ( p_context.literal() != null )
            return new CAchievementGoalLiteral( (ILiteral) this.visitLiteral( p_context.literal() ), p_context.DOUBLEEXCLAMATIONMARK() != null );

        if ( p_context.variable() != null )
            return new CAchievementGoalVariable( (IVariable<?>) this.visitVariable( p_context.variable() ), p_context.DOUBLEEXCLAMATIONMARK() != null );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "achievmentgoal", p_context.getText() ) );
    }

    @Override
    public Object visitTernary_operation( final AgentParser.Ternary_operationContext p_context )
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
    public Object visitTest_action( final AgentParser.Test_actionContext p_context )
    {
        // dollar sign is used to recognize a rule
        return p_context.DOLLAR() != null
               ? new CTestRule( CPath.from( (String) this.visitAtom( p_context.atom() ) ) )
               : new CTestGoal( CPath.from( (String) this.visitAtom( p_context.atom() ) ) );
    }

    @Override
    public Object visitBelief_action( final AgentParser.Belief_actionContext p_context )
    {
        if ( p_context.PLUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.ADD );

        if ( p_context.MINUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.DELETE );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "beliefaction", p_context.getText() ) );
    }

    @Override
    public Object visitDeconstruct_expression( final AgentParser.Deconstruct_expressionContext p_context )
    {
        return new CDeconstruct<>(
                p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) ).collect( Collectors.toList() ),
                (ITerm) ( p_context.literal() != null ? this.visitLiteral( p_context.literal() ) : this.visitVariable( p_context.variable() ) )
        );
    }

    @Override
    public Object visitLiteral( final AgentParser.LiteralContext p_context )
    {
        return new CLiteral(
                p_context.AT() != null,
                p_context.STRONGNEGATION() != null,
                CPath.from( this.visitAtom( p_context.atom() ).toString() ),
                (Collection<ITerm>) this.visitTermlist( p_context.termlist() ),
                (Collection<ILiteral>) this.visitLiteralset( p_context.literalset() )
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitLiteralset( final AgentParser.LiteralsetContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_LIST;

        return p_context.literal().stream().map( i -> this.visitLiteral( i ) ).filter( i -> i != null ).collect( Collectors.toList() );
    }

    @Override
    public Object visitTerm( final AgentParser.TermContext p_context )
    {
        if ( p_context.string() != null )
            return this.visitString( p_context.string() );
        if ( p_context.number() != null )
            return this.visitNumber( p_context.number() );
        if ( p_context.logicalvalue() != null )
            return this.visitLogicalvalue( p_context.logicalvalue() );

        if ( p_context.literal() != null )
            return this.visitLiteral( p_context.literal() );
        if ( p_context.variable() != null )
            return this.visitVariable( p_context.variable() );

        if ( p_context.termlist() != null )
            return this.visitTermlist( p_context.termlist() );
        if ( p_context.expression() != null )
            return this.visitExpression( p_context.expression() );
        if ( p_context.ternary_operation() != null )
            return this.visitTernary_operation( p_context.ternary_operation() );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "termunknown", p_context.getText() ) );
    }

    @Override
    public Object visitTermlist( final AgentParser.TermlistContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_LIST;

        return p_context.term().stream().map( i -> this.visitTerm( i ) ).filter( i -> i != null ).map(
                i -> i instanceof ITerm ? (ITerm) i : CRawTerm.from( i )
        ).collect( Collectors.toList() );
    }

    @Override
    public final Object visitVariablelist( final AgentParser.VariablelistContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitNumber( final AgentParser.NumberContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- raw rules -------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitIntegernumber( final AgentParser.IntegernumberContext p_context )
    {
        return p_context.integernumber_negative() != null ? this.visitIntegernumber_negative( p_context.integernumber_negative() )
                                                          : this.visitIntegernumber_positive( p_context.integernumber_positive() );
    }

    @Override
    public Object visitIntegernumber_positive( final AgentParser.Integernumber_positiveContext p_context )
    {
        return Long.valueOf( p_context.getText() );
    }

    @Override
    public Object visitIntegernumber_negative( final AgentParser.Integernumber_negativeContext p_context )
    {
        return Long.valueOf( p_context.getText() );
    }

    @Override
    public Object visitFloatnumber( final AgentParser.FloatnumberContext p_context )
    {
        if ( p_context.getText().equals( "infinity" ) )
            return p_context.MINUS() == null ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

        final Double l_constant = lightjason.grammar.CCommon.NUMERICCONSTANT.get( p_context.getText() );
        if ( l_constant != null )
            return l_constant;

        return Double.valueOf( p_context.getText() );
    }

    @Override
    public Object visitLogicalvalue( final AgentParser.LogicalvalueContext p_context )
    {
        return p_context.TRUE() != null;
    }

    @Override
    public final Object visitConstant( final AgentParser.ConstantContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitString( final AgentParser.StringContext p_context )
    {
        // remove quotes
        final String l_text = p_context.getText();
        return l_text.length() < 3 ? "" : l_text.substring( 1, l_text.length() - 1 );
    }

    @Override
    public Object visitAtom( final AgentParser.AtomContext p_context )
    {
        return p_context.getText();
    }

    @Override
    public Object visitVariable( final AgentParser.VariableContext p_context )
    {
        return p_context.AT() == null ? new CVariable<>( p_context.getText() ) : new CMutexVariable<>( p_context.getText() );
    }

    @Override
    public Object visitExpression( final AgentParser.ExpressionContext p_context )
    {
        // bracket expression
        if ( p_context.expression_bracket() != null )
            return this.visitExpression_bracket( p_context.expression_bracket() );

        // or-expression
        return lightjason.grammar.CCommon.createLogicalBinaryExpression(
                EOperator.OR,
                (IExpression) this.visitExpression_logical_and( p_context.expression_logical_and() ),
                p_context.expression() != null
                ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                        Collectors.toList() )
                : Collections.<IExpression>emptyList()
        );
    }

    @Override
    public final Object visitExpression_bracket( final AgentParser.Expression_bracketContext p_context )
    {
        return this.visitExpression( p_context.expression() );
    }

    @Override
    public Object visitExpression_logical_and( final AgentParser.Expression_logical_andContext p_context )
    {
        return lightjason.grammar.CCommon.createLogicalBinaryExpression(
                EOperator.AND,
                (IExpression) this.visitExpression_logical_xor( p_context.expression_logical_xor() ),
                p_context.expression() != null
                ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                        Collectors.toList() )
                : Collections.<IExpression>emptyList()
        );
    }

    @Override
    public Object visitExpression_logical_xor( final AgentParser.Expression_logical_xorContext p_context )
    {
        if ( p_context.expression_logical_element() != null )
            return lightjason.grammar.CCommon.createLogicalBinaryExpression(
                    EOperator.XOR,
                    (IExpression) this.visitExpression_logical_element( p_context.expression_logical_element() ),
                    p_context.expression() != null
                    ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                            Collectors.toList() )
                    : Collections.<IExpression>emptyList()
            );

        if ( p_context.expression_logical_negation() != null )
            return this.visitExpression_logical_negation( p_context.expression_logical_negation() );

        if ( p_context.expression_numeric() != null )
            return this.visitExpression_numeric( p_context.expression_numeric() );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "logicallefthandside", p_context.getText() ) );
    }

    @Override
    public Object visitExpression_logical_negation( final AgentParser.Expression_logical_negationContext p_context )
    {
        return new CUnary( EOperator.NEGATION, (IExpression) this.visitExpression( p_context.expression() ) );
    }

    @Override
    public Object visitExpression_logical_element( final AgentParser.Expression_logical_elementContext p_context )
    {
        if ( p_context.logicalvalue() != null )
            return new CAtom( this.visitLogicalvalue( p_context.logicalvalue() ) );

        if ( p_context.variable() != null )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( p_context.unification() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitUnification( p_context.unification() ) );

        if ( p_context.executable_action() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_action( p_context.executable_action() ) );

        if ( p_context.executable_rule() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_rule( p_context.executable_rule() ) );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "logicalelement", p_context.getText() ) );
    }

    @Override
    public Object visitExpression_numeric( final AgentParser.Expression_numericContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_relation( p_context.expression_numeric_relation() );

        if ( p_context.EQUAL() != null )
            return new CComparable(
                    EOperator.EQUAL,
                    (IExpression) this.visitExpression_numeric_relation( p_context.expression_numeric_relation() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.NOTEQUAL() != null )
            return new CComparable(
                    EOperator.NOTEQUAL,
                    (IExpression) this.visitExpression_numeric_relation( p_context.expression_numeric_relation() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "compareoperator", p_context.getText() ) );
    }

    @Override
    public Object visitExpression_numeric_relation( final AgentParser.Expression_numeric_relationContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_additive( p_context.expression_numeric_additive() );

        if ( p_context.GREATER() != null )
            return new CRelational(
                    EOperator.GREATER,
                    (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.GREATEREQUAL() != null )
            return new CRelational(
                    EOperator.GREATEREQUAL,
                    (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.LESS() != null )
            return new CRelational(
                    EOperator.LESS,
                    (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.LESSEQUAL() != null )
            return new CRelational(
                    EOperator.LESSEQUAL,
                    (IExpression) this.visitExpression_numeric_additive( p_context.expression_numeric_additive() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "relationaloperator", p_context.getText() ) );
    }

    @Override
    public Object visitExpression_numeric_additive( final AgentParser.Expression_numeric_additiveContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() );

        if ( p_context.PLUS() != null )
            return new CAdditive(
                    EOperator.PLUS,
                    (IExpression) this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.MINUS() != null )
            return new CAdditive(
                    EOperator.MINUS,
                    (IExpression) this.visitExpression_numeric_multiplicative( p_context.expression_numeric_multiplicative() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "additiveoperator", p_context.getText() ) );
    }

    @Override
    public Object visitExpression_numeric_multiplicative( final AgentParser.Expression_numeric_multiplicativeContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_power( p_context.expression_numeric_power() );

        if ( p_context.MULTIPLY() != null )
            return new CMultiplicative(
                    EOperator.MULTIPLY,
                    (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.SLASH() != null )
            return new CMultiplicative(
                    EOperator.DIVIDE,
                    (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        if ( p_context.MODULO() != null )
            return new CMultiplicative(
                    EOperator.MODULO,
                    (IExpression) this.visitExpression_numeric_power( p_context.expression_numeric_power() ),
                    (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
            );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "multiplicativeoperator", p_context.getText() ) );
    }

    @Override
    public Object visitExpression_numeric_power( final AgentParser.Expression_numeric_powerContext p_context )
    {
        if ( p_context.expression_numeric() == null )
            return this.visitExpression_numeric_element( p_context.expression_numeric_element() );

        return new CPower(
                EOperator.POWER,
                (IExpression) this.visitExpression_numeric_element( p_context.expression_numeric_element() ),
                (IExpression) this.visitExpression_numeric( p_context.expression_numeric() )
        );
    }

    @Override
    public Object visitExpression_numeric_element( final AgentParser.Expression_numeric_elementContext p_context )
    {

        if ( p_context.number() != null )
            return new CAtom( this.visitNumber( p_context.number() ) );

        if ( p_context.variable() != null )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        if ( p_context.executable_action() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_action( p_context.executable_action() ) );

        if ( p_context.executable_rule() != null )
            return new CProxyReturnExpression<>( (IExecution) this.visitExecutable_rule( p_context.executable_rule() ) );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "numericelement", p_context.getText() ) );
    }

    @Override
    public Object visitExecutable_action( final AgentParser.Executable_actionContext p_context )
    {
        return new CProxyAction( m_actions, (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public Object visitExecutable_rule( final AgentParser.Executable_ruleContext p_context )
    {
        return new CAchievmentRule( (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public final Object visitUnaryoperator( final AgentParser.UnaryoperatorContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitBinaryoperator( final AgentParser.BinaryoperatorContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Set<ILiteral> getInitialBeliefs()
    {
        return m_InitialBeliefs;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- getter structure ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Set<IPlan> getPlans()
    {
        return m_plans;
    }

    @Override
    public final Set<IRule> getRules()
    {
        return new HashSet<>( m_rules.values() );
    }

    @Override
    public final ILiteral getInitialGoal()
    {
        return m_InitialGoal;
    }

    /**
     * create a rule placeholder object
     *
     * @param p_context logical rule context
     * @return placeholder rule
     */
    protected Object visitLogicrulePlaceHolder( final AgentParser.LogicruleContext p_context )
    {
        return new CRulePlaceholder( (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
