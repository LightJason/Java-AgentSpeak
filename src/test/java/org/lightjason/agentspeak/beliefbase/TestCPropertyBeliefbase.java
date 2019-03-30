/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.beliefbase;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.beliefbase.storage.CClassStorage;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test agent with beliebase properties
 */
public final class TestCPropertyBeliefbase extends IBaseTest
{

    /**
     * test beliefs exists
     *
     * @throws Exception is thrown on intialization error
     */
    @Test
    public void belieflist() throws Exception
    {
        final IAgent<?> l_agent = new CAgent.CAgentGenerator( "" ).generatesingle();

        if ( PRINTENABLE )
            l_agent.beliefbase().stream().forEach( System.out::println );


        Assert.assertArrayEquals(
            Stream.of(
                "self/m_fuzzy",
                "self/m_trigger",
                "self/m_cycletime",
                "self/m_rules",
                "self/m_stringvalue",
                "self/m_storage",
                "self/m_sleepingterm",
                "self/m_runningplans",
                "self/m_sleepingcycles",
                "self/m_beliefbase",
                "self/m_unifier",
                "self/m_variablebuilder",
                "self/m_plans",
                "self/m_integervalue"
            ).toArray(),
            l_agent.beliefbase().stream().map( i -> i.fqnfunctor().path() ).toArray()
        );

        final Set<ILiteral> l_beliefs = l_agent.beliefbase().stream().collect( Collectors.toSet() );
        Assert.assertTrue(
            "beliefs not found",
            Stream.of(
                CLiteral.of( "self/m_stringvalue" ),
                CLiteral.of( "self/m_integervalue", CRawTerm.of( 42 ) )
            ).allMatch( l_beliefs::contains )
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<IAgent<?>>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 3053608318122134408L;
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
        private CAgent( final IAgentConfiguration<IAgent<?>> p_configuration )
        {
            super( p_configuration );
            m_beliefbase.add( new CBeliefbase( new CClassStorage<>( this ) ).create( "self", m_beliefbase ) );
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
                super( IOUtils.toInputStream( p_asl, "UTF-8" ), IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY );
            }

            @Override
            public IAgent<?> generatesingle( final Object... p_data )
            {
                return new CAgent( m_configuration );
            }
        }

    }

}
