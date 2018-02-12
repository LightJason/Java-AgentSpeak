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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lightjason.agentspeak.action.builtin.web.IBaseWeb;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
     * object mapper
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
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

        return CFuzzyValue.from( true );

    }

    /**
     * converts a literal to a map structure
     *
     * @param p_literal literal
     * @return map
     */
    private static Map<String, Object> literalmap( final ILiteral p_literal )
    {
        final Map<String, Object> l_structure = new HashMap<>();
        l_structure.put(
            p_literal.functor(),
            literalvalues( p_literal )
        );

        return l_structure;
    }

    /**
     * converts literal values to a map structure
     *
     * @param p_literal input literal
     * @return map
     */
    private static Map<String, Object> literalvalues( final ILiteral p_literal )
    {
        return p_literal.values()
                 .filter( i -> Objects.nonNull( i.raw() ) )
                 .collect(
                     Collectors.toMap(
                         ITerm::functor,
                         i -> i instanceof ILiteral ? literalvalues( i.raw() ) : i.raw()
                     )
                 );
    }

}
