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

package org.lightjason.agentspeak.language.execution.expression;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * binary expression
 */
public class CBinaryExpression implements IBinaryExpression
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2672638767158023575L;
    /**
     * operator
     */
    private final EBinaryOperator m_operator;
    /**
     * left-hand-side
     */
    private final IExecution m_lhs;
    /**
     * right-hand-side
     */
    private final IExecution m_rhs;

    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_lhs left-hand-side
     * @param p_rhs right-hand-side
     */
    public CBinaryExpression( @Nonnull final EBinaryOperator p_operator, @Nonnull final IExecution p_lhs, @Nonnull final IExecution p_rhs )
    {
        m_operator = p_operator;
        m_lhs = p_lhs;
        m_rhs = p_rhs;
    }


    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                         @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_return = CCommon.argumentlist();

        if ( ( !m_lhs.execute( p_parallel, p_context, p_argument, l_return ).value() ) || ( l_return.size() != 1 ) )
            return CFuzzyValue.of( false );

        if ( ( !m_rhs.execute( p_parallel, p_context, p_argument, l_return ).value() ) || ( l_return.size() != 2 ) )
            return CFuzzyValue.of( false );

        p_return.add(
            CRawTerm.of(
                m_operator.apply( l_return.get( 0 ), l_return.get( 1 ) )
            )
        );

        return CFuzzyValue.of( true );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.concat( m_lhs.variables(), m_rhs.variables() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "( {0} {1} {2} )", m_lhs, m_operator, m_rhs );
    }
}
