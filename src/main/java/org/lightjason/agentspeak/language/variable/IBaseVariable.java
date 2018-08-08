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
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CNoSuchElementException;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Objects;


/**
 * base class of variable structures
 */
public abstract class IBaseVariable<T> implements IVariable<T>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1070797517179308816L;
    /**
     * variable / functor name
     */
    protected final IPath m_functor;
    /**
     * boolean flag, that defines an variable which matchs always
     */
    private final boolean m_any;


    /**
     * ctor
     *
     * @param p_functor name
     */
    protected IBaseVariable( @Nonnull final String p_functor )
    {
        this( CPath.of( p_functor ) );
    }

    /**
     * ctor
     *
     * @param p_functor name
     */
    protected IBaseVariable( @Nonnull final IPath p_functor )
    {
        m_any = p_functor.empty() || "_".equals( p_functor.toString() );
        m_functor = p_functor;
    }

    /**
     * set the value
     *
     * @param p_value new value
     * @return self reference
     */
    @Nonnull
    protected abstract IVariable<T> setvalue( @Nullable final T p_value );

    /**
     * retruns the value
     *
     * @return value
     */
    @Nullable
    protected abstract T getvalue();

    @Override
    public final boolean allocated()
    {
        return Objects.nonNull( this.getvalue() );
    }

    @Override
    public final boolean any()
    {
        return m_any;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public final <N extends ITerm> N term()
    {
        return (N) this;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public final IVariable<T> set( @Nullable final T p_value )
    {
        return m_any
               ? this
               : this.setvalue(
                   p_value instanceof ITerm
                   ? ( (ITerm) p_value ).raw()
                   : p_value
               );
    }

    @Override
    public final int structurehash()
    {
        return 0;
    }

    @Nonnull
    @Override
    public final IVariable<T> thrownotallocated() throws IllegalStateException
    {
        if ( !this.allocated() )
            throw new CNoSuchElementException( CCommon.languagestring( this, "notallocated", m_functor ) );

        return this;
    }

    @Override
    public final boolean valueassignableto( @Nonnull final Class<?> p_class )
    {
        return Objects.isNull( this.getvalue() ) || p_class.isAssignableFrom( this.getvalue().getClass() );
    }

    @Nonnull
    @Override
    public final IVariable<T> throwvaluenotassignableto( @Nonnull final Class<?> p_class ) throws IllegalStateException
    {
        if ( !this.valueassignableto( p_class ) )
            throw new CNoSuchElementException( CCommon.languagestring( this, "notassignable", m_functor, p_class ) );

        return this;
    }

    @Nonnull
    @Override
    public final String functor()
    {
        return m_functor.suffix();
    }

    @Nonnull
    @Override
    public final IPath functorpath()
    {
        return m_functor.subpath( 0, m_functor.size() - 1 );
    }

    @Nonnull
    @Override
    public final IPath fqnfunctor()
    {
        return m_functor;
    }

    @Override
    public final boolean hasVariable()
    {
        return true;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final <N> N raw()
    {
        return (N) this.getvalue();
    }

    @Override
    public final int hashCode()
    {
        return m_functor.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return p_object instanceof IVariable<?> && this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})", m_functor, Objects.nonNull( this.getvalue() ) ? this.getvalue() : "" );
    }


}
