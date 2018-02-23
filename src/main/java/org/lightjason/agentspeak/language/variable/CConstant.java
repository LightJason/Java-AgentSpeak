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

package org.lightjason.agentspeak.language.variable;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalStateException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * constant definition
 *
 * @tparam T data type
 */
public final class CConstant<T> extends CVariable<T>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8207552612082585231L;

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    public CConstant( @Nonnull final String p_functor, @Nullable final T p_value )
    {
        super( p_functor, p_value );
    }

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    public CConstant( @Nonnull final IPath p_functor, @Nullable final T p_value )
    {
        super( p_functor, p_value );
    }

    @Nonnull
    @Override
    public final IVariable<T> set( final T p_value )
    {
        throw new CIllegalStateException( CCommon.languagestring( this, "set", m_functor ) );
    }

    @Nonnull
    @Override
    public final IVariable<T> shallowcopy( final IPath... p_prefix )
    {
        return ( p_prefix == null ) || ( p_prefix.length == 0 )
               ? new CConstant<>( m_functor, m_value )
               : new CConstant<>( p_prefix[0].append( m_functor ), m_value );
    }


    @Nonnull
    @Override
    public final IVariable<T> shallowcopysuffix()
    {
        return new CConstant<>( m_functor.suffix(), m_value );
    }

}
