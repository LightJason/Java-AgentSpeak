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

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.lightjason.agentspeak.grammar.builder.CAgentSpeak;
import org.lightjason.agentspeak.grammar.builder.CTerm;
import org.lightjason.agentspeak.language.ILiteral;

import javax.annotation.Nonnull;
import java.util.Collections;


/**
 * parser for complex-datatypes
 */
@SuppressWarnings( {"all", "warnings", "unchecked", "unused", "cast"} )
public final class CASTVisitorManual extends AbstractParseTreeVisitor<Object> implements IASTVisitorManual
{
    /**
     * parsed literal
     */
    private ILiteral m_literal = ILiteral.EMPTY;


    // --- start rules -----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitRoot_literal( final ManualParser.Root_literalContext p_context )
    {
        m_literal = (ILiteral) this.visit( p_context.literal() );
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitTerm( final ManualParser.TermContext p_context )
    {
        return CTerm.term(
            this,

            p_context.termvalue(),
            p_context.termvaluelist(),

            p_context.variable(),
            p_context.literal(),

            p_context.execute_action(),
            p_context.execute_rule(),
            p_context.execute_variable()
        );
    }

    @Override
    public Object visitTermvalue( final ManualParser.TermvalueContext p_context )
    {
        return CTerm.termvalue(
            this,

            p_context.STRING(),
            p_context.NUMBER(),
            p_context.LOGICALVALUE()
        );
    }

    @Override
    public Object visitTermvaluelist( final ManualParser.TermvaluelistContext p_context )
    {
        return CTerm.termvaluelist( this, p_context.termvalue() );
    }

    @Override
    public Object visitLiteral( final ManualParser.LiteralContext p_context )
    {
        return CTerm.literal( this, p_context.AT(), p_context.STRONGNEGATION(), p_context.ATOM(), p_context.termlist() );
    }

    @Override
    public Object visitTermlist( final ManualParser.TermlistContext p_context )
    {
        return CTerm.termlist( this, p_context.term() );
    }

    @Override
    public Object visitVariable( final ManualParser.VariableContext p_context )
    {
        return CTerm.variable( p_context.AT(), p_context.VARIABLEATOM() );
    }

    @Override
    public Object visitVariablelist( final ManualParser.VariablelistContext p_context )
    {
        return CAgentSpeak.variablelist( this, p_context.variable() );
    }





    @Override
    public Object visitExecute_action( final ManualParser.Execute_actionContext p_context )
    {
        return CAgentSpeak.executeaction( this, p_context.literal(), Collections.emptyMap() );
    }

    @Override
    public Object visitExecute_rule( final ManualParser.Execute_ruleContext p_context )
    {
        return CAgentSpeak.executerule( this, p_context.literal(), p_context.execute_variable() );
    }

    @Override
    public Object visitExecute_variable( final ManualParser.Execute_variableContext p_context )
    {
        return CAgentSpeak.passvaribaleliteral( this, p_context.variable(), p_context.termlist() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- non used rules --------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Object visitBelief( final ManualParser.BeliefContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlan( final ManualParser.PlanContext p_context )
    {
        return null;
    }

    @Override
    public Object visitPlandefinition( final ManualParser.PlandefinitionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLogicrule( final ManualParser.LogicruleContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBody( final ManualParser.BodyContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBlock_formula( final ManualParser.Block_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression( final ManualParser.ExpressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitRepair_formula( final ManualParser.Repair_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBody_formula( final ManualParser.Body_formulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBelief_action( final ManualParser.Belief_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTest_action( final ManualParser.Test_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAchievement_goal_action( final ManualParser.Achievement_goal_actionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitDeconstruct_expression( final ManualParser.Deconstruct_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression( final ManualParser.Assignment_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression_singlevariable( final ManualParser.Assignment_expression_singlevariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignment_expression_multivariable( final ManualParser.Assignment_expression_multivariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnary_expression( final ManualParser.Unary_expressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation( final ManualParser.Ternary_operationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation_true( final ManualParser.Ternary_operation_trueContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernary_operation_false( final ManualParser.Ternary_operation_falseContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnification( final ManualParser.UnificationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnification_constraint( final ManualParser.Unification_constraintContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda( final ManualParser.LambdaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda_initialization( final ManualParser.Lambda_initializationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda_element( final ManualParser.Lambda_elementContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda_return( final ManualParser.Lambda_returnContext p_context )
    {
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- getter structure ------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public ILiteral literal()
    {
        return m_literal;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
