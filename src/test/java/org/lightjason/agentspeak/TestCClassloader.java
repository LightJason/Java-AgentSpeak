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

package org.lightjason.agentspeak;

import org.junit.Test;
import org.lightjason.agentspeak.common.CCommon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * test new classloader
 */
public final class TestCClassloader
{

    /**
     * test new classloader
     * @throws IOException on any io error
     */
    @Test
    public void path() throws IOException
    {
        System.out.println(
            classes(
                Thread.currentThread().getContextClassLoader(),
                MessageFormat.format( "{0}.{1}", CCommon.PACKAGEROOT, "action.builtin" )
            ).collect( Collectors.toList() )
        );
    }

    /**
     * get class list
     *
     * @param p_loader loader
     * @param p_package package
     * @return class stream
     * @throws IOException on any io error
     */
    private static Stream<? extends Class<?>> classes( @Nullable final ClassLoader p_loader, @Nonnull final String p_package ) throws IOException
    {
        if ( Objects.isNull( p_loader ) )
            return Stream.empty();

        final String l_root = p_package.replace( ".", "/" );

        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    p_loader.getResources( l_root ).asIterator(),
                    Spliterator.ORDERED
                ),
                false
            ).flatMap( i -> findclasses( new File( i.getFile() ), l_root ) );
    }

    /**
     * returns a stream of classes
     *
     * @param p_directory directory
     * @param p_directorypath classpage as directory
     * @return class stream
     */
    private static Stream<? extends Class<?>> findclasses( @Nonnull final File p_directory, @Nonnull final String p_directorypath )
    {
        if ( !p_directory.exists() )
            return Stream.empty();

        final String l_package = p_directory.getAbsolutePath().substring( p_directory.getAbsolutePath().indexOf( p_directorypath ) ).replace( "/", "." );
        return Arrays.stream( Objects.requireNonNull( p_directory.listFiles() ) )
                     .parallel()
                     .filter( i -> i.getName().endsWith( ".class" ) )
                     .map( i -> MessageFormat.format( "{0}.{1}", l_package, i.getName().replace( ".class", "" ) ) )
                     .map( i ->
                     {
                         try
                         {
                             return Class.forName( i );
                         }
                         catch ( final ClassNotFoundException l_exception )
                         {
                             return null;
                         }
                     } )
                     .filter( Objects::nonNull );

    }

}
