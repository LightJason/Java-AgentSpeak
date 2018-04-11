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
 * unify a literal
 */
public final class CVariableUnify extends CDefaultUnify
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8392150596878840771L;
    /**
     * unification variable with literal
     */
    private final IVariable<?> m_variable;

    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_literal variable with literal
     * @param p_variable expression
     */
    public CVariableUnify( final boolean p_parallel, @Nonnull final ILiteral p_literal, @Nonnull final IVariable<?> p_variable )
    {
        super( p_parallel, p_literal );
        m_variable = p_variable;
    }


    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}>>({1}, {2})", m_parallel ? "@" : "", m_value, m_variable );
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final Set<IVariable<?>> l_variables = p_context.agent()
                                                       .unifier()
                                                       .unify(
                                                           CCommon.replaceFromContext( p_context, m_variable ).raw(),
                                                           CCommon.replaceFromContext( p_context, m_value ).raw()
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
            Stream.of( m_variable ),
            super.variables()
        );
    }
}
