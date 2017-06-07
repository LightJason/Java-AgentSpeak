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

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.beliefbase.CBeliefbasePersistent;
import org.lightjason.agentspeak.beliefbase.storage.CClassStorage;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.instantiable.plan.IInjection;

import java.util.Collections;
import java.util.stream.Stream;


/**
 * test agent with beliebase properties
 */
public final class TestCPropertyAgent extends IBaseTest
{

    /**
     * test beliefs exists
     *
     * @throws Exception is thrown on intialization error
     */
    @Test
    public final void belieflist() throws Exception
    {
        final IAgent<?> l_agent = new CAgent.CAgentGenerator( "" ).generatesingle();


        Assert.assertArrayEquals(
            l_agent.beliefbase().stream().toArray(),
            Stream.of(
                CLiteral.from( "self/m_stringvalue", CRawTerm.EMPTY ),
                CLiteral.from( "self/m_integervalue", CRawTerm.from( 42 ) )
            ).toArray()
        );
    }


    /**
     * manuell running test
     *
     * @param p_args arguments
     * @throws Exception on parsing exception
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new TestCPropertyAgent().invoketest();
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<IAgent<?>>
    {
        /**
         * test string property
         */
        private String m_stringvalue;
        /**
         * test int property
         */
        private final int m_integervalue = 42;
        /**
         * test string property
         */
        private final transient String m_stringvaluenotlisten = "not shown";


        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        @SuppressWarnings( "unchecked" )
        private CAgent( final IAgentConfiguration<IAgent<?>> p_configuration )
        {
            super( p_configuration );
            m_beliefbase.add( new CBeliefbasePersistent<>( new CClassStorage<>( this ) ).create( "self", m_beliefbase ) );
        }

        /**
         * agent generator class
         */
        private static final class CAgentGenerator extends IBaseAgentGenerator<IAgent<?>>
        {

            /**
             * ctor
             *
             * @param p_asl asl string code
             * @throws Exception thrown on error
             */
            CAgentGenerator( final String p_asl ) throws Exception
            {
                super( IOUtils.toInputStream( p_asl, "UTF-8" ), Collections.emptySet(), IInjection.EMPTY );
            }

            @Override
            public IAgent<?> generatesingle( final Object... p_data )
            {
                return new CAgent( m_configuration );
            }
        }

    }

}
