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

package org.lightjason.agentspeak.agent;

import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.score.IAggregation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;


/**
 * test for agent execution ordering
 */
public final class TestCAgentExecution
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
    private final List<Object> m_log = Collections.synchronizedList( new LinkedList<>() );


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
            assertTrue( "asl could not be read", true );
        }
    }

    /**
     * execution ordering test
     *
     * @throws Exception is thrown on agent execution error
     */
    @Test
    public final void executionorder() throws Exception
    {
        while ( m_running.get() )
            m_agent.call();
    }



    /**
     * main method for manual test
     *
     * @param p_args CLI arguments
     * @throws Exception on any execution error
     */
    public static void main( final String[] p_args ) throws Exception
    {
        final TestCAgentExecution l_test = new TestCAgentExecution();
        l_test.initialize();
        l_test.executionorder();
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
                ).collect( Collectors.toSet() ),
                IAggregation.EMPTY
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
        public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                                   final List<ITerm> p_annotation
        )
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
        public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                                   final List<ITerm> p_annotation
        )
        {
            m_log.add( p_argument.get( 0 ).<Object>raw() );
            return CFuzzyValue.from( true );
        }
    }
}
