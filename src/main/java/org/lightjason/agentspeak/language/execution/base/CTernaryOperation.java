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

package org.lightjason.agentspeak.language.execution.base;

import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * if-else structure
 */
public final class CTernaryOperation extends IBaseExecution<IExpression>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5518298371186501138L;
    /**
     * true execution block
     */
    private final IExecution m_true;
    /**
     * false execution block
     */
    private final IExecution m_false;


    /**
     * ctor
     *
     * @param p_expression expression
     * @param p_true true execution block
     * @param p_false false execution block
     */
    public CTernaryOperation( @Nonnull final IExpression p_expression, @Nonnull final IExecution p_true, @Nonnull final IExecution p_false )
    {
        super( p_expression );
        m_true = p_true;
        m_false = p_false;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_argument = CCommon.argumentlist();

        if ( !p_context.agent().fuzzy().defuzzification().success(
                p_context.agent().fuzzy().defuzzification().apply(
                    m_value.execute( p_parallel, p_context, Collections.emptyList(), l_argument )
                )
        ) )
            return m_false.execute( p_parallel, p_context, Collections.emptyList(), p_return );

        if ( l_argument.size() != 1 )
            throw new CExecutionIllegalStateException(
                p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "incorrectreturnargument"
            ) );

        return l_argument.get( 0 ).raw()
               ? m_true.execute( p_parallel, p_context, Collections.emptyList(), p_return )
               : m_false.execute( p_parallel, p_context, Collections.emptyList(), p_return );
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() ^ m_true.hashCode() ^ m_false.hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof IExecution && this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0} ? {1} : {2}", m_value, m_true, m_false );
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Stream.concat( m_true.variables(), m_false.variables() );
    }
}
