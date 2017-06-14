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

package org.lightjason.agentspeak.language.variable;

import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * thread-safe variable
 *
 * @tparam T data type
 */
public class CMutexVariable<T> extends CVariable<T>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 9040218112516254186L;

    /**
     * ctor
     *
     * @param p_functor name
     */
    public CMutexVariable( final String p_functor )
    {
        super( p_functor );
    }

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    public CMutexVariable( final String p_functor, final T p_value )
    {
        super( p_functor, p_value );
    }

    /**
     * ctor
     *
     * @param p_functor name
     */
    public CMutexVariable( final IPath p_functor )
    {
        super( p_functor );
    }

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    public CMutexVariable( @Nonnull final IPath p_functor, @Nullable final T p_value )
    {
        super( p_functor, p_value );
    }

    @Nonnull
    @Override
    public final synchronized IVariable<T> set( final T p_value )
    {
        return super.set( p_value );
    }

    @Override
    public final synchronized <N> N raw()
    {
        return super.raw();
    }

    @Nonnull
    @Override
    public final synchronized IVariable<T> thrownotallocated() throws IllegalStateException
    {
        return super.thrownotallocated();
    }

    @Nonnull
    @Override
    public final synchronized IVariable<T> throwvaluenotassignableto( @Nonnull final Class<?>... p_class ) throws IllegalArgumentException
    {
        return super.throwvaluenotassignableto( p_class );
    }

    @Override
    public final synchronized boolean allocated()
    {
        return super.allocated();
    }

    @Override
    public final synchronized boolean valueassignableto( @Nonnull final Class<?>... p_class )
    {
        return super.valueassignableto( p_class );
    }

    @Override
    public final synchronized String toString()
    {
        return super.toString();
    }

    @Nonnull
    @Override
    public IVariable<T> shallowcopy( @Nullable final IPath... p_prefix )
    {
        return ( p_prefix == null ) || ( p_prefix.length == 0 )
               ? new CMutexVariable<>( m_functor, m_value )
               : new CMutexVariable<>( p_prefix[0].append( m_functor ), m_value );
    }

    @Nonnull
    @Override
    public IVariable<T> shallowcopysuffix()
    {
        return new CMutexVariable<>( m_functor.getSuffix(), m_value );
    }

    @Override
    public final boolean mutex()
    {
        return true;
    }
}
