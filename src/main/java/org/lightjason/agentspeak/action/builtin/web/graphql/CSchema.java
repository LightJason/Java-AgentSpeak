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

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.GraphQL;
import org.apache.http.client.utils.URIBuilder;
import org.lightjason.agentspeak.action.builtin.web.IBaseWeb;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;


/**
 * create a grahql schema object based on an url.
 * The action creates of each input url a graphql
 * query object. Mostly the
 *
 * @see https://github.com/graphql/graphql-js/blob/master/src/utilities/introspectionQuery.js
 */
public class CSchema extends IBaseWeb
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8422764918617310599L;

    /**
     * ctor
     */
    public CSchema()
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
        // https://github.com/graphql-java/graphql-java-tools

        CCommon.flatten( p_argument )
               .map( ITerm::<String>raw )
               .filter( Objects::nonNull )
               .map( CSchema::schema )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

    /**
     * builds a graphql object
     *
     * @param p_url url of the schema
     * @return graphql object
     */
    @Nonnull
    private static GraphQL schema( @Nonnull final String p_url )
    {

// https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java
        try
        {
            System.out.println( MessageFormat.format(
                "{0}?{1}={2}",
                p_url,
                "query",
                "query%20IntrospectionQuery%20%7B%0A%20%20__schema%20%7B%0A%20%20%20%20queryType%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%7D%0A%20%20%20%20mutationType%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%7D%0A%20%20%20%20subscriptionType%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%7D%0A%20%20%20%20types%20%7B%0A%20%20%20%20%20%20...FullType%0A%20%20%20%20%7D%0A%20%20%20%20directives%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%20%20description%0A%20%20%20%20%20%20args%20%7B%0A%20%20%20%20%20%20%20%20...InputValue%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%7D%0A%20%20%7D%0A%7D%0A%0Afragment%20FullType%20on%20__Type%20%7B%0A%20%20kind%0A%20%20name%0A%20%20description%0A%20%20fields(includeDeprecated%3A%20true)%20%7B%0A%20%20%20%20name%0A%20%20%20%20description%0A%20%20%20%20args%20%7B%0A%20%20%20%20%20%20...InputValue%0A%20%20%20%20%7D%0A%20%20%20%20type%20%7B%0A%20%20%20%20%20%20...TypeRef%0A%20%20%20%20%7D%0A%20%20%20%20isDeprecated%0A%20%20%20%20deprecationReason%0A%20%20%7D%0A%20%20inputFields%20%7B%0A%20%20%20%20...InputValue%0A%20%20%7D%0A%20%20interfaces%20%7B%0A%20%20%20%20...TypeRef%0A%20%20%7D%0A%20%20enumValues(includeDeprecated%3A%20true)%20%7B%0A%20%20%20%20name%0A%20%20%20%20description%0A%20%20%20%20isDeprecated%0A%20%20%20%20deprecationReason%0A%20%20%7D%0A%20%20possibleTypes%20%7B%0A%20%20%20%20...TypeRef%0A%20%20%7D%0A%7D%0A%0Afragment%20InputValue%20on%20__InputValue%20%7B%0A%20%20name%0A%20%20description%0A%20%20type%20%7B%0A%20%20%20%20...TypeRef%0A%20%20%7D%0A%20%20defaultValue%0A%7D%0A%0Afragment%20TypeRef%20on%20__Type%20%7B%0A%20%20kind%0A%20%20name%0A%20%20ofType%20%7B%0A%20%20%20%20kind%0A%20%20%20%20name%0A%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20kind%0A%20%20%20%20%20%20name%0A%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%7D%0A%20%20%7D%0A%7D%0A" )

            );

            return GraphQL.newGraphQL(
                 SchemaParser.newParser()
                             .schemaString(
                                 httpdata(
                                    MessageFormat.format(
                                        "{0}?{1}={3}",
                                        p_url,
                                        "query",
                                        "query%20IntrospectionQuery%20%7B%0A%20%20__schema%20%7B%0A%20%20%20%20queryType%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%7D%0A%20%20%20%20mutationType%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%7D%0A%20%20%20%20subscriptionType%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%7D%0A%20%20%20%20types%20%7B%0A%20%20%20%20%20%20...FullType%0A%20%20%20%20%7D%0A%20%20%20%20directives%20%7B%0A%20%20%20%20%20%20name%0A%20%20%20%20%20%20description%0A%20%20%20%20%20%20args%20%7B%0A%20%20%20%20%20%20%20%20...InputValue%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%7D%0A%20%20%7D%0A%7D%0A%0Afragment%20FullType%20on%20__Type%20%7B%0A%20%20kind%0A%20%20name%0A%20%20description%0A%20%20fields(includeDeprecated%3A%20true)%20%7B%0A%20%20%20%20name%0A%20%20%20%20description%0A%20%20%20%20args%20%7B%0A%20%20%20%20%20%20...InputValue%0A%20%20%20%20%7D%0A%20%20%20%20type%20%7B%0A%20%20%20%20%20%20...TypeRef%0A%20%20%20%20%7D%0A%20%20%20%20isDeprecated%0A%20%20%20%20deprecationReason%0A%20%20%7D%0A%20%20inputFields%20%7B%0A%20%20%20%20...InputValue%0A%20%20%7D%0A%20%20interfaces%20%7B%0A%20%20%20%20...TypeRef%0A%20%20%7D%0A%20%20enumValues(includeDeprecated%3A%20true)%20%7B%0A%20%20%20%20name%0A%20%20%20%20description%0A%20%20%20%20isDeprecated%0A%20%20%20%20deprecationReason%0A%20%20%7D%0A%20%20possibleTypes%20%7B%0A%20%20%20%20...TypeRef%0A%20%20%7D%0A%7D%0A%0Afragment%20InputValue%20on%20__InputValue%20%7B%0A%20%20name%0A%20%20description%0A%20%20type%20%7B%0A%20%20%20%20...TypeRef%0A%20%20%7D%0A%20%20defaultValue%0A%7D%0A%0Afragment%20TypeRef%20on%20__Type%20%7B%0A%20%20kind%0A%20%20name%0A%20%20ofType%20%7B%0A%20%20%20%20kind%0A%20%20%20%20name%0A%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20kind%0A%20%20%20%20%20%20name%0A%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20ofType%20%7B%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20kind%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20name%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%20%20%7D%0A%20%20%20%20%20%20%7D%0A%20%20%20%20%7D%0A%20%20%7D%0A%7D%0A" )

                                 )
                             )
                             .build()
                             .makeExecutableSchema()
            )
            .build();
        }
        catch ( final IOException l_exceptin )
        {
            throw new RuntimeException( l_exceptin );
        }
    }
}
