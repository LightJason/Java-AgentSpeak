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

package org.lightjason.agentspeak.language.execution.unify;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;


/**
 * unifier a static literal
 */
public final class CLiteralUnify extends CDefaultUnify
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3657631715981402911L;
    /**
     * literal for unification
     */
    private final ILiteral m_unifyliteral;

    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_literal literal
     */
    public CLiteralUnify( final boolean p_parallel, @Nonnull final ILiteral p_literal, @Nonnull final ILiteral p_unifyliteral )
    {
        super( p_parallel, p_literal );
        m_unifyliteral = p_unifyliteral;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}>>({1}, {2})", m_parallel ? "@" : "", m_value, m_unifyliteral );
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                         @Nonnull final List<ITerm> p_return )
    {
        final Set<IVariable<?>> l_variables = p_context.agent()
                                                       .unifier()
                                                       .unify(
                                                           CCommon.replacebycontext( p_context, m_unifyliteral ).raw(),
                                                           CCommon.replacebycontext( p_context, m_value ).raw()
                                                       );

        if ( l_variables.size() != m_variablenumber )
            return CFuzzyValue.of( false );

        CCommon.updatecontext( p_context, l_variables.stream() );
        return CFuzzyValue.of( true );
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            literalvariable( m_unifyliteral ),
            super.variables()
        );
    }

    /**
     * get a stream of all variables within the literal
     *
     * @param p_literal literal
     * @return variable stream
     */
    private static Stream<IVariable<?>> literalvariable( @Nonnull final ILiteral p_literal )
    {
        return p_literal.values()
                        .flatMap( i -> i instanceof ILiteral ? literalvariable( i.term() ) : Stream.of( i ) )
                        .filter( i -> i instanceof IVariable<?> )
                        .map( ITerm::term );
    }
}
