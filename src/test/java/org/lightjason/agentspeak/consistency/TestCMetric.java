/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.beliefbase.CBeliefBase;
import org.lightjason.agentspeak.beliefbase.IView;
import org.lightjason.agentspeak.beliefbase.IViewGenerator;
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
    private final IViewGenerator<IAgent<?>> m_generator = new CGenerator();


    /**
     * test symmetric weight metric
     */
    @Test
    public final void testSymmetricWeight()
    {
        final IFilter l_filter = new CAll();
        final IMetric l_metric = new CSymmetricDifference();

        final Set<ILiteral> l_beliefs = Stream.of(
            CLiteral.from( "toplevel" ),
            CLiteral.from( "first/sub1" ),
            CLiteral.from( "first/sub2" ),
            CLiteral.from( "second/sub1" ),
            CLiteral.from( "second/sub2" ),
            CLiteral.from( "second/sub/sub1" )
        ).collect( Collectors.toSet() );

        this.check( "symmetric difference equality", l_filter, l_metric, l_beliefs, l_beliefs, 0, 0 );
        this.check(
            "symmetric difference inequality",
            l_filter,
            l_metric,
            l_beliefs,
            Stream.concat( l_beliefs.stream(), Stream.of( CLiteral.from( "diff" ) ) ).collect( Collectors.toSet() ),
            1,
            0
        );
    }


    /**
     * test symmetric metric
     */
    @Test
    public final void testWeight()
    {
        final IFilter l_filter = new CAll();
        final IMetric l_metric = new CWeightedDifference();

        final Set<ILiteral> l_beliefs = Stream.of(
            CLiteral.from( "toplevel" ),
            CLiteral.from( "first/sub1" ),
            CLiteral.from( "first/sub2" ),
            CLiteral.from( "second/sub1" ),
            CLiteral.from( "second/sub2" ),
            CLiteral.from( "second/sub/sub1" )
        ).collect( Collectors.toSet() );

        this.check( "weight difference equality", l_filter, l_metric, l_beliefs, l_beliefs, 24, 0 );
        this.check(
            "weight difference inequality",
            l_filter,
            l_metric,
            l_beliefs,
            Stream.concat( l_beliefs.stream(), Stream.of( CLiteral.from( "diff" ) ) ).collect( Collectors.toSet() ),
            28 + 1.0 / 6,
            0
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

        l_test.testSymmetricWeight();
        //l_test.testWeight();
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
        final IAgent<?> l_agent1 = this.getAgent( p_belief1 );
        final IAgent<?> l_agent2 = this.getAgent( p_belief2 );

        System.out.println( l_agent1 );
        System.out.println( l_agent2 );

        final double l_value = p_metric.calculate(
            p_filter.filter( l_agent1 ).collect( Collectors.toList() ),
            p_filter.filter( l_agent2 ).collect( Collectors.toList() )
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
        p_literals.parallelStream().forEach( i -> l_agent.getBeliefBase().generate( i.getFunctorPath(), m_generator ).add( i ) );
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
            return new CBeliefBase<>( new CMultiStorage<>() ).create( p_name, p_parent );
        }
    }

}
