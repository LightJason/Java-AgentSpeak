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

import com.rits.cloning.Cloner;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Arrays;


/**
 * default variable definition
 *
 * @note variable set is not thread-safe on default
 */
public class CVariable<T> implements IVariable<T>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5542578381343603600L;
    /**
     * variable / functor name
     */
    protected final IPath m_functor;
    /**
     * boolean flag, that defines an variable which matchs always
     */
    protected final boolean m_any;
    /**
     * value of the variable
     */
    protected T m_value;

    /**
     * ctor
     *
     * @param p_functor name
     */
    public CVariable( @Nonnull final String p_functor )
    {
        this( CPath.from( p_functor ), null );
    }

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    public CVariable( @Nonnull final String p_functor, @Nullable final T p_value )
    {
        this( CPath.from( p_functor ), p_value );
    }

    /**
     * ctor
     *
     * @param p_functor name
     */
    public CVariable( @Nonnull final IPath p_functor )
    {
        this( p_functor, null );
    }

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    @SuppressFBWarnings( "EC_UNRELATED_CLASS_AND_INTERFACE" )
    public CVariable( @Nonnull final IPath p_functor, @Nullable final T p_value )
    {
        m_any = p_functor.empty() || p_functor.equals( "_" );
        m_functor = p_functor;
        this.internalset( p_value );
    }

    @Nonnull
    @Override
    public IVariable<T> set( @Nullable final T p_value )
    {
        return this.internalset( p_value );
    }

    @Override
    public boolean allocated()
    {
        return m_value != null;
    }

    @Override
    public final boolean any()
    {
        return m_any;
    }

    @Override
    public boolean mutex()
    {
        return false;
    }

    @Nonnull
    @Override
    public IVariable<T> thrownotallocated() throws IllegalStateException
    {
        if ( !this.allocated() )
            throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "notallocated", m_functor ) );

        return this;
    }

    @Override
    public boolean valueassignableto( @Nonnull final Class<?>... p_class )
    {
        return m_value == null || Arrays.stream( p_class ).anyMatch( i -> i.isAssignableFrom( m_value.getClass() ) );
    }

    @Nonnull
    @Override
    public IVariable<T> throwvaluenotassignableto( @Nonnull final Class<?>... p_class ) throws IllegalArgumentException
    {
        if ( !this.valueassignableto( p_class ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "notassignable", m_functor, Arrays.asList( p_class ) ) );

        return this;
    }

    @Override
    public final int hashCode()
    {
        return m_functor.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IVariable<?> ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})", m_functor, m_value == null ? "" : m_value );
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
    public <N> N raw()
    {
        return (N) m_value;
    }

    @Nonnull
    @Override
    public IVariable<T> shallowcopy( final IPath... p_prefix )
    {
        return ( p_prefix == null ) || ( p_prefix.length == 0 )
               ? new CVariable<>( m_functor, m_value )
               : new CVariable<>( p_prefix[0].append( m_functor ), m_value );
    }

    @Nonnull
    @Override
    public IVariable<T> shallowcopysuffix()
    {
        return new CVariable<>( m_functor.suffix(), m_value );
    }

    @Nonnull
    @Override
    public ITerm deepcopy( final IPath... p_prefix )
    {
        return new CVariable<>(
            ( p_prefix == null ) || ( p_prefix.length == 0 )
            ? m_functor
            : m_functor.append( p_prefix[0] ),
            new Cloner().deepClone( m_value )
        );
    }

    @Nonnull
    @Override
    public ITerm deepcopysuffix()
    {
        return new CVariable<>( m_functor.suffix(), new Cloner().deepClone( m_value ) );
    }

    @Override
    public final int structurehash()
    {
        return 0;
    }

    /**
     * internel set for avoid any exception throwing
     *
     * @param p_value value
     * @return self reference
     */
    @SuppressWarnings( "unchecked" )
    private IVariable<T> internalset( final T p_value )
    {
        // value must be set manually to avoid exception throwing (see CVariable.set)
        if ( !m_any )
            m_value = p_value instanceof ITerm ? ( (ITerm) p_value ).raw() : p_value;
        return this;
    }
}
