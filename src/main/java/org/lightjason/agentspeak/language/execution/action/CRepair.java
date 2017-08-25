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

package org.lightjason.agentspeak.language.execution.action;

import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * defines an execution element with a repair call
 */
public class CRepair extends IBaseExecution<IExecution>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7095678561033158953L;
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
    public CRepair( @Nonnull final IExecution p_value, @Nonnull final IExecution p_fallback )
    {
        super( p_value );
        m_fallback = p_fallback;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final IFuzzyValue<Boolean> l_return = m_value.execute( p_parallel, p_context, p_argument, p_return );
        return l_return.value() ? l_return : m_fallback.execute( p_parallel, p_context, p_argument, p_return );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.concat(
            m_value == null ? Stream.empty() : m_value.variables(),
            m_fallback.variables()
        );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} << {1}", m_value, m_fallback );
    }
}
