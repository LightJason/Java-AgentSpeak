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

package org.lightjason.agentspeak.language.execution.passing;

import org.apache.commons.lang3.StringUtils;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * proxy for any execution
 */
public final class CPassAction implements IExecution
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -6403311021295080608L;
    /**
     * parallel execution
     */
    private final boolean m_parallel;
    /**
     * execution reference
     */
    private final IExecution m_execution;
    /**
     * arguments
     */
    private final ITerm[] m_arguments;


    /**
     * ctor
     *
     * @param p_parallel parallel execution
     * @param p_execution execution
     * @param p_arguments arguments
     */
    public CPassAction( final boolean p_parallel, @Nonnull final IExecution p_execution, @Nonnull final Stream<ITerm> p_arguments )
    {
        m_parallel = p_parallel;
        m_execution = p_execution;
        m_arguments = p_arguments.toArray( ITerm[]::new );
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                               @Nonnull final List<ITerm> p_return )
    {
        return m_execution.execute(
            m_parallel,
            p_context,
            Collections.unmodifiableList( CCommon.replaceFromContext( p_context, Arrays.stream( m_arguments ) ).collect( Collectors.toList() ) ),
            p_return
        );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return m_execution.variables();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format(
            ".{0}{1}{2}",
            m_execution,
            m_parallel ? "@" : "",
            m_arguments.length == 0 ? "" : "[" + StringUtils.join( m_arguments, ", " ) + "]" );
    }
}
