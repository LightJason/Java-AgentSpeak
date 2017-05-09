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

package org.lightjason.agentspeak.action.buildin;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.rest.CJsonList;
import org.lightjason.agentspeak.action.buildin.rest.CJsonObject;
import org.lightjason.agentspeak.action.buildin.rest.CXMLObject;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * REST-API test
 */
public final class TestCActionRest extends IBaseTest
{

    /**
     * tets json list
     */
    @Test
    public final void jsonlist()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonList().execute(
            null,
            false,
            Stream.of( "https://api.github.com/repos/LightJason/SocialForce/commits", "testjsonlist", "item" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( l_return.get( 0 ).<ILiteral>raw().functor(), "testjsonlist" );
        Assert.assertFalse( l_return.get( 0 ).<ILiteral>raw().values().map( ITerm::functor ).noneMatch( i -> i.equals( "item" ) ) );
    }


    /**
     * test json object
     */
    @Test
    public final void jsonobject()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonObject().execute(
            null,
            false,
            Stream.of( "https://maps.googleapis.com/maps/api/geocode/json?address=Frankfurt", "testjsonobject" )
                  .map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( l_return.get( 0 ).<ILiteral>raw().functor(), "testjsonobject" );
    }


    /**
     * test xml object
     */
    @Test
    public final void xmlobject()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CXMLObject().execute(
            null,
            false,
            Stream.of( "https://en.wikipedia.org/wiki/Special:Export/Normalized_compression_distance", "testxml" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( l_return.get( 0 ).<ILiteral>raw().functor(), "testxml" );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionRest().invoketest();
    }
}
