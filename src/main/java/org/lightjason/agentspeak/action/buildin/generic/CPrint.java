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

package org.lightjason.agentspeak.action.buildin.generic;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.action.buildin.math.blas.CFormat1D;
import org.lightjason.agentspeak.action.buildin.math.blas.CFormat2D;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * action for sum of elements.
 * Prints a set of messages to the commandline / output-stream, the command
 * can be used with a variable set of arguments and fails never
 *
 * @code generic/print("string A=", A, "-- B=", B, "-- C=", C, "-- D=", D, "-- X=", X, "-- Y=", Y); @endcode
 */
public final class CPrint extends IBuildinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4271829260928469828L;
    /**
     * output stream
     */
    private final PrintStream m_stream;
    /**
     * argument seperator
     */
    private final String m_seperator;
    /**
     * list mit individual format calls
     */
    private final Set<IFormatter<?>> m_formatter;


    /**
     * ctor
     *
     * @note generates an output stream to system.out
     */
    public CPrint()
    {
        this( "   ", System.out, new CFormat2D(), new CFormat1D() );
    }

    /**
     * ctor
     *
     * @param p_seperator argument seperator
     * @param p_stream any byte output stream
     * @param p_formatter formatter elements
     */
    public CPrint( @Nonnull final String p_seperator, @Nonnull final PrintStream p_stream, @Nullable final IFormatter<?>... p_formatter )
    {
        super( 2 );
        m_stream = p_stream;
        m_seperator = p_seperator;
        m_formatter = p_formatter != null ? new HashSet<>( Arrays.asList( p_formatter ) ) : Collections.emptySet();
    }

    /**
     * returns the formatter list
     *
     * @return formatter set
     */
    public final Set<IFormatter<?>> formatter()
    {
        return m_formatter;
    }


    @Override
    public final int minimalArgumentNumber()
    {
        return 0;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        m_stream.println( MessageFormat.format( "{0}", this.format( p_argument ) ) );
        return CFuzzyValue.from( true );
    }

    /**
     * format definition
     *
     * @param p_argument arguments list
     * @return string
     */
    @Nonnull
    private String format( @Nonnull final Collection<ITerm> p_argument )
    {
        return p_argument.stream()
                         .map( ITerm::raw )
                         .map( i -> {
                             if ( i == null )
                                 return "";

                             final IFormatter<?> l_formatter = m_formatter.parallelStream()
                                                                          .filter( j -> j.isAssignableTo( i.getClass() ) )
                                                                          .limit( 1 )
                                                                          .findFirst()
                                                                          .orElse( null );

                             return l_formatter == null ? i.toString() : l_formatter.toString( i );
                         } )
                         .collect( Collectors.joining( m_seperator ) );
    }


    /**
     * formating class
     *
     * @tparam any type
     */
    public abstract static class IFormatter<T>
    {
        /**
         * checks if a type is assigneable
         *
         * @param p_class assignable class
         * @return assignable flag
         */
        public final boolean isAssignableTo( final Class<?> p_class )
        {
            return this.getType().isAssignableFrom( p_class );
        }

        @Override
        public final int hashCode()
        {
            return this.getType().hashCode();
        }

        @Override
        public final boolean equals( final Object p_object )
        {
            return ( p_object != null ) && ( p_object instanceof IFormatter<?> ) && ( this.hashCode() == p_object.hashCode() );
        }

        /**
         * to string
         *
         * @param p_data object type
         * @return output string
         */
        @SuppressWarnings( "unchecked" )
        public final String toString( final Object p_data )
        {
            return this.format( (T) p_data );
        }

        /**
         * returns type to match the formatter
         *
         * @return class type
         */
        protected abstract Class<?> getType();

        /**
         * formatter call
         *
         * @param p_data object type
         * @return formatted string
         */
        protected abstract String format( final T p_data );
    }

}
