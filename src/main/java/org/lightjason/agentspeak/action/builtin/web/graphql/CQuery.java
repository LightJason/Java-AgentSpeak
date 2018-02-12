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

import org.lightjason.agentspeak.action.builtin.web.IBaseWeb;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action to run an synchronized graphql query.
 * The calls the data of a graphql service and returns a literal
 * based on the query result, the input argument is at the first
 * position the graphql service url, all other arguments will used
 * as literal which represent the query structure of the service
 *
 * @code

 * @endcode
 * @see http://graphql.org/
 * @see https://github.com/graphql-java/graphql-java
 */
public final class CQuery extends IBaseWeb
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1697770409712623281L;

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
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                               @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        // https://github.com/graphql-java/graphql-java-tools
        // https://github.com/graphql-java/graphql-java-http-example
        // https://stackoverflow.com/questions/42024158/how-to-access-github-graphql-api-using-java
        // http://graphql-java.readthedocs.io/en/v6/execution.html
        // https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
        // https://github.com/graphql/graphql-js/blob/master/src/utilities/introspectionQuery.js
        // https://stackoverflow.com/questions/36914918/sending-graphql-query-in-java
        // https://github.com/k0kubun/graphql-query-builder
        // https://www.pluralsight.com/guides/java-and-j2ee/building-a-graphql-server-with-spring-boot

        System.out.println( query( l_arguments.get( 1 ).raw() ) );
        return CFuzzyValue.from( true );
    }

    /**
     * converts a literal to a map structure
     *
     * @param p_literal literal
     * @return query string
     */
    private static String query( final ILiteral p_literal )
    {
        if ( p_literal.emptyValues() )
            return p_literal.functor();

        final String l_query = queryarguments( p_literal.values() );
        final String l_fields = p_literal.values()
                                         .filter( i -> i instanceof ILiteral )
                                         .map( i -> i.<ILiteral>raw() )
                                         .collect( Collectors.toMap( i -> i.functor(), i -> query( i ), ( i, j ) -> i ) )
                                         .values()
                                         .stream()
                                         .collect( Collectors.joining( " " ) )
                                         .trim();

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
    private static String queryarguments( final Stream<ITerm> p_stream )
    {
        return p_stream.filter( i -> i instanceof ILiteral )
                       .collect( Collectors.toMap( i -> i.functor(), i -> formatqueryargument( i.raw() ), ( i, j ) -> i ) )
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
    private static String formatqueryargument( final ILiteral p_literal )
    {
        return p_literal.orderedvalues()
                        .filter( i -> i instanceof IRawTerm<?> )
                        .findFirst()
                        .map( ITerm::raw )
                        .map( i -> i instanceof String ? "\"" + i + "\"" : i )
                        .map( i -> p_literal.functor() + " : " + i )
                        .orElse( "" );
    }

}
