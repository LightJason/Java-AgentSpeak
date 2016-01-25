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
import com.google.common.collect.SetMultimap;
import lightjason.agent.action.IAction;
import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.error.CSyntaxErrorException;
import lightjason.language.CLiteral;
import lightjason.language.CMutexVariable;
import lightjason.language.CRawTerm;
import lightjason.language.CVariable;
import lightjason.language.ILiteral;
import lightjason.language.ITerm;
import lightjason.language.IVariable;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.action.CAchievementGoal;
import lightjason.language.execution.action.CBarrier;
import lightjason.language.execution.action.CBeliefAction;
import lightjason.language.execution.action.CDeconstruct;
import lightjason.language.execution.action.CIfElse;
import lightjason.language.execution.action.CLambdaExpression;
import lightjason.language.execution.action.CMultiAssignment;
import lightjason.language.execution.action.CProxyAction;
import lightjason.language.execution.action.CRawAction;
import lightjason.language.execution.action.CSingleAssignment;
import lightjason.language.execution.action.CTestGoal;
import lightjason.language.execution.annotation.CAtomAnnotation;
import lightjason.language.execution.annotation.CNumberAnnotation;
import lightjason.language.execution.annotation.CSymbolicAnnotation;
import lightjason.language.execution.annotation.IAnnotation;
import lightjason.language.execution.expression.CAtom;
import lightjason.language.execution.expression.EOperator;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.expression.logical.CBinary;
import lightjason.language.execution.expression.logical.CUnary;
import lightjason.language.execution.expression.numerical.CAdditive;
import lightjason.language.execution.expression.numerical.CComparable;
import lightjason.language.execution.expression.numerical.CMultiplicative;
import lightjason.language.execution.expression.numerical.CPower;
import lightjason.language.execution.expression.numerical.CRelational;
import lightjason.language.execution.unaryoperator.CDecrement;
import lightjason.language.execution.unaryoperator.CIncrement;
import lightjason.language.plan.CPlan;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.CTrigger;
import lightjason.language.plan.trigger.ITrigger;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * default abstract-syntax-tree (AST) visitor for plan-bundles and agent scripts
 *
 * @note methods are implemented twice agent and plan-bundle, because both use equal
 * AgentSpeak(L) grammer, but AntLR visitor does not support inheritance by the grammar definition
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public class CParser extends AbstractParseTreeVisitor<Object> implements IParseAgent, IParsePlanBundle
{
    /**
     * numeric constant values - infinity is defined manually
     */
    @SuppressWarnings( "serial" )
    protected static final Map<String, Double> NUMERICCONSTANT = new HashMap<String, Double>()
    {{

        put( "pi", Math.PI );
        put( "euler", Math.E );
        put( "lightspeed", 299792458.0 );
        put( "avogadro", 6.0221412927e23 );
        put( "boltzmann", 8.617330350e-15 );
        put( "gravity", 6.67408e-11 );
        put( "electron", 9.10938356e-31 );
        put( "neutron", 1674927471214e-27 );
        put( "proton", 1.6726219e-27 );

    }};
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
    protected final SetMultimap<ITrigger<?>, IPlan> m_plans = HashMultimap.create();
    /**
     * map with action definition
     */
    protected final Map<CPath, IAction> m_actions;

    /**
     * ctor
     *
     * @param p_actions set with actions
     */
    public CParser( final Set<IAction> p_actions )
    {
        m_actions = p_actions.stream().collect( Collectors.toMap( IAction::getName, i -> i ) );
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
        return null;
    }



    @Override
    public Object visitInitial_goal( final AgentParser.Initial_goalContext p_context )
    {
        m_InitialGoal = new CLiteral( p_context.atom().getText() );
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    // --- plan bundle rules -----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitPlanbundle( final PlanBundleParser.PlanbundleContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    // --- AgentSpeak(L) rules ---------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitBelief( final AgentParser.BeliefContext p_context )
    {
        return new CLiteral( (CLiteral) this.visitLiteral( p_context.literal() ), p_context.STRONGNEGATION() != null );
    }

    @Override
    public Object visitBelief( final PlanBundleParser.BeliefContext p_context )
    {
        return new CLiteral( (CLiteral) this.visitLiteral( p_context.literal() ), p_context.STRONGNEGATION() != null );
    }



    @Override
    public final Object visitPlans( final AgentParser.PlansContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitPlans( final PlanBundleParser.PlansContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public Object visitLogicrules( final AgentParser.LogicrulesContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitLogicrules( final PlanBundleParser.LogicrulesContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public Object visitPlan( final AgentParser.PlanContext p_context )
    {
        final ILiteral l_head = (ILiteral) this.visitLiteral( p_context.literal() );
        final ITrigger.EType l_trigger = (ITrigger.EType) this.visitPlan_trigger( p_context.plan_trigger() );
        final Set<IAnnotation<?>> l_annotation = (Set) this.visitAnnotations( p_context.annotations() );

        // parallel stream does not work with multi hashmap
        p_context.plandefinition().stream().forEach( i -> {

            final Pair<Object, List<IExecution>> l_content = (Pair<Object, List<IExecution>>) this.visitPlandefinition( i );
            final IPlan l_plan = new CPlan( new CTrigger( l_trigger, l_head.getFQNFunctor() ), l_head, l_content.getRight(), l_annotation );
            m_plans.put( l_plan.getTrigger(), l_plan );

        } );

        return null;
    }

    @Override
    public Object visitPlan( final PlanBundleParser.PlanContext p_context )
    {
        final ILiteral l_head = (ILiteral) this.visitLiteral( p_context.literal() );
        final ITrigger.EType l_trigger = (ITrigger.EType) this.visitPlan_trigger( p_context.plan_trigger() );
        final Set<IAnnotation<?>> l_annotation = (Set) this.visitAnnotations( p_context.annotations() );

        // parallel stream does not work with multi hashmap
        p_context.plandefinition().stream().forEach( i -> {

            final Pair<Object, List<IExecution>> l_content = (Pair<Object, List<IExecution>>) this.visitPlandefinition( i );
            final IPlan l_plan = new CPlan( new CTrigger( l_trigger, l_head.getFQNFunctor() ), l_head, l_content.getRight(), l_annotation );
            m_plans.put( l_plan.getTrigger(), l_plan );

        } );

        return null;
    }



    @Override
    public Object visitPlandefinition( final AgentParser.PlandefinitionContext p_context )
    {
        return new ImmutablePair<Object, List<IExecution>>(
                this.visitPlan_context( p_context.plan_context() ), (List<IExecution>) this.visitBody( p_context.body() ) );
    }

    @Override
    public Object visitPlandefinition( final PlanBundleParser.PlandefinitionContext p_context )
    {
        return new ImmutablePair<Object, List<IExecution>>(
                this.visitPlan_context( p_context.plan_context() ), (List<IExecution>) this.visitBody( p_context.body() ) );
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
    public Object visitAnnotations( final PlanBundleParser.AnnotationsContext p_context )
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
            return new CAtomAnnotation( IAnnotation.EType.ATOMIC );

        if ( p_context.EXCLUSIVE() != null )
            return new CAtomAnnotation( IAnnotation.EType.EXCLUSIVE );

        if ( p_context.PARALLEL() != null )
            return new CAtomAnnotation( IAnnotation.EType.PARALLEL );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "atomannotation", p_context.getText() ) );
    }

    @Override
    public Object visitAnnotation_atom( final PlanBundleParser.Annotation_atomContext p_context )
    {
        if ( p_context.ATOMIC() != null )
            return new CAtomAnnotation( IAnnotation.EType.ATOMIC );

        if ( p_context.EXCLUSIVE() != null )
            return new CAtomAnnotation( IAnnotation.EType.EXCLUSIVE );

        if ( p_context.PARALLEL() != null )
            return new CAtomAnnotation( IAnnotation.EType.PARALLEL );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "atomannotation", p_context.getText() ) );
    }



    @Override
    public final Object visitAnnotation_literal( final AgentParser.Annotation_literalContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitAnnotation_literal( final PlanBundleParser.Annotation_literalContext p_context )
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
    public Object visitAnnotation_numeric_literal( final PlanBundleParser.Annotation_numeric_literalContext p_context )
    {
        if ( p_context.FUZZY() != null )
            return new CNumberAnnotation<>( IAnnotation.EType.FUZZY, (Number) this.visitNumber( p_context.number() ) );

        if ( p_context.SCORE() != null )
            return new CNumberAnnotation<>( IAnnotation.EType.SCORE, ( (Number) this.visitNumber( p_context.number() ) ).longValue() );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "numberannotation", p_context.getText() ) );
    }



    @Override
    public Object visitAnnotation_symbolic_literal( final AgentParser.Annotation_symbolic_literalContext p_context )
    {
        if ( p_context.EXPIRES() != null )
            return new CSymbolicAnnotation( IAnnotation.EType.EXPIRES, (ILiteral) this.visitAtom( p_context.atom() ) );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "symbolicliteralannotation", p_context.getText() ) );
    }

    @Override
    public Object visitAnnotation_symbolic_literal( final PlanBundleParser.Annotation_symbolic_literalContext p_context )
    {
        if ( p_context.EXPIRES() != null )
            return new CSymbolicAnnotation( IAnnotation.EType.EXPIRES, (ILiteral) this.visitAtom( p_context.atom() ) );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "symbolicliteralannotation", p_context.getText() ) );
    }



    @Override
    public final Object visitPlan_trigger( final AgentParser.Plan_triggerContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitPlan_trigger( final PlanBundleParser.Plan_triggerContext p_context )
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
    public Object visitPlan_goal_trigger( final PlanBundleParser.Plan_goal_triggerContext p_context )
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
            case "-+":
                return ITrigger.EType.CHANGEBELIEF;

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "belieftrigger", p_context.getText() ) );
        }
    }

    @Override
    public Object visitPlan_belief_trigger( final PlanBundleParser.Plan_belief_triggerContext p_context )
    {
        switch ( p_context.getText() )
        {
            case "+":
                return ITrigger.EType.ADDBELIEF;
            case "-":
                return ITrigger.EType.DELETEBELIEF;
            case "-+":
                return ITrigger.EType.CHANGEBELIEF;

            default:
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "belieftrigger", p_context.getText() ) );
        }
    }



    @Override
    public Object visitPlan_context( final AgentParser.Plan_contextContext p_context )
    {
        return p_context == null ? "" : p_context.getText();
    }

    @Override
    public Object visitPlan_context( final PlanBundleParser.Plan_contextContext p_context )
    {
        return p_context == null ? "" : p_context.getText();
    }



    @Override
    public final Object visitBody( final AgentParser.BodyContext p_context )
    {
        // filter null values of the body formular, because blank lines add a null value
        return p_context.body_formula().stream().filter( i -> i != null ).map( i -> this.createExecution( this.visitBody_formula( i ) ) ).filter(
                i -> i instanceof IExecution ).collect(
                Collectors.toList() );
    }

    @Override
    public final Object visitBody( final PlanBundleParser.BodyContext p_context )
    {
        // filter null values of the body formular, because blank lines add a null value
        return p_context.body_formula().stream().filter( i -> i != null ).map( i -> this.createExecution( this.visitBody_formula( i ) ) ).filter(
                i -> i instanceof IExecution ).collect(
                Collectors.toList() );
    }


    @Override
    public Object visitBarrier( final AgentParser.BarrierContext p_context )
    {
        return p_context.integernumber_positive() == null
               ? new CBarrier( (IExpression) this.visitExpression( p_context.expression() ) )
               : new CBarrier(
                       (IExpression) this.visitExpression( p_context.expression() ),
                       (Long) this.visitIntegernumber_positive( p_context.integernumber_positive() )
               );
    }

    @Override
    public Object visitBarrier( final PlanBundleParser.BarrierContext p_context )
    {
        return p_context.integernumber_positive() == null
               ? new CBarrier( (IExpression) this.visitExpression( p_context.expression() ) )
               : new CBarrier(
                       (IExpression) this.visitExpression( p_context.expression() ),
                       (Long) this.visitIntegernumber_positive( p_context.integernumber_positive() )
               );
    }


    @Override
    public Object visitUnification( final AgentParser.UnificationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnification( final PlanBundleParser.UnificationContext p_context )
    {
        return null;
    }



    @Override
    public final Object visitBody_formula( final AgentParser.Body_formulaContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitBody_formula( final PlanBundleParser.Body_formulaContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public Object visitLogicrule( final AgentParser.LogicruleContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitLogicrule( final PlanBundleParser.LogicruleContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public final Object visitBlock_formula( final AgentParser.Block_formulaContext p_context )
    {
        if ( p_context.body_formula() != null )
        {
            final LinkedList<IExecution> l_statement = new LinkedList<>();
            l_statement.add( this.createExecution( this.visitBody_formula( p_context.body_formula() ) ) );
            return l_statement;
        }

        return this.visitBody( p_context.body() );
    }

    @Override
    public final Object visitBlock_formula( final PlanBundleParser.Block_formulaContext p_context )
    {
        if ( p_context.body_formula() != null )
        {
            final LinkedList<IExecution> l_statement = new LinkedList<>();
            l_statement.add( this.createExecution( this.visitBody_formula( p_context.body_formula() ) ) );
            return l_statement;
        }

        return this.visitBody( p_context.body() );
    }



    @Override
    public Object visitIf_else( final AgentParser.If_elseContext p_context )
    {
        return p_context.else_block() == null
               ? new CIfElse(
                (IExpression) this.visitExpression( p_context.expression() ), (List<IExecution>) this.visitBlock_formula( p_context.block_formula() ) )
               : new CIfElse(
                       (IExpression) this.visitExpression( p_context.expression() ), (List<IExecution>) this.visitBlock_formula( p_context.block_formula() ),
                       (List<IExecution>) this.visitElse_block( p_context.else_block() )
               );
    }

    @Override
    public Object visitIf_else( final PlanBundleParser.If_elseContext p_context )
    {
        return p_context.else_block() == null
               ? new CIfElse(
                (IExpression) this.visitExpression( p_context.expression() ), (List<IExecution>) this.visitBlock_formula( p_context.block_formula() ) )
               : new CIfElse(
                       (IExpression) this.visitExpression( p_context.expression() ), (List<IExecution>) this.visitBlock_formula( p_context.block_formula() ),
                       (List<IExecution>) this.visitElse_block( p_context.else_block() )
               );
    }



    @Override
    public Object visitElse_block( final AgentParser.Else_blockContext p_context )
    {
        return this.visitBlock_formula( p_context.block_formula() );
    }

    @Override
    public Object visitElse_block( final PlanBundleParser.Else_blockContext p_context )
    {
        return this.visitBlock_formula( p_context.block_formula() );
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
    public Object visitLambda( final PlanBundleParser.LambdaContext p_context )
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
            return this.createExecution( this.visitVariable( p_context.variable() ) );

        if ( p_context.term() != null )
            return this.createExecution( this.visitTerm( p_context.term() ) );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "lambdainitialization", p_context.getText() ) );
    }

    @Override
    public Object visitLambda_initialization( final PlanBundleParser.Lambda_initializationContext p_context )
    {
        if ( p_context.variable() != null )
            return this.createExecution( this.visitVariable( p_context.variable() ) );

        if ( p_context.term() != null )
            return this.createExecution( this.visitTerm( p_context.term() ) );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "lambdainitialization", p_context.getText() ) );
    }


    @Override
    public Object visitLambda_return( final AgentParser.Lambda_returnContext p_context )
    {
        return this.visitVariable( p_context.variable() );
    }

    @Override
    public Object visitLambda_return( final PlanBundleParser.Lambda_returnContext p_context )
    {
        return this.visitVariable( p_context.variable() );
    }

    @Override
    public Object visitAssignment_expression( final AgentParser.Assignment_expressionContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitAssignment_expression( final PlanBundleParser.Assignment_expressionContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public Object visitAssignment_expression_singlevariable( final AgentParser.Assignment_expression_singlevariableContext p_context )
    {
        return new CSingleAssignment<>( (IVariable<?>) this.visitVariable( p_context.variable() ), this.createExecution( this.visitTerm( p_context.term() ) ) );
    }

    @Override
    public Object visitAssignment_expression_singlevariable( final PlanBundleParser.Assignment_expression_singlevariableContext p_context )
    {
        return new CSingleAssignment<>( (IVariable<?>) this.visitVariable( p_context.variable() ), this.createExecution( this.visitTerm( p_context.term() ) ) );
    }



    @Override
    public Object visitAssignment_expression_multivariable( final AgentParser.Assignment_expression_multivariableContext p_context )
    {
        return new CMultiAssignment<>(
                p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) ).collect( Collectors.toList() ),
                this.createExecution( this.visitTerm( p_context.term() ) )
        );
    }

    @Override
    public Object visitAssignment_expression_multivariable( final PlanBundleParser.Assignment_expression_multivariableContext p_context )
    {
        return new CMultiAssignment<>(
                p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) ).collect( Collectors.toList() ),
                this.createExecution( this.visitTerm( p_context.term() ) )
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
    public Object visitUnary_expression( final PlanBundleParser.Unary_expressionContext p_context )
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
        return new CAchievementGoal( (ILiteral) this.visitLiteral( p_context.literal() ), p_context.DOUBLEEXCLAMATIONMARK() != null );
    }

    @Override
    public Object visitAchievement_goal_action( final PlanBundleParser.Achievement_goal_actionContext p_context )
    {
        return new CAchievementGoal( (ILiteral) this.visitLiteral( p_context.literal() ), p_context.DOUBLEEXCLAMATIONMARK() != null );
    }



    @Override
    public Object visitTest_goal_action( final AgentParser.Test_goal_actionContext p_context )
    {
        return new CTestGoal( (ILiteral) this.visitLiteral( p_context.literal() ) );
    }

    @Override
    public Object visitTest_goal_action( final PlanBundleParser.Test_goal_actionContext p_context )
    {
        return new CTestGoal( (ILiteral) this.visitLiteral( p_context.literal() ) );
    }



    @Override
    public Object visitBelief_action( final AgentParser.Belief_actionContext p_context )
    {
        if ( p_context.PLUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Add );

        if ( p_context.MINUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Delete );

        if ( p_context.MINUSPLUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Change );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "beliefaction", p_context.getText() ) );
    }

    @Override
    public Object visitBelief_action( final PlanBundleParser.Belief_actionContext p_context )
    {
        if ( p_context.PLUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Add );

        if ( p_context.MINUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Delete );

        if ( p_context.MINUSPLUS() != null )
            return new CBeliefAction( (ILiteral) this.visitLiteral( p_context.literal() ), CBeliefAction.EAction.Change );

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
    public Object visitDeconstruct_expression( final PlanBundleParser.Deconstruct_expressionContext p_context )
    {
        return new CDeconstruct<>(
                p_context.variablelist().variable().stream().map( i -> (IVariable<?>) this.visitVariable( i ) ).collect( Collectors.toList() ),
                (ITerm) ( p_context.literal() != null ? this.visitLiteral( p_context.literal() ) : this.visitVariable( p_context.variable() ) )
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitLiteral( final AgentParser.LiteralContext p_context )
    {
        return new CLiteral(
                p_context.AT() != null,
                this.visitAtom( p_context.atom() ).toString(),
                (Collection<ITerm>) this.visitTermlist( p_context.termlist() ),
                (Collection<ILiteral>) this.visitLiteralset( p_context.literalset() )
        );
    }

    @Override
    public Object visitLiteral( final PlanBundleParser.LiteralContext p_context )
    {
        return new CLiteral(
                p_context.AT() != null,
                this.visitAtom( p_context.atom() ).toString(),
                (Collection<ITerm>) this.visitTermlist( p_context.termlist() ),
                (Collection<ILiteral>) this.visitLiteralset( p_context.literalset() )
        );
    }



    @Override
    public Object visitLiteralset( final AgentParser.LiteralsetContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_LIST;

        return p_context.literal().stream().map( i -> this.visitLiteral( i ) ).filter( i -> i != null ).collect( Collectors.toList() );
    }

    @Override
    public Object visitLiteralset( final PlanBundleParser.LiteralsetContext p_context )
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

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "termunknown", p_context.getText() ) );
    }

    @Override
    public Object visitTerm( final PlanBundleParser.TermContext p_context )
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
    public Object visitTermlist( final PlanBundleParser.TermlistContext p_context )
    {
        if ( ( p_context == null ) || ( p_context.isEmpty() ) )
            return Collections.EMPTY_LIST;

        return p_context.term().stream().map( i -> this.visitTerm( i ) ).filter( i -> i != null ).map(
                i -> i instanceof ITerm ? (ITerm) i : CRawTerm.from( i )
        ).collect( Collectors.toList() );
    }



    @Override
    public Object visitVariablelist( final AgentParser.VariablelistContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitVariablelist( final PlanBundleParser.VariablelistContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- raw rules -------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitNumber( final AgentParser.NumberContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitNumber( final PlanBundleParser.NumberContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public final Object visitIntegernumber( final AgentParser.IntegernumberContext p_context )
    {
        return p_context.integernumber_negative() != null ? this.visitIntegernumber_negative( p_context.integernumber_negative() )
                                                          : this.visitIntegernumber_positive( p_context.integernumber_positive() );
    }

    @Override
    public Object visitIntegernumber( final PlanBundleParser.IntegernumberContext p_context )
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
    public Object visitIntegernumber_positive( final PlanBundleParser.Integernumber_positiveContext p_context )
    {
        return Long.valueOf( p_context.getText() );
    }

    @Override
    public Object visitIntegernumber_negative( final PlanBundleParser.Integernumber_negativeContext p_context )
    {
        return Long.valueOf( p_context.getText() );
    }



    @Override
    public Object visitFloatnumber( final AgentParser.FloatnumberContext p_context )
    {
        if ( p_context.getText().equals( "infinity" ) )
            return p_context.MINUS() == null ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

        final Double l_constant = NUMERICCONSTANT.get( p_context.getText() );
        if ( l_constant != null )
            return l_constant;

        return Double.valueOf( p_context.getText() );
    }

    @Override
    public Object visitFloatnumber( final PlanBundleParser.FloatnumberContext p_context )
    {
        if ( p_context.getText().equals( "infinity" ) )
            return p_context.MINUS() == null ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

        final Double l_constant = NUMERICCONSTANT.get( p_context.getText() );
        if ( l_constant != null )
            return l_constant;

        return Double.valueOf( p_context.getText() );
    }



    @Override
    public final Object visitLogicalvalue( final AgentParser.LogicalvalueContext p_context )
    {
        return p_context.TRUE() != null;
    }

    @Override
    public Object visitLogicalvalue( final PlanBundleParser.LogicalvalueContext p_context )
    {
        return p_context.TRUE() != null;
    }



    @Override
    public final Object visitConstant( final AgentParser.ConstantContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public Object visitConstant( final PlanBundleParser.ConstantContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public final Object visitString( final AgentParser.StringContext p_context )
    {
        // remove quotes
        final String l_text = p_context.getText();
        return l_text.length() < 3 ? "" : l_text.substring( 1, l_text.length() - 1 );
    }

    @Override
    public Object visitString( final PlanBundleParser.StringContext p_context )
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
    public Object visitAtom( final PlanBundleParser.AtomContext p_context )
    {
        return p_context.getText();
    }



    @Override
    public Object visitVariable( final AgentParser.VariableContext p_context )
    {
        return p_context.AT() == null ? new CVariable<>( p_context.getText() ) : new CMutexVariable<>( p_context.getText() );
    }

    @Override
    public Object visitVariable( final PlanBundleParser.VariableContext p_context )
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
        return this.createLogicalBinaryExpression(
                EOperator.OR,
                (IExpression) this.visitExpression_logical_and( p_context.expression_logical_and() ),
                p_context.expression() != null
                ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                        Collectors.toList() )
                : Collections.<IExpression>emptyList()
        );
    }

    @Override
    public Object visitExpression( final PlanBundleParser.ExpressionContext p_context )
    {
        // bracket expression
        if ( p_context.expression_bracket() != null )
            return this.visitExpression_bracket( p_context.expression_bracket() );

        // or-expression
        return this.createLogicalBinaryExpression(
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
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitExpression_bracket( final PlanBundleParser.Expression_bracketContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public Object visitExpression_logical_and( final AgentParser.Expression_logical_andContext p_context )
    {
        return this.createLogicalBinaryExpression(
                EOperator.AND,
                (IExpression) this.visitExpression_logical_xor( p_context.expression_logical_xor() ),
                p_context.expression() != null
                ? p_context.expression().stream().map( i -> (IExpression) this.visitExpression( i ) ).collect(
                        Collectors.toList() )
                : Collections.<IExpression>emptyList()
        );
    }

    @Override
    public Object visitExpression_logical_and( final PlanBundleParser.Expression_logical_andContext p_context )
    {
        return this.createLogicalBinaryExpression(
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
            return this.createLogicalBinaryExpression(
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
    public Object visitExpression_logical_xor( final PlanBundleParser.Expression_logical_xorContext p_context )
    {
        if ( p_context.expression_logical_element() != null )
            return this.createLogicalBinaryExpression(
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
    public Object visitExpression_logical_negation( final PlanBundleParser.Expression_logical_negationContext p_context )
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

        //if ( p_context.literal() != null )
        //    return this.createExecution( p_context.literal() );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "logicalelement", p_context.getText() ) );
    }

    @Override
    public Object visitExpression_logical_element( final PlanBundleParser.Expression_logical_elementContext p_context )
    {
        if ( p_context.logicalvalue() != null )
            return new CAtom( this.visitLogicalvalue( p_context.logicalvalue() ) );

        if ( p_context.variable() != null )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        //if ( p_context.literal() != null )
        //    return this.createExecution( p_context.literal() );

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
    public Object visitExpression_numeric( final PlanBundleParser.Expression_numericContext p_context )
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
    public Object visitExpression_numeric_relation( final PlanBundleParser.Expression_numeric_relationContext p_context )
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
    public Object visitExpression_numeric_additive( final PlanBundleParser.Expression_numeric_additiveContext p_context )
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
    public Object visitExpression_numeric_multiplicative( final PlanBundleParser.Expression_numeric_multiplicativeContext p_context )
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
    public Object visitExpression_numeric_power( final PlanBundleParser.Expression_numeric_powerContext p_context )
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

        //if ( p_context.literal() != null )
        //    return this.createExecution( p_context.literal() );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "numericelement", p_context.getText() ) );
    }

    @Override
    public Object visitExpression_numeric_element( final PlanBundleParser.Expression_numeric_elementContext p_context )
    {
        if ( p_context.number() != null )
            return new CAtom( this.visitNumber( p_context.number() ) );

        if ( p_context.variable() != null )
            return new CAtom( this.visitVariable( p_context.variable() ) );

        //if ( p_context.literal() != null )
        //    return this.createExecution( p_context.literal() );

        throw new CSyntaxErrorException( CCommon.getLanguageString( this, "numericelement", p_context.getText() ) );
    }



    @Override
    public final Object visitUnaryoperator( final AgentParser.UnaryoperatorContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitUnaryoperator( final PlanBundleParser.UnaryoperatorContext p_context )
    {
        return this.visitChildren( p_context );
    }



    @Override
    public final Object visitBinaryoperator( final AgentParser.BinaryoperatorContext p_context )
    {
        return this.visitChildren( p_context );
    }

    @Override
    public final Object visitBinaryoperator( final PlanBundleParser.BinaryoperatorContext p_context )
    {
        return this.visitChildren( p_context );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- getter and helper structure -------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Set<ILiteral> getInitialBeliefs()
    {
        return m_InitialBeliefs;
    }

    @Override
    public final SetMultimap<ITrigger<?>, IPlan> getPlans()
    {
        return m_plans;
    }

    @Override
    public final Map<String, Object> getRules()
    {
        return null;
    }

    @Override
    public final ILiteral getInitialGoal()
    {
        return m_InitialGoal;
    }

    /**
     * creates an executable structure of a parsed item
     *
     * @param p_item any parsed item
     * @return execution structure
     *
     * @bug literal call must be changes, literals which does not match an action must be an unifying call
     */
    protected IExecution createExecution( final Object p_item )
    {
        // body actions directly return
        if ( p_item instanceof IExecution )
            return (IExecution) p_item;

        // literals are actions
        if ( p_item instanceof ILiteral )
        {
            final ILiteral l_literal = (ILiteral) p_item;
            return new CProxyAction( m_actions, l_literal );
        }

        // otherwise only simple types encapsulate
        return p_item == null ? null : new CRawAction<>( p_item );
    }

    /**
     * creates a logical expression concationation with single operator
     *
     * @param p_operator operator
     * @param p_lefthandside left-hand-side expression
     * @param p_righthandside right-hand-side expressions
     * @return concat expression
     */
    protected IExpression createLogicalBinaryExpression( final EOperator p_operator, final IExpression p_lefthandside,
                                                         final Collection<IExpression> p_righthandside
    )
    {
        if ( ( !p_operator.isBinary() ) || ( !p_operator.isLogical() ) )
            throw new CSyntaxErrorException( CCommon.getLanguageString( this, "notbinarylogicoperator", p_operator ) );

        final List<IExpression> l_expression = new LinkedList<>();
        l_expression.add( p_lefthandside );
        l_expression.addAll( p_righthandside );

        // only left-hand-side is existing
        if ( l_expression.size() == 1 )
            return l_expression.get( 0 );

        // otherwise creare concat expression
        while ( l_expression.size() > 1 )
            l_expression.add( 0, new CBinary( p_operator, l_expression.remove( 0 ), l_expression.remove( 0 ) ) );

        return l_expression.get( 0 );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
