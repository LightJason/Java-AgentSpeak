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
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;


/**
 * test agent method calls
 */
public final class TestCAgentMethods extends IBaseTest
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

        Assert.assertTrue( l_agent.toString().startsWith( "org.lightjason.agentspeak.testing.IBaseTest$CAgent@" ) );
        Assert.assertTrue( l_agent.toString().contains( " ( Trigger: [] / Running Plans: [] / Beliefbase: beliefbase" ) );
    }

    /**
     * test trigger variable error
     *
     * @throws IOException parsing error
     */
    @Test( expected = IllegalArgumentException.class )
    public void triggervariableerror() throws IOException
    {
        final IAgent<?> l_agent = new CAgentGenerator().generatesingle();
        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.parse( "foo(X)" ) ) );
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

        Assert.assertTrue( agentcycle( l_agent ) );
        Assert.assertTrue( l_agent.cycletime() / 1000000 >= l_sleepingtime );
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
                return Stream.of();
            }
            catch ( final InterruptedException l_exception )
            {
                return p_context.agent().fuzzy().membership().fail();
            }
        }
    }
}
