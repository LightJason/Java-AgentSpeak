/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language;

import lightjason.common.CPath;


/**
 * thread-safe variable
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
    public CMutexVariable( final CPath p_functor )
    {
        super( p_functor );
    }

    /**
     * ctor
     *
     * @param p_functor name
     * @param p_value value
     */
    public CMutexVariable( final CPath p_functor, final T p_value )
    {
        super( p_functor, p_value );
    }

    @Override
    public synchronized final IVariable<T> set( final T p_value )
    {
        return super.set( p_value );
    }

    @Override
    public synchronized final T get()
    {
        return super.get();
    }

    @Override
    public synchronized final boolean isAllocated()
    {
        return super.isAllocated();
    }

    @Override
    public synchronized final boolean isValueAssignableTo( final Class<?> p_class )
    {
        return super.isValueAssignableTo( p_class );
    }

    @Override
    public synchronized final int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public synchronized final boolean equals( final Object p_object )
    {
        return super.equals( p_object );
    }

    @Override
    public synchronized final String toString()
    {
        return super.toString();
    }

    @Override
    protected synchronized final Object clone() throws CloneNotSupportedException
    {
        return new CMutexVariable<T>( m_functor.toString(), m_value );
    }
}
