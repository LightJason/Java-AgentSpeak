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
import lightjason.error.CIllegalArgumentException;
import lightjason.error.CIllegalStateException;


/**
 * term structure for simple datatypes
 */
public final class CRawTerm<T> implements ITerm
{
    /**
     * value data
     */
    private final T m_value;
    /**
     * functor
     */
    private final CPath m_functor;


    /**
     * ctor
     *
     * @param p_value data
     */
    public CRawTerm( final T p_value )
    {
        m_value = p_value;
        m_functor = new CPath( m_value.toString() );
    }

    @Override
    public final int hashCode()
    {
        return m_value == null ? super.hashCode() : m_value.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return m_value == null ? super.equals( p_object ) : m_value.equals( p_object );
    }

    @Override
    public final String toString()
    {
        return m_value == null ? "" : m_value.toString();
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

    /**
     * returns the raw valuw
     *
     * @return value
     */
    public final T get()
    {
        return m_value;
    }

    /**
     * gets the value with cast
     *
     * @return casted value
     *
     * @tparam N casted type
     */
    @SuppressWarnings( "unchecked" )
    public final <N> N getTyped()
    {
        return (N) m_value;
    }

    ;

    /**
     * returns allocated state
     *
     * @return boolean flag
     */
    public final boolean isAllocated()
    {
        return m_value != null;
    }

    /**
     * throws an illegal state exception
     * iif the raw term is not allocated
     *
     * @return object itself
     *
     * @throws IllegalStateException on non-allocated
     */
    public final CRawTerm<T> throwNotAllocated() throws IllegalStateException
    {
        if ( !this.isAllocated() )
            throw new CIllegalStateException( lightjason.common.CCommon.getLanguageString( this, "notallocated", this ) );

        return this;
    }

    /**
     * checkes assignable of the value
     *
     * @param p_class class
     * @return assignable (on null always true)
     */
    public final boolean isValueAssignableTo( final Class<?> p_class )
    {
        return m_value == null ? true : p_class.isAssignableFrom( m_value.getClass() );
    }

    /**
     * throws an illegal argument exception
     * iif the value is not assignable to the
     * class
     *
     * @param p_class assignable class
     * @return object itself
     *
     * @throws IllegalArgumentException on assignable error
     */
    public final CRawTerm<T> throwValueNotAssignableTo( final Class<?> p_class ) throws IllegalArgumentException
    {
        if ( !this.isValueAssignableTo( p_class ) )
            throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "notassignable", this, p_class ) );

        return this;
    }

}
