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

import java.util.Arrays;


/**
 * term structure for simple datatypes
 *
 * @warning hash code is defined on the input data type
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
     * hash code cache
     */
    private final int m_hashcode;
    /**
     * value hash
     */
    private final int m_valuehash;

    /**
     * ctor
     *
     * @param p_value any value tue
     * @tparam N value type
     */
    @SuppressWarnings( "unchecked" )
    public <N> CRawTerm( final N p_value )
    {
        if ( p_value instanceof CRawTerm<?> )
        {
            final CRawTerm<?> l_term = (CRawTerm<?>) p_value;
            m_value = l_term.getTyped();
            m_functor = l_term.getFQNFunctor();
        }
        else if ( p_value instanceof IVariable<?> )
        {
            final IVariable<?> l_variable = (IVariable<?>) p_value;
            m_value = (T) l_variable.get();
            m_functor = l_variable.getFQNFunctor();
        }
        else
        {
            m_value = (T) p_value;
            m_functor = p_value == null ? CPath.EMPTY : CPath.from( p_value.toString() );
        }

        m_hashcode = m_value == null ? super.hashCode() : m_value.hashCode();
        m_valuehash = 0;
        //CCommon.getTermHashing().putObject( m_value, CCommon.<>getTermFunnel() ).hash().asInt();
    }



    /**
     * factory for a raw term
     *
     * @param p_value any value
     * @return raw term
     *
     * @tparam N type
     */
    public static <N> CRawTerm<N> from( final N p_value )
    {
        return new CRawTerm<N>( p_value );
    }


    @Override
    public final int hashCode()
    {
        return m_hashcode;
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return m_hashcode == p_object.hashCode();
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

    @Override
    public final int valuehash()
    {
        return m_valuehash;
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
     * @param p_name optional name text for the error message
     * @return object itself
     *
     * @throws IllegalStateException on non-allocated
     */
    public final CRawTerm<T> throwNotAllocated( final String... p_name ) throws IllegalStateException
    {
        if ( !this.isAllocated() )
            throw new CIllegalStateException( lightjason.common.CCommon.getLanguageString( this, "notallocated", p_name != null ? p_name[0] : this ) );

        return this;
    }

    /**
     * checkes assignable of the value
     *
     * @param p_class class
     * @return assignable (on null always true)
     */
    public final boolean isValueAssignableTo( final Class<?>... p_class )
    {
        return m_value == null ? true : Arrays.asList( p_class ).stream().map( i -> i.isAssignableFrom( m_value.getClass() ) ).anyMatch( i -> i );
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
    public final CRawTerm<T> throwValueNotAssignableTo( final Class<?>... p_class ) throws IllegalArgumentException
    {
        if ( !this.isValueAssignableTo( p_class ) )
            throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "notassignable", this, p_class ) );

        return this;
    }

}
