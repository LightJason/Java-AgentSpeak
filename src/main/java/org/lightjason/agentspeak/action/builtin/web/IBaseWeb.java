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

package org.lightjason.agentspeak.action.builtin.web;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.common.CCommon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.Consumer;


/**
 * web base action class
 */
public abstract class IBaseWeb extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -334813846636762405L;

    /**
     * ctor
     *
     * @param p_length length
     */
    protected IBaseWeb( final int p_length )
    {
        super( p_length );
    }

    /**
     * creates a http connection
     *
     * @param p_url url
     * @return http connection
     */
    @Nonnull
    protected static HttpURLConnection httpconnection( @Nonnull final String p_url ) throws IOException
    {
        final HttpURLConnection l_connection = (HttpURLConnection) new URL( p_url ).openConnection();

        // follow HTTP redirects
        l_connection.setInstanceFollowRedirects( true );
        l_connection.setDoOutput( true );

        // set a HTTP-User-Agent if not exists
        l_connection.setRequestProperty(
            "User-Agent",
            ( System.getProperty( "http.agent" ) == null ) || ( System.getProperty( "http.agent" ).isEmpty() )
            ? CCommon.PACKAGEROOT + CCommon.configuration().getString( "version" )
            : System.getProperty( "http.agent" )
        );

        return l_connection;
    }

    /**
     * read http out of a connection
     *
     * @param p_connection connection
     * @return output as string
     * @throws IOException on io exception
     */
    @Nonnull
    protected static String httpoutput( @Nonnull final HttpURLConnection p_connection ) throws IOException
    {
        // read stream data
        final InputStream l_stream = p_connection.getInputStream();
        final String l_return = CharStreams.toString(
            new InputStreamReader(
                l_stream,
                ( p_connection.getContentEncoding() == null ) || ( p_connection.getContentEncoding().isEmpty() )
                ? Charsets.UTF_8
                : Charset.forName( p_connection.getContentEncoding() )
            )
        );
        Closeables.closeQuietly( l_stream );

        return l_return;
    }

    /**
     * creates a HTTP connection and reads the data
     *
     * @param p_url url
     * @return url data
     *
     * @throws IOException is thrown on connection errors
     */
    @Nonnull
    protected static String httpget( @Nonnull final String p_url, @Nullable final Consumer<HttpURLConnection>... p_connetion ) throws IOException
    {
        final HttpURLConnection l_connection = httpconnection( p_url );
        if ( p_connetion != null )
            Arrays.stream( p_connetion ).forEach( i -> i.accept( l_connection ) );
        return httpoutput( l_connection );
    }



}
