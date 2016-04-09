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
import lightjason.agent.action.buildin.bind.CBind;
import lightjason.agent.action.buildin.bind.IActionBind;
import lightjason.agent.generator.CDefaultAgentGenerator;
import lightjason.agent.unify.CUnifier;
import lightjason.language.CCommon;
import lightjason.language.CConstant;
import lightjason.language.IVariable;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.instantiable.IInstantiable;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            l_map = CCommon.getActionsFromPackage().stream().collect(
                    Collectors.toMap( i -> i, j -> new Double( l_random.nextInt( 15 ) ) ) );

            l_map.putAll( CBind.get( false, new CBinding() ).stream().collect( Collectors.toMap( i -> i, j -> new Double( l_random.nextInt( 15 ) ) ) ) );
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
    public final void testASL()
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
     * manuell running test
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args ) throws Exception
    {
        // run test manually for catching execpetions
        new CDefaultAgentGenerator(
                new FileInputStream( "src/test/resources/agentsimple.asl" ),
                ACTIONS.keySet(),
                new CUnifier(),
                new CAggregation( ACTIONS ),
                new CVariableBuilder()
        ).generate().call();
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
            l_agent = new CDefaultAgentGenerator( l_stream, ACTIONS.keySet(), new CUnifier(), new CAggregation( ACTIONS ), new CVariableBuilder() ).generate();

            // run 5 cycles
            IntStream.range( 0, 5 ).forEach( i -> {
                try
                {
                    l_agent.call();
                }
                catch ( final Exception p_exception )
                {
                }
            } );

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
        public final double evaluate( final IAgent p_agent, final Multiset<IAction> p_score )
        {
            return p_score.isEmpty() ? 0 : p_score.stream().mapToDouble( i -> m_actions.get( i ) ).sum();
        }

        @Override
        public final double evaluate( final Collection<Double> p_values )
        {
            return p_values.parallelStream().mapToDouble( i -> i ).sum();
        }
    }


    /**
     * variable builder
     */
    private static final class CVariableBuilder implements IVariableBuilder
    {

        @Override
        @SuppressWarnings( "serial" )
        public final Set<IVariable<?>> generate( final IAgent p_agent, final IInstantiable p_runningcontext
        )
        {
            return new HashSet<IVariable<?>>()
            {{
                add( new CConstant<>( "MyConstInt", 123 ) );
                add( new CConstant<>( "MyConstString", "here is a test string" ) );
            }};
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
        protected final int calculate( final int p_a, final int p_b )
        {
            return p_a + p_b;
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
