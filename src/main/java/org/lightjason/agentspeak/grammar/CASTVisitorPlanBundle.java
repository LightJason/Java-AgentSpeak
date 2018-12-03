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
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.grammar.builder.CAgentSpeak;
import org.lightjason.agentspeak.grammar.builder.CTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
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
     * action generator
     */
    private final IActionGenerator m_actions;
    /**
     * set with lambda-streaming structure
     */
    private final Set<ILambdaStreaming<?>> m_lambdastream;

    /**
     * ctor
     *
     * @param p_actions action generator
     * @param p_lambdastreaming lambda streaming
     */
    public CASTVisitorPlanBundle( @Nonnull final IActionGenerator p_actions, @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming )
    {
        m_actions = p_actions;
        m_lambdastream = p_lambdastreaming;
        LOGGER.info( MessageFormat.format( "create parser with action generator : {0}", m_actions ) );
    }


    // --- plan bundle rules -----------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitPlanbundle( final PlanBundleParser.PlanbundleContext p_context )
    {
        p_context.belief()
                 .stream()
                 .map( i -> (ILiteral) this.visit( i ) )
                 .forEach( i -> m_initialbeliefs.add( i ) );

        p_context.logicrule()
                 .stream()
                 .flatMap( i -> (Stream<IRule>) this.visit( i ) )
                 .forEach( i -> m_rules.put( i.identifier().fqnfunctor(), i ) );

        p_context.plan()
                 .stream()
                 .flatMap( i -> (Stream<IPlan>) this.visit( i ) )
                 .forEach( i -> m_plans.add( i ) );

        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- AgentSpeak(L++) rules -------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitBelief( final PlanBundleParser.BeliefContext p_context )
    {
        return this.visit( p_context.literal() );
    }

    @Override
    public Object visitLogicrule( final PlanBundleParser.LogicruleContext p_context )
    {
        return CAgentSpeak.rule(
            this,
            p_context.literal(),
            p_context.body()
        );
    }

    @Override
    public Object visitPlan( final PlanBundleParser.PlanContext p_context )
    {
        return CAgentSpeak.plan(
            this,
            p_context.ANNOTATION(),
            p_context.plantrigger(),
            p_context.literal(),
            p_context.plandefinition()
        );
    }

    @Override
    public Object visitPlantrigger( final PlanBundleParser.PlantriggerContext p_context )
    {
        return CAgentSpeak.plantrigger( p_context );
    }

    @Override
    public Object visitPlandefinition( final PlanBundleParser.PlandefinitionContext p_context )
    {
        return CAgentSpeak.plandefinition( this, p_context.expression(), p_context.body() );
    }

    @Override
    public Object visitBody( final PlanBundleParser.BodyContext p_context )
    {
        return CAgentSpeak.repair( this, p_context.repairformula() );
    }

    @Override
    public Object visitRepairformula( final PlanBundleParser.RepairformulaContext p_context )
    {
        return CAgentSpeak.repairformula( this, p_context.bodyformula() );
    }

    @Override
    public Object visitBlockformula( final PlanBundleParser.BlockformulaContext p_context )
    {
        return CAgentSpeak.blockformular( this, p_context.repairformula(), p_context.body() );
    }

    @Override
    public Object visitBodyformula( final PlanBundleParser.BodyformulaContext p_context )
    {
        return CAgentSpeak.bodyformular(
            this,
            p_context.ternaryoperation(),
            p_context.beliefaction(),
            p_context.expression(),
            p_context.deconstructexpression(),
            p_context.assignmentexpression(),
            p_context.unaryexpression(),
            p_context.testaction(),
            p_context.achievementgoal(),
            p_context.unification(),
            p_context.lambda()
        );
    }




    @Override
    public Object visitUnification( final PlanBundleParser.UnificationContext p_context )
    {
        return CAgentSpeak.unification(
            this,
            p_context.AT(),
            p_context.literal(),
            p_context.unificationconstraint()
        );
    }

    @Override
    public Object visitUnificationconstraint( final PlanBundleParser.UnificationconstraintContext p_context )
    {
        return CAgentSpeak.unificationconstraint( this, p_context.variable(), p_context.literal(), p_context.expression() );
    }




    @Override
    public Object visitAchievementgoal( final PlanBundleParser.AchievementgoalContext p_context )
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
    public Object visitTestaction( final PlanBundleParser.TestactionContext p_context )
    {
        return CAgentSpeak.executetestgoal(
            p_context.DOLLAR(),
            p_context.ATOM()
        );
    }

    @Override
    public Object visitBeliefaction( final PlanBundleParser.BeliefactionContext p_context )
    {
        return CAgentSpeak.executebelief(
            this,
            p_context.literal(),
            p_context.ARITHMETICOPERATOR3()
        );
    }

    @Override
    public Object visitDeconstructexpression( final PlanBundleParser.DeconstructexpressionContext p_context )
    {
        return CAgentSpeak.deconstruct(
            this,
            p_context.variablelist().variable(),
            p_context.literal(),
            p_context.variable()
        );
    }




    @Override
    public Object visitLambda( final PlanBundleParser.LambdaContext p_context )
    {
        return CAgentSpeak.lambda(
            this,
            Objects.nonNull( p_context.AT() ),
            p_context.lambdastream(),
            p_context.variable(),
            p_context.blockformula(),
            p_context.lambdareturn()
        );
    }

    @Override
    public Object visitLambdastream( final PlanBundleParser.LambdastreamContext p_context )
    {
        return CAgentSpeak.lambdastream(
            this,
            m_lambdastream,
            p_context.HASH(),
            p_context.NUMBER(),
            p_context.variable(),
            p_context.lambdaelement()
        );
    }

    @Override
    public Object visitLambdaelement( final PlanBundleParser.LambdaelementContext p_context )
    {
        return CAgentSpeak.lambdaelement(
            this,
            p_context.NUMBER(),
            p_context.variable()
        );
    }

    @Override
    public Object visitLambdareturn( final PlanBundleParser.LambdareturnContext p_context )
    {
        return this.visit( p_context.variable() );
    }

    @Override
    public Object visitExpression( final PlanBundleParser.ExpressionContext p_context )
    {
        return CAgentSpeak.expression(
            this,
            p_context.term(),
            p_context.unification(),
            p_context.STRONGNEGATION(),
            p_context.single,
            p_context.binaryoperator,
            p_context.lhs,
            p_context.rhs
        );
    }

    @Override
    public Object visitAssignmentexpression( final PlanBundleParser.AssignmentexpressionContext p_context )
    {
        return CAgentSpeak.assignmentexpression(
            this,
            p_context.assignmentexpressionsinglevariable(),
            p_context.assignmentexpressionmultivariable()
        );
    }

    @Override
    public Object visitAssignmentexpressionsinglevariable( final PlanBundleParser.AssignmentexpressionsinglevariableContext p_context )
    {
        return CAgentSpeak.singleassignment(
            this,
            p_context.variable(),
            p_context.ASSIGNOPERATOR(),
            p_context.ternaryoperation(),
            p_context.expression()
        );
    }

    @Override
    public Object visitAssignmentexpressionmultivariable( final PlanBundleParser.AssignmentexpressionmultivariableContext p_context )
    {
        return CAgentSpeak.multiassignment( this, p_context.variablelist(), p_context.expression() );
    }

    @Override
    public Object visitUnaryexpression( final PlanBundleParser.UnaryexpressionContext p_context )
    {
        return CAgentSpeak.executeunary( this, p_context.UNARYOPERATOR(), p_context.variable() );
    }




    @Override
    public Object visitTernaryoperation( final PlanBundleParser.TernaryoperationContext p_context )
    {
        return CAgentSpeak.executeternary(
            this,
            p_context.expression(),
            p_context.ternaryoperationtrue(),
            p_context.ternaryoperationfalse()
        );
    }

    @Override
    public Object visitTernaryoperationtrue( final PlanBundleParser.TernaryoperationtrueContext p_context )
    {
        return this.visit( p_context.expression() );
    }

    @Override
    public Object visitTernaryoperationfalse( final PlanBundleParser.TernaryoperationfalseContext p_context )
    {
        return this.visit( p_context.expression() );
    }




    @Override
    public Object visitExecuteaction( final PlanBundleParser.ExecuteactionContext p_context )
    {
        return CAgentSpeak.executeaction( this, p_context.literal(), m_actions );
    }

    @Override
    public Object visitExecuterule( final PlanBundleParser.ExecuteruleContext p_context )
    {
        return CAgentSpeak.executerule( this, p_context.literal(), p_context.executevariable() );
    }

    @Override
    public Object visitExecutevariable( final PlanBundleParser.ExecutevariableContext p_context )
    {
        return CAgentSpeak.passvaribaleliteral( this, p_context.variable(), p_context.termlist() );
    }




    @Override
    public Object visitTerm( final PlanBundleParser.TermContext p_context )
    {
        return CTerm.term(
            this,

            p_context.termvalue(),
            p_context.termvaluelist(),

            p_context.variable(),
            p_context.literal(),

            p_context.executeaction(),
            p_context.executerule(),
            p_context.executevariable()
        );
    }

    @Override
    public Object visitTermvalue( final PlanBundleParser.TermvalueContext p_context )
    {
        return CTerm.termvalue(
            this,

            p_context.STRING(),
            p_context.NUMBER(),
            p_context.LOGICALVALUE()
        );
    }

    @Override
    public Object visitTermvaluelist( final PlanBundleParser.TermvaluelistContext p_context )
    {
        return CTerm.termvaluelist( this, p_context.termvalue() );
    }

    @Override
    public Object visitLiteral( final PlanBundleParser.LiteralContext p_context )
    {
        return CTerm.literal( this, p_context.AT(), p_context.STRONGNEGATION(), p_context.ATOM(), p_context.termlist() );
    }

    @Override
    public Object visitTermlist( final PlanBundleParser.TermlistContext p_context )
    {
        return CTerm.termlist( this, p_context.term() );
    }

    @Override
    public Object visitVariable( final PlanBundleParser.VariableContext p_context )
    {
        return CTerm.variable( p_context.AT(), p_context.VARIABLEATOM() );
    }


    @Override
    public Object visitVariablelist( final PlanBundleParser.VariablelistContext p_context )
    {
        return CAgentSpeak.variablelist( this, p_context.variable() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- getter structure ------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public Set<ILiteral> initialbeliefs()
    {
        return m_initialbeliefs;
    }

    @Nonnull
    @Override
    public Set<IPlan> plans()
    {
        return m_plans;
    }

    @Nonnull
    @Override
    public Set<IRule> rules()
    {
        return new HashSet<>( m_rules.values() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------
}
