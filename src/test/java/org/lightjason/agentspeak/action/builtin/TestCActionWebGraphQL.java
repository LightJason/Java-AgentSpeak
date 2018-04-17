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

package org.lightjason.agentspeak.action.builtin;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.web.graphql.CQueryLiteral;
import org.lightjason.agentspeak.action.builtin.web.graphql.CQueryNative;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test graphql actions
 */
public final class TestCActionWebGraphQL extends IBaseTest
{

    /**
     * run graphql query test with literal
     */
    @Test
    public void queryliteral()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            new CQueryLiteral().execute(
                false,
                IContext.EMPTYPLAN,
                Stream.of(
                    CRawTerm.of( "https://fakerql.com/graphql" ),
                    CLiteral.of(
                        "allUsers",
                        CLiteral.of( "id" ),
                        CLiteral.of( "firstName" ),
                        CLiteral.of( "lastName" )
                    ),
                    CRawTerm.of( "graphql" )
                ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ) instanceof ILiteral );
        Assert.assertEquals( "graphql", l_return.get( 0 ).<ILiteral>raw().functor() );
    }


    /**
     * run graphql query test with native query
     */
    @Test
    public void querymanual()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            new CQueryNative().execute(
                false,
                IContext.EMPTYPLAN,
                Stream.of(
                    CRawTerm.of( "https://fakerql.com/graphql" ),
                    CRawTerm.of( "{Product(id: \"cjdn6szou00dw25107gcuy114\") {id price name}}" ),
                    CRawTerm.of( "graphql" )
                ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ) instanceof ILiteral );
        Assert.assertEquals( "graphql", l_return.get( 0 ).<ILiteral>raw().functor() );

        // test-case returns random datasets back
        Assert.assertEquals(
            CLiteral.of( "graphql", CLiteral.of(
                "data", CLiteral.of(
                    "product", CLiteral.of(
                        "id",
                        CRawTerm.of( "cjdn6szou00dw25107gcuy114" )
                    ),
                    CLiteral.of(
                        "price",
                        CRawTerm.of( 126D )
                    ),
                    CLiteral.of(
                        "name",
                        CRawTerm.of( "Handmade Granite Cheese" )
                    )
                )
            ) ).structurehash(),
            l_return.get( 0 ).<ILiteral>raw().structurehash()
        );
    }

}
