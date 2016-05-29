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

package org.lightjason.agentspeak.agent;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.beliefbase.IBeliefBaseUpdate;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.generator.CDefaultAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.score.IAggregation;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;


/**
 * test agent structure
 */
public final class TestCAgent
{
    /**
     * list with default (non-working) actions
     */
    private static final Map<IAction, Double> ACTIONS;
    /**
     * map with all testing asl
     */
    private static final Map<String, String> ASL = StreamUtils.zip(

        Stream.of(
            "src/test/resources/agent/complete.asl",
            "src/test/resources/agent/default.asl"
        ),

        Stream.of(
            "full-test agent", "default agent"
        ),

        AbstractMap.SimpleImmutableEntry::new

    ).collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue ) );

    static
    {
        // disable logger
        LogManager.getLogManager().reset();

        // create actions
        final Random l_random = new Random();
        Map<IAction, Double> l_map = new HashMap<>();
        try
        {
            l_map = CCommon.getActionsFromPackage().stream().collect( Collectors.toMap( i -> i, j -> new Double( l_random.nextInt( 15 ) ) ) );
        }
        catch ( final IOException l_exception )
        {
            l_map = Collections.emptyMap();
        }

        ACTIONS = l_map;
    }

    /**
     * asl parsing test
     */
    @Test
    public final void testASLManual()
    {
        ASL.entrySet()
            .stream()
            .forEach( i ->
            {
                final Pair<Boolean, String> l_result = testAgentManual( i.getKey(), i.getValue() );
                assertTrue( l_result.getRight(), l_result.getLeft() );
                System.out.println( l_result.getValue() );
            } );
    }

    /**
     * test for default generators and configuration
     */
    @Test
    public final void testASLDefault()
    {
        final Set<String> l_result = ASL.entrySet()
                                        .stream()
                                        .map( i ->
                                        {
                                            try
                                            (
                                                final InputStream l_stream = new FileInputStream( i.getKey() );
                                            )
                                            {
                                                new CDefaultAgentGenerator<>(
                                                    l_stream,
                                                    ACTIONS.keySet(),
                                                    IAggregation.EMPTY,
                                                    IBeliefBaseUpdate.EMPTY,
                                                    new CVariableBuilder()
                                                ).generatesingle().call();
                                                return null;
                                            }
                                            catch ( final Exception l_exception )
                                            {
                                                return MessageFormat.format( "{0}: {1}", i.getValue(), l_exception );
                                            }
                                        } )
                                        .filter( i -> i != null )
                                        .collect( Collectors.toSet() );

        assertTrue( l_result.toString(), l_result.isEmpty() );
    }

    /**
     * manuell running test
     *
     * @param p_args arguments
     * @throws Exception on parsing exception
     */
    public static void main( final String[] p_args ) throws Exception
    {
        final TestCAgent l_test = new TestCAgent();

        l_test.testASLDefault();
        //l_test.testASLManual();
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * static function to run an agent
     *
     * @param p_script script path
     * @param p_name agent name
     * @return tupel & string
     */
    private static Pair<Boolean, String> testAgentManual( final String p_script, final String p_name )
    {
        final IAgent<?> l_agent;
        try (
            final InputStream l_stream = new FileInputStream( p_script );
        )
        {
            l_agent = new CDefaultAgentGenerator<>(
                l_stream,
                ACTIONS.keySet(),
                new CAggregation( ACTIONS ),
                Collections.<IPlanBundle>emptySet(),
                new CBeliefBaseUpdate(),
                new CVariableBuilder()
            ).generatesingle();

            // run 5 cycles
            IntStream.range( 0, 5 )
                     .forEach( i ->
                     {
                         try
                         {
                             l_agent.call();
                             l_agent.getBeliefBase().add( CLiteral.from( "counter", Stream.of( CRawTerm.from( i ) ) ) );
                             l_agent.trigger(
                                 CTrigger.from( ITrigger.EType.DELETEGOAL, CLiteral.from( "myexternal" ) )
                             );
                         }
                         catch ( final Exception l_exception )
                         {
                             assertTrue(
                                 MessageFormat.format(
                                     "{0} {1}",
                                     l_exception.getClass().getName(),
                                     l_exception.getMessage().isEmpty() ? "" : l_exception.getMessage()
                                 ).trim(),
                                 false
                             );
                         }
                     } );
        }
        catch ( final Exception l_exception )
        {
            return new ImmutablePair<>( false, MessageFormat.format( "{0} passed with failure: {1}", p_name, l_exception ) );
        }

        return new ImmutablePair<>( true, MessageFormat.format( "{0} passed successfully in: {1}", p_name, l_agent ) );
    }

    /**
     * beliefbase update e.g. environment updates
     */
    private static final class CBeliefBaseUpdate implements IBeliefBaseUpdate<IAgent<?>>
    {
        @Override
        public final IAgent<?> beliefupdate( final IAgent<?> p_agent )
        {
            return p_agent;
        }
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
        public final double evaluate( final IAgent<?> p_agent, final Multiset<IAction> p_score )
        {
            return p_score.isEmpty() ? 0 : p_score.stream().mapToDouble( m_actions::get ).sum();
        }

        @Override
        public final double evaluate( final Stream<Double> p_values )
        {
            return p_values.mapToDouble( i -> i ).sum();
        }

        @Override
        public final double error()
        {
            return Double.POSITIVE_INFINITY;
        }
    }


    /**
     * variable builder
     */
    private static final class CVariableBuilder implements IVariableBuilder
    {

        @Override
        public final Stream<IVariable<?>> generate( final IAgent<?> p_agent, final IInstantiable p_runningcontext )
        {
            return Stream.of( new CConstant<>( "MyConstInt", 123 ), new CConstant<>( "MyConstString", "here is a test string" ) );
        }

    }

}
