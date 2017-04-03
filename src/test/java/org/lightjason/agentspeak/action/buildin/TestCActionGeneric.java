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
import org.lightjason.agentspeak.error.CRuntimeException;
import org.lightjason.agentspeak.language.CRawTerm;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
     * text call
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args ) throws UnsupportedEncodingException
    {
        final TestCActionGeneric l_test = new TestCActionGeneric();

        l_test.testprint();

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
