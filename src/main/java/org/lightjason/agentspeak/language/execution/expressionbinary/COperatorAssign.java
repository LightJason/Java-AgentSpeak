/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.expressionbinary;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * assign operator expression
 */
public final class COperatorAssign implements IBinaryExpression
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 720534461732785140L;
    /**
     * left-hand-side
     */
    private final IVariable<?> m_lhs;
    /**
     * right-hand-side
     */
    private final ITerm m_rhs;
    /**
     * operator
     */
    private final EOperator m_operator;


    /**
     * ctor
     *
     * @param p_lhs left-hand-side variable
     * @param p_rhs right-hand-side data
     * @param p_operator operator
     */
    public COperatorAssign( final IVariable<?> p_lhs, final ITerm p_rhs, final EOperator p_operator )
    {
        m_lhs = p_lhs;
        m_operator = p_operator;
        m_rhs = p_rhs;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                         @Nonnull final List<ITerm> p_return )
    {
        final IVariable<Number> l_lhs = CCommon.replaceFromContext( p_context, m_lhs ).term();
        l_lhs.set( m_operator.apply( l_lhs.raw(), CCommon.replaceFromContext( p_context, m_rhs ).raw() ) );

        return CFuzzyValue.from( true );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} {1} {2}", m_lhs, m_operator, m_rhs );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            Stream.of( m_lhs ),
            m_rhs instanceof IVariable<?>
            ? Stream.of( m_rhs.term() )
            : Stream.empty()
        );
    }
}
