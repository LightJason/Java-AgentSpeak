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

package org.lightjason.agentspeak.action.buildin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.buildin.storage.CAdd;
import org.lightjason.agentspeak.action.buildin.storage.CClear;
import org.lightjason.agentspeak.action.buildin.storage.CExists;
import org.lightjason.agentspeak.action.buildin.storage.CRemove;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.instantiable.plan.CPlan;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.score.IAggregation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
     * execution context
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
    public final void initialize() throws Exception
    {
        m_context = new CContext(
            new CGenerator( new ByteArrayInputStream( "".getBytes( StandardCharsets.UTF_8 ) ), Collections.emptySet(), IAggregation.EMPTY ).generatesingle(),
            new CPlan( CTrigger.from( ITrigger.EType.ADDGOAL, CLiteral.from( "nothing" ) ), Collections.emptyList(), Collections.emptySet() ),
            Collections.emptyList()
        );
    }

    /**
     * test add action without forbidden keys
     */
    @Test
    public final void addwithoutkeys()
    {
        Assume.assumeNotNull( m_context );

        new CAdd().execute(
            m_context,
            false,
            Stream.of(  "testnumber", 123, "teststring", "foobar" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( m_context.agent().storage().get( "testnumber" ), 123 );
        Assert.assertEquals( m_context.agent().storage().get( "teststring" ), "foobar" );
    }

    /**
     * test add action with forbidden keys
     */
    @Test
    public final void addwithkeys()
    {
        new CAdd( "bar" ).execute(
            m_context,
            false,
            Stream.of( "bar", 123 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertNull( m_context.agent().storage().get( "bar" ) );
    }


    /**
     * test remove action without keys
     */
    @Test
    public final void removewithoutkeys()
    {
        Assume.assumeNotNull( m_context );

        m_context.agent().storage().put( "xxx", 123 );

        final List<ITerm> l_return = new ArrayList<>();
        new CRemove().execute(
            m_context,
            false,
            Stream.of( "xxx" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertTrue( m_context.agent().storage().isEmpty() );
        Assert.assertNull( m_context.agent().storage().get( "xxx" ) );
        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Integer>raw(), Integer.valueOf( 123 ) );
    }


    /**
     * test remove action without keys
     */
    @Test
    public final void removewithkeys()
    {
        Assume.assumeNotNull( m_context );

        m_context.agent().storage().put( "foo", 123 );
        m_context.agent().storage().put( "bar", 456 );

        final List<ITerm> l_return = new ArrayList<>();
        new CRemove( "foo" ).execute(
            m_context,
            false,
            Stream.of( "foo", "bar" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( m_context.agent().storage().size(), 1 );
        Assert.assertNotNull( m_context.agent().storage().get( "foo" ) );
        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Integer>raw(), Integer.valueOf( 456 ) );
    }


    /**
     * test clear action without keys
     */
    @Test
    public final void clearwithoutkeys()
    {
        Assume.assumeNotNull( m_context );

        IntStream.range( 0, 100 ).parallel()
                 .mapToObj( i -> RandomStringUtils.random( 25 ) )
                 .forEach( i -> m_context.agent().storage().put( i, RandomStringUtils.random( 5 ) ) );

        Assert.assertEquals( m_context.agent().storage().size(), 100 );

        new CClear().execute(
            m_context,
            false,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertTrue( m_context.agent().storage().isEmpty() );
    }


    /**
     * test clear action with keys
     */
    @Test
    public final void clearwithkeys()
    {
        Assume.assumeNotNull( m_context );

        IntStream.range( 0, 100 ).parallel()
                 .forEach( i -> m_context.agent().storage().put( MessageFormat.format( "value {0}", i ), i ) );

        Assert.assertEquals( m_context.agent().storage().size(), 100 );

        new CClear( "value 1", "value 5", "value 73" ).execute(
            m_context,
            false,
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( m_context.agent().storage().size(), 3 );
        Assert.assertArrayEquals( m_context.agent().storage().keySet().toArray(), Stream.of( "value 73", "value 5", "value 1" ).toArray() );
        Assert.assertArrayEquals( m_context.agent().storage().values().toArray(), Stream.of(  73, 5, 1 ).toArray() );
    }


    /**
     * test exists action without keys
     */
    @Test
    public final void existwithoutkeys()
    {
        Assume.assumeNotNull( m_context );

        final List<ITerm> l_content = IntStream.range( 0, 100 ).parallel()
                                               .mapToObj( i -> RandomStringUtils.random( 25 ) )
                                               .peek( i -> m_context.agent().storage().put( i, RandomStringUtils.random( 5 ) ) )
                                               .map( CRawTerm::from )
                                               .collect( Collectors.toList() );

        final List<ITerm> l_return = new ArrayList<>();
        new CExists().execute(
            m_context,
            false,
            l_content,
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 100 );
        Assert.assertTrue( l_return.stream().allMatch( ITerm::<Boolean>raw ) );
    }


    /**
     * test resolver access
     */
    @Test
    public final void resolver()
    {
        final Set<String> l_keys = Stream.of( "a", "x", "y" ).collect( Collectors.toSet() );

        Assert.assertArrayEquals(
            new CAdd( l_keys::contains ).forbiddenkeys( "x", "z" ).toArray(),
            Stream.of( true, false ).toArray()
        );

        Assert.assertArrayEquals(
            new CRemove( l_keys::contains ).forbiddenkeys( "x", "z" ).toArray(),
            Stream.of( true, false ).toArray()
        );

        Assert.assertArrayEquals(
            new CClear( l_keys::contains ).forbiddenkeys( "x", "z" ).toArray(),
            Stream.of( true, false ).toArray()
        );

        Assert.assertArrayEquals(
            new CExists( l_keys::contains ).forbiddenkeys( "x", "z" ).toArray(),
            Stream.of( true, false ).toArray()
        );
    }


    /**
     * main test call
     *
     * @param p_args command line arguments
     *
     * @throws Exception on any error
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new TestCActionStorage().invoketest();
    }


    /**
     * test agent
     */
    private static final class CAgent extends IBaseAgent<CAgent>
    {
        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CAgent( final IAgentConfiguration<CAgent> p_configuration )
        {
            super( p_configuration );
        }
    }

    /**
     * agent generator
     */
    private static final class CGenerator extends IBaseAgentGenerator<CAgent>
    {
        /**
         * ctor
         *
         * @param p_stream asl stream
         * @param p_actions actions
         * @param p_aggregation aggregation
         * @throws Exception is thrown on any error
         */
        CGenerator( final InputStream p_stream, final Set<IAction> p_actions, final IAggregation p_aggregation ) throws Exception
        {
            super( p_stream, p_actions, p_aggregation );
        }

        @Override
        public final CAgent generatesingle( final Object... p_data )
        {
            return new CAgent( m_configuration );
        }
    }

}
