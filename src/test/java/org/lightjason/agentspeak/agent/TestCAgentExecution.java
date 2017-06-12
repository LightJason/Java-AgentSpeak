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

package org.lightjason.agentspeak.agent;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;


/**
 * test for agent execution ordering
 */
public final class TestCAgentExecution extends IBaseTest
{
    /**
     * asl source
     */
    private static final String ASL = "src/test/resources/agent/execution.asl";
    /**
     * agent reference
     */
    private IAgent<?> m_agent;
    /**
     * running flag (agent can disable execution)
     */
    private AtomicBoolean m_running;
    /**
     * logs for plan execution
     */
    private final Multimap<Long, String> m_log = Multimaps.synchronizedMultimap( HashMultimap.create() );
    /**
     * log results
     */
    private final Multimap<Long, String> m_result = HashMultimap.create();


    static
    {
        // disable logger
        LogManager.getLogManager().reset();
    }


    /**
     * initializing
     */
    @Before
    public void initialize()
    {
        m_running = new AtomicBoolean( true );
        try
            (
                final InputStream l_asl = new FileInputStream( ASL );
            )
        {
            m_agent = new CGenerator( l_asl ).generatesingle();
        }
        catch ( final Exception l_exception )
        {
            l_exception.printStackTrace();
            assertTrue( "asl could not be read: {0}", true );
        }


        // define execution results
        m_result.put( 0L, "main" );
        m_result.put( 1L, "first" );
        m_result.put( 1L, "second" );
        m_result.put( 1L, "twovalues equal type" );
        m_result.put( 1L, "twovalues different type" );
        m_result.put( 1L, "twovalues with literal" );
        m_result.put( 2L, "single" );
    }

    /**
     * execution ordering test
     *
     * @throws Exception is thrown on agent execution error
     */
    @Test
    public final void executionorder() throws Exception
    {
        Assume.assumeNotNull( m_agent );
        Assume.assumeNotNull( m_running );


        while ( m_running.get() )
            m_agent.call();


        // check execution results
        assertTrue(
            MessageFormat.format(  "number of cycles are incorrect, excpected [{0}] contains [{1}]", m_result.asMap().size(), m_log.asMap().size() ),
            LongStream.range( 0, m_result.asMap().size() ).allMatch( m_log::containsKey )
        );

        assertTrue(
            MessageFormat.format( "number of log elements during execution are incorrect, expected {0} result {1}", m_result.asMap(), m_log.asMap() ),
            LongStream.range( 0, m_result.asMap().size() )
                      .allMatch( i -> m_result.get( i ).size() == m_log.asMap().getOrDefault( i, Collections.emptyList() ).size() )
        );

        LongStream.range( 0, m_result.asMap().size() ).forEach( i -> assertTrue(
            MessageFormat.format( "expected result {0} for index {2} is not equal to log {1}", m_result.get( i ), m_log.get( i ), i ),
            m_result.get( i ).stream().allMatch( j -> m_log.get( i ).contains( j ) )
        ) );

    }



    /**
     * main method for manual test
     *
     * @param p_args CLI arguments
     * @throws Exception on any execution error
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new TestCAgentExecution().invoketest();
    }




    /**
     * agent generator
     */
    private final class CGenerator extends IBaseAgentGenerator<CAgent>
    {
        /**
         * ctor
         *
         * @param p_stream asl stream
         * @throws Exception on any error
         */
        CGenerator( final InputStream p_stream ) throws Exception
        {
            super(
                p_stream,
                Stream.concat(
                    CCommon.actionsFromPackage(),
                    Stream.of(
                        new CStop(),
                        new CLog()
                    )
                ).collect( Collectors.toSet() )
            );
        }

        @Override
        public final CAgent generatesingle( final Object... p_data )
        {
            return new CAgent( m_configuration );
        }
    }


    /**
     * agent class
     */
    private static class CAgent extends IBaseAgent<CAgent>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -7467073439000881088L;

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
     * stop action
     */
    private final class CStop extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 5466369414656444520L;

        @Override
        public final IPath name()
        {
            return CPath.from( "stop" );
        }

        @Override
        public final int minimalArgumentNumber()
        {
            return 0;
        }

        @Override
        public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                   @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            m_running.set( false );
            return CFuzzyValue.from( true );
        }
    }

    /**
     * log action
     */
    private final class CLog extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 4536335097194230205L;

        @Override
        public final IPath name()
        {
            return CPath.from( "log" );
        }

        @Override
        public final int minimalArgumentNumber()
        {
            return 1;
        }

        @Override
        public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                   @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            m_log.put( p_context.agent().cycle(), p_argument.get( 0 ).<String>raw()  );
            return CFuzzyValue.from( true );
        }
    }
}
