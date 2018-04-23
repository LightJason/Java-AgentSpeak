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

package org.lightjason.agentspeak.agent;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.CConstant;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test agent structure.
 * If a file agentprintin.conf exists on the main directory alls print statements will be shown
 */
@RunWith( DataProviderRunner.class )
public final class TestCAgent extends IBaseTest
{
    /**
     * list with successful plans
     */
    private List<Pair<Boolean, String>> m_testlog;

    static
    {
        // disable logger
        LogManager.getLogManager().reset();
    }

    /**
     * data provider for defining asl files
     * @return triple of test-cases (asl file, number of iterations, expected log items)
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.of(
            new ImmutableTriple<>( "src/test/resources/agent/language/crypto.asl", 2, 9 ),
            new ImmutableTriple<>( "src/test/resources/agent/language/math.asl", 2, 10 ),
            new ImmutableTriple<>( "src/test/resources/agent/language/collection.asl", 2, 5 ),
            new ImmutableTriple<>( "src/test/resources/agent/language/generic.asl", 3, 27 ),
            new ImmutableTriple<>( "src/test/resources/agent/language/rules.asl", 2, 3 ),
            new ImmutableTriple<>( "src/test/resources/agent/language/trigger.asl", 3, 21 ),
            new ImmutableTriple<>( "src/test/resources/agent/language/execution.asl", 3, 4 ),
            new ImmutableTriple<>( "src/test/resources/agent/language/webservice.asl", 4, 5 )
        ).toArray();
    }

    /**
     * return list initialize
     */
    @Before
    public void initialize()
    {
        m_testlog = Collections.synchronizedList( new ArrayList<>() );
    }

    /**
     * test for default generators and configuration
     *
     * @param p_asl tripel of asl code, cycles and expected success calls
     * @throws Exception on any error
     */
    @Test
    @UseDataProvider( "generate" )
    public void testASLDefault( final Triple<String, Number, Number> p_asl ) throws Exception
    {
        try
        (
            final InputStream l_stream = new FileInputStream( p_asl.getLeft() )
        )
        {
            final IAgent<?> l_agent = new CAgentGenerator(
                l_stream,

                Stream.concat(
                    PRINTENABLE
                    ? Stream.of( new CTestResult() )
                    : Stream.of( new CTestResult(), new CEmptyPrint() ),
                    CCommon.actionsFromPackage()
                ).collect( Collectors.toSet() ),

                CCommon.lambdastreamingFromPackage().collect( Collectors.toSet() ),

                ( p_agent, p_runningcontext ) -> Stream.of(
                    new CConstant<>( "MyConstInt", 123 ),
                    new CConstant<>( "MyConstString", "here is a test string" )
                )
            ).generatesingle();

            IntStream.range( 0, p_asl.getMiddle().intValue() )
                     .forEach( i ->
                     {
                         try
                         {
                             l_agent.call();
                         }
                         catch ( final Exception l_exception )
                         {
                             Assert.fail( MessageFormat.format( "{0}: {1}", p_asl.getLeft(), l_exception.getMessage() ) );
                         }
                     } );
        }
        catch ( final Exception l_exception )
        {
            Assert.fail( MessageFormat.format( "{0}: {1}", p_asl.getLeft(), l_exception.getMessage() ) );
        }

        Assert.assertEquals(
            MessageFormat.format( "{0} {1}", "number of tests", p_asl.getLeft() ),
            p_asl.getRight().longValue(),
            m_testlog.size()
        );
        Assert.assertTrue(
            MessageFormat.format( "{0}", m_testlog.stream().filter( i -> !i.getLeft() ).map( Pair::getRight ).collect( Collectors.toList() ) ),
            m_testlog.stream().anyMatch( Pair::getLeft )
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * empty print action
     */
    private static final class CEmptyPrint extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 8344720639088993942L;

        @Nonnull
        @Override
        public IPath name()
        {
            return CPath.of( "generic/print" );
        }

        @Nonnegative
        @Override
        public int minimalArgumentNumber()
        {
            return 0;
        }

        @Nonnull
        @Override
        public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                   @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
        )
        {
            return CFuzzyValue.of( true );
        }
    }

    /**
     * test action
     */
    private final class CTestResult extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 9032624165822970132L;

        @Nonnull
        @Override
        public IPath name()
        {
            return CPath.of( "test/result" );
        }

        @Nonnegative
        @Override
        public int minimalArgumentNumber()
        {
            return 1;
        }

        @Nonnull
        @Override
        public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                             @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
        )
        {
            m_testlog.add(
                new ImmutablePair<>(
                    p_argument.get( 0 ).<Boolean>raw(),
                    p_argument.size() > 1
                    ? p_argument.get( 1 ).<String>raw()
                    : ""
                )
            );

            return CFuzzyValue.of( p_argument.get( 0 ).<Boolean>raw() );
        }
    }

}
