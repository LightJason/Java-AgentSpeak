/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

import com.codepoetics.protonpack.StreamUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.action.buildin.string.CBase64Decode;
import org.lightjason.agentspeak.action.buildin.string.CBase64Encode;
import org.lightjason.agentspeak.action.buildin.string.CConcat;
import org.lightjason.agentspeak.action.buildin.string.CContains;
import org.lightjason.agentspeak.action.buildin.string.CEndsWith;
import org.lightjason.agentspeak.action.buildin.string.CLower;
import org.lightjason.agentspeak.action.buildin.string.CRandom;
import org.lightjason.agentspeak.action.buildin.string.CReverse;
import org.lightjason.agentspeak.action.buildin.string.CSize;
import org.lightjason.agentspeak.action.buildin.string.CUpper;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for string actions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionString
{

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.of(
                Stream.of( "fooo", "#!$foo", "1234o097", "AboCDef", "foo", "BARo" ).collect( Collectors.toList() )
        ).toArray();
    }

    /**
     * test base64 en- and decode
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void base64( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();
        final List<ITerm> l_result = new ArrayList<>();

        new CBase64Encode().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        new CBase64Decode().execute(
            null,
            false,
                      l_return,
                      l_result,
                      Collections.emptyList()
        );

        StreamUtils.zip(
            p_input.stream(),
            l_result.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }


    /**
     * test concat
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void concat( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CConcat().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals(
            l_return.get( 0 ).<String>raw(),
            p_input.stream().collect( Collectors.joining() )
        );
    }


    /**
     * test concat
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void contains( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CContains().execute(
            null,
            false,
            Stream.concat(
                Stream.of( p_input.stream().collect( Collectors.joining() ) ),
                p_input.stream()
            ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertTrue(
            l_return.stream()
                    .allMatch( ITerm::<Boolean>raw )
        );
    }


    /**
     * test lower
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void lower( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CLower().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        StreamUtils.zip(
            p_input.stream().map( i -> i.toLowerCase( Locale.ROOT ) ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }


    /**
     * test reverse
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void reverse( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CReverse().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        StreamUtils.zip(
            p_input.stream().map( i -> new StringBuilder( i ).reverse().toString() ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }


    /**
     * test size
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void size( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSize().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        StreamUtils.zip(
            p_input.stream().mapToLong( String::length ).boxed(),
            l_return.stream().map( ITerm::<Number>raw ).map( Number::longValue ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }


    /**
     * test random
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void random( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CRandom().execute(
            null,
            false,
            Stream.concat(
                Stream.of( p_input.stream().collect( Collectors.joining() ) ),
                p_input.stream().mapToInt( String::length ).boxed()
            ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        StreamUtils.zip(
            p_input.stream().mapToInt( String::length ).boxed(),
            l_return.stream().map( ITerm::<String>raw ).mapToInt( String::length ).boxed(),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }


    /**
     * test upper
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void upper( final List<String> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CUpper().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        StreamUtils.zip(
            p_input.stream().map( i -> i.toUpperCase( Locale.ROOT ) ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }


    /**
     * main test call
     *
     * @param p_args arguments
     */
    @SuppressWarnings( "unchecked" )
    public static void main( final String[] p_args )
    {
        final TestCActionString l_test = new TestCActionString();

        Arrays.stream( TestCActionString.generate() )
              .map( i -> (List<String>) i )
              .forEach( i -> {
                  l_test.base64( i );
                  l_test.concat( i );
                  l_test.contains( i );
                  l_test.lower( i );
                  l_test.size( i );
                  l_test.reverse( i );
                  l_test.random( i );
                  l_test.upper( i );
              } );
    }

}
