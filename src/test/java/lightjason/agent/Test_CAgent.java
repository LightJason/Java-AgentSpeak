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
import lightjason.agent.action.IBaseAction;
import lightjason.agent.generator.CDefaultAgentGenerator;
import lightjason.common.CPath;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.score.IAggregation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;


/**
 * test agent structure
 */
@SuppressWarnings( "serial" )
public final class Test_CAgent
{
    /**
     * list with default (non-working) actions
     */
    private static final Map<IAction, Double> ACTIONS = new HashMap<IAction, Double>()
    {{
        put( new CGeneric( "setProperty", 3 ), 1.0 );
        put( new CGeneric( "min", 1 ), 2.0 );
        put( new CGeneric( "print", 1 ), 3.0 );
    }};


    /**
     * asl parsing test
     */
    @Test
    public void test_asl()
    {
        final Map<String, String> l_testing = new HashMap<String, String>()
        {{

            put( "src/test/resources/agentsuccess.asl", "complex successful agent" );
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
        catch ( final Exception l_exception )
        {
            return new ImmutablePair<>( false, MessageFormat.format( "{0} passed with failure: {1}", p_name, l_exception ) );
        }

        return new ImmutablePair<>( true, MessageFormat.format( "{0} passed successfully in: {1}", p_name, l_agent ) );
    }


    /**
     * generic action to define any action
     */
    private static final class CGeneric extends IBaseAction
    {
        /**
         * name of the action
         **/
        private final CPath m_name;

        /**
         * number of arguments
         */
        private final int m_arguments;


        public CGeneric( final String p_name, final int p_arguments )
        {
            m_name = CPath.from( p_name );
            m_arguments = p_arguments;
        }

        @Override
        public CPath getName()
        {
            return m_name;
        }

        @Override
        public final int getMinimalArgumentNumber()
        {
            return m_arguments;
        }

        @Override
        public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Collection<ITerm> p_annotation, final Collection<ITerm> p_parameter,
                                                   final Collection<ITerm> p_return
        )
        {
            return CBoolean.from( true );
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
