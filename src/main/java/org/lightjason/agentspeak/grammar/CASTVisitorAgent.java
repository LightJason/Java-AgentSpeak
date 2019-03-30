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

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.grammar.builder.CAgentSpeak;
import org.lightjason.agentspeak.grammar.builder.CTerm;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
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
    private ITrigger m_initialgoal;
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
     * lambda generator
     */
    private final ILambdaStreamingGenerator m_lambda;

    /**
     * ctor
     *
     * @param p_actions action generator
     * @param p_lambda lambda generator
     */
    public CASTVisitorAgent( @Nonnull final IActionGenerator p_actions, @Nonnull final ILambdaStreamingGenerator p_lambda )
    {
        m_lambda = p_lambda;
        m_actions = p_actions;
        LOGGER.info( MessageFormat.format( "create parser with action generator : {0}", m_actions ) );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- agent rules -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitAgent( final AgentParser.AgentContext p_context )
    {
        m_initialgoal = Objects.isNull( p_context.INITIALGOAL() )
                        ? ITrigger.EMPTY
                        : CTrigger.EType.ADDGOAL.builddefault( CLiteral.of( p_context.INITIALGOAL().getText().replace( "!", "" ).replace( ".", "" ) ) );

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
    public Object visitBelief( final AgentParser.BeliefContext p_context )
    {
        return this.visit( p_context.literal() );
    }

    @Override
    public Object visitLogicrule( final AgentParser.LogicruleContext p_context )
    {
        return CAgentSpeak.rule(
            this,
            p_context.literal(),
            p_context.body()
        );
    }

    @Override
    public Object visitPlan( final AgentParser.PlanContext p_context )
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
    public Object visitPlantrigger( final AgentParser.PlantriggerContext p_context )
    {
        return CAgentSpeak.plantrigger( p_context );
    }

    @Override
    public Object visitPlandefinition( final AgentParser.PlandefinitionContext p_context )
    {
        return CAgentSpeak.plandefinition( this, p_context.expression(), p_context.body() );
    }

    @Override
    public Object visitBody( final AgentParser.BodyContext p_context )
    {
        return CAgentSpeak.repair( this, p_context.repairformula() );
    }

    @Override
    public Object visitRepairformula( final AgentParser.RepairformulaContext p_context )
    {
        return CAgentSpeak.repairformula( this, p_context.bodyformula() );
    }

    @Override
    public Object visitBlockformula( final AgentParser.BlockformulaContext p_context )
    {
        return CAgentSpeak.blockformular( this, p_context.repairformula(), p_context.body() );
    }

    @Override
    public Object visitBodyformula( final AgentParser.BodyformulaContext p_context )
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
    public Object visitUnification( final AgentParser.UnificationContext p_context )
    {
        return CAgentSpeak.unification(
            this,
            p_context.AT(),
            p_context.literal(),
            p_context.unificationconstraint()
        );
    }

    @Override
    public Object visitUnificationconstraint( final AgentParser.UnificationconstraintContext p_context )
    {
        return CAgentSpeak.unificationconstraint( this, p_context.variable(), p_context.literal(), p_context.expression() );
    }



    @Override
    public Object visitAchievementgoal( final AgentParser.AchievementgoalContext p_context )
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
    public Object visitTestaction( final AgentParser.TestactionContext p_context )
    {
        return CAgentSpeak.executetestgoal(
            p_context.DOLLAR(),
            p_context.ATOM()
        );
    }

    @Override
    public Object visitBeliefaction( final AgentParser.BeliefactionContext p_context )
    {
        return CAgentSpeak.executebelief(
            this,
            p_context.literal(),
            p_context.ARITHMETICOPERATOR3()
        );
    }

    @Override
    public Object visitDeconstructexpression( final AgentParser.DeconstructexpressionContext p_context )
    {
        return CAgentSpeak.deconstruct(
            this,
            p_context.variablelist().variable(),
            p_context.literal(),
            p_context.variable()
        );
    }



    @Override
    public Object visitLambda( final AgentParser.LambdaContext p_context )
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
    public Object visitLambdastream( final AgentParser.LambdastreamContext p_context )
    {
        return CAgentSpeak.lambdastream(
            this,
            m_lambda,
            p_context.HASH(),
            p_context.NUMBER(),
            p_context.variable(),
            p_context.lambdaelement()
        );
    }

    @Override
    public Object visitLambdaelement( final AgentParser.LambdaelementContext p_context )
    {
        return CAgentSpeak.lambdaelement(
            this,
            p_context.NUMBER(),
            p_context.variable()
        );
    }

    @Override
    public Object visitLambdareturn( final AgentParser.LambdareturnContext p_context )
    {
        return this.visit( p_context.variable() );
    }



    @Override
    public Object visitExpression( final AgentParser.ExpressionContext p_context )
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
    public Object visitAssignmentexpression( final AgentParser.AssignmentexpressionContext p_context )
    {
        return CAgentSpeak.assignmentexpression(
            this,
            p_context.assignmentexpressionsinglevariable(),
            p_context.assignmentexpressionmultivariable()
        );
    }

    @Override
    public Object visitAssignmentexpressionsinglevariable( final AgentParser.AssignmentexpressionsinglevariableContext p_context )
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
    public Object visitAssignmentexpressionmultivariable( final AgentParser.AssignmentexpressionmultivariableContext p_context )
    {
        return CAgentSpeak.multiassignment( this, p_context.variablelist(), p_context.expression() );
    }

    @Override
    public Object visitUnaryexpression( final AgentParser.UnaryexpressionContext p_context )
    {
        return CAgentSpeak.executeunary( this, p_context.UNARYOPERATOR(), p_context.variable() );
    }



    @Override
    public Object visitTernaryoperation( final AgentParser.TernaryoperationContext p_context )
    {
        return CAgentSpeak.executeternary(
            this,
            p_context.expression(),
            p_context.ternaryoperationtrue(),
            p_context.ternaryoperationfalse()
        );
    }

    @Override
    public Object visitTernaryoperationtrue( final AgentParser.TernaryoperationtrueContext p_context )
    {
        return this.visit( p_context.expression() );
    }

    @Override
    public Object visitTernaryoperationfalse( final AgentParser.TernaryoperationfalseContext p_context )
    {
        return this.visit( p_context.expression() );
    }



    @Override
    public Object visitExecuteaction( final AgentParser.ExecuteactionContext p_context )
    {
        return CAgentSpeak.executeaction( this, p_context.literal(), m_actions );
    }

    @Override
    public Object visitExecuterule( final AgentParser.ExecuteruleContext p_context )
    {
        return CAgentSpeak.executerule( this, p_context.literal(), p_context.executevariable() );
    }

    @Override
    public Object visitExecutevariable( final AgentParser.ExecutevariableContext p_context )
    {
        return CAgentSpeak.passvaribaleliteral( this, p_context.variable(), p_context.termlist() );
    }



    @Override
    public Object visitTerm( final AgentParser.TermContext p_context )
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
    public Object visitTermvalue( final AgentParser.TermvalueContext p_context )
    {
        return CTerm.termvalue(
            this,

            p_context.STRING(),
            p_context.NUMBER(),
            p_context.LOGICALVALUE()
        );
    }

    @Override
    public Object visitTermvaluelist( final AgentParser.TermvaluelistContext p_context )
    {
        return CTerm.termvaluelist( this, p_context.termvalue() );
    }

    @Override
    public Object visitLiteral( final AgentParser.LiteralContext p_context )
    {
        return CTerm.literal( this, p_context.AT(), p_context.STRONGNEGATION(), p_context.ATOM(), p_context.termlist() );
    }

    @Override
    public Object visitTermlist( final AgentParser.TermlistContext p_context )
    {
        return CTerm.termlist( this, p_context.term() );
    }

    @Override
    public Object visitVariable( final AgentParser.VariableContext p_context )
    {
        return CTerm.variable( p_context.AT(), p_context.VARIABLEATOM() );
    }

    @Override
    public Object visitVariablelist( final AgentParser.VariablelistContext p_context )
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

    @Nonnull
    @Override
    public ITrigger initialgoal()
    {
        return m_initialgoal;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
