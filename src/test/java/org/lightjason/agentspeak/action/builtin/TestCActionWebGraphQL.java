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
import org.junit.Assume;
import org.junit.Before;
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
     * result query literal
     */
    private ILiteral m_result;

    /**
     * initialize
     */
    @Before
    public void initialize()
    {
        try
        {
            m_result = CLiteral.parse( "graphql( data( stationWithEvaId( name( 'Frankfurt (Main) Hbf' ), location( latitude( 50.107145 ), longitude( 8.663789 ) ) ) ) ) )" );
        }
        catch ( final Exception l_exception )
        {
            Assert.fail( l_exception.getMessage() );
        }
    }

    /**
     * run graphql query test with literal
     */
    @Test
    public void queryliteral()
    {
        Assume.assumeNotNull( m_result );

        final List<ITerm> l_return = new ArrayList<>();
        Assert.assertTrue(
            new CQueryLiteral().execute(
                false,
                IContext.EMPTYPLAN,
                Stream.of(
                    CRawTerm.of( "https://developer.deutschebahn.com/free1bahnql/graphql" ),
                    CLiteral.of(
                        "stationWithEvaId",
                        CLiteral.of( "evaId", CRawTerm.of( 8000105 ) ),
                        CLiteral.of( "name" ),
                        CLiteral.of( "location", CLiteral.of( "latitude" ), CLiteral.of( "longitude" ) )
                    ),
                    CRawTerm.of( "graphql" )
                ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ) instanceof ILiteral );
        Assert.assertEquals( m_result.hashCode(), l_return.get( 0 ).<ILiteral>raw().hashCode() );
    }


    /**
     * run graphql query test with native query
     */
    @Test
    public void querymanual()
    {
        Assume.assumeNotNull( m_result );

        final List<ITerm> l_return = new ArrayList<>();
        Assert.assertTrue(
            new CQueryNative().execute(
                false,
                IContext.EMPTYPLAN,
                Stream.of(
                    CRawTerm.of( "https://developer.deutschebahn.com/free1bahnql/graphql" ),
                    CRawTerm.of( "{ stationWithEvaId(evaId: 8000105) { name location { latitude longitude } } }" ),
                    CRawTerm.of( "graphql" )
                ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ) instanceof ILiteral );
        Assert.assertEquals( m_result.hashCode(), l_return.get( 0 ).<ILiteral>raw().hashCode() );
    }

}
