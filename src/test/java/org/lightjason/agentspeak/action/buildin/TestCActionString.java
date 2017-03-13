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
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.buildin.string.CBase64Decode;
import org.lightjason.agentspeak.action.buildin.string.CBase64Encode;
import org.lightjason.agentspeak.action.buildin.string.CLower;
import org.lightjason.agentspeak.action.buildin.string.CReverse;
import org.lightjason.agentspeak.action.buildin.string.CSize;
import org.lightjason.agentspeak.action.buildin.string.CUpper;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

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
public final class TestCActionString
{

    /**
     * test base64 en- and decode
     */
    @Test
    public final void base64()
    {
        final List<ITerm> l_arguments = Stream.of( "test", "#!$foo", "1234097" ).map( CRawTerm::from ).collect( Collectors.toList() );
        final List<ITerm> l_return = new ArrayList<>();
        final List<ITerm> l_result = new ArrayList<>();

        new CBase64Encode().execute(
            null,
            false,
            l_arguments,
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
            l_arguments.stream(),
            l_result.stream(),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }


    /**
     * test upper
     */
    @Test
    public final void upper()
    {
        final List<String> l_input = Stream.of( "AbCDef", "foo", "BAR" ).collect( Collectors.toList() );
        final List<ITerm> l_return = new ArrayList<>();

        new CUpper().execute(
            null,
            false,
            l_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        StreamUtils.zip(
            l_input.stream().map( i -> i.toUpperCase( Locale.ROOT ) ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }

    /**
     * test lower
     */
    @Test
    public final void lower()
    {
        final List<String> l_input = Stream.of( "AbCDef", "foo", "BAR" ).collect( Collectors.toList() );
        final List<ITerm> l_return = new ArrayList<>();

        new CLower().execute(
            null,
            false,
            l_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        StreamUtils.zip(
            l_input.stream().map( i -> i.toLowerCase( Locale.ROOT ) ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }

    /**
     * test reverse
     */
    @Test
    public final void reverse()
    {
        final List<String> l_input = Stream.of( "AbCDef", "foo", "BAR" ).collect( Collectors.toList() );
        final List<ITerm> l_return = new ArrayList<>();

        new CReverse().execute(
            null,
            false,
            l_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        StreamUtils.zip(
            l_input.stream().map( i -> new StringBuilder( i ).reverse().toString() ),
            l_return.stream().map( ITerm::<String>raw ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }

    /**
     * test size
     */
    @Test
    public final void size()
    {
        final List<String> l_input = Stream.of( "AbCDef", "foo", "BAR" ).collect( Collectors.toList() );
        final List<ITerm> l_return = new ArrayList<>();

        new CSize().execute(
            null,
            false,
            l_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );


        StreamUtils.zip(
            l_input.stream().mapToLong( String::length ).boxed(),
            l_return.stream().map( ITerm::<Number>raw ).map( Number::longValue ),
            AbstractMap.SimpleImmutableEntry::new
        ).forEach( i -> Assert.assertEquals( i.getKey(), i.getValue() ) );
    }


    /**
     * main test call
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args )
    {
        final TestCActionString l_test = new TestCActionString();

        l_test.base64();
        l_test.upper();
        l_test.lower();
        l_test.reverse();
        l_test.size();
    }

}
