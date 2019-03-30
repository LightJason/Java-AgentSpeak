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

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.grammar.builder.CAgentSpeak;
import org.lightjason.agentspeak.grammar.builder.CTerm;
import org.lightjason.agentspeak.language.ILiteral;

import javax.annotation.Nonnull;


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

            p_context.executeaction(),
            p_context.executerule(),
            p_context.executevariable()
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
    public Object visitExecuteaction( final ManualParser.ExecuteactionContext p_context )
    {
        return CAgentSpeak.executeaction( this, p_context.literal(), IActionGenerator.EMPTY );
    }

    @Override
    public Object visitExecuterule( final ManualParser.ExecuteruleContext p_context )
    {
        return CAgentSpeak.executerule( this, p_context.literal(), p_context.executevariable() );
    }

    @Override
    public Object visitExecutevariable( final ManualParser.ExecutevariableContext p_context )
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
    public Object visitPlantrigger( final ManualParser.PlantriggerContext p_context )
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
    public Object visitBlockformula( final ManualParser.BlockformulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitExpression( final ManualParser.ExpressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitRepairformula( final ManualParser.RepairformulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBodyformula( final ManualParser.BodyformulaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitBeliefaction( final ManualParser.BeliefactionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTestaction( final ManualParser.TestactionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAchievementgoal( final ManualParser.AchievementgoalContext p_context )
    {
        return null;
    }

    @Override
    public Object visitDeconstructexpression( final ManualParser.DeconstructexpressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignmentexpression( final ManualParser.AssignmentexpressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignmentexpressionsinglevariable( final ManualParser.AssignmentexpressionsinglevariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitAssignmentexpressionmultivariable( final ManualParser.AssignmentexpressionmultivariableContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnaryexpression( final ManualParser.UnaryexpressionContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernaryoperation( final ManualParser.TernaryoperationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernaryoperationtrue( final ManualParser.TernaryoperationtrueContext p_context )
    {
        return null;
    }

    @Override
    public Object visitTernaryoperationfalse( final ManualParser.TernaryoperationfalseContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnification( final ManualParser.UnificationContext p_context )
    {
        return null;
    }

    @Override
    public Object visitUnificationconstraint( final ManualParser.UnificationconstraintContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambda( final ManualParser.LambdaContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambdastream( final ManualParser.LambdastreamContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambdaelement( final ManualParser.LambdaelementContext p_context )
    {
        return null;
    }

    @Override
    public Object visitLambdareturn( final ManualParser.LambdareturnContext p_context )
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
