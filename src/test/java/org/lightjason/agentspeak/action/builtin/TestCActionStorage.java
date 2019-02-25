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

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.storage.CAdd;
import org.lightjason.agentspeak.action.builtin.storage.CClear;
import org.lightjason.agentspeak.action.builtin.storage.CExists;
import org.lightjason.agentspeak.action.builtin.storage.CRemove;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test action storage
 */
public final class TestCActionStorage extends IBaseTest
{
    /**
     * execute context
     */
    private IContext m_context;

    static
    {
        LogManager.getLogManager().reset();
    }

    /**
     * initialize
     *
     * @throws Exception is thrown on any error
     */
    @Before
    public void initialize() throws Exception
    {
        m_context = new CContext(
            new CAgentGenerator().generatesingle(),
            IPlan.EMPTY,
            Collections.emptyList()
        );
    }

    /**
     * test add action without forbidden keys
     */
    @Test
    public void addwithoutkeys()
    {
        Assume.assumeNotNull( m_context );

        new CAdd().execute(
            false, m_context,
            Stream.of(  "testnumber", 123, "teststring", "foobar" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( 2, m_context.agent().storage().size() );
        Assert.assertEquals( 123, m_context.agent().storage().get( "testnumber" ) );
        Assert.assertEquals( "foobar", m_context.agent().storage().get( "teststring" ) );
    }

    /**
     * test add action with forbidden keys
     */
    @Test
    public void addwithkeys()
    {
        new CAdd( "bar" ).execute(
            false, m_context,
            Stream.of( "bar", 123 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( 0, m_context.agent().storage().size() );
        Assert.assertNull( m_context.agent().storage().get( "bar" ) );
    }


    /**
     * test add action with forbidden key strean
     */
    @Test
    public void addwithkeystrean()
    {
        new CAdd( Stream.of( "abc" ) ).execute(
            false, m_context,
            Stream.of( "abc", 123 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( 0, m_context.agent().storage().size() );
        Assert.assertNull( m_context.agent().storage().get( "abc" ) );
    }


    /**
     * test remove action without keys
     */
    @Test
    public void removewithoutkeys()
    {
        Assume.assumeNotNull( m_context );

        m_context.agent().storage().put( "xxx", 123 );

        final List<ITerm> l_return = new ArrayList<>();
        new CRemove().execute(
            false, m_context,
            Stream.of( "xxx" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertTrue( m_context.agent().storage().isEmpty() );
        Assert.assertNull( m_context.agent().storage().get( "xxx" ) );
        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( Integer.valueOf( 123 ), l_return.get( 0 ).<Integer>raw() );
    }


    /**
     * test clear action without keys
     */
    @Test
    public void clearwithoutkeys()
    {
        Assume.assumeNotNull( m_context );

        IntStream.range( 0, 100 )
                 .mapToObj( i -> RandomStringUtils.random( 25 ) )
                 .forEach( i -> m_context.agent().storage().put( i, RandomStringUtils.random( 5 ) ) );

        Assert.assertEquals( 100, m_context.agent().storage().size() );

        new CClear().execute(
            false, m_context,
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertTrue( m_context.agent().storage().isEmpty() );
    }


    /**
     * test remove action without keys
     */
    @Test
    public void removewithkeys()
    {
        Assume.assumeNotNull( m_context );

        m_context.agent().storage().put( "foo", 123 );
        m_context.agent().storage().put( "bar", 456 );

        final List<ITerm> l_return = new ArrayList<>();
        new CRemove( "foo" ).execute(
            false, m_context,
            Stream.of( "foo", "bar" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, m_context.agent().storage().size() );
        Assert.assertNotNull( m_context.agent().storage().get( "foo" ) );
        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( Integer.valueOf( 456 ), l_return.get( 0 ).<Integer>raw() );
    }


    /**
     * test remove action without key stream
     */
    @Test
    public void removewithkeystream()
    {
        Assume.assumeNotNull( m_context );

        m_context.agent().storage().put( "xx", 189 );
        m_context.agent().storage().put( "yy", 267 );

        final List<ITerm> l_return = new ArrayList<>();
        new CRemove( Stream.of( "xx" ) ).execute(
            false, m_context,
            Stream.of( "xx", "yy" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, m_context.agent().storage().size() );
        Assert.assertNotNull( m_context.agent().storage().get( "xx" ) );
        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( Integer.valueOf( 267 ), l_return.get( 0 ).<Integer>raw() );
    }


    /**
     * test clear action with keys
     */
    @Test
    public void clearwithkeys()
    {
        Assume.assumeNotNull( m_context );

        IntStream.range( 0, 100 )
                 .forEach( i -> m_context.agent().storage().put( MessageFormat.format( "value {0}", i ), i ) );

        Assert.assertEquals( m_context.agent().storage().size(), 100 );

        new CClear( "value 1", "value 5", "value 73" ).execute(
            false, m_context,
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( 3, m_context.agent().storage().size() );
        Assert.assertArrayEquals( Stream.of( "value 73", "value 5", "value 1" ).toArray(), m_context.agent().storage().keySet().toArray() );
        Assert.assertArrayEquals( Stream.of(  73, 5, 1 ).toArray(), m_context.agent().storage().values().toArray() );
    }


    /**
     * test clear action with key stream
     */
    @Test
    public void clearwithkeystream()
    {
        Assume.assumeNotNull( m_context );

        IntStream.range( 0, 100 )
                 .forEach( i -> m_context.agent().storage().put( MessageFormat.format( "value {0}", i ), i ) );

        Assert.assertEquals( 100, m_context.agent().storage().size() );

        new CClear( Stream.of( "value 7", "value 23", "value 91" ) ).execute(
            false, m_context,
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( 3, m_context.agent().storage().size() );
        Assert.assertArrayEquals( Stream.of( "value 7", "value 23", "value 91" ).toArray(), m_context.agent().storage().keySet().toArray() );
        Assert.assertArrayEquals( Stream.of(  7, 23, 91 ).toArray(), m_context.agent().storage().values().toArray() );
    }


    /**
     * test exists action without keys
     */
    @Test
    public void existswithoutkeys()
    {
        Assume.assumeNotNull( m_context );

        final List<ITerm> l_content = IntStream.range( 0, 100 )
                                               .mapToObj( i -> RandomStringUtils.random( 25 ) )
                                               .peek( i -> m_context.agent().storage().put( i, RandomStringUtils.random( 5 ) ) )
                                               .map( CRawTerm::of )
                                               .collect( Collectors.toList() );

        final List<ITerm> l_return = new ArrayList<>();
        new CExists().execute(
            false, m_context,
            l_content,
            l_return
        );

        Assert.assertEquals( 100, l_return.size() );
        Assert.assertTrue( l_return.stream().allMatch( ITerm::raw ) );
    }


    /**
     * test exists action with keys
     */
    @Test
    public void existswithkeys()
    {
        Assume.assumeNotNull( m_context );

        m_context.agent().storage().put( "value 9", 5 );
        m_context.agent().storage().put( "value 7", 5 );

        final List<ITerm> l_return = new ArrayList<>();
        new CExists( "value 9", "value 77", "57" ).execute(
            false, m_context,
            Stream.of( "value 9", "value 7", "value 23", "value 77", "57", "123" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( false, true, false, false, false, false ).toArray(),
            l_return.stream().map( ITerm::raw ).toArray()
        );
    }


    /**
     * test exists action with key stream
     */
    @Test
    public void existswithkeystream()
    {
        Assume.assumeNotNull( m_context );

        m_context.agent().storage().put( "value 33", 5 );
        m_context.agent().storage().put( "value 177", 5 );
        m_context.agent().storage().put( "value 23", 19 );

        final List<ITerm> l_return = new ArrayList<>();
        new CExists( Stream.of( "value 33", "value 88", "23" ) ).execute(
            false, m_context,
            Stream.of( "value 33", "value 177", "value 23", "value 137" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            Stream.of( false, true, true, false ).toArray(),
            l_return.stream().map( ITerm::raw ).toArray()
        );
    }



    /**
     * test for checking minimal arguments
     */
    @Test
    public void arguments()
    {
        Assert.assertArrayEquals(

            Stream.of( 1, 0, 1, 1 ).toArray(),

            Stream.of(
                new CAdd().minimalArgumentNumber(),
                new CClear().minimalArgumentNumber(),
                new CExists().minimalArgumentNumber(),
                new CRemove().minimalArgumentNumber()
            ).toArray()

        );
    }



    /**
     * test resolver access
     */
    @Test
    public void resolver()
    {
        final Set<String> l_keys = Stream.of( "a", "x", "y" ).collect( Collectors.toSet() );

        Assert.assertArrayEquals(
            Stream.of( true, false ).toArray(),
            new CAdd( l_keys::contains ).forbiddenkeys( "x", "z" ).toArray()
        );

        Assert.assertArrayEquals(
            Stream.of( true, false ).toArray(),
            new CRemove( l_keys::contains ).forbiddenkeys( "x", "z" ).toArray()
        );

        Assert.assertArrayEquals(
            Stream.of( true, false ).toArray(),
            new CClear( l_keys::contains ).forbiddenkeys( "x", "z" ).toArray()
        );

        Assert.assertArrayEquals(
            Stream.of( true, false ).toArray(),
            new CExists( l_keys::contains ).forbiddenkeys( "x", "z" ).toArray()
        );
    }

}
