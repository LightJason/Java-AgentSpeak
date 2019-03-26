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

package org.lightjason.agentspeak.action;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.web.rest.CJsonList;
import org.lightjason.agentspeak.action.web.rest.CJsonObject;
import org.lightjason.agentspeak.action.web.rest.CXMLObject;
import org.lightjason.agentspeak.error.context.CExecutionIllegalStateException;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * REST-API test
 */
public final class TestCActionWebRest extends IBaseTest
{

    /**
     * test json list error
     */
    @Test( expected = CExecutionIllegalStateException.class )
    public void jsonlisterror()
    {
        new CJsonList().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( "jsonlist", "testjsonlist" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }

    /**
     * test json list with single argument
     */
    @Test
    public void jsonlistsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonList().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "https://api.github.com/repos/LightJason/Examples/commits", "testjsonlist" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertFalse( l_return.isEmpty() );
        Assert.assertTrue( l_return.stream().map( ITerm::raw ).allMatch( i -> i instanceof ILiteral ) );
        Assert.assertTrue( l_return.stream().map( ITerm::<ILiteral>raw ).map( ITerm::functor ).allMatch( i -> i.equals( "testjsonlist" ) ) );
    }


    /**
     * test json list with multiple argument
     */
    @Test
    public void jsonlistmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonList().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "https://api.github.com/repos/LightJason/Examples/commits", "testjsonlist", "item" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( "testjsonlist", l_return.get( 0 ).<ILiteral>raw().functor() );
        Assert.assertFalse( l_return.get( 0 ).<ILiteral>raw().values().map( ITerm::functor ).noneMatch( i -> i.equals( "item" ) ) );
    }


    /**
     * test json object error
     */
    @Test( expected = CExecutionIllegalStateException.class )
    public void jsonobjecterror()
    {

        new CJsonObject().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( "jsonobject", "testjsonobject" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }


    /**
     * test json object with single argument
     */
    @Test
    public void jsonobjectsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonObject().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "https://maps.googleapis.com/maps/api/geocode/json?address=Frankfurt", "testjsonobject" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( "testjsonobject", l_return.get( 0 ).<ILiteral>raw().functor() );
    }


    /**
     * test json object with multiple arguments
     */
    @Test
    public void jsonobjectmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CJsonObject().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "https://maps.googleapis.com/maps/api/geocode/json?address=Frankfurt", "testjsonobject", "loc" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( "testjsonobject", l_return.get( 0 ).<ILiteral>raw().functor() );
        Assert.assertTrue( l_return.get( 0 ).<ILiteral>raw().values().findFirst().isPresent() );
        Assert.assertEquals(
            "loc",
            l_return.get( 0 ).<ILiteral>raw().values().findFirst().map( ITerm::functor ).orElseThrow( IllegalArgumentException::new )
        );
    }


    /**
     * test xml object error
     */
    @Test( expected = CExecutionIllegealArgumentException.class )
    public void xmlobjecterror()
    {
        new CXMLObject().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( "xmlobject", "testxml" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }


    /**
     * test xml object with single argument
     */
    @Test
    public void xmlobjectsingle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CXMLObject().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "https://en.wikipedia.org/wiki/Special:Export/Normalized_compression_distance", "testxml" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( "testxml", l_return.get( 0 ).<ILiteral>raw().functor() );
    }


    /**
     * test xml object with single argument
     */
    @Test
    public void xmlobjectmultiple()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CXMLObject().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "https://en.wikipedia.org/wiki/Special:Export/Normalized_compression_distance", "testxml", "ncd" )
                  .map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ILiteral );
        Assert.assertEquals( "testxml", l_return.get( 0 ).<ILiteral>raw().functor() );
        Assert.assertTrue( l_return.get( 0 ).<ILiteral>raw().values().findFirst().isPresent() );
        Assert.assertEquals(
            "ncd",
            l_return.get( 0 ).<ILiteral>raw().values().findFirst().map( ITerm::functor ).orElseThrow( IllegalArgumentException::new )
        );
    }

}
