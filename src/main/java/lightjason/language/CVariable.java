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

package lightjason.language;

import lightjason.common.CPath;

import java.text.MessageFormat;


/**
 * default variable definition
 *
 * @note variable set is not thread-safe on default
 * @todo replace instanceof with generic
 */
public class CVariable<T> implements IVariable<T>
{
    /**
     * variable / functor name
     */
    protected final CPath m_functor;
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
    public CVariable( final String p_functor )
    {
        this( CPath.from( p_functor ), null );
    }

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    public CVariable( final String p_functor, final T p_value )
    {
        this( CPath.from( p_functor ), p_value );
    }

    /**
     * ctor
     *
     * @param p_functor name
     */
    public CVariable( final CPath p_functor )
    {
        this( p_functor, null );
    }

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    public CVariable( final CPath p_functor, final T p_value )
    {
        m_any = ( p_functor == null ) || p_functor.isEmpty() || p_functor.equals( "_" );
        m_functor = p_functor;
        this.set( p_value );
    }

    @Override
    public IVariable<T> set( final T p_value )
    {
        if ( !m_any )
            // value can be a raw-term (depend on AST access), so unpack a raw-term to the native value
            m_value = CCommon.getRawValue( p_value );
        return this;
    }

    @Override
    public T get()
    {
        return m_value;
    }

    @Override
    public boolean isAllocated()
    {
        return m_value != null;
    }

    @Override
    public boolean isValueAssignableTo( final Class<?> p_class )
    {
        return m_value == null ? true : p_class.isAssignableFrom( m_value.getClass() );
    }

    @Override
    public int hashCode()
    {
        return m_functor.hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})", m_functor, m_value == null ? "" : m_value );
    }

    @Override
    public final String getFunctor()
    {
        return m_functor.getSuffix();
    }

    @Override
    public final CPath getFunctorPath()
    {
        return m_functor.getSubPath( 0, m_functor.size() - 1 );
    }

    @Override
    public final CPath getFQNFunctor()
    {
        return m_functor;
    }

    @Override
    public IVariable<T> clone()
    {
        return new CVariable<T>( m_functor.toString(), m_value );
    }
}
