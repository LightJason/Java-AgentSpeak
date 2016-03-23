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

import lightjason.common.IPath;


/**
 * thread-safe variable
 *
 * @tparam T data type
 */
public final class CMutexVariable<T> extends CVariable<T>
{

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
    public CMutexVariable( final IPath p_functor, final T p_value )
    {
        super( p_functor, p_value );
    }

    @Override
    public final synchronized IVariable<T> set( final T p_value )
    {
        return super.set( p_value );
    }

    @Override
    public final synchronized T get()
    {
        return super.get();
    }

    @Override
    public final synchronized <N> N getTyped()
    {
        return super.getTyped();
    }

    @Override
    public final synchronized IVariable<T> throwNotAllocated() throws IllegalStateException
    {
        return super.throwNotAllocated();
    }

    @Override
    public final synchronized IVariable<T> throwValueNotAssignableTo( final Class<?>... p_class ) throws IllegalArgumentException
    {
        return super.throwValueNotAssignableTo( p_class );
    }

    @Override
    public final synchronized boolean isAllocated()
    {
        return super.isAllocated();
    }

    @Override
    public final synchronized boolean isValueAssignableTo( final Class<?>... p_class )
    {
        return super.isValueAssignableTo( p_class );
    }

    @Override
    public final synchronized int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public final synchronized boolean equals( final Object p_object )
    {
        return super.equals( p_object );
    }

    @Override
    public final synchronized String toString()
    {
        return super.toString();
    }

    @Override
    public final IVariable<T> shallowcopy( final IPath... p_prefix )
    {
        return ( p_prefix == null ) || ( p_prefix.length == 0 )
               ? new CMutexVariable<T>( m_functor, m_value )
               : new CMutexVariable<T>( p_prefix[0].append( m_functor ), m_value );
    }


    @Override
    public final IVariable<T> shallowcopySuffix()
    {
        return new CMutexVariable<T>( m_functor.getSuffix(), m_value );
    }

}
