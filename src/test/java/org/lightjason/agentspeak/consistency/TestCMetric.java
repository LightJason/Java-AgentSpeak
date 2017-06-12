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

package org.lightjason.agentspeak.consistency;

import org.apache.commons.io.IOUtils;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.beliefbase.CBeliefbasePersistent;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.beliefbase.view.IViewGenerator;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.consistency.filter.CAll;
import org.lightjason.agentspeak.consistency.filter.IFilter;
import org.lightjason.agentspeak.consistency.metric.CSymmetricDifference;
import org.lightjason.agentspeak.consistency.metric.CWeightedDifference;
import org.lightjason.agentspeak.consistency.metric.IMetric;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


/**
 * metric tests
 */
public final class TestCMetric extends IBaseTest
{
    /**
     * agent generator
     */
    private CAgent.CAgentGenerator m_agentgenerator;
    /**
     * literal view generator
     */
    private IViewGenerator<CAgent> m_generator;
    /**
     * set with testing literals
     */
    private Set<ILiteral> m_literals;

    /**
     * test initialize
     *
     * @throws Exception on any parsing error
     */
    @Before
    public void initialize() throws Exception
    {
        m_generator = new CGenerator();
        m_agentgenerator = new CAgent.CAgentGenerator( IOUtils.toInputStream( "", "UTF-8" ), Collections.emptySet() );

        m_literals = Stream.of(
            CLiteral.from( "toplevel" ),
            CLiteral.from( "first/sub1" ),
            CLiteral.from( "first/sub2" ),
            CLiteral.from( "second/sub3" ),
            CLiteral.from( "second/sub4" ),
            CLiteral.from( "second/sub/sub5" )
        ).collect( Collectors.toSet() );
    }



    /**
     * test symmetric weight metric equality
     */
    @Test
    public final void symmetricweightequality()
    {
        Assume.assumeNotNull( m_literals );
        Assume.assumeFalse( "testing literals are empty", m_literals.isEmpty() );
        this.check(
            "symmetric difference equality",
            new CAll(), new CSymmetricDifference(),
            m_literals,
            m_literals,
            0, 0
        );
    }


    /**
     * test symmetric weight metric inequality
     */
    @Test
    public final void symmetricweightinequality()
    {
        Assume.assumeNotNull( m_literals );
        Assume.assumeFalse( "testing literals are empty", m_literals.isEmpty() );
        this.check(
            "symmetric difference inequality",
            new CAll(), new CSymmetricDifference(),
            m_literals,
            Stream.concat( m_literals.stream(), Stream.of( CLiteral.from( "diff" ) ) ).collect( Collectors.toSet() ),
            1, 0
        );
    }


    /**
     * test symmetric metric equality
     */
    @Test
    public final void weightequality()
    {
        Assume.assumeNotNull( m_literals );
        Assume.assumeFalse( "testing literals are empty", m_literals.isEmpty() );

        this.check(
            "weight difference equality",
            new CAll(), new CWeightedDifference(),
            m_literals,
            m_literals,
            24, 0
        );
    }


    /**
     * test symmetric metric equality
     */
    @Test
    public final void weightinequality()
    {
        Assume.assumeNotNull( m_literals );
        Assume.assumeFalse( "testing literals are empty", m_literals.isEmpty() );

        this.check(
            "weight difference inequality",
            new CAll(),
            new CWeightedDifference(),
            m_literals,
            Stream.concat( m_literals.stream(), Stream.of( CLiteral.from( "diff" ) ) ).collect( Collectors.toSet() ),
            28 + 1.0 / 6, 0
        );
    }


    /**
     * runs the check
     *
     * @param p_message error / successful message
     * @param p_filter agent filter
     * @param p_metric metric value
     * @param p_belief1 belief set 1
     * @param p_belief2 belief set 2
     * @param p_excepted expected value
     * @param p_delta delta
     */
    private void check( final String p_message, final IFilter p_filter, final IMetric p_metric, final Collection<ILiteral> p_belief1,
                        final Collection<ILiteral> p_belief2, final double p_excepted, final double p_delta )
    {
        final double l_value = p_metric.apply(
            p_filter.apply( this.agent( p_belief1 ) ),
            p_filter.apply( this.agent( p_belief2 ) )
        );
        assertEquals( p_message, p_excepted, l_value, p_delta );
    }


    /**
     * generates an agent
     *
     * @param p_literals literal collection
     * @return agent
     */
    private CAgent agent( final Collection<ILiteral> p_literals )
    {
        Assume.assumeNotNull( m_generator );

        final CAgent l_agent = m_agentgenerator.generatesingle();
        p_literals.forEach( i -> l_agent.beliefbase().generate( m_generator, i.functorpath() ).add( i ) );
        return l_agent;
    }


    /**
     * manuell running test
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCMetric().invoketest();
    }


    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<CAgent>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 4390503811927101766L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        private CAgent( final IAgentConfiguration<CAgent> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * agent generator class
         */
        private static final class CAgentGenerator extends IBaseAgentGenerator<CAgent>
        {
            /**
             * ctor
             *
             * @throws Exception on any error
             */
            CAgentGenerator( @Nonnull final InputStream p_stream, @Nonnull final Set<IAction> p_actions ) throws Exception
            {
                super( p_stream, p_actions );
            }

            @Override
            public CAgent generatesingle( final Object... p_data )
            {
                return new CAgent( m_configuration );
            }
        }

    }


    /**
     * test belief generator
     */
    private static final class CGenerator implements IViewGenerator<CAgent>
    {
        @Override
        public final IView<CAgent> apply( final String p_name, final IView<CAgent> p_parent )
        {
            return new CBeliefbasePersistent<CAgent>( new CMultiStorage<>() ).create( p_name, p_parent );
        }
    }

}
