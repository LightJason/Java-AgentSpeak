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

package org.lightjason.agentspeak.agent;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.score.IAggregation;
import org.lightjason.agentspeak.language.variable.CConstant;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test agent structure
 */
@RunWith( DataProviderRunner.class )
public final class TestCAgent extends IBaseTest
{
    /**
     * enable printing of test-data
     */
    private static final boolean PRINTENABLE = false;
    /**
     * list with successful plans
     */
    private final List<Pair<Boolean, String>> m_testlog = Collections.synchronizedList( new ArrayList<>() );

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
            new ImmutableTriple<>( "src/test/resources/agent/complete.asl", 5, 0 )
            /*
            new ImmutableTriple<>( "src/test/resources/agent/math.asl", 2, 10 ),
            new ImmutableTriple<>( "src/test/resources/agent/crypto.asl", 2, 9 ),
            new ImmutableTriple<>( "src/test/resources/agent/collection.asl", 2, 4 ),
            new ImmutableTriple<>( "src/test/resources/agent/webservice.asl", 4, 1 ),
            new ImmutableTriple<>( "src/test/resources/agent/rules.asl", 2, 4 ),
            new ImmutableTriple<>( "src/test/resources/agent/generic.asl", 2, 14 )
            */
        ).toArray();
    }


    /**
     * test for default generators and configuration
     *
     * @throws Exception on any error
     */
    @Test
    @UseDataProvider( "generate" )
    public final void testASLDefault( final Triple<String, Number, Number> p_asl ) throws Exception
    {
        try
        (
            final InputStream l_stream = new FileInputStream( p_asl.getLeft() );
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
                                          new CVariableBuilder()
                                      ).generatesingle();

            IntStream.range( 0, p_asl.getMiddle().intValue() )
                     .forEach( i -> {
                         try
                         {
                             l_agent.call();
                         }
                         catch ( final Exception l_exception )
                         {
                             Assert.assertTrue( MessageFormat.format( "{0}: {1}", p_asl.getLeft(), l_exception.getMessage() ), false );
                         }
                     } );
        }
        catch ( final Exception l_exception )
        {
            Assert.assertTrue( MessageFormat.format( "{0}: {1}", p_asl.getLeft(), l_exception.getMessage() ), false );
        }


        Assert.assertEquals(  MessageFormat.format( "{0} {1}", "number of tests", p_asl.getLeft() ), p_asl.getRight().longValue(), m_testlog.size() );
        m_testlog.stream()
                 .filter( i -> !i.getKey() )
                 .forEach( i -> Assert.assertTrue( MessageFormat.format( "{0} {1}", p_asl.getLeft(), i.getValue() ), false ) );
    }

    /**
     * manuell running test
     *
     * @param p_args arguments
     * @throws Exception on parsing exception
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new TestCAgent().invoketest();
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * empty print action
     */
    private static final class CEmptyPrint extends IBaseAction
    {

        @Override
        public final IPath name()
        {
            return CPath.from( "generic/print" );
        }

        @Override
        public final int minimalArgumentNumber()
        {
            return 0;
        }

        @Override
        public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                                   final List<ITerm> p_annotation
        )
        {
            return CFuzzyValue.from( true );
        }
    }

    /**
     * test action
     */
    private final class CTestResult extends IBaseAction
    {

        @Override
        public final IPath name()
        {
            return CPath.from( "test/result" );
        }

        @Override
        public final int minimalArgumentNumber()
        {
            return 1;
        }

        @Override
        public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                             final List<ITerm> p_annotation
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

            return CFuzzyValue.from( p_argument.get( 0 ).<Boolean>raw() );
        }
    }

    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<IAgent<?>>
    {
        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CAgent( final IAgentConfiguration<IAgent<?>> p_configuration )
        {
            super( p_configuration );
        }
    }


    /**
     * agent generator class
     */
    private static final class CAgentGenerator extends IBaseAgentGenerator<IAgent<?>>
    {

        /**
         * ctor
         *
         * @param p_stream input stream
         * @param p_actions set with action
         * @param p_variablebuilder variable builder (can be set to null)
         * @throws Exception thrown on error
         */
        CAgentGenerator( final InputStream p_stream, final Set<IAction> p_actions, final IVariableBuilder p_variablebuilder
        ) throws Exception
        {
            super( p_stream, p_actions, IAggregation.EMPTY, p_variablebuilder );
        }

        @Override
        public IAgent<?> generatesingle( final Object... p_data )
        {
            return new CAgent( m_configuration );
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
