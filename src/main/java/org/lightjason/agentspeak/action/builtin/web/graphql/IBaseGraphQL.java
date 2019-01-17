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

package org.lightjason.agentspeak.action.builtin.web.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.lightjason.agentspeak.action.builtin.web.IBaseWeb;
import org.lightjason.agentspeak.action.builtin.web.rest.IBaseRest;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * base graphql access
 */
public abstract class IBaseGraphQL extends IBaseWeb
{
    /**
     * json mapper
     */
    protected static final ObjectMapper JSONMAPPER = new ObjectMapper();
    /**
     * serial id
     */
    private static final long serialVersionUID = -4140550516932255685L;


    /**
     * ctor
     *
     * @param p_length length
     */
    protected IBaseGraphQL( final int p_length )
    {
        super( p_length );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    /**
     * executes graphql query and returns a stream of terms of the result
     *
     * @param p_url endpoint url
     * @param p_query query
     * @return result literal stream
     *
     * @throws IOException is thrown on parsing and connection errors
     */
    protected static Stream<ITerm> sendquery( @Nonnull final String p_url, @Nonnull final String p_query ) throws IOException
    {
        final HttpPost l_post = httppost( p_url );
        l_post.setHeader( "Content-Type", "application/json" );
        l_post.setEntity( new StringEntity( p_query ) );

        return flatterm( JSONMAPPER.readValue( httppostexecute( l_post ), Map.class ) );
    }

    @Nonnull
    @Override
    public final Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                 @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_argument = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( l_argument.size() < 3 )
            return CFuzzyValue.of( false );

        try
        {
            p_return.add(
                p_argument.size() == 3
                ? CLiteral
                    .of( l_argument.get( l_argument.size() - 1 ).<String>raw(), sendquery( l_argument.get( 0 ).raw(), this.query( l_argument.get( 1 ) ) ) )
                : IBaseRest.baseliteral(
                    l_argument.stream().skip( 2 ).map( ITerm::<String>raw ),
                    sendquery( l_argument.get( 0 ).raw(), this.query( l_argument.get( 1 ) ) )
                )
            );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }

        return Stream.of();
    }

    /**
     * converts a term argument to a graphql query
     *
     * @param p_argument query argument
     * @return graphql query
     *
     * @note query must be encapsulate as string in a json object with key query
     */
    @Nonnull
    private String query( @Nonnull final ITerm p_argument )
    {
        return MessageFormat.format(
            "'{'\"query\" : \"{0}\"'}'",
            this.argumentquery( p_argument ).replace( "\"", "\\\"" )
        );
    }

    /**
     * convert argument into graphql query
     *
     * @param p_argument query argument
     * @return query string
     */
    protected abstract String argumentquery( @Nonnull final ITerm p_argument );
}
