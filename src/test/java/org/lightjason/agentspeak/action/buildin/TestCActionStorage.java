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
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.buildin.storage.CAdd;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.instantiable.plan.CPlan;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.score.IAggregation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test action storage
 */
public final class TestCActionStorage
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
     * test add action
     */
    @Test
    public final void testadd()
    {
        Assume.assumeNotNull( m_context );

        new CAdd().execute(
            m_context,
            false,
            Stream.of(
                CRawTerm.from( "testnumber" ),
                CRawTerm.from( 123 ),
                CRawTerm.from( "teststring" ),
                CRawTerm.from( "foobar" )
            ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( m_context.agent().storage().get( "testnumber" ), 123 );
        Assert.assertEquals( m_context.agent().storage().get( "teststring" ), "foobar" );
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
        final TestCActionStorage l_test = new TestCActionStorage();

        l_test.initialize();
        l_test.testadd();
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
