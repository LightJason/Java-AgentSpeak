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

import org.lightjason.agentspeak.error.context.CExecutionIllegalStateExcepton;
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
public final class CBinaryExpression implements IBinaryExpression
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
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                           @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_return = CCommon.argumentlist();

        if ( !execute( m_lhs, p_parallel, p_context, p_argument, l_return ) || l_return.size() != 1 )
            throw new CExecutionIllegalStateExcepton( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "binary execution fails" ) );

        if ( !execute( m_rhs, p_parallel, p_context, p_argument, l_return ) || l_return.size() != 2 )
            throw new CExecutionIllegalStateExcepton( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "binary execution fails" ) );

        p_return.add(
            CRawTerm.of(
                m_operator.apply( l_return.get( 0 ), l_return.get( 1 ) )
            )
        );

        return Stream.of();
    }

    /**
     * execute expression and add return execution result if needed
     *
     * @param p_execution execution
     * @param p_parallel parallel execution
     * @param p_context execution context
     * @param p_argument argument list
     * @param p_return return list
     * @return execution result
     */
    private static boolean execute( @Nonnull final IExecution p_execution, final boolean p_parallel, @Nonnull final IContext p_context,
                                    @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final int l_arguments = p_return.size();
        final boolean l_result = p_context.agent().fuzzy().getValue().defuzzify(
            p_execution.execute( p_parallel, p_context, p_argument, p_return )
        );

        // if no result value exists from execution, just use defuzzificated execution result
        if ( p_return.size() == l_arguments )
            p_return.add( CRawTerm.of( l_result ) );

        return l_result;
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Stream.concat( m_lhs.variables(), m_rhs.variables() );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "( {0} {1} {2} )", m_lhs, m_operator, m_rhs );
    }
}
