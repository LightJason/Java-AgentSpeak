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

import com.google.common.base.CharMatcher;
import org.junit.Test;
import org.lightjason.agentspeak.common.CCommon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
            ).collect( Collectors.toSet() )
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
    @Nonnull
    private static Stream<? extends Class<?>> classes( @Nullable final ClassLoader p_loader, @Nonnull final String p_package ) throws IOException
    {
        return Objects.isNull( p_loader )
               ? Stream.empty()
               : p_loader.resources( p_package.replace( ".", "/" ) ).flatMap( i -> findclasses( i, p_package ) );
    }

    /**
     * returns a stream of classes
     *
     * @param p_url directory
     * @param p_package package
     * @return class stream
     */
    @Nonnull
    private static Stream<? extends Class<?>> findclasses( @Nonnull final URL p_url, @Nonnull final String p_package )
    {
        if ( !p_url.getProtocol().equals( "file" ) )
            return Stream.empty();

        final Path l_directory;
        try
        {
             l_directory = Paths.get( p_url.toURI() );
        }
        catch ( final URISyntaxException l_exception )
        {
            return Stream.empty();
        }

        try
        {
             return Files.walk( l_directory )
                         .parallel()
                         .map( i -> i.normalize().toAbsolutePath().toFile() )
                         .filter( i -> i.getName().endsWith( ".class" ) )
                         .map( Object::toString )
                         .map( i -> classname( i, p_package ) )
                         .map( i ->
                         {
                             try
                             {
                                 //System.out.println( i );
                                 return Class.forName( i );
                             }
                             catch ( final ClassNotFoundException l_exception )
                             {
                                 return null;
                             }
                         } )
                         .filter( Objects::nonNull );
        }
        catch ( final IOException l_exception )
        {
            return Stream.empty();
        }
    }

    /**
     * creates a class name from file input string
     *
     * @param p_name relative package path and filename
     * @return full-qualified class name
     */
    @Nonnull
    private static String classname( @Nonnull final String p_name, @Nonnull final String p_package )
    {
        String l_classname = p_name.replace( "/", "." ).replace( ".class", "" );
        l_classname = l_classname.substring( l_classname.indexOf( p_package ) );

        final int l_dollar = l_classname.lastIndexOf(36 );
        return l_dollar == -1
            ? l_classname
            : Stream.of(
                l_classname.substring( 0, l_dollar ),
                CharMatcher.anyOf( "0123456789" ).trimLeadingFrom( l_classname.substring( l_dollar + 1 ) )
            ).filter( i -> !i.isEmpty() ).collect( Collectors.joining( "." ) );
    }
}
