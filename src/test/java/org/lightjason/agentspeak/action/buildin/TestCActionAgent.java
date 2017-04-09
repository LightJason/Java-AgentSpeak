/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.buildin.agent.CAddPlan;
import org.lightjason.agentspeak.action.buildin.agent.CCycleTime;
import org.lightjason.agentspeak.action.buildin.agent.CGetPlan;
import org.lightjason.agentspeak.action.buildin.agent.CPlanList;
import org.lightjason.agentspeak.action.buildin.agent.CRemovePlan;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.annotation.IAnnotation;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.IBaseInstantiable;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.score.IAggregation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * test agent action
 */
public final class TestCActionAgent extends IBaseTest
{
    /**
     * agent context
     */
    private IContext m_context;



    static
    {
        LogManager.getLogManager().reset();
    }


    /**
     * initialize
     *
     * @throws Exception on initialize error
     */
    @Before
    public void initialize() throws Exception
    {
        m_context = new CContext(
            new CGenerator( new ByteArrayInputStream( "".getBytes( StandardCharsets.UTF_8 ) ), Collections.emptySet(), IAggregation.EMPTY ).generatesingle(),
            new CEmptyPlan( CTrigger.from( ITrigger.EType.ADDGOAL, CLiteral.from( "contextplan" ) ) ),
            Collections.emptyList()
        );
    }


    /**
     * test cycle
     */
    @Test
    public final void cycle()
    {
        Assert.assertEquals( m_context.agent().cycle(), 0 );
        LongStream.range( 1, new Random().nextInt( 200 ) + 1 ).forEach( i -> Assert.assertEquals( this.next().agent().cycle(), i ) );
    }


    /**
     * test plan list
     */
    @Test
    public final void planlist()
    {
        final ITrigger l_trigger = CTrigger.from( ITrigger.EType.ADDGOAL, CLiteral.from( "testplanlist" ) );
        final IPlan l_plan = new CEmptyPlan( l_trigger );
        final List<ITerm> l_return = new ArrayList<>();

        new CPlanList().execute(
            m_context,
            false,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().size(), 0 );


        m_context.agent().plans().put( l_plan.getTrigger(), new ImmutableTriple<>( l_plan, new AtomicLong(), new AtomicLong() ) );

        new CPlanList().execute(
            m_context,
            false,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof List<?> );
        Assert.assertEquals( l_return.get( 1 ).<List<?>>raw().size(), 1 );
        Assert.assertTrue( l_return.get( 1 ).<List<?>>raw().get( 0 ) instanceof AbstractMap.Entry<?, ?> );
        Assert.assertEquals( l_return.get( 1 ).<List<AbstractMap.Entry<String, ILiteral>>>raw().get( 0 ).getKey(), l_trigger.getType().sequence() );
        Assert.assertEquals( l_return.get( 1 ).<List<AbstractMap.Entry<String, ILiteral>>>raw().get( 0 ).getValue(), l_trigger.getLiteral() );
    }


    /**
     * test add plan
     */
    @Test
    public final void addplan()
    {
        final IPlan l_plan = new CEmptyPlan( CTrigger.from( ITrigger.EType.ADDGOAL, CLiteral.from( "testaddplan" ) ) );

        new CAddPlan().execute(
            m_context,
            false,
            Stream.of( l_plan ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( m_context.agent().plans().size(), 1 );
        Assert.assertArrayEquals( m_context.agent().plans().values().stream().map( Triple::getLeft ).toArray(), Stream.of( l_plan ).toArray() );
    }


    /**
     * test cycle-time
     */
    @Test
    public final void cycletime()
    {
        this.next();

        final List<ITerm> l_return = new ArrayList<>();
        new CCycleTime().execute(
            m_context,
            false,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).<Number>raw().longValue() > 0 );
    }


    /**
     * test get plan
     */
    @Test
    public final void getplan()
    {
        final IPlan l_plan = new CEmptyPlan( CTrigger.from( ITrigger.EType.ADDGOAL, CLiteral.from( "testgetplan" ) ) );
        final List<ITerm> l_return = new ArrayList<>();


        new CGetPlan().execute(
            m_context,
            false,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertTrue( l_return.isEmpty() );


        m_context.agent().plans().put( l_plan.getTrigger(), new ImmutableTriple<>( l_plan, new AtomicLong(), new AtomicLong() ) );

        new CGetPlan().execute(
            m_context,
            false,
            Stream.of( "+!", "testgetplan" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().size(), 1 );
        Assert.assertArrayEquals( l_return.get( 0 ).<List<?>>raw().toArray(), Stream.of( l_plan ).toArray() );
    }


    /**
     * test remove plan
     */
    @Test
    public final void removeplan()
    {
        final IPlan l_plan = new CEmptyPlan( CTrigger.from( ITrigger.EType.ADDGOAL, CLiteral.from( "testremoveplan" ) ) );
        m_context.agent().plans().put( l_plan.getTrigger(), new ImmutableTriple<>( l_plan, new AtomicLong(), new AtomicLong() ) );

        Assert.assertTrue(
            new CRemovePlan().execute(
                m_context,
                false,
                Stream.of( "+!", "testremoveplan" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }


    /**
     * test remove plan error
     */
    @Test
    public final void removeplanerror()
    {
        Assert.assertFalse(
            new CRemovePlan().execute(
                m_context,
                false,
                Stream.of( "+!", "testremoveerrorplan" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }


    /**
     * execute agent cycle
     *
     * @return execution context
     */
    private IContext next()
    {
        try
        {
            m_context.agent().call();
        }
        catch ( final Exception l_exception )
        {
            Assert.assertTrue( l_exception.getMessage(), false );
        }

        return m_context;
    }

    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionAgent().invoketest();
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<CAgent>
    {

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CAgent( final IAgentConfiguration<CAgent> p_configuration )
        {
            super( p_configuration );
        }
    }

    /**
     * agent generator
     */
    private static final class CGenerator extends IBaseAgentGenerator<CAgent>
    {

        CGenerator( final InputStream p_stream, final Set<IAction> p_actions,
                    final IAggregation p_aggregation ) throws Exception
        {
            super( p_stream, p_actions, p_aggregation );
        }

        @Override
        public final CAgent generatesingle( final Object... p_data )
        {
            return new CAgent( m_configuration );
        }
    }


    /**
     * empty plan
     */
    private static class CEmptyPlan extends IBaseInstantiable implements IPlan
    {
        /**
         * trigger
         */
        private final ITrigger m_trigger;

        /**
         * ctor
         *
         * @param p_trigger trigger
         */
        CEmptyPlan( final ITrigger p_trigger )
        {
            super( Collections.emptyList(), Collections.emptySet(), 0 );
            m_trigger = p_trigger;
        }

        @Override
        public final ITrigger getTrigger()
        {
            return m_trigger;
        }

        @Override
        public final Collection<IAnnotation<?>> getAnnotations()
        {
            return m_annotation.values();
        }

        @Override
        public final List<IExecution> getBodyActions()
        {
            return Collections.emptyList();
        }

        @Override
        public final IFuzzyValue<Boolean> condition( final IContext p_context )
        {
            return CFuzzyValue.from( true );
        }
    }

}
