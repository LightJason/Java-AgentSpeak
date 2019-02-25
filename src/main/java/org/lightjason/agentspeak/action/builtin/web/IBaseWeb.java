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

package org.lightjason.agentspeak.action.builtin.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.action.builtin.web.rest.IBaseRest;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * web base action class
 */
public abstract class IBaseWeb extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 4839156213009145751L;

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
     * @return input request
     *
     * @tparam T request type
     */
    private static <T extends HttpRequestBase> T header( @Nonnull final T p_request )
    {
        p_request.setHeader(
            "User-Agent",
            ( Objects.isNull( System.getProperty( "http.agent" ) ) ) || ( System.getProperty( "http.agent" ).isEmpty() )
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
     *
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
     *
     * @throws IOException is thrown on connection errors
     */
    protected static String httppostexecute( @Nonnull final HttpPost p_post ) throws IOException
    {
        return EntityUtils.toString( HttpClientBuilder.create().build().execute( p_post ).getEntity() );
    }

    /**
     * creates a literal structure of a stream of string elements,
     * the string stream will be build in a tree structure
     *
     * @param p_functor stream with functor strings
     * @param p_values value stream
     * @return term
     */
    @Nonnull
    protected static ITerm baseliteral( @Nonnull final Stream<String> p_functor, @Nonnull final Stream<ITerm> p_values )
    {
        final Stack<String> l_tree = p_functor.collect( Collectors.toCollection( Stack::new ) );

        ILiteral l_literal = CLiteral.of( l_tree.pop(), p_values );
        while ( !l_tree.isEmpty() )
            l_literal = CLiteral.of( l_tree.pop(), l_literal );

        return l_literal;
    }

    /**
     * converts an object into a term stream
     *
     * @param p_object object
     * @return term stream
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    protected static Stream<ITerm> flatterm( @Nullable final Object p_object )
    {
        if ( Objects.isNull( p_object ) || p_object instanceof Map && ( (Map<String, ?>) p_object ).isEmpty() )
            return Stream.empty();

        return p_object instanceof Map
               ? flatmap( (Map<String, ?>) p_object )
               : p_object instanceof Collection
                 ? flatcollection( (Collection) p_object )
                 : Stream.of( CRawTerm.of( p_object ) );
    }



    /**
     * transformas a map into a literal
     *
     * @param p_map input map
     * @return term stream
     */
    @Nonnull
    private static Stream<ITerm> flatmap( @Nonnull final Map<String, ?> p_map )
    {
        return p_map.entrySet()
                    .stream()
                    .map( i ->
                              CLiteral.of(
                                  StringUtils.uncapitalize( i.getKey() ).replaceAll( "[^([a-zA-Z][0-9]\\-_/]", "-" ),
                                  flatterm( i.getValue() )
                              )
                    );
    }

    /**
     * transforms a collection into a term stream
     *
     * @param p_collection collection
     * @return term stream
     */
    @Nonnull
    private static Stream<ITerm> flatcollection( @Nonnull final Collection<?> p_collection )
    {
        return p_collection.stream().flatMap( IBaseRest::flatterm );
    }

}
