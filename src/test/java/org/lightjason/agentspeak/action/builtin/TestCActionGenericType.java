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

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.generic.type.CCreateLiteral;
import org.lightjason.agentspeak.action.builtin.generic.type.CIs;
import org.lightjason.agentspeak.action.builtin.generic.type.CIsNull;
import org.lightjason.agentspeak.action.builtin.generic.type.CIsNumeric;
import org.lightjason.agentspeak.action.builtin.generic.type.CIsString;
import org.lightjason.agentspeak.action.builtin.generic.type.CParseLiteral;
import org.lightjason.agentspeak.action.builtin.generic.type.CParseNumber;
import org.lightjason.agentspeak.action.builtin.generic.type.CToNumber;
import org.lightjason.agentspeak.action.builtin.generic.type.CToString;
import org.lightjason.agentspeak.action.builtin.generic.type.CType;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test generic-type actions
 */
public final class TestCActionGenericType extends IBaseTest
{

    /**
     * test create literal action
     */
    @Test
    public void createliteral()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreateLiteral().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "functor", "stringvalue", 1234, true ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals(
            CLiteral.of(
                    "functor",
                    CRawTerm.of( "stringvalue" ),
                    CRawTerm.of( 1234 ),
                    CRawTerm.of( true )
            ),
            l_return.get( 0 ).<ILiteral>raw()
        );
    }


    /**
     * test parse literal action
     */
    @Test
    public void parseliteral()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CParseLiteral().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "main/parsefunctor( \"hello\", 666, false )" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals(
            CLiteral.of(
                    "main/parsefunctor",
                    CRawTerm.of( "hello" ),
                    CRawTerm.of( 666D ),
                    CRawTerm.of( false )
            ),
            l_return.get( 0 ).<ILiteral>raw()
        );
    }


    /**
     * test parse literal action with error
     */
    @Test( expected = CExecutionIllegealArgumentException.class )
    public void parseliteralerror()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CParseLiteral().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( "Main/parsefunctor( hello, XXXXX, false )" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );
    }


    /**
     * test parse-float action
     */
    @Test
    public void parsefloat()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CParseNumber().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "732.489", "64.091248", "-78129.01" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertEquals( 732.489, l_return.get( 0 ).<Number>raw().doubleValue(), 0 );
        Assert.assertEquals( 64.091248, l_return.get( 1 ).<Number>raw().doubleValue(), 0 );
        Assert.assertEquals( -78129.01, l_return.get( 2 ).<Number>raw().doubleValue(), 0 );
    }

    /**
     * test parse-float action error
     */
    @Test( expected = CExecutionIllegealArgumentException.class )
    public void parsefloaterror()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CParseNumber().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "foo" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );
    }


    /**
     * test type action
     */
    @Test
    public void type()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CType().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( new ArrayList<>(), 123L, "test value", new HashSet<>() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 4, l_return.size() );
        Assert.assertEquals( "java.util.ArrayList", l_return.get( 0 ).<String>raw() );
        Assert.assertEquals( "java.lang.Long", l_return.get( 1 ).<String>raw() );
        Assert.assertEquals( "java.lang.String", l_return.get( 2 ).<String>raw() );
        Assert.assertEquals( "java.util.HashSet", l_return.get( 3 ).<String>raw() );
    }


    /**
     * test "is" action
     */
    @Test
    public void is()
    {
        Assert.assertFalse(
            execute(
                new CIs(),
                false,
                Stream.of( "java.lang.String", "text foo", 123, 88.98 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assert.assertTrue(
            execute(
                new CIs(),
                false,
                Stream.of( "java.lang.Number", 123, 44.5 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );
    }


    /**
     * test "isnull"action
     */
    @Test
    public void isnull()
    {
        Assert.assertFalse(
            execute(
                new CIsNull(),
                false,
                Stream.of( "test type string", null ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assert.assertTrue(
            execute(
                new CIsNull(),
                false,
                Stream.of( CRawTerm.of( null ) ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );
    }


    /**
     * test "isnumeric" action
     */
    @Test
    public void isnumeric()
    {
        Assert.assertFalse(
            execute(
                new CIsNumeric(),
                false,
                Stream.of( "test type string", 123, 77L, 112.123, 44.5f ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assert.assertTrue(
            execute(
                new CIsNumeric(),
                false,
                Stream.of( 123, 77L, 112.123, 44.5f ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );
    }


    /**
     * test "isstring" action
     */
    @Test
    public void isstring()
    {
        Assert.assertFalse(
            execute(
                new CIsString(),
                false,
                Stream.of( "test foobar", 123, "string again", true, new Object(), 77.8, 'a' ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );

        Assert.assertTrue(
            execute(
                new CIsString(),
                false,
                Stream.of( "okay 1", 'c', "ok 2" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            )
        );
    }


    /**
     * test "tostring"
     */
    @Test
    public void tostring()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToString().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "", 123, 5.5, new Object() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 4, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).<String>raw().isEmpty() );
        Assert.assertEquals( "123", l_return.get( 1 ).raw() );
        Assert.assertEquals( "5.5", l_return.get( 2 ).raw() );
        Assert.assertTrue( l_return.get( 3 ).raw() instanceof String );
    }


    /**
     * test "tofloat"
     */
    @Test
    public void tofloat()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CToNumber().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 2, 3.2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Double );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof Double );
        Assert.assertTrue( l_return.get( 2 ).raw() instanceof Double );
    }

}
