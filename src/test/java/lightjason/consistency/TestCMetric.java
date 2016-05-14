/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.consistency;

import lightjason.agent.CAgent;
import lightjason.agent.IAgent;
import lightjason.agent.configuration.CDefaultAgentConfiguration;
import lightjason.beliefbase.CBeliefBase;
import lightjason.beliefbase.CStorage;
import lightjason.beliefbase.IView;
import lightjason.consistency.filter.CAll;
import lightjason.consistency.filter.IFilter;
import lightjason.consistency.metric.CSymmetricDifference;
import lightjason.consistency.metric.CWeightedDifference;
import lightjason.consistency.metric.IMetric;
import lightjason.language.CLiteral;
import lightjason.language.ILiteral;
import org.junit.Test;

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
    private final IView.IGenerator m_generator = new CGenerator();


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
        l_test.testWeight();
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
        assertEquals( p_message, l_value, p_excepted, p_delta );
        System.out.println( MessageFormat.format( "{0} value: {1}", p_message, l_value ) );
    }


    /**
     * generates an agent
     *
     * @param p_literals literal collection
     * @return agent
     */
    private IAgent getAgent( final Collection<ILiteral> p_literals )
    {
        final IAgent l_agent = new CAgent( new CDefaultAgentConfiguration() );
        p_literals.parallelStream().forEach( i -> l_agent.getBeliefBase().add( i, m_generator ) );
        return l_agent;
    }

    /**
     * test belief generator
     */
    private static final class CGenerator implements IView.IGenerator
    {
        @Override
        public final IView generate( final String p_name )
        {
            return new CBeliefBase( new CStorage<>() ).create( p_name );
        }
    }

}
