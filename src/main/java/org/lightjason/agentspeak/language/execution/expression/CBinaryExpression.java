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

package org.lightjason.agentspeak.language.execution.expression;

import org.lightjason.agentspeak.common.IBiFunction;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Arrays;
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
     * left-hand-side strict-bind
     */
    private final IBiFunction<ITerm, List<ITerm>, Boolean> m_lhsbind;

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

        // on boolean expression "or" & "and" the left-hand-side has got a more strict bind
        switch ( m_operator )
        {
            case AND:
                m_lhsbind = ( i, j ) ->
                {
                    if ( !i.<Boolean>raw() )
                        j.add( CRawTerm.of( false ) );
                    return !i.<Boolean>raw();
                };
                break;

            case OR:
                m_lhsbind = ( i, j ) ->
                {
                    if ( i.<Boolean>raw() )
                        j.add( CRawTerm.of( true ) );
                    return i.<Boolean>raw();
                };
                break;

            default:
                m_lhsbind = ( i, j ) -> false;
        }

    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_return = CCommon.argumentlist();

        // left-hand-side execution
        final IFuzzyValue<?>[] l_left = execute( m_lhs, p_parallel, p_context, p_argument, l_return );
        if ( l_return.size() != 1 )
            throw new CExecutionIllegalStateException( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "incorrectreturnargument" ) );

        // additional check for left-hand-arguments
        if ( m_lhsbind.apply( l_return.get( 0 ), p_return ) )
            return Stream.of();

        // right-hand-side execution
        final IFuzzyValue<?>[] l_right = execute( m_rhs, p_parallel, p_context, p_argument, l_return );
        if ( l_return.size() != 2 )
            throw new CExecutionIllegalStateException( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "incorrectreturnargument" ) );

        p_return.add(
            CRawTerm.of(
                m_operator.apply( l_return.get( 0 ), l_return.get( 1 ) )
            )
        );

        return Stream.concat( Arrays.stream( l_left ), Arrays.stream( l_right ) );
    }

    /**
     * execute expression and add return execution result if needed
     *
     * @param p_execution execution
     * @param p_parallel parallel execution
     * @param p_context execution context
     * @param p_argument argument list
     * @param p_return return list
     * @return return fuzzy result
     */
    private static IFuzzyValue<?>[] execute( @Nonnull final IExecution p_execution, final boolean p_parallel, @Nonnull final IContext p_context,
                                             @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final int l_arguments = p_return.size();
        final boolean l_result = p_context.agent().fuzzy().defuzzification().success(
            p_context.agent().fuzzy().defuzzification().apply(
                p_execution.execute( p_parallel, p_context, p_argument, p_return )
            )
        );

        // if no result value exists from execution, just use defuzzificated execution result
        if ( p_return.size() == l_arguments )
            p_return.add( CRawTerm.of( l_result ) );

        return l_result
               ? p_context.agent().fuzzy().membership().success().toArray( IFuzzyValue[]::new )
               : p_context.agent().fuzzy().membership().fail().toArray( IFuzzyValue[]::new );
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
