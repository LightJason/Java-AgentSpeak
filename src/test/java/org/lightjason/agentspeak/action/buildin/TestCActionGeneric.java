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

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.buildin.generic.CPrint;
import org.lightjason.agentspeak.action.buildin.generic.CThrow;
import org.lightjason.agentspeak.action.buildin.generic.type.CCreateLiteral;
import org.lightjason.agentspeak.action.buildin.generic.type.CParseFloat;
import org.lightjason.agentspeak.action.buildin.generic.type.CParseInt;
import org.lightjason.agentspeak.action.buildin.generic.type.CParseLiteral;
import org.lightjason.agentspeak.action.buildin.generic.type.CType;
import org.lightjason.agentspeak.error.CRuntimeException;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test generic actions
 */
public final class TestCActionGeneric
{

    /**
     * test throw action
     */
    @Test( expected = CRuntimeException.class )
    public final void testthrowparameter()
    {
        new CThrow().execute(
            null,
            false,
            Stream.of( true, "test message" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );
    }


    /**
     * test throw action
     */
    @Test( expected = CRuntimeException.class )
    public final void testthrow()
    {
        new CThrow().execute(
            null,
            false,
            Stream.of( true ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );
    }


    /**
     * test print action
     *
     * @throws UnsupportedEncodingException is thrown on encoding errors
     */
    @Test
    public final void testprint() throws UnsupportedEncodingException
    {
        final ByteArrayOutputStream l_output = new ByteArrayOutputStream();

        new CPrint( "-", new PrintStream( l_output, false, "utf-8" ) ).execute(
            null,
            false,
            Stream.of( "foobar", 1234, true ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_output.toString( "utf-8" ), "foobar-1234-true\n" );
    }


    /**
     * test create literal action
     */
    @Test
    public final void testcreateliteral()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreateLiteral().execute(
            null,
            false,
            Stream.of( "functor", "stringvalue", 1234, true ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals(
            l_return.get( 0 ).<ILiteral>raw(),
            CLiteral.from(
                "functor",
                CRawTerm.from( "stringvalue" ),
                CRawTerm.from( 1234 ),
                CRawTerm.from( true )
            )
        );
    }


    /**
     * test parse literal action
     */
    @Test
    public final void testparseliteral()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CParseLiteral().execute(
            null,
            false,
            Stream.of( "main/parsefunctor( \"hello\", 666, false )" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals(
            l_return.get( 0 ).<ILiteral>raw(),
            CLiteral.from(
                "main/parsefunctor",
                CRawTerm.from( "hello" ),
                CRawTerm.from( 666 ),
                CRawTerm.from( false )
            )
        );
    }


    /**
     * test parse-int action
     */
    @Test
    public final void testparseint()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CParseInt().execute(
            null,
            false,
            Stream.of( "666", "123", "-123", "xxx" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw().longValue(), 666 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw().longValue(), 123 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw().longValue(), -123 );
        Assert.assertNull( l_return.get( 3 ).raw() );
    }


    /**
     * test parse-float action
     */
    @Test
    public final void testparsefloat()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CParseFloat().execute(
            null,
            false,
            Stream.of( "732.489", "64.091248", "-78129.01", "foo" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw().doubleValue(), 732.489, 0 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw().doubleValue(), 64.091248, 0 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw().doubleValue(), -78129.01, 0 );
        Assert.assertNull( l_return.get( 3 ).raw() );
    }


    /**
     * test type action
     */
    @Test
    public final void testtype()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CType().execute(
            null,
            false,
            Stream.of( new ArrayList<>(), 123L, "test value", new HashSet<>() ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertEquals( l_return.get( 0 ).<String>raw(), "java.util.ArrayList" );
        Assert.assertEquals( l_return.get( 1 ).<String>raw(), "java.lang.Long" );
        Assert.assertEquals( l_return.get( 2 ).<String>raw(), "java.lang.String" );
        Assert.assertEquals( l_return.get( 3 ).<String>raw(), "java.util.HashSet" );
    }



    /**
     * text call
     *
     * @param p_args arguments
     * @throws UnsupportedEncodingException is thrown on encoding errors
     */
    public static void main( final String[] p_args ) throws UnsupportedEncodingException
    {
        final TestCActionGeneric l_test = new TestCActionGeneric();

        l_test.testprint();
        l_test.testcreateliteral();
        l_test.testparseliteral();
        l_test.testparseint();
        l_test.testparsefloat();
        l_test.testtype();

        try
        {
            l_test.testthrowparameter();
        }
        catch ( final CRuntimeException l_exception )
        {

        }

        try
        {
            l_test.testthrow();
        }
        catch ( final CRuntimeException l_exception )
        {

        }
    }
}
