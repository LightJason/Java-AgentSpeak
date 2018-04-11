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

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Objects;


/**
 * class for a relocated variable
 *
 * @tparam T variable type
 */
public final class CRelocateVariable<T> extends IBaseVariable<T> implements IRelocateVariable<T>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7809714416124597575L;
    /**
     * reference to relocated variable
     */
    private final IVariable<?> m_relocate;
    /**
     * value
     */
    private T m_value;

    /**
     * ctor
     *
     * @param p_variable variable which should be reloacted
     */
    public CRelocateVariable( final IVariable<?> p_variable )
    {
        super( p_variable.functor() );
        this.setvalue( p_variable.raw() );
        m_relocate = p_variable;
    }

    /**
     * ctor
     *
     * @param p_functor variable name
     * @param p_relocate variable which should be relocated
     */
    public CRelocateVariable( @Nonnull final IPath p_functor, @Nonnull final IVariable<?> p_relocate )
    {
        super( p_functor );
        this.setvalue( p_relocate.raw() );
        m_relocate = p_relocate;
    }

    /**
     * private ctor for creating object-copy
     *
     * @param p_functor functor
     * @param p_variable referenced variable
     * @param p_value value
     */
    private CRelocateVariable( @Nonnull final IPath p_functor, @Nonnull final IVariable<?> p_variable, @Nullable final T p_value
    )
    {
        super( p_functor );
        this.setvalue( p_value );
        m_relocate = p_variable;
    }

    /**
     * private ctor for creating object-copy
     *
     * @param p_functor functor
     * @param p_variable referenced variable
     * @param p_value value
     */
    private CRelocateVariable( @Nonnull final String p_functor, @Nonnull final IVariable<?> p_variable, @Nullable final T p_value
    )
    {
        super( p_functor );
        this.setvalue( p_value );
        m_relocate = p_variable;
    }

    @Nonnull
    @Override
    public IVariable<?> relocate()
    {
        return m_relocate instanceof CConstant<?>
               ? m_relocate
               : m_relocate.set( this.raw() );
    }

    @Override
    public boolean mutex()
    {
        return false;
    }

    @Nonnull
    @Override
    public IVariable<T> shallowcopy( @Nullable final IPath... p_prefix )
    {
        return ( Objects.isNull( p_prefix ) ) || ( p_prefix.length == 0 )
               ? new CRelocateVariable<>( m_functor, m_relocate, m_value )
               : new CRelocateVariable<>( p_prefix[0].append( m_functor ), m_relocate, m_value );
    }

    @Nonnull
    @Override
    public IVariable<T> shallowcopysuffix()
    {
        return new CRelocateVariable<>( m_functor.suffix(), m_relocate, m_value );
    }

    @Nonnull
    @Override
    public ITerm deepcopy( @Nullable final IPath... p_prefix )
    {
        return new CRelocateVariable<>(
            ( Objects.isNull( p_prefix ) ) || ( p_prefix.length == 0 )
            ? m_functor
            : m_functor.append( p_prefix[0] ),
            m_relocate, CCommon.deepclone( m_value )
        );
    }

    @Nonnull
    @Override
    public ITerm deepcopysuffix()
    {
        return new CRelocateVariable<>( m_functor.suffix(), m_relocate, CCommon.deepclone( m_value ) );
    }


    @Nonnull
    @Override
    protected IVariable<T> setvalue( @Nullable final T p_value )
    {
        m_value = p_value;
        return this;
    }

    @Nullable
    @Override
    protected T getvalue()
    {
        return m_value;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})>{2}", m_functor, Objects.isNull( m_value ) ? "" : m_value, m_relocate );
    }
}
