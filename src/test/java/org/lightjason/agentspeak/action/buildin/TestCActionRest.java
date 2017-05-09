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
     * test json list error
     */
    @Test
    public final void jsonlisterror()
    {
        Assert.assertFalse(
            new CJsonList().execute(
                null,
                false,
                Stream.of( "jsonlist", "testjsonlist" )
                      .map( CRawTerm::from )
                      .collect( Collectors.toList() ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }

    /**
     * test json list with single argument
     */
    @Test
    public final void jsonlistsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonList().execute(
            null,
            false,
            Stream.of( "https://api.github.com/repos/LightJason/SocialForce/commits", "testjsonlist" )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        Assert.assertFalse( l_return.isEmpty() );
        Assert.assertTrue( l_return.stream().map( ITerm::raw ).allMatch( i -> i instanceof ILiteral ) );
        Assert.assertTrue( l_return.stream().map( ITerm::<ILiteral>raw ).map( ITerm::functor ).allMatch( i -> i.equals( "testjsonlist" ) ) );
    }


    /**
     * test json list with multiple argument
     */
    @Test
    public final void jsonlistmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonList().execute(
            null,
            false,
            Stream.of( "https://api.github.com/repos/LightJason/SocialForce/commits", "testjsonlist", "item" )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( l_return.get( 0 ).<ILiteral>raw().functor(), "testjsonlist" );
        Assert.assertFalse( l_return.get( 0 ).<ILiteral>raw().values().map( ITerm::functor ).noneMatch( i -> i.equals( "item" ) ) );
    }


    /**
     * test json object error
     */
    @Test
    public final void jsonobjecterror()
    {
        Assert.assertFalse(
            new CJsonObject().execute(
                null,
                false,
                Stream.of( "jsonobject", "testjsonobject" )
                      .map( CRawTerm::from )
                      .collect( Collectors.toList() ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }


    /**
     * test json object with single argument
     */
    @Test
    public final void jsonobjectsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonObject().execute(
            null,
            false,
            Stream.of( "https://maps.googleapis.com/maps/api/geocode/json?address=Frankfurt", "testjsonobject" )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( l_return.get( 0 ).<ILiteral>raw().functor(), "testjsonobject" );
    }


    /**
     * test json object with multiple arguments
     */
    @Test
    public final void jsonobjectmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonObject().execute(
            null,
            false,
            Stream.of( "https://maps.googleapis.com/maps/api/geocode/json?address=Frankfurt", "testjsonobject", "loc" )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( l_return.get( 0 ).<ILiteral>raw().functor(), "testjsonobject" );
        Assert.assertTrue( l_return.get( 0 ).<ILiteral>raw().values().findFirst().isPresent() );
        Assert.assertEquals(
            l_return.get( 0 ).<ILiteral>raw().values().findFirst().map( ITerm::functor ).orElseThrow( IllegalArgumentException::new ),
            "loc"
        );
    }


    /**
     * test xml object error
     */
    @Test
    public final void xmlobjecterror()
    {
        Assert.assertFalse(
            new CXMLObject().execute(
                null,
                false,
                Stream.of( "xmlobject", "testxml" )
                      .map( CRawTerm::from )
                      .collect( Collectors.toList() ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }


    /**
     * test xml object with single argument
     */
    @Test
    public final void xmlobjectsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CXMLObject().execute(
            null,
            false,
            Stream.of( "https://en.wikipedia.org/wiki/Special:Export/Normalized_compression_distance", "testxml" )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( l_return.get( 0 ).<ILiteral>raw().functor(), "testxml" );
    }


    /**
     * test xml object with single argument
     */
    @Test
    public final void xmlobjectmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CXMLObject().execute(
            null,
            false,
            Stream.of( "https://en.wikipedia.org/wiki/Special:Export/Normalized_compression_distance", "testxml", "ncd" )
                  .map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( l_return.get( 0 ).<ILiteral>raw().functor(), "testxml" );
        Assert.assertTrue( l_return.get( 0 ).<ILiteral>raw().values().findFirst().isPresent() );
        Assert.assertEquals(
            l_return.get( 0 ).<ILiteral>raw().values().findFirst().map( ITerm::functor ).orElseThrow( IllegalArgumentException::new ),
            "ncd"
        );
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
