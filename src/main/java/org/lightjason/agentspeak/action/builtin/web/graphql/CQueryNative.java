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

import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;


/**
 * action to run a graphql query by a native graphql query.
 * The calls the data of a graphql service and returns a literal
 * based on the query result, the input argument is at the first
 * position the graphql service url, the second argument a string
 * with a grapqhl query structure, and all other arguments the
 * literal functor structure. The action fails on connection
 * and parsing errors.
 *
 * @code GQ = web/graphql/querynative( "https://fakerql.com/graphql", '{Product(id: "cjdn6szou00dw25107gcuy114") {id price name}}', "graphql-fake" ); @endcode
 * @see http://graphql.org/
 */
public final class CQueryNative extends IBaseGraphQL
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 569503639553301289L;

    /**
     * ctor
     */
    public CQueryNative()
    {
        super( 3 );
    }

    @Override
    protected final String argumentquery( @Nonnull final ITerm p_argument )
    {
        return p_argument.raw();
    }
}
