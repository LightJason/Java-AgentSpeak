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
        m_literal = (ILiteral) this.visitChildren( p_context );
        return null;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- simple datatypes ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Object visitTerm( final ManualParser.TermContext p_context )
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
    public final Object visitExecute_action( final ManualParser.Execute_actionContext p_context )
    {
        return CAgentSpeak.executeaction( this, p_context.literal(), Collections.emptyMap() );
    }

    @Override
    public final Object visitExecute_rule( final ManualParser.Execute_ruleContext p_context )
    {
        return CAgentSpeak.executerule( this, p_context.literal(), p_context.execute_variable() );
    }

    @Override
    public final Object visitExecute_variable( final ManualParser.Execute_variableContext p_context )
    {
        return CAgentSpeak.passvaribaleliteral( this, p_context.variable(), p_context.termlist() );
    }

    @Override
    public final Object visitLiteral( final ManualParser.LiteralContext p_context )
    {
        return CTerm.literal( this, p_context.AT(), p_context.STRONGNEGATION(), p_context.ATOM(), p_context.termlist() );
    }

    @Override
    public final Object visitVariable( final ManualParser.VariableContext p_context )
    {
        return CTerm.variable( p_context.AT(), p_context.VARIABLEATOM() );
    }

    @Override
    public final Object visitTermlist( final ManualParser.TermlistContext p_context )
    {
        return CTerm.termlist( this, p_context.term() );
    }

    @Override
    public final Object visitVariablelist( final ManualParser.VariablelistContext p_context )
    {
        return CAgentSpeak.variablelist( this, p_context.variable() );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    // --- getter structure ------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public final ILiteral literal()
    {
        return m_literal;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
