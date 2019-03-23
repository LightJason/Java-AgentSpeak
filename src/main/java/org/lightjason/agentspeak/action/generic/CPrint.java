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

package org.lightjason.agentspeak.action.generic;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.common.ISupplier;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action for sum of elements.
 * Prints a set of messages to the commandline / output-stream, the command
 * can be used with a variable set of arguments
 *
 * {@code .generic/print("string A=", A, "-- B=", B, "-- C=", C, "-- D=", D, "-- X=", X, "-- Y=", Y);}
 */
public final class CPrint extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4271829260928469828L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CPrint.class, "generic" );
    /**
     * output stream
     */
    private transient PrintStream m_stream;
    /**
     * supplier of print stream field
     */
    private final ISupplier<PrintStream> m_streamsupplier;
    /**
     * argument seperator
     */
    private final String m_seperator;
    /**
     * list mit individual format calls
     */
    private final Set<IFormatter> m_formatter;

    /**
     * ctor
     *
     * @throws Exception is thrown on supplierer error
     * @note generates an output stream to system.out
     */
    public CPrint() throws Exception
    {
        this( () -> System.out, "   " );
    }

    /**
     * ctor
     *
     * @param p_streamsupplier print stream supplier
     * @throws Exception is thrown on supplierer error
     */
    public CPrint( @Nonnull final ISupplier<PrintStream> p_streamsupplier ) throws Exception
    {
        this( p_streamsupplier, "   " );
    }

    /**
     * ctor
     *
     * @param p_streamsupplier print stream supplier
     * @param p_seperator argument seperator
     * @param p_formatter formatter elements
     * @throws Exception is thrown on supplierer error
     */
    public CPrint( @Nonnull final ISupplier<PrintStream> p_streamsupplier, @Nonnull final String p_seperator, @Nullable final IFormatter... p_formatter )
        throws Exception
    {
        m_streamsupplier = p_streamsupplier;
        m_stream = m_streamsupplier.get();
        m_seperator = p_seperator;
        m_formatter = Objects.nonNull( p_formatter ) ? new HashSet<>( Arrays.asList( p_formatter ) ) : Collections.emptySet();
    }

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    /**
     * deserializable call
     *
     * @param p_stream object stream
     * @throws Exception is thrown on io error
     * @throws Exception is thrown on deserialization error
     */
    private void readObject( final ObjectInputStream p_stream ) throws Exception
    {
        p_stream.defaultReadObject();
        m_stream = m_streamsupplier.get();
    }

    /**
     * returns the formatter list
     *
     * @return formatter set
     */
    public Set<IFormatter> formatter()
    {
        return m_formatter;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        m_stream.println( MessageFormat.format( "{0}", this.format( p_argument ) ) );
        return Stream.of();
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
                         .map( i ->
                         {
                             if ( Objects.isNull( i ) )
                                 return "";

                             final Optional<IFormatter> l_formatter = m_formatter.parallelStream()
                                                                                 .filter( j -> j.get().isAssignableFrom( i.getClass() ) )
                                                                                 .limit( 1 )
                                                                                 .findFirst();

                             return l_formatter.isPresent()
                                    ? l_formatter.get().apply( i )
                                    : i.toString();
                         } )
                         .collect( Collectors.joining( m_seperator ) );
    }


}
