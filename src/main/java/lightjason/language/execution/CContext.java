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
import lightjason.common.IPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.IVariable;

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
    private final Map<IPath, IVariable<?>> m_variables;


    /**
     * ctor
     *
     * @param p_agent agent
     * @param p_instance instance object
     * @param p_variables instance variables
     */
    public CContext( final IAgent p_agent, final T p_instance, final Collection<IVariable<?>> p_variables )
    {
        if ( ( p_agent == null ) || ( p_instance == null ) || ( p_variables == null ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notnull" ) );

        m_agent = p_agent;
        m_instance = p_instance;
        m_variables = Collections.unmodifiableMap( p_variables.parallelStream().collect( Collectors.toMap( IVariable::getFQNFunctor, i -> i ) ) );
    }

    @Override
    public IContext<T> duplicate()
    {
        return new CContext<T>( m_agent, m_instance, m_variables.values().parallelStream().map( i -> i.shallowcopy() ).collect( Collectors.toSet() ) );
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
    public final Map<IPath, IVariable<?>> getInstanceVariables()
    {
        return m_variables;
    }

    @Override
    public final int hashCode()
    {
        return m_agent.hashCode() + m_instance.hashCode() + m_variables.keySet().hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} [{1} | {2} | {3}]", super.toString(), m_agent, m_instance, m_variables );
    }

}
