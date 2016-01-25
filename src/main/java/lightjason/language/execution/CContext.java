/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.language.execution;

import lightjason.agent.IAgent;
import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;
import lightjason.language.IVariable;
import lightjason.language.plan.IPlan;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * execution context
 *
 * @tparam T instance type (plan or rule)
 */
public final class CContext<T> implements IContext<T>
{
    /**
     * agent of the running context
     */
    private final IAgent m_agent;
    /**
     * current instance object
     */
    private final T m_instance;
    /**
     * plan variables with their data
     */
    private final Map<CPath, IVariable<?>> m_variables;
    /**
     * current running plans
     */
    private final Map<ILiteral, IPlan> m_runningplans;

    /**
     * ctor
     *
     * @param p_agent agent
     * @param p_instance instance object
     * @param p_variables instance variables
     * @param p_runningplans current running plans
     */
    public CContext( final IAgent p_agent, final T p_instance, final Set<IVariable<?>> p_variables, final Map<ILiteral, IPlan> p_runningplans )
    {
        if ( ( p_agent == null ) || ( p_instance == null ) || ( p_variables == null ) || ( p_runningplans == null ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notnull" ) );

        m_agent = p_agent;
        m_instance = p_instance;
        m_variables = Collections.unmodifiableMap( p_variables.parallelStream().collect( Collectors.toMap( IVariable::getFQNFunctor, i -> i ) ) );
        m_runningplans = p_runningplans;
    }


    @Override
    public final IAgent getAgent()
    {
        return m_agent;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <N> N getInstance()
    {
        return (N) m_instance;
    }

    @Override
    public final Map<CPath, IVariable<?>> getInstanceVariables()
    {
        return m_variables;
    }

    @Override
    public final int hashCode()
    {
        return m_agent.hashCode() + m_instance.hashCode() + m_variables.keySet().hashCode() + m_runningplans.values().hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} [{1} | {2} | {3} | {4}]", super.toString(), m_agent, m_instance, m_variables, m_runningplans.values() );
    }

}
