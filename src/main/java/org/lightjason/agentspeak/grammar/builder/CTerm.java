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

package org.lightjason.agentspeak.grammar.builder;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.CMutexVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * builder for term structure
 */
public final class CTerm
{
    /**
     * ctor
     */
    private CTerm()
    {

    }

    /**
     * build a literal
     *
     * @param p_at at terminal
     * @param p_strongnegation strongnegation terminal
     * @param p_atom atom terminal
     * @param p_termlist termlist
     * @return literal
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static ILiteral literal( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                    @Nullable final TerminalNode p_at, @Nullable final TerminalNode p_strongnegation,
                                    @Nonnull final TerminalNode p_atom, @Nullable final RuleContext p_termlist )
    {
        return Objects.isNull( p_termlist )
            ? CLiteral.of( Objects.nonNull( p_at ), Objects.nonNull( p_strongnegation ), CPath.of( p_atom.getText() ) )
            : CLiteral.of(
                Objects.nonNull( p_at ),
                Objects.nonNull( p_strongnegation ),
                CPath.of( p_atom.getText() ),
                (Stream<ITerm>) p_visitor.visit( p_termlist )
            );
    }

    /**
     * build variable
     *
     * @return variable
     */
    @Nonnull
    public static IVariable<?> variable( @Nullable final TerminalNode p_at, @Nonnull final TerminalNode p_variable )
    {
        return Objects.isNull( p_at )
               ? new CVariable<>( p_variable.getText() )
               : new CMutexVariable<>( p_variable.getText() );
    }

    /**
     * build term terminal values
     *
     * @param p_string string terminal
     * @param p_number number terminal
     * @param p_logicalvalue logical terminal
     * @return data object or null
     */
    @Nullable
    public static Object termterminals( @Nullable final TerminalNode p_string, @Nullable final TerminalNode p_number, @Nullable final TerminalNode p_logicalvalue )
    {
        if ( Objects.nonNull( p_string ) )
            return CRaw.stringvalue( p_string );

        if ( Objects.nonNull( p_number ) )
            return CRaw.numbervalue( p_number );

        if ( Objects.nonNull( p_logicalvalue ) )
            return CRaw.logicalvalue( p_logicalvalue );

        return null;
    }

    /**
     * build term
     *
     * @param p_visitor visitor
     * @param p_string string
     * @param p_number number
     * @param p_logic logical value
     * @param p_executeaction action execution
     * @param p_executerule rule execution
     * @param p_executevariable variable execution
     * @param p_variable variable
     * @param p_literal literal
     * @return term
     */
    @Nonnull
    public static Object term( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nullable final TerminalNode p_string, @Nullable final TerminalNode p_number,
                               @Nullable final TerminalNode p_logic, @Nullable final RuleContext p_executeaction, @Nullable final RuleContext p_executerule,
                               @Nullable final RuleContext p_executevariable, @Nullable final RuleContext p_variable, @Nullable final RuleContext p_literal )
    {
        final Object l_terminal = termterminals( p_string, p_number, p_logic );
        if ( Objects.nonNull( l_terminal ) )
            return l_terminal;

        if ( Objects.nonNull( p_executeaction ) )
            return p_visitor.visit( p_executeaction );
        if ( Objects.nonNull( p_executerule ) )
            return p_visitor.visit( p_executerule );
        if ( Objects.nonNull( p_executevariable ) )
            return p_visitor.visit( p_executevariable );

        if ( Objects.nonNull( p_literal ) )
            return p_visitor.visit( p_literal );
        if ( Objects.nonNull( p_variable ) )
            return p_visitor.visit( p_variable );

        throw new CIllegalArgumentException( CCommon.languagestring( CTerm.class, "termunknown" ) );
    }


    /**
     * build termlist
     *
     * @param p_visitor visitor
     * @param p_termlist term stream
     * @return term list
     */
    @Nonnull
    public static Stream<ITerm> termlist( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nullable final List<? extends RuleContext> p_termlist )
    {
        return Objects.isNull( p_termlist )
            ? Stream.empty()
            : p_termlist.stream()
                          .map( p_visitor::visit )
                          .filter( Objects::nonNull )
                          .map( i -> i instanceof ITerm ? (ITerm) i : CRawTerm.of( i ) );
    }
}
