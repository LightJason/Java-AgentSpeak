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
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.CMutexVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
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
    public static ILiteral literal( @Nullable final TerminalNode p_at, @Nullable final TerminalNode p_strongnegation,
                                    @Nonnull final TerminalNode p_atom, @Nonnull final Collection<ITerm> p_termlist )
    {
        return CLiteral.from(
            Objects.nonNull( p_at ),
            Objects.nonNull( p_strongnegation ),
            CPath.from( p_atom.getText() ),
            p_termlist.stream()
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
     * build term.terminal values
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
     * build termlist
     *
     * @param p_visitor visitor
     * @param p_termstream term stream
     * @return term list
     */
    @Nonnull
    public static Collection<ITerm> termlist( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final Stream<? extends RuleContext> p_termstream )
    {
        return p_termstream
                    .map( p_visitor::visit )
                    .filter( Objects::nonNull )
                    .map( i -> i instanceof ITerm ? (ITerm) i : CRawTerm.from( i ) )
                    .collect( Collectors.toList() );
    }
}
