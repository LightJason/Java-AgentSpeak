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

import com.codepoetics.protonpack.StreamUtils;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.string.CBase64Decode;
import org.lightjason.agentspeak.action.builtin.string.CBase64Encode;
import org.lightjason.agentspeak.action.builtin.string.CConcat;
import org.lightjason.agentspeak.action.builtin.string.CContains;
import org.lightjason.agentspeak.action.builtin.string.CEndsWith;
import org.lightjason.agentspeak.action.builtin.string.CLevenshtein;
import org.lightjason.agentspeak.action.builtin.string.CLower;
import org.lightjason.agentspeak.action.builtin.string.CNCD;
import org.lightjason.agentspeak.action.builtin.string.CRandom;
import org.lightjason.agentspeak.action.builtin.string.CReverse;
import org.lightjason.agentspeak.action.builtin.string.CSize;
import org.lightjason.agentspeak.action.builtin.string.CStartsWith;
import org.lightjason.agentspeak.action.builtin.string.CUpper;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for string actions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionString extends IBaseTest
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

        Assert.assertTrue(
            new CBase64Encode().execute(
                false, IContext.EMPTYPLAN,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertTrue(
            new CBase64Decode().execute(
                false, IContext.EMPTYPLAN,
                l_return,
                l_result
            ).value()
        );

        StreamUtils.zip(
            p_input.stream(),
            l_result.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getValue(), i.getKey() ) );
    }


    /**
     * test base64 decode with errors
     *
     * @throws UnsupportedEncodingException is thrown on test data encoding
     */
    @Test
    public final void base64decodeerror() throws UnsupportedEncodingException
    {
        Assert.assertFalse(
            new CBase64Decode().execute(
                false, IContext.EMPTYPLAN,
                Stream.of( new String( "test encodingwith german additional character: öäß".getBytes( "UTF-16" ), "UTF-16" ) )
                      .map( CRawTerm::of )
                      .collect( Collectors.toList() ),
                Collections.emptyList()
            ).value()
        );
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

        Assert.assertTrue(
            new CConcat().execute(
                false, IContext.EMPTYPLAN,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals(
            p_input.stream().collect( Collectors.joining() ),
            l_return.get( 0 ).<String>raw()
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

        Assert.assertTrue(
            new CContains().execute(
                false, IContext.EMPTYPLAN,
                Stream.concat(
                    Stream.of( p_input.stream().collect( Collectors.joining() ) ),
                    p_input.stream()
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
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

        Assert.assertTrue(
            new CLower().execute(
                false, IContext.EMPTYPLAN,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        StreamUtils.zip(
            p_input.stream().map( i -> i.toLowerCase( Locale.ROOT ) ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getValue(), i.getKey() ) );
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

        Assert.assertTrue(
            new CReverse().execute(
                false, IContext.EMPTYPLAN,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        StreamUtils.zip(
            p_input.stream().map( i -> new StringBuilder( i ).reverse().toString() ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getValue(), i.getKey() ) );
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

        Assert.assertTrue(
            new CSize().execute(
                false, IContext.EMPTYPLAN,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        StreamUtils.zip(
            p_input.stream().mapToLong( String::length ).boxed(),
            l_return.stream().map( ITerm::<Number>raw ).map( Number::longValue ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getValue(), i.getKey() ) );
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

        Assert.assertTrue(
            new CRandom().execute(
                false, IContext.EMPTYPLAN,
                Stream.concat(
                    Stream.of( p_input.stream().collect( Collectors.joining() ) ),
                    p_input.stream().mapToInt( String::length ).boxed()
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        StreamUtils.zip(
            p_input.stream().mapToInt( String::length ).boxed(),
            l_return.stream().map( ITerm::<String>raw ).mapToInt( String::length ).boxed(),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getValue(), i.getKey() ) );
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

        Assert.assertTrue(
            new CUpper().execute(
                false, IContext.EMPTYPLAN,
                p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        StreamUtils.zip(
            p_input.stream().map( i -> i.toUpperCase( Locale.ROOT ) ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getValue(), i.getKey() ) );
    }


    /**
     * test starts-with
     */
    @Test
    public final void startswith()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            new CStartsWith().execute(
                false, IContext.EMPTYPLAN,
                Stream.of( "this is an input text", "this", "th", "is" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertTrue( l_return.get( 1 ).<Boolean>raw() );
        Assert.assertFalse( l_return.get( 2 ).<Boolean>raw() );
    }


    /**
     * test ends-with
     */
    @Test
    public final void endswidth()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            new CEndsWith().execute(
                false, IContext.EMPTYPLAN,
                Stream.of( "this is a new input text with a cool ending", "ing", "this", "g" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertFalse( l_return.get( 1 ).<Boolean>raw() );
        Assert.assertTrue( l_return.get( 2 ).<Boolean>raw() );
    }


    /**
     * tets for levenshtein distance error
     */
    @Test
    public final void levenshteinerror()
    {
        Assert.assertFalse(
            new CLevenshtein().execute(
                false, IContext.EMPTYPLAN,
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }


    /**
     * test levenshtein distance
     */
    @Test
    public final void levenshtein()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            new CLevenshtein().execute(
                false, IContext.EMPTYPLAN,
                Stream.of( "kitten", "sitting", "singing" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( 3, l_return.get( 0 ).<Number>raw().intValue() );
        Assert.assertEquals( 5, l_return.get( 1 ).<Number>raw().intValue() );
    }


    /**
     * test normalized compression distance
     */
    @Test
    public final void ncd()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            new CNCD().execute(
                false, IContext.EMPTYPLAN,
                Stream.of( "test", "tests", "this a complete other string", "test" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertEquals( 0.04878048780487805, l_return.get( 0 ).<Number>raw().doubleValue(), 0.0001 );
        Assert.assertEquals( 0.38333333333333336, l_return.get( 1 ).<Number>raw().doubleValue(), 0.0001 );
        Assert.assertEquals( 0, l_return.get( 2 ).<Number>raw().doubleValue(), 0 );

        Assert.assertTrue(
            new CNCD().execute(
                false, IContext.EMPTYPLAN,
                Stream.of( "GZIP", "test", "tests", "this a complete other string", "test" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            ).value()
        );

        Assert.assertEquals( 6, l_return.size() );
        Assert.assertEquals( 0.12, l_return.get( 3 ).<Number>raw().doubleValue(), 0 );
        Assert.assertEquals( 0.5833333333333334, l_return.get( 4 ).<Number>raw().doubleValue(),  0.0001 );
        Assert.assertEquals( 0, l_return.get( 5 ).<Number>raw().doubleValue(), 0 );
    }


    /**
     * test normalized compression distance error
     */
    @Test
    public final void ncderror()
    {
        Assert.assertFalse(
            new CNCD().execute(
                false, IContext.EMPTYPLAN,
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }

}
