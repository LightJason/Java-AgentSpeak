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

package org.lightjason.agentspeak.language.execution;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * execution context
 *
 * @tparam T instance type (plan or rule)
 */
public final class CContext implements IContext
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 2813094837634259389L;
    /**
     * agent of the running context
     */
    private final IAgent<?> m_agent;
    /**
     * current instance object
     */
    private final IInstantiable m_instance;
    /**
     * plan variables with their data
     */
    private final Map<IPath, IVariable<?>> m_variables;


    /**
     * ctor
     *
     * @param p_agent agent
     * @param p_instance instance object
     * @param p_variables instance variables
     */
    public CContext( @Nonnull final IAgent<?> p_agent, @Nonnull final IInstantiable p_instance, @Nonnull final Collection<IVariable<?>> p_variables )
    {
        m_agent = p_agent;
        m_instance = p_instance;
        m_variables = Collections.unmodifiableMap( p_variables.parallelStream().collect( Collectors.toMap( IVariable::fqnfunctor, i -> i ) ) );
    }

    @Nonnull
    @Override
    public final IContext duplicate()
    {
        return new CContext( m_agent, m_instance, m_variables.values().parallelStream().map( i -> i.shallowcopy() ).collect( Collectors.toSet() ) );
    }

    @Nonnull
    @Override
    public final IAgent<?> agent()
    {
        return m_agent;
    }

    @Nonnull
    @Override
    public final IInstantiable instance()
    {
        return m_instance;
    }

    @Nonnull
    @Override
    public final Map<IPath, IVariable<?>> instancevariables()
    {
        return m_variables;
    }

    @Override
    public final int hashCode()
    {
        return m_agent.hashCode() ^ m_instance.hashCode() ^ m_variables.keySet().hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IContext ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} [{1} | {2} | {3}]", super.toString(), m_variables.values(), m_instance, m_agent );
    }

}
