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

package org.lightjason.agentspeak.language.execution;

import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.stream.Stream;


/**
 * test goal action
 *
 * @tparam T value type
 */
public abstract class IBaseExecution<T> implements IExecution
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 2572294057575105363L;
    /**
     * data
     */
    protected final T m_value;

    /**
     * ctor
     *
     * @param p_value data
     */
    protected IBaseExecution( @Nonnull final T p_value )
    {
        m_value = p_value;
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Stream.empty();
    }

    @Override
    public String toString()
    {
        return m_value.toString();
    }

    @Override
    public int hashCode()
    {
        return m_value.hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof IExecution && this.hashCode() == p_object.hashCode();
    }

}
