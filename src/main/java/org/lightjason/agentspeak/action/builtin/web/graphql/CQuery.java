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

package org.lightjason.agentspeak.action.builtin.web.graphql;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.lightjason.agentspeak.action.builtin.web.IBaseWeb;
import org.lightjason.agentspeak.action.builtin.web.rest.IBaseRest;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action to run an synchronized graphql query.
 * The calls the data of a graphql service and returns a literal
 * based on the query result, the input argument is at the first
 * position the graphql service url, the second argument a literal, which
 * defines the query structure and all other arguments the literal functor structure.
 * The action fails on connection and parsing errors.
 *
 * @code L = generic/type/parseliteral( "allUsers(id, firstName, lastName)" ); GQ = web/graphql/query( "https://fakerql.com/graphql", L, "graphql-fake" ); @endcode
 * @see http://graphql.org/
 */
public final class CQuery extends IBaseWeb
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1297247775003220187L;
    /**
     * json mapper
     */
    private static final ObjectMapper JSONMAPPER = new ObjectMapper();

    /**
     * ctor
     */
    public CQuery()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_argument = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( l_argument.size() < 3 )
            return CFuzzyValue.from( false );

        try
        {
            final HttpPost l_post = httppost( l_argument.get( 0 ).raw() );
            l_post.setHeader( "Content-Type", "application/json" );
            l_post.setEntity( new StringEntity( query( l_argument.get( 1 ).raw() ) ) );

            p_return.add(
                p_argument.size() == 3
                ? CLiteral.from( l_argument.get( l_argument.size() - 1 ).<String>raw(), flatterm( JSONMAPPER.readValue( httppostexecute( l_post ), Map.class ) ) )
                : IBaseRest.baseliteral(
                    l_argument.stream().skip( 2 ).map( ITerm::<String>raw ),
                    flatterm( JSONMAPPER.readValue( httppostexecute( l_post ), Map.class ) )
                )
            );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }

        return CFuzzyValue.from( true );
    }

    /**
     * converts a literal to a query
     *
     * @param p_literal literal
     * @return graphql query
     * @note query must be encapsulate as string in a json object with key query
     */
    @Nonnull
    private static String query( @Nonnull final ILiteral p_literal )
    {
        return MessageFormat.format(
            "'{'\"query\" : \"{0}\"'}'",
            MessageFormat.format( "'{'{0}'}'", root( p_literal ) ).replace( "\"", "\\\"" )
        );
    }

    /**
     * converts a literal query structure
     *
     * @param p_literal literal
     * @return query string
     */
    @Nonnull
    private static String root( @Nonnull final ILiteral p_literal )
    {
        if ( p_literal.emptyValues() )
            return p_literal.functor();

        final String l_query = values( p_literal.values() );
        final String l_fields = fields( p_literal.values() );

        return MessageFormat.format(
            "{0}{1}{2}",
            p_literal.functor(), l_query.isEmpty() ? "" : MessageFormat.format( "({0})", l_query ),
            l_fields.isEmpty() ? "" : MessageFormat.format( "'{'{0}'}'", l_fields )
        );
    }

    /**
     * argument values
     *
     * @param p_stream term stream
     * @return string arguemnts
     */
    @Nonnull
    private static String values( @Nonnull final Stream<ITerm> p_stream )
    {
        return p_stream.filter( i -> i instanceof ILiteral )
                       .map( ITerm::<ILiteral>raw )
                       .collect( Collectors.toMap( ITerm::functor, i -> valueformat( i.raw() ), ( i, j ) -> i ) )
                       .values()
                       .stream()
                       .collect( Collectors.joining( " " ) )
                       .trim();
    }

    /**
     * creates the field list
     *
     * @param p_stream field stream
     * @return string definition
     */
    @Nonnull
    private static String fields( @Nonnull final Stream<ITerm> p_stream )
    {
        return p_stream.filter( i -> i instanceof ILiteral )
                       .map( ITerm::<ILiteral>raw )
                       .filter( i -> i.values().noneMatch( j -> j instanceof IRawTerm ) )
                       .collect( Collectors.toMap( ITerm::functor, CQuery::root, ( i, j ) -> i ) )
                       .values()
                       .stream()
                       .collect( Collectors.joining( " " ) )
                       .trim();
    }

    /**
     * format query argument
     * @param p_literal literal
     * @return functor with value
     */
    @Nonnull
    private static String valueformat( @Nonnull final ILiteral p_literal )
    {
        return p_literal.values()
                        .filter( i -> i instanceof IRawTerm<?> )
                        .findFirst()
                        .map( ITerm::raw )
                        .map( CQuery::typeformat )
                        .map( i -> p_literal.functor() + " : " + i )
                        .orElse( "" );
    }

    /**
     * type mapping
     *
     * @param p_value any value
     * @return graphql string
     */
    private static String typeformat( final Object p_value )
    {
        if ( p_value instanceof String )
            return MessageFormat.format( "\"{0}\"", p_value );

        if ( p_value instanceof Collection<?> )
            return MessageFormat.format(
                "[{0}]",
                ( (Collection<?>) p_value ).stream().map( CQuery::typeformat ).collect( Collectors.joining( ", " ) )
            );

        return p_value.toString();
    }

}
