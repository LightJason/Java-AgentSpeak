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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.execution.action;

import com.google.common.collect.ImmutableMultiset;
import lightjason.agent.IAgent;
import lightjason.language.IVariable;
import lightjason.language.execution.IExecution;
import lightjason.language.score.IAggregation;

import java.util.Collections;
import java.util.Set;


/**
 * test goal action
 */
public abstract class IBaseExecution<T> implements IExecution
{
    /**
     * data
     */
    protected final T m_value;

    /**
     * ctor
     */
    protected IBaseExecution()
    {
        m_value = null;
    }

    /**
     * ctor
     *
     * @param p_value data
     */
    protected IBaseExecution( final T p_value )
    {
        m_value = p_value;
    }

    /**
     * checkes assinable of the value
     *
     * @param p_class class
     * @return assinable (on null always true)
     */
    public final boolean isValueAssignableTo( final Class<?> p_class )
    {
        return ( m_value == null ) || p_class.isAssignableFrom( m_value.getClass() );
    }

    /**
     * returns the value of the action
     *
     * @return value
     */
    public final T getValue()
    {
        return m_value;
    }

    @Override
    public double score( final IAggregation p_aggregate, final IAgent p_agent )
    {
        return p_aggregate.evaluate( p_agent, ImmutableMultiset.of() );
    }

    @Override
    public Set<IVariable<?>> getVariables()
    {
        return Collections.<IVariable<?>>emptySet();
    }
}
