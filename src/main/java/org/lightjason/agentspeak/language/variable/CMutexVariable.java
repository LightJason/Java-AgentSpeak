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

package org.lightjason.agentspeak.language.variable;

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


/**
 * thread-safe variable
 *
 * @tparam T data type
 */
public final class CMutexVariable<T> extends IBaseVariable<T>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5373030465593522996L;
    /**
     * thread-safe value
     */
    private final AtomicReference<T> m_value = new AtomicReference<>();


    /**
     * ctor
     *
     * @param p_functor functor
     */
    public CMutexVariable( @Nonnull final String p_functor )
    {
        super( p_functor );
    }

    /**
     * ctor
     *
     * @param p_functor functor
     */
    public CMutexVariable( @Nonnull final IPath p_functor )
    {
        super( p_functor );
    }

    /**
     * ctor
     *
     * @param p_functor functor
     * @param p_value value
     */
    public CMutexVariable( @Nonnull final String p_functor, @Nullable final T p_value )
    {
        super( p_functor );
        this.set( p_value );
    }

    /**
     * ctor
     *
     * @param p_functor functor
     * @param p_value value
     */
    public CMutexVariable( @Nonnull final IPath p_functor, @Nullable final T p_value )
    {
        super( p_functor );
        this.set( p_value );
    }

    @Override
    public boolean mutex()
    {
        return true;
    }

    @Nonnull
    @Override
    public IVariable<T> shallowcopy( @Nullable final IPath... p_prefix )
    {
        return ( Objects.isNull( p_prefix ) ) || ( p_prefix.length == 0 )
               ? new CMutexVariable<>( m_functor, m_value.get() )
               : new CMutexVariable<>( p_prefix[0].append( m_functor ), m_value.get() );
    }

    @Nonnull
    @Override
    public IVariable<T> shallowcopysuffix()
    {
        return new CMutexVariable<>( m_functor.suffix(), m_value.get() );
    }

    @Nonnull
    @Override
    public ITerm deepcopy( @Nullable final IPath... p_prefix )
    {
        return new CMutexVariable<>(
            ( Objects.isNull( p_prefix ) ) || ( p_prefix.length == 0 )
            ? m_functor
            : m_functor.append( p_prefix[0] ),
            CCommon.deepclone( m_value.get() )
        );
    }

    @Nonnull
    @Override
    public ITerm deepcopysuffix()
    {
        return new CMutexVariable<>( m_functor.suffix(), CCommon.deepclone( m_value.get() ) );
    }

    @Nonnull
    @Override
    protected IVariable<T> setvalue( @Nullable final T p_value )
    {
        m_value.set( p_value );
        return this;
    }

    @Nullable
    @Override
    protected T getvalue()
    {
        return m_value.get();
    }
}
