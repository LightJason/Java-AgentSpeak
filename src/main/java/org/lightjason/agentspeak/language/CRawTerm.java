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

package org.lightjason.agentspeak.language;

import com.rits.cloning.Cloner;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CIllegalStateException;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.Arrays;


/**
 * term structure for simple datatypes
 *
 * @warning hash code is defined on the input data type
 */
public final class CRawTerm<T> implements IRawTerm<T>
{
    /**
     * empty raw term
     */
    public static final ITerm EMPTY = new CRawTerm<>( null );
    /**
     * value data
     */
    private final T m_value;
    /**
     * functor
     */
    private final IPath m_functor;
    /**
     * hash code cache
     */
    private final int m_hashcode;


    /**
     * ctor
     *
     * @param p_value any value tue
     * @tparam N value type
     */
    @SuppressWarnings( "unchecked" )
    public <N> CRawTerm( final N p_value )
    {
        if ( p_value instanceof ITerm )
        {
            final ITerm l_term = (ITerm) p_value;
            m_value = l_term.raw();
            m_functor = l_term.fqnfunctor();
        }
        else
        {
            m_value = (T) p_value;
            m_functor = p_value == null ? IPath.EMPTY : CPath.from( p_value.toString() );
        }

        m_hashcode = m_value == null ? super.hashCode() : m_value.hashCode();
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
        return new CRawTerm<>( p_value );
    }


    @Override
    public final int hashCode()
    {
        return m_hashcode;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    @SuppressFBWarnings( "EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS" )
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null )
               && (
                   (
                        ( p_object instanceof IVariable<?> )
                        && ( ( (IVariable<?>) p_object ).allocated() )
                        && ( this.hashCode() == ( (IVariable<?>) p_object ).raw().hashCode() )
                   )
                   || ( ( p_object instanceof ITerm ) && ( this.hashCode() == p_object.hashCode() ) )
               );
    }

    @Override
    public final String toString()
    {
        return m_value == null ? "" : m_value.toString();
    }

    @Override
    public final String functor()
    {
        return m_functor.getSuffix();
    }

    @Override
    public final IPath functorpath()
    {
        return m_functor.getSubPath( 0, m_functor.size() - 1 );
    }

    @Override
    public final IPath fqnfunctor()
    {
        return m_functor;
    }

    @Override
    public final boolean hasVariable()
    {
        return false;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final <T> T raw()
    {
        return (T) m_value;
    }

    @Override
    public final boolean allocated()
    {
        return m_value != null;
    }

    @Override
    public final IRawTerm<T> throwNotAllocated( final String... p_name ) throws IllegalStateException
    {
        if ( !this.allocated() )
            throw new CIllegalStateException( org.lightjason.agentspeak.common.CCommon
                                                  .languagestring( this, "notallocated", p_name != null ? p_name[0] : this ) );

        return this;
    }

    @Override
    public final boolean valueAssignableTo( final Class<?>... p_class )
    {
        return m_value == null || Arrays.stream( p_class ).map( i -> i.isAssignableFrom( m_value.getClass() ) ).anyMatch( i -> i );
    }

    @Override
    public final IRawTerm<T> throwValueNotAssignableTo( final Class<?>... p_class ) throws IllegalArgumentException
    {
        if ( !this.valueAssignableTo( p_class ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "notassignable", this, p_class ) );

        return this;
    }

    @Override
    public final ITerm deepcopy( final IPath... p_prefix )
    {
        return CRawTerm.from( new Cloner().deepClone( m_value ) );
    }

    @Override
    public final ITerm deepcopysuffix()
    {
        return CRawTerm.from( m_value );
    }
}
