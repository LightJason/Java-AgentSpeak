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

package lightjason.agent;

import com.google.common.collect.Multiset;
import lightjason.agent.action.IAction;
import lightjason.agent.generator.CDefaultAgentGenerator;
import lightjason.language.CCommon;
import lightjason.language.score.IAggregation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;


/**
 * test agent structure
 */
@SuppressWarnings( "serial" )
public final class TestCAgent
{
    /**
     * list with default (non-working) actions
     */
    private static final Map<IAction, Double> ACTIONS;

    static
    {
        final Random l_random = new Random();
        Map<IAction, Double> l_map = new HashMap<>();
        try
        {
            l_map = CCommon.getActionsFromPackage( "lightjason.agent.action.buildin" ).stream().collect(
                    Collectors.toMap( i -> i, j -> new Double( l_random.nextInt( 15 ) ) ) );
        }
        catch ( final IOException p_exception )
        {
            l_map = Collections.emptyMap();
        }

        ACTIONS = l_map;
    }

    /**
     * asl parsing test
     */
    @Test
    public void testASL()
    {
        final Map<String, String> l_testing = new HashMap<String, String>()
        {{

            //put( "src/test/resources/agentsuccess.asl", "successful agent" );
            put( "src/test/resources/agentsimple.asl", "simple agent" );

        }};


        l_testing.entrySet().stream().forEach( i -> {
            final Pair<Boolean, String> l_result = this.testAgent( i.getKey(), i.getValue() );

            assertTrue( l_result.getRight(), l_result.getLeft() );
            System.out.println( l_result.getValue() );
        } );
    }


    /**
     * static function to run an agent
     *
     * @param p_script script path
     * @param p_name agent name
     * @return tupel & string
     */
    private Pair<Boolean, String> testAgent( final String p_script, final String p_name )
    {
        final IAgent l_agent;
        try (
                final InputStream l_stream = new FileInputStream( p_script );
        )
        {
            l_agent = new CDefaultAgentGenerator( l_stream, ACTIONS.keySet(), new CAggregation( ACTIONS ) ).generate().call();
        }
        catch ( final Exception p_exception )
        {
            return new ImmutablePair<>( false, MessageFormat.format( "{0} passed with failure: {1}", p_name, p_exception ) );
        }

        return new ImmutablePair<>( true, MessageFormat.format( "{0} passed successfully in: {1}", p_name, l_agent ) );
    }


    /**
     * aggregation function
     */
    private static final class CAggregation implements IAggregation
    {
        /**
         * action & score value
         */
        private final Map<IAction, Double> m_actions;

        /**
         * ctor
         *
         * @param p_actions action score map
         */
        public CAggregation( final Map<IAction, Double> p_actions )
        {
            m_actions = p_actions;
        }

        @Override
        public double evaluate( final IAgent p_agent, final Multiset<IAction> p_score )
        {
            return p_score.isEmpty() ? 0 : p_score.stream().mapToDouble( i -> m_actions.get( i ) ).sum();
        }

        @Override
        public double evaluate( final Collection<Double> p_values )
        {
            return p_values.parallelStream().mapToDouble( i -> i ).sum();
        }
    }

}
