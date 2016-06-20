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

package org.lightjason.agentspeak.language.execution.action;

import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * defines an execution element with a repair call
 */
public class CRepair extends IBaseExecution<IExecution>
{
    /**
     * fallback execution
     */
    private final IExecution m_fallback;

    /**
     * ctor
     *
     * @param p_value execution element
     * @param p_fallback fallback execution
     */
    public CRepair( final IExecution p_value, final IExecution p_fallback )
    {
        super( p_value );
        m_fallback = p_fallback;
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                         final List<ITerm> p_annotation
    )
    {
        final IFuzzyValue<Boolean> l_return = m_value.execute( p_context, p_parallel, p_argument, p_return, p_annotation );
        return l_return.value() ? l_return : m_fallback.execute( p_context, p_parallel, p_argument, p_return, p_annotation );
    }

    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            m_value.variables(),
            m_fallback.variables()
        );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} << {1}", m_value, m_fallback );
    }
}
