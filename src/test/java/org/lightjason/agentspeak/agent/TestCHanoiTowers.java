/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

import edu.umd.cs.findbugs.annotations.NonNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;
import org.lightjason.agentspeak.testing.action.CTestPrint;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.LogManager;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * abstract test for playing <a href="https://en.wikipedia.org/wiki/Tower_of_Hanoi">towers of hanoi</a>
 * If a file agentprintin.conf exists on the main directory alls print statements will be shown
 */
public final class TestCHanoiTowers extends IBaseTest
{
    /**
     * number of maximum cycles
     */
    private static final int MAXIMUMCYCLES = 1000;
    /**
     * action fail probability
     */
    private static final double FAILPROBABILITY = 0.1;
    /**
     * agent map
     */
    private Map<Integer, CAgent> m_agents;
    /**
     * number of towers
     **/
    private Long m_towernumber;
    /**
     * number of slices
     */
    private Long m_slicenumber;
    /**
     * tower map
     */
    private Map<Integer, CTower> m_tower;
    /**
     * running flag (agent can disable execute)
     */
    private AtomicBoolean m_running;


    static
    {
        // disable logger
        LogManager.getLogManager().reset();
    }


    /**
     * test initialize
     *
     * @throws Exception on initialize error
     */
    @Before
    public void initialize() throws Exception
    {
        this.setup(
            1, 3, 3,
            this.getClass().getClassLoader().getResource( "hanoi.asl" ).getPath(), Stream.of() );
    }


    /**
     * test execute
     *
     * @throws InterruptedException is thrown on execute error
     */
    @Test
    public void play() throws InterruptedException
    {
        this.execute();
    }


    /**
     * initialize call
     * @param p_agentnumber number of agents
     * @param p_towernumber number of towers
     * @param p_slicenumber number of slices
     * @param p_asl asl file
     * @param p_action action stream
     * @throws Exception thrown on initialize error
     */
    private void setup( final int p_agentnumber, final long p_towernumber, final long p_slicenumber,
                        final String p_asl, final Stream<IAction> p_action ) throws Exception
    {
        m_towernumber = p_towernumber;
        m_slicenumber = p_slicenumber;
        m_running = new AtomicBoolean( true );

        final Map<Integer, CTower> l_towermap = new ConcurrentHashMap<>();
        IntStream.range( 0, m_towernumber.intValue() )
                 .forEach( i ->
                 {
                     final CTower l_tower = new CTower();
                     l_towermap.put( i, l_tower );
                     if ( i == 0 )
                         IntStream.range( 0, m_slicenumber.intValue() ).forEach( j -> l_tower.push( new CSlice( m_slicenumber.intValue() - j ) ) );
                 } );
        m_tower = Collections.unmodifiableMap( l_towermap );


        final Map<Integer, CAgent> l_agentmap = new ConcurrentHashMap<>();
        try
            (
                final InputStream l_asl = new FileInputStream( p_asl )
            )
        {
            final CGenerator l_generator = new CGenerator( l_asl, p_action );
            IntStream.range( 0, p_agentnumber )
                      .forEach( i -> l_agentmap.put( i, l_generator.generatesingle( i ) ) );
        }
        catch ( final IOException l_exception )
        {
            l_exception.printStackTrace();
            Assert.fail( "asl could not be read" );
        }
        m_agents = Collections.unmodifiableMap( l_agentmap );
    }



    /**
     * running towers of hanoi
     */
    private void execute()
    {
        int l_cycles = MAXIMUMCYCLES;
        while ( ( m_running.get() ) && ( l_cycles > 0 ) )
        {
            l_cycles--;
            if ( PRINTENABLE )
                System.out.println( MessageFormat.format( "\ntower configuration: {0}", m_tower ) );
            m_agents.values()
                    .parallelStream()
                    .forEach( IBaseTest::agentcycle );
        }

        Assert.assertTrue( "agent did not terminate", l_cycles > 0 );
    }


    /**
     * agent class
     */
    private class CAgent extends IBaseAgent<CAgent>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 9183183177551189228L;
        /**
         * id of the agent
         */
        private final int m_id;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         * @param p_id id of the agent
         */
        CAgent( final IAgentConfiguration<CAgent> p_configuration, final int p_id )
        {
            super( p_configuration );
            m_id = p_id;
        }

        /**
         * returns the id of the agent
         *
         * @return id
         */
        final int id()
        {
            return m_id;
        }
    }


    /**
     * agent generator
     *
     * @warning variable builder within anonymous class cannot be defined
     * with lambda expression, because of stack memory error
     */
    private final class CGenerator extends IBaseAgentGenerator<CAgent>
    {

        /**
         * ctor
         *
         * @param p_stream asl stream
         * @param p_action stream of action
         * @throws Exception on any error
         */
        CGenerator( final InputStream p_stream, final Stream<IAction> p_action ) throws Exception
        {
            super(
                p_stream,

                new CActionStaticGenerator(
                    Stream.concat(
                        Stream.concat(
                            Stream.of(
                                new CTowerPush( FAILPROBABILITY ),
                                new CTowerPop(),
                                new CTowerSize(),
                                new CStop(),
                                new CTestPrint( PRINTENABLE )
                            ),
                            p_action
                        ),
                        CCommon.actionsFromPackage()
                    )
                ),
                ILambdaStreamingGenerator.EMPTY,
                new IVariableBuilder()
                {
                    @Override
                    public Stream<IVariable<?>> apply( final IAgent<?> p_agent, final IInstantiable p_instantiable
                    )
                    {
                        return Stream.of(
                            new CConstant<>( "MyID", p_agent.<CAgent>raw().id() ),
                            new CConstant<>( "TowerCount", TestCHanoiTowers.this.m_towernumber ),
                            new CConstant<>( "TowerMaxIndex", TestCHanoiTowers.this.m_towernumber - 1 ),
                            new CConstant<>( "SliceCount", TestCHanoiTowers.this.m_slicenumber )
                        );
                    }
                }
            );
        }

        @Override
        @SuppressWarnings( "unchecked" )
        public CAgent generatesingle( final Object... p_data )
        {
            return new CAgent( m_configuration, (int) p_data[0] );
        }
    }

    /**
     * stop action
     */
    private final class CStop extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 1356103671899402899L;

        @Nonnull
        @Override
        public IPath name()
        {
            return CPath.of( "stop" );
        }

        @Nonnegative
        @Override
        public int minimalArgumentNumber()
        {
            return 0;
        }

        @Nonnull
        @Override
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
        )
        {
            m_running.set( false );
            return Stream.of();
        }
    }


    /**
     * returns the number of elements of an tower
     */
    private final class CTowerSize extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -621010269747370196L;

        @Nonnull
        @Override
        public IPath name()
        {
            return CPath.of( "tower/size" );
        }

        @Nonnegative
        @Override
        public int minimalArgumentNumber()
        {
            return 1;
        }

        @Nonnull
        @Override
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            final CTower l_tower = m_tower.get( p_argument.get( 0 ).<Number>raw().intValue() );
            if ( Objects.isNull( l_tower ) )
                return p_context.agent().fuzzy().membership().fail();

            p_return.add( CRawTerm.of( l_tower.size() ) );
            return p_context.agent().fuzzy().membership().success();
        }
    }


    /**
     * pushs a slice to a tower
     */
    private final class CTowerPush extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -3816901920109893840L;
        /**
         * probability for action failing
         */
        private final double m_failprobability;

        /**
         * ctor
         *
         * @param p_failprobability failing probability
         */
        CTowerPush( final double p_failprobability )
        {
            m_failprobability = p_failprobability;
        }

        @Nonnull
        @Override
        public IPath name()
        {
            return CPath.of( "tower/push" );
        }

        @Nonnegative
        @Override
        public int minimalArgumentNumber()
        {
            return 2;
        }

        @Nonnull
        @Override
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            final CTower l_tower = m_tower.get( p_argument.get( 0 ).<Number>raw().intValue() );
            if ( ( Objects.isNull( l_tower ) ) || ( Math.random() < m_failprobability ) )
                return p_context.agent().fuzzy().membership().fail();

            try
            {
                l_tower.push( p_argument.get( 1 ).raw() );
                return p_context.agent().fuzzy().membership().success();
            }
            catch ( final IllegalStateException l_exception )
            {
                return p_context.agent().fuzzy().membership().fail();
            }
        }
    }

    /**
     * pops an elements of a tower
     */
    private final class CTowerPop extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 7757699013244193313L;

        @Nonnull
        @Override
        public IPath name()
        {
            return CPath.of( "tower/pop" );
        }

        @Nonnegative
        @Override
        public int minimalArgumentNumber()
        {
            return 1;
        }

        @Nonnull
        @Override
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            final CTower l_tower = m_tower.get( p_argument.get( 0 ).<Number>raw().intValue() );
            if ( Objects.isNull( l_tower ) )
                return p_context.agent().fuzzy().membership().success();

            try
            {
                p_return.add( CRawTerm.of( l_tower.pop() ) );
                return p_context.agent().fuzzy().membership().success();
            }
            catch ( final IllegalStateException l_exception )
            {
                return p_context.agent().fuzzy().membership().fail();
            }
        }
    }


    /**
     * defines a slice
     */
    private static final class CSlice
    {
        /**
         * slice size
         */
        private final int m_size;

        /**
         * ctor
         *
         * @param p_size slice size
         */
        CSlice( final int p_size )
        {
            m_size = p_size;
        }

        @Override
        public String toString()
        {
            return MessageFormat.format( "slice {0}", m_size );
        }

        /**
         * returns the size
         *
         * @return slice size
         */
        final int size()
        {
            return m_size;
        }
    }

    /**
     * tower
     */
    private static final class CTower extends Stack<CSlice>
    {
        /**
         * serial id
         */
        private static final transient long serialVersionUID = 1361367629042813689L;

        @Override
        public synchronized CSlice push( @NonNull final CSlice p_item )
        {
            if ( ( this.size() > 0 ) && ( this.peek().size() < p_item.size() ) )
                throw new IllegalStateException();

            return super.push( p_item );
        }

        @Override
        public synchronized CSlice pop()
        {
            if ( this.isEmpty() )
                throw new IllegalStateException();

            return super.pop();
        }

        @Override
        public synchronized CSlice peek()
        {
            return super.peek();
        }

        @Override
        public synchronized boolean empty()
        {
            return super.empty();
        }

        @Override
        public synchronized int search( final Object p_object )
        {
            return super.search( p_object );
        }
    }

}
