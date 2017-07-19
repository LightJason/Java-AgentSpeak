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

package org.lightjason.agentspeak.action.builtin;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.generic.CPrint;
import org.lightjason.agentspeak.action.builtin.generic.CThrow;
import org.lightjason.agentspeak.error.CRuntimeException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test generic actions
 */
public final class TestCActionGeneric extends IBaseTest
{

    /**
     * test throw action
     */
    @Test( expected = CRuntimeException.class )
    public final void throwparameter()
    {
        new CThrow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( true, "test message" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }


    /**
     * test throw action
     */
    @Test( expected = CRuntimeException.class )
    public final void throwwithoutparameter()
    {
        new CThrow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( true ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }


    /**
     * test throw without throwing
     */
    @Test
    public final void thrownot()
    {
        new CThrow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( false, "this should not be thrown" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }


    /**
     * test print action
     *
     * @throws Exception is thrown on encoding errors
     */
    @Test
    public final void print() throws Exception
    {
        final ByteArrayOutputStream l_output = new ByteArrayOutputStream();

        new CPrint( "-", () -> new PrintStream( l_output, false, "utf-8" ) ).execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "foobar", 1234, true ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( l_output.toString( "utf-8" ), "foobar-1234-true\n" );
    }

    /**
     * test print action with formatter
     *
     * @throws Exception is thrown on encoding errors
     */
    @Test
    public final void printformatter() throws Exception
    {
        final CPrint.IFormatter<?> l_format1 = new CStringFormatter();
        final CPrint.IFormatter<?> l_format2 = new CBooleanFormatter();

        Assert.assertFalse( l_format1.equals( l_format2 ) );


        final ByteArrayOutputStream l_output = new ByteArrayOutputStream();
        final CPrint l_print = new CPrint( "-", () -> new PrintStream( l_output, false, "utf-8" ) );

        l_print.formatter().add( l_format1 );
        l_print.formatter().add( l_format2 );

        l_print.execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "foobar", 1234, true ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( l_output.toString( "utf-8" ), "FOOBAR-1234-yes\n" );
    }

    /**
     * text call
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionGeneric().invoketest();
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * test formatter for strings (translate each string to an upper-case string)
     */
    private static final class CStringFormatter extends CPrint.IFormatter<String>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -8753365775971744734L;

        @Override
        protected final Class<?> getType()
        {
            return String.class;
        }

        @Override
        protected final String format( final String p_data )
        {
            return p_data.toUpperCase();
        }
    }

    /**
     * test formatter for boolean (translate each boolean to an yes/no string)
     */
    private static final class CBooleanFormatter extends CPrint.IFormatter<Boolean>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -1447139962315061035L;

        @Override
        protected final Class<?> getType()
        {
            return Boolean.class;
        }

        @Override
        protected final String format( final Boolean p_data )
        {
            return p_data ? "yes" : "no";
        }
    }

}
