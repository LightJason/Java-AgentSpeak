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

package org.lightjason.agentspeak.consistency;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.beliefbase.CBeliefbasePersistent;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.beliefbase.view.IViewGenerator;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.configuration.CDefaultAgentConfiguration;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.consistency.filter.CAll;
import org.lightjason.agentspeak.consistency.filter.IFilter;
import org.lightjason.agentspeak.consistency.metric.CSymmetricDifference;
import org.lightjason.agentspeak.consistency.metric.CWeightedDifference;
import org.lightjason.agentspeak.consistency.metric.IMetric;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


/**
 * metric tests
 */
public final class TestCMetric
{
    /**
     * literal view generator
     */
    private IViewGenerator<IAgent<?>> m_generator;
    /**
     * set with testing literals
     */
    private Set<ILiteral> m_literals;

    /**
     * test initialize
     */
    @Before
    public void initialize()
    {
        m_generator = new CGenerator();

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
    public final void testSymmetricWeightEquality()
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
    public final void testSymmetricWeightInequality()
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
    public final void testWeightEquality()
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
    public final void testWeightInequality()
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
     * manuell running test
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args )
    {
        final TestCMetric l_test = new TestCMetric();

        l_test.initialize();

        l_test.testSymmetricWeightEquality();
        l_test.testSymmetricWeightInequality();
        l_test.testWeightEquality();
        l_test.testWeightInequality();
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
                        final Collection<ILiteral> p_belief2,
                        final double p_excepted, final double p_delta
    )
    {
        final double l_value = p_metric.calculate(
            p_filter.filter( this.getAgent( p_belief1 ) ).collect( Collectors.toList() ),
            p_filter.filter( this.getAgent( p_belief2 ) ).collect( Collectors.toList() )
        );
        assertEquals( p_message, p_excepted, l_value, p_delta );
        System.out.println( MessageFormat.format( "{0} value: {1}", p_message, l_value ) );
    }


    /**
     * generates an agent
     *
     * @param p_literals literal collection
     * @return agent
     */
    private IAgent<IAgent<?>> getAgent( final Collection<ILiteral> p_literals )
    {
        final IAgent<IAgent<?>> l_agent = new CAgent( new CDefaultAgentConfiguration<>() );
        p_literals.forEach( i -> l_agent.beliefbase().generate( m_generator, i.functorpath() ).add( i ) );
        return l_agent;
    }

    /**
     * agetn class
     */
    private static class CAgent extends IBaseAgent<IAgent<?>>
    {
        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CAgent( final IAgentConfiguration<IAgent<?>> p_configuration )
        {
            super( p_configuration );
        }
    }

    /**
     * test belief generator
     */
    private static final class CGenerator implements IViewGenerator<IAgent<?>>
    {
        @Override
        public final IView<IAgent<?>> generate( final String p_name, final IView<IAgent<?>> p_parent )
        {
            return new CBeliefbasePersistent<>( new CMultiStorage<>() ).create( p_name, p_parent );
        }
    }

}
