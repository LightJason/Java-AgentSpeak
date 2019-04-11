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

package org.lightjason.agentspeak.language.execution.assignment;

import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * assignment action of a single-variable
 */
public final class CSingleAssignment extends IBaseExecution<IVariable<?>>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3668379813439056262L;
    /**
     * right-hand argument
     */
    private final IExecution m_rhs;
    /**
     * operator
     */
    private final EAssignOperator m_operator;

    /**
     * ctor
     *
     * @param p_operator assignment operator
     * @param p_lhs left-hand argument (variable)
     * @param p_rhs right-hand argument
     */
    public CSingleAssignment( @Nonnull final EAssignOperator p_operator, @Nonnull final IVariable<?> p_lhs, @Nonnull final IExecution p_rhs )
    {
        super( p_lhs );
        m_operator = p_operator;
        m_rhs = p_rhs;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_result = CCommon.argumentlist();

        if ( !p_context.agent().fuzzy().defuzzification().success(
                p_context.agent().fuzzy().defuzzification().apply( m_rhs.execute( p_parallel, p_context, Collections.emptyList(), l_result ) )
             )
        )
            return p_context.agent().fuzzy().membership().fail();


        if ( l_result.size() != 1 )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "rhsincorrect" )
            );

        final IVariable<Object> l_lhs = CCommon.replacebycontext( p_context, m_value ).term();
        l_lhs.set( m_operator.apply( l_lhs, l_result.get( 0 ) ) );

        return Stream.of();
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() ^ m_operator.hashCode() ^ m_rhs.hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof IExecution && this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0} = {1}", m_value, m_rhs );
    }

    @Override
    @Nonnull
    public Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            Stream.of( m_value ),
            m_rhs.variables()
        );
    }
}
