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

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.Multiset;
import lightjason.agent.action.IAction;
import lightjason.agent.action.buildin.bind.CBind;
import lightjason.agent.action.buildin.bind.IActionBind;
import lightjason.agent.generator.CDefaultAgentGenerator;
import lightjason.agent.unify.CUnifier;
import lightjason.beliefbase.IBeliefBaseUpdate;
import lightjason.language.CCommon;
import lightjason.language.CLiteral;
import lightjason.language.CRawTerm;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.instantiable.IInstantiable;
import lightjason.language.instantiable.plan.trigger.CTrigger;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import lightjason.language.score.IAggregation;
import lightjason.language.variable.CConstant;
import lightjason.language.variable.IVariable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        // disable logger
        LogManager.getLogManager().reset();

        // create actions
        final Random l_random = new Random();
        Map<IAction, Double> l_map = new HashMap<>();
        try
        {
            l_map = CCommon.getActionsFromPackage().stream().collect(
                    Collectors.toMap( i -> i, j -> new Double( l_random.nextInt( 15 ) ) ) );

            l_map.putAll( CBind.get( false, new CBinding() ).stream().collect( Collectors.toMap( i -> i, j -> new Double( l_random.nextInt( 15 ) ) ) ) );
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
    public final void testASL()
    {
        final Map<String, String> l_testing = StreamUtils.zip(

                Stream.of(
                        "src/test/resources/agent/complete.asl"
                ),

                Stream.of(
                        "full-test agent"
                ),

                ( k, v ) -> new AbstractMap.SimpleImmutableEntry<>( k, v )

        ).collect( Collectors.toConcurrentMap( Map.Entry::getKey, Map.Entry::getValue ) );

        l_testing.entrySet().stream().forEach( i -> {
            final Pair<Boolean, String> l_result = this.testAgent( i.getKey(), i.getValue() );
            assertTrue( l_result.getRight(), l_result.getLeft() );
            System.out.println( l_result.getValue() );
        } );
    }

    /**
     * manuell running test
     *
     * @param p_args arguments
     * @throws Exception on parsing exception
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new TestCAgent().testASL();
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
            l_agent = new CDefaultAgentGenerator(
                    l_stream, ACTIONS.keySet(), new CUnifier(), new CAggregation( ACTIONS ), new CBeliefBaseUpdate(), new CVariableBuilder() ).generate();

            // run 5 cycles
            IntStream.range( 0, 5 ).forEach( i -> {
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


    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * beliefbase update e.g. environment updates
     */
    private static final class CBeliefBaseUpdate implements IBeliefBaseUpdate
    {

        @Override
        public final IAgent beliefupdate( final IAgent p_agent )
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
        public final double evaluate( final IAgent p_agent, final Multiset<IAction> p_score )
        {
            return p_score.isEmpty() ? 0 : p_score.stream().mapToDouble( i -> m_actions.get( i ) ).sum();
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
        public final Stream<IVariable<?>> generate( final IAgent p_agent, final IInstantiable p_runningcontext )
        {
            return Stream.of( new CConstant<>( "MyConstInt", 123 ), new CConstant<>( "MyConstString", "here is a test string" ) );
        }

    }


    /**
     * super-class for testing binding action
     */
    private static class CBindingSuper
    {

        private void runsuper()
        {

        }
    }


    /**
     * class for testing binding actions
     */
    private static final class CBinding extends CBindingSuper
    {

        /**
         * protected method with parameter
         */
        protected final int calculate( final int p_left, final int p_right )
        {
            return p_left + p_right;
        }

        /**
         * any public method
         */
        private final void first()
        {
        }

        /**
         * overloaded
         *
         * @param p_value string value
         */
        private final void first( final String p_value )
        {

        }

        /**
         * any private method
         */
        private void second()
        {

        }

        /**
         * annotation test
         */
        @IActionBind( bind = false )
        private void notuse()
        {
        }

    }

}
