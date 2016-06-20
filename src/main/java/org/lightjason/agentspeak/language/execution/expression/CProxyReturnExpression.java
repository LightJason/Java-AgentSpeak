/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;


/**
 * encpasulating any execution context
 *
 * @warning the class returns the result of the execution call only
 */
public final class CProxyReturnExpression<T extends IExecution> implements IExpression
{
    /**
     * execution
     */
    private final T m_execution;

    /**
     * ctor
     *
     * @param p_execution execution
     */
    public CProxyReturnExpression( final T p_execution )
    {
        m_execution = p_execution;
    }


    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final IFuzzyValue<Boolean> l_return = m_execution.execute( p_context, p_parallel, p_argument, new LinkedList<>(), p_annotation );
        p_return.add( CRawTerm.from( l_return.value() ) );
        return l_return;
    }

    @Override
    public final double score( final IAgent<?> p_agent )
    {
        return m_execution.score( p_agent );
    }

    @Override
    public final Stream<IVariable<?>> getVariables()
    {
        return m_execution.getVariables();
    }

    @Override
    public final int hashCode()
    {
        return m_execution.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return m_execution.equals( p_object );
    }

    @Override
    public final String toString()
    {
        return m_execution.toString();
    }

}
