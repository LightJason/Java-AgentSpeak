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

import org.checkerframework.checker.index.qual.NonNegative;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.CDefaultPlanBundleConfiguration;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.IAgentGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.instantiable.plan.statistic.IPlanStatistic;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * test agent method calls
 */
public final class TestCAgentStructure extends IBaseTest
{

    /**
     * test agent tostring
     *
     * @throws IOException parsing error
     */
    @Test
    public void agenttostring() throws IOException
    {
        final IAgent<?> l_agent = new CAgentGenerator().generatesingle();

        Assertions.assertTrue( l_agent.toString().startsWith( "org.lightjason.agentspeak.testing.IBaseTest$CAgent@" ) );
        Assertions.assertTrue( l_agent.toString().contains( " ( Trigger: [] / Running Plans: [] / Beliefbase: beliefbase" ) );
    }

    /**
     * test trigger variable error
     *
     * @throws IOException parsing error
     */
    @Test
    public void triggervariableerror() throws IOException
    {
        final IAgent<?> l_agent = new CAgentGenerator().generatesingle();

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.parse( "foo(X)" ) ) )
        );
    }

    /**
     * test agent cycle time
     *
     * @throws IOException parsing error
     */
    @Test
    public void cycletime() throws IOException
    {
        final long l_sleepingtime = 500;

        final IAgent<?> l_agent = new CAgentGenerator(
            "!do. +!do <- .test/sleep.",
            new CActionStaticGenerator( Stream.of( new CTestSleep( l_sleepingtime ) ) ),
            ILambdaStreamingGenerator.EMPTY
        ).generatesingle();

        Assertions.assertTrue( agentcycle( l_agent ) );
        Assertions.assertTrue( l_agent.cycletime() / 1000000 >= l_sleepingtime );
    }

    /**
     * test sleep default behaviour
     *
     * @throws IOException parsing error
     */
    @Test
    public void sleepdefault() throws IOException
    {
        final IAgent<?> l_agent = new CAgentGenerator( "+!wakeup <- success." ).generatesingle();

        Assertions.assertFalse( l_agent.sleeping() );
        l_agent.sleep( 2 );
        Assertions.assertTrue( l_agent.sleeping() );
        agentcycle( l_agent );
        Assertions.assertTrue( l_agent.sleeping() );
        agentcycle( l_agent );
        Assertions.assertFalse( l_agent.sleeping() );

        l_agent.inspect( new IInspector()
        {
            @Override
            public void inspectsleeping( final long p_value )
            {

            }

            @Override
            public void inspectcycletime( final long p_value )
            {

            }

            @Override
            public void inspectbelief( final Stream<ILiteral> p_value )
            {

            }

            @Override
            public void inspectplans( @Nonnull final Stream<IPlanStatistic> p_value
            )
            {

            }

            @Override
            public void inspectrules( @Nonnull final Stream<IRule> p_value )
            {

            }

            @Override
            public void inspectrunningplans( @Nonnull final Stream<ILiteral> p_value )
            {
                Assertions.assertEquals( CLiteral.of( "wakeup" ), p_value.findFirst().get() );
            }

            @Override
            public void inspectstorage( @Nonnull final Stream<? extends Map.Entry<String, ?>> p_value )
            {

            }

            @Override
            public void inspectpendingtrigger( @Nonnull final Stream<ITrigger> p_value )
            {
            }
        } );

        agentcycle( l_agent );
        Assertions.assertFalse( l_agent.sleeping() );

        l_agent.inspect( new IInspector()
        {
            @Override
            public void inspectsleeping( final long p_value )
            {

            }

            @Override
            public void inspectcycletime( final long p_value )
            {

            }

            @Override
            public void inspectbelief( final Stream<ILiteral> p_value )
            {

            }

            @Override
            public void inspectplans( @Nonnull final Stream<IPlanStatistic> p_value
            )
            {

            }

            @Override
            public void inspectrules( @Nonnull final Stream<IRule> p_value )
            {

            }

            @Override
            public void inspectrunningplans( @Nonnull final Stream<ILiteral> p_value )
            {
                Assertions.assertEquals( 0, p_value.count() );
            }

            @Override
            public void inspectstorage( @Nonnull final Stream<? extends Map.Entry<String, ?>> p_value )
            {

            }

            @Override
            public void inspectpendingtrigger( @Nonnull final Stream<ITrigger> p_value )
            {
                Assertions.assertEquals( 0, p_value.count() );
            }
        } );
    }

    /**
     * test wake-up
     *
     * @throws IOException on stream error
     */
    @Test
    public void sleepwakeup() throws IOException
    {
        final IAgent<?> l_agent = new CAgentGenerator( "+!wakeup <- success." ).generatesingle();

        Assertions.assertFalse( l_agent.sleeping() );
        l_agent.sleep( Long.MAX_VALUE );
        Assertions.assertTrue( l_agent.sleeping() );

        Assertions.assertFalse( l_agent.wakeup().sleeping() );
        agentcycle( l_agent );

        l_agent.inspect( new IInspector()
        {
            @Override
            public void inspectsleeping( final long p_value )
            {

            }

            @Override
            public void inspectcycletime( final long p_value )
            {

            }

            @Override
            public void inspectbelief( final Stream<ILiteral> p_value )
            {

            }

            @Override
            public void inspectplans( @Nonnull final Stream<IPlanStatistic> p_value
            )
            {

            }

            @Override
            public void inspectrules( @Nonnull final Stream<IRule> p_value )
            {

            }

            @Override
            public void inspectrunningplans( @Nonnull final Stream<ILiteral> p_value )
            {
                Assertions.assertEquals( CLiteral.of( "wakeup" ), p_value.findFirst().get() );
            }

            @Override
            public void inspectstorage( @Nonnull final Stream<? extends Map.Entry<String, ?>> p_value )
            {

            }

            @Override
            public void inspectpendingtrigger( @Nonnull final Stream<ITrigger> p_value )
            {
            }
        } );
    }

    /**
     * test plan bundle
     */
    @Test
    public void defaultplanbundle()
    {
        final IPlanBundle l_bundle = new CDefaultPlanBundle(
            new CDefaultPlanBundleConfiguration(
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptySet()
            )
        );

        Assertions.assertEquals( Collections.emptySet(), l_bundle.initialbeliefs() );
        Assertions.assertEquals( Collections.emptySet(), l_bundle.plans() );
        Assertions.assertEquals( Collections.emptySet(), l_bundle.rules() );
    }

    /**
     * test empty agent
     */
    @Test
    public void emptyagent()
    {
        Assertions.assertEquals( 0, IAgent.EMPTY.trigger( ITrigger.EMPTY ).count() );

        Assertions.assertEquals( IView.EMPTY, IAgent.EMPTY.beliefbase() );

        Assertions.assertTrue( IAgent.EMPTY.rules().isEmpty() );
        Assertions.assertTrue( IAgent.EMPTY.plans().isEmpty() );
        Assertions.assertTrue( IAgent.EMPTY.runningplans().isEmpty() );
        Assertions.assertTrue( IAgent.EMPTY.storage().isEmpty() );

        IAgent.EMPTY.sleep( 10 );
        Assertions.assertFalse( IAgent.EMPTY.sleeping() );

        Assertions.assertEquals( 0, IAgent.EMPTY.cycletime() );

        Assertions.assertEquals( IAgentGenerator.DEFAULTFUZZYBUNDLE, IAgent.EMPTY.fuzzy() );

        Assertions.assertEquals( IVariableBuilder.EMPTY, IAgent.EMPTY.variablebuilder() );

        Assertions.assertEquals( 0, IAgent.EMPTY.hashCode() );

        Assertions.assertEquals( IAgent.EMPTY, IAgent.EMPTY.raw() );
        try
        {
            Assertions.assertEquals( IAgent.EMPTY, IAgent.EMPTY.call() );
        }
        catch ( final Exception l_exception )
        {
            l_exception.printStackTrace();
            Assertions.fail();
        }

        Assertions.assertEquals( IAgent.EMPTY, IAgent.EMPTY.inspect( IInspector.EMPTY ) );
    }

    /**
     * test empty inspector
     */
    @Test
    public void emptyinspector()
    {
        IInspector.EMPTY.inspectbelief( Stream.empty() );
        IInspector.EMPTY.inspectcycletime( 0 );
        IInspector.EMPTY.inspectpendingtrigger( Stream.empty() );
        IInspector.EMPTY.inspectplans( Stream.empty() );
        IInspector.EMPTY.inspectrules( Stream.empty() );
        IInspector.EMPTY.inspectrunningplans( Stream.empty() );
        IInspector.EMPTY.inspectsleeping( 0 );
        IInspector.EMPTY.inspectstorage( Stream.empty() );
    }

    /**
     * test action for sleeping
     */
    private static final class CTestSleep extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -2459743760331515477L;
        /**
         * sleeping time
         */
        private final long m_time;

        /**
         * ctor
         *
         * @param p_time sleeping time
         */
        CTestSleep( @NonNegative final long p_time )
        {
            m_time = p_time;
        }

        @Nonnull
        @Override
        public IPath name()
        {
            return CPath.of( "test/sleep" );
        }

        @Nonnull
        @Override
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                               @Nonnull final List<ITerm> p_return )
        {
            try
            {
                Thread.sleep( m_time );
                return Stream.empty();
            }
            catch ( final InterruptedException l_exception )
            {
                return p_context.agent().fuzzy().membership().fail();
            }
        }
    }
}
