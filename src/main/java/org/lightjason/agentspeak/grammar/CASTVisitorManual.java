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
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.grammar.builder.CTerm;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;


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
    public final Object visitExecute_action( final ManualParser.Execute_actionContext p_context )
    {
        return null;
    }

    @Override
    public final Object visitExecute_rule( final ManualParser.Execute_ruleContext p_context )
    {
        return null;
    }

    @Override
    public final Object visitExecute_variable( final ManualParser.Execute_variableContext p_context )
    {
        return null;
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
        if ( ( Objects.isNull( p_context ) ) || ( p_context.isEmpty() ) )
            return Collections.<ITerm>emptyList();

        return p_context.term().stream()
                        .map( i -> this.visitTerm( i ) )
                        .filter( i -> Objects.nonNull( i ) )
                        .map( i -> i instanceof ITerm ? (ITerm) i : CRawTerm.of( i ) )
                        .collect( Collectors.toList() );
    }

    @Override
    public final Object visitVariablelist( final ManualParser.VariablelistContext p_context )
    {
        return null;
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
