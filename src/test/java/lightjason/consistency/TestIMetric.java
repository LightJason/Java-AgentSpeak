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
import lightjason.beliefbase.CBeliefStorage;
import lightjason.beliefbase.IView;
import lightjason.consistency.metric.CSymmetricDifference;
import lightjason.language.CLiteral;
import lightjason.language.ILiteral;
import lightjason.language.plan.IPlan;
import lightjason.language.plan.trigger.ITrigger;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * metric tests
 */
public final class TestIMetric
{
    /**
     * literal view generator
     */
    private final IView.IGenerator l_gen = new CGenerator();
    ;


    /**
     * test symmetric weight metric
     */
    @Test
    public final void testSymmetricWeight()
    {
        final IAgent l_agent1 = this.getAgent(
                IntStream.range( 0, 15 )
                         .boxed()
                         .map( i -> CLiteral.from( RandomStringUtils.random( 12, "~abcdefghijklmnopqrstuvwxyz/".toCharArray() ) ) )
                         .collect( Collectors.toSet() )
        );

        final IAgent l_agent2 = this.getAgent(
                IntStream.range( 0, 15 )
                         .boxed()
                         .map( i -> CLiteral.from( RandomStringUtils.random( 12, "~abcdefghijklmnopqrstuvwxyz/".toCharArray() ) ) )
                         .collect( Collectors.toSet() )
        );

        System.out.println( MessageFormat.format( "symmetric difference: {0}", new CSymmetricDifference().calculate( l_agent1, l_agent2 ) ) );
    }


    /**
     * test symmetric metric
     */
    @Test
    public final void testWeight()
    {

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
        p_literals.parallelStream().forEach( i -> l_agent.getBeliefBase().add( i, l_gen ) );
        return l_agent;
    }

    /**
     * test belief generator
     */
    private static final class CGenerator implements IView.IGenerator
    {
        @Override
        public final IView createBeliefbase( final String p_name )
        {
            return new CBeliefBase( new CBeliefStorage<>() ).create( p_name );
        }
    }

}
