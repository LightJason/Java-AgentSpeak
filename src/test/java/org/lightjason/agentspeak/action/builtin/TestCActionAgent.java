/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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

package org.lightjason.agentspeak.action.builtin;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.agent.CAddPlan;
import org.lightjason.agentspeak.action.builtin.agent.CBeliefList;
import org.lightjason.agentspeak.action.builtin.agent.CClearBeliefbase;
import org.lightjason.agentspeak.action.builtin.agent.CCycleTime;
import org.lightjason.agentspeak.action.builtin.agent.CGetPlan;
import org.lightjason.agentspeak.action.builtin.agent.CPlanList;
import org.lightjason.agentspeak.action.builtin.agent.CRemovePlan;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.instantiable.plan.annotation.IAnnotation;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.execution.instantiable.IBaseInstantiable;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.CPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.IPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
            new CAgentGenerator( new ByteArrayInputStream( "".getBytes( StandardCharsets.UTF_8 ) ) ).generatesingle(),
            new CEmptyPlan( ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "contextplan" ) ) ),
            Collections.emptyList()
        );
    }


    /**
     * test plan list
     */
    @Test
    public final void planlist()
    {
        final ITrigger l_trigger = ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "testplanlist" ) );
        final IPlan l_plan = new CEmptyPlan( l_trigger );
        final List<ITerm> l_return = new ArrayList<>();

        new CPlanList().execute(
            false, m_context,
            Collections.emptyList(),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );
        Assert.assertEquals( l_return.get( 0 ).<List<?>>raw().size(), 0 );


        m_context.agent().plans().put( l_plan.trigger(), CPlanStatistic.of( l_plan ) );

        new CPlanList().execute(
            false, m_context,
            Collections.emptyList(),
            l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof List<?> );
        Assert.assertEquals( l_return.get( 1 ).<List<?>>raw().size(), 1 );
        Assert.assertTrue( l_return.get( 1 ).<List<?>>raw().get( 0 ) instanceof AbstractMap.Entry<?, ?> );
        Assert.assertEquals( l_return.get( 1 ).<List<AbstractMap.Entry<String, ILiteral>>>raw().get( 0 ).getKey(), l_trigger.type().sequence() );
        Assert.assertEquals( l_return.get( 1 ).<List<AbstractMap.Entry<String, ILiteral>>>raw().get( 0 ).getValue(), l_trigger.literal() );
    }


    /**
     * test add plan
     */
    @Test
    public final void addplan()
    {
        final IPlan l_plan = new CEmptyPlan( ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "testaddplan" ) ) );

        new CAddPlan().execute(
            false, m_context,
            Stream.of( l_plan ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( m_context.agent().plans().size(), 1 );
        Assert.assertArrayEquals(
            m_context.agent().plans().values().stream().map( IPlanStatistic::plan ).toArray(),
            Stream.of( l_plan ).toArray()
        );
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
            false, m_context,
            Collections.emptyList(),
            l_return
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
        final IPlan l_plan = new CEmptyPlan( ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "testgetplan" ) ) );
        final List<ITerm> l_return = new ArrayList<>();


        new CGetPlan().execute(
            false, m_context,
            Collections.emptyList(),
            l_return
        );

        Assert.assertTrue( l_return.isEmpty() );


        m_context.agent().plans().put( l_plan.trigger(), CPlanStatistic.of( l_plan ) );

        new CGetPlan().execute(
            false, m_context,
            Stream.of( "+!", "testgetplan" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
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
        final IPlan l_plan = new CEmptyPlan( ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "testremoveplan" ) ) );
        m_context.agent().plans().put( l_plan.trigger(), CPlanStatistic.of( l_plan ) );

        Assert.assertTrue(
            new CRemovePlan().execute(
                false, m_context,
                Stream.of( "+!", "testremoveplan" ).map( CRawTerm::of ).collect( Collectors.toList() ),
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
                false, m_context,
                Stream.of( "+!", "testremoveerrorplan" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
            ).value()
        );
    }


    /**
     * test clear-beliefbase
     */
    @Test
    public final void clearbeliefbase()
    {
        IntStream.range( 0, 100 )
                 .mapToObj( i -> RandomStringUtils.random( 12, "abcdefghijklmnop" ) )
                 .map( i -> CLiteral.of( i ) )
                 .forEach( i -> m_context.agent().beliefbase().add( i ) );

        Assert.assertEquals( m_context.agent().beliefbase().size(), 100 );


        new CClearBeliefbase().execute(
            false, m_context,
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( m_context.agent().beliefbase().size(), 0 );
    }


    /**
     * test belieflist
     */
    @Test
    public final void belieflist()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final Set<String> l_list = IntStream.range( 0, 100 )
                                            .mapToObj( i -> RandomStringUtils.random( 12, "abcdefghijklmnop" ) )
                                            .peek( i -> m_context.agent().beliefbase().add( CLiteral.of( i ) ) )
                                            .collect( Collectors.toSet() );

        Assert.assertEquals( m_context.agent().beliefbase().size(), 100 );

        new CBeliefList().execute(
            false, m_context,
            Collections.emptyList(),
            l_return
        );


        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof List<?> );

        Assert.assertTrue(
            l_return.get( 0 )
                    .<List<ILiteral>>raw()
                    .stream()
                    .map( i -> i.fqnfunctor().toString() )
                    .allMatch( l_list::contains )
        );
    }


    /**
     * execute agent cycle
     *
     * @return execution context
     */
    private IContext next()
    {
        agentcycleassert( m_context.agent() );
        return m_context;
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * empty plan
     */
    private static class CEmptyPlan extends IBaseInstantiable implements IPlan
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 6885053756134284862L;
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
            super( new IAnnotation<?>[0], new IExecution[0], 0 );
            m_trigger = p_trigger;
        }

        @Nonnull
        @Override
        public final ITrigger trigger()
        {
            return m_trigger;
        }

        @Nonnull
        @Override
        public final IFuzzyValue<Boolean> condition( final IContext p_context )
        {
            return CFuzzyValue.of( true );
        }
    }

}
