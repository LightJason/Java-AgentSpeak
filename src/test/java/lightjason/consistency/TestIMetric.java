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

import com.google.common.collect.ImmutableSetMultimap;
import lightjason.agent.CAgent;
import lightjason.agent.IAgent;
import lightjason.agent.configuration.CDefaultAgentConfiguration;
import lightjason.beliefbase.CBeliefBase;
import lightjason.beliefbase.CStorage;
import lightjason.beliefbase.IView;
import lightjason.consistency.metric.CSymmetricDifference;
import lightjason.consistency.metric.CWeightedDifference;
import lightjason.consistency.metric.IMetric;
import lightjason.language.CLiteral;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;


/**
 * metric tests
 */
@SuppressWarnings( "serial" )
public final class TestIMetric
{
    /**
     * literal view generator
     */
    private final IView.IGenerator m_generator = new CGenerator();
    ;


    /**
     * test symmetric weight metric
     */
    @Test
    public final void testSymmetricWeight()
    {
        final IMetric l_metric = new CSymmetricDifference();

        final Set<ILiteral> l_beliefs = new HashSet<>();
        l_beliefs.add( CLiteral.from( "toplevel" ) );
        l_beliefs.add( CLiteral.from( "first/sub1" ) );
        l_beliefs.add( CLiteral.from( "first/sub2" ) );
        l_beliefs.add( CLiteral.from( "second/sub1" ) );
        l_beliefs.add( CLiteral.from( "second/sub2" ) );
        l_beliefs.add( CLiteral.from( "second/sub/sub1" ) );

        this.check( "symmetric difference equality", l_metric, l_beliefs, l_beliefs, 0, 0 );
        this.check( "symmetric difference inequality", l_metric, l_beliefs, new HashSet<ILiteral>( l_beliefs )
        {{
            add( CLiteral.from( "diff" ) );
        }}, 1, 0 );
    }


    /**
     * test symmetric metric
     */
    @Test
    public final void testWeight()
    {
        final IMetric l_metric = new CWeightedDifference();

        final Set<ILiteral> l_beliefs = new HashSet<>();
        l_beliefs.add( CLiteral.from( "toplevel" ) );
        l_beliefs.add( CLiteral.from( "first/sub1" ) );
        l_beliefs.add( CLiteral.from( "first/sub2" ) );
        l_beliefs.add( CLiteral.from( "second/sub1" ) );
        l_beliefs.add( CLiteral.from( "second/sub2" ) );
        l_beliefs.add( CLiteral.from( "second/sub/sub1" ) );

        this.check( "weight difference equality", l_metric, l_beliefs, l_beliefs, 24, 0 );
        this.check( "weight difference inequality", l_metric, l_beliefs, new HashSet<ILiteral>( l_beliefs )
        {{
            add( CLiteral.from( "diff" ) );
        }}, 28 + 1.0 / 6, 0 );
    }

    /**
     * manuell running test
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args )
    {
        final TestIMetric l_test = new TestIMetric();

        l_test.testSymmetricWeight();
        l_test.testWeight();
    }

    /**
     * runs the check
     *
     * @param p_message error / successful message
     * @param p_metric metric value
     * @param p_belief1 belief set 1
     * @param p_belief2 belief set 2
     * @param p_excepted expected value
     * @param p_delta delta
     */
    private void check( final String p_message, final IMetric p_metric, final Collection<ILiteral> p_belief1, final Collection<ILiteral> p_belief2,
                        final double p_excepted, final double p_delta
    )
    {
        final double l_value = p_metric.calculate( this.getAgent( p_belief1 ), this.getAgent( p_belief2 ) );
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
        final IAgent l_agent = new CAgent( new CDefaultAgentConfiguration(
                Collections.<ILiteral>emptyList(),
                ImmutableSetMultimap.<ITrigger<?>, IPlan>of(),
                null, null, null
        ) );
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
