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
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.generic.CPrint;
import org.lightjason.agentspeak.action.generic.CThrow;
import org.lightjason.agentspeak.action.generic.CUuid;
import org.lightjason.agentspeak.error.context.CExecutionException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    @Test( expected = CExecutionException.class )
    public void throwparameter()
    {
        new CThrow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( true, "test message" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }


    /**
     * test throw action
     */
    @Test( expected = CExecutionException.class )
    public void throwwithoutparameter()
    {
        new CThrow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( true ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }


    /**
     * test throw without throwing
     */
    @Test
    public void thrownot()
    {
        new CThrow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( false, "this should not be thrown" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }


    /**
     * test print action
     *
     * @throws Exception is thrown on encoding errors
     */
    @Test
    public void print() throws Exception
    {
        final ByteArrayOutputStream l_output = new ByteArrayOutputStream();

        new CPrint( () -> new PrintStream( l_output, false, StandardCharsets.UTF_8 ), "-" ).execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "foobar", 1234, true ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( "foobar-1234-true\n", l_output.toString( StandardCharsets.UTF_8 ) );
    }

    /**
     * test print action with formatter
     *
     * @throws Exception is thrown on encoding errors
     */
    @Test
    public void printformatter() throws Exception
    {
        final CPrint.IFormatter<?> l_format1 = new CStringFormatter();
        final CPrint.IFormatter<?> l_format2 = new CBooleanFormatter();

        Assert.assertFalse( l_format1.equals( l_format2 ) );


        final ByteArrayOutputStream l_output = new ByteArrayOutputStream();
        final CPrint l_print = new CPrint( () -> new PrintStream( l_output, false, StandardCharsets.UTF_8 ), "-" );

        l_print.formatter().add( l_format1 );
        l_print.formatter().add( l_format2 );

        Assert.assertTrue(
            execute(
                l_print,
                false,
                Stream.of( "foobar", 1234, true ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assert.assertEquals( "FOOBAR-1234-yes\n", l_output.toString( StandardCharsets.UTF_8 ) );
    }

    /**
     * test single uuid
     */
    @Test
    public void uuid()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CUuid().execute(
            false, IContext.EMPTYPLAN,
            Collections.emptyList(),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( String.class, l_return.get( 0 ).raw().getClass() );
        Assert.assertTrue( !l_return.get( 0 ).<String>raw().isEmpty() );
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
