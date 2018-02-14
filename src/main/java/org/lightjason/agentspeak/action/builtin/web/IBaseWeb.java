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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
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
     * returns a http-post connection
     *
     * @param p_url url
     * @return request
     */
    protected static HttpPost httppost( @Nonnull final String p_url )
    {
        return header( new HttpPost( p_url ) );
    }

    /**
     * returns a http-get connection
     *
     * @param p_url url
     * @return request
     */
    protected static HttpGet httpget( @Nonnull final String p_url )
    {
        return header( new HttpGet( p_url ) );
    }

    /**
     * sets the default header definition
     *
     * @param p_request request
     * @tparam T request type
     * @return input request
     */
    private static <T extends HttpRequestBase> T header( @Nonnull final T p_request )
    {
        p_request.setHeader(
            "User-Agent",
            ( System.getProperty( "http.agent" ) == null ) || ( System.getProperty( "http.agent" ).isEmpty() )
            ? CCommon.PACKAGEROOT + CCommon.configuration().getString( "version" )
            : System.getProperty( "http.agent" )
        );

        return p_request;
    }


    /**
     * execute http-get request
     *
     * @param p_url url
     * @return url data
     *
     * @throws IOException is thrown on connection errors
     */
    @Nonnull
    protected static String httpgetexecute( @Nonnull final String p_url ) throws IOException
    {
        return EntityUtils.toString( HttpClientBuilder.create().build().execute( httpget( p_url ) ).getEntity() );
    }

    /**
     * execute http-get request
     *
     * @param p_get get request
     * @return output data as string
     * @throws IOException is thrown on connection errors
     */
    protected static String httpgetexecute( @Nonnull final HttpGet p_get ) throws IOException
    {
        return EntityUtils.toString( HttpClientBuilder.create().build().execute( p_get ).getEntity() );
    }

    /**
     * execute http-post request
     *
     * @param p_post post request
     * @return output data as string
     * @throws IOException is thrown on connection errors
     */
    protected static String httppostexecute( @Nonnull final HttpPost p_post ) throws IOException
    {
        return EntityUtils.toString( HttpClientBuilder.create().build().execute( p_post ).getEntity() );
    }



}
