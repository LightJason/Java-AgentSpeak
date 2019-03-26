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

package org.lightjason.agentspeak.beliefbase;

import com.codepoetics.protonpack.StreamUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.beliefbase.view.CViewMap;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.CLambdaStreamingStaticGenerator;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *test of map-view
 */
public final class TestCViewMap extends IBaseTest
{
    /**
     * actions
     */
    private final Set<IAction> m_actions = Stream.concat(
        PRINTENABLE
        ? Stream.of( new CTestResult() )
        : Stream.of( new CTestResult(), new CEmptyPrint() ),
        CCommon.actionsFromPackage()
    ).collect( Collectors.toSet() );
    /**
     * map reference
     */
    private Map<String, Object> m_data;
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
     * initialize
     *
     * @throws IOException is thrown on parsing error
     */
    @Before
    @SuppressWarnings( "unchecked" )
    public void initialize() throws IOException
    {
        m_testlog = Collections.synchronizedList( new ArrayList<>() );
        m_data = new ObjectMapper().readValue(
            "{ \"val\" : 123, \"str\" : \"text value\", \"logic\" : true, \"obj\" : { \"name\" : \"abcdef\", \"val\" : 357 }, \"ar\" : [1, 3, 5] }",
            Map.class
        );
    }

    /**
     * test stream
     */
    @Test
    public void stream()
    {
        Assume.assumeNotNull( m_data );

        Assert.assertTrue(
            StreamUtils.zip(
                new CViewMap( "main", m_data ).stream().limit( m_data.size() - 2 ),
                Stream.of(
                    CLiteral.of( "val", CRawTerm.of( 123L ) ),
                    CLiteral.of( "str", CRawTerm.of( "text value" ) ),
                    CLiteral.of( "logic", CRawTerm.of( true ) ),
                    CLiteral.of( "obj/name", CRawTerm.of( "abcdef" ) ),
                    CLiteral.of( "obj/val", CRawTerm.of( 357L ) )
                ),
                Object::equals
            ).allMatch( i -> i )
        );
    }

    /**
     * test contains literal
     */
    @Test
    public void containsliteral()
    {
        Assume.assumeNotNull( m_data );
        final IView l_view = new CViewMap( "main", m_data );

        Assert.assertTrue( l_view.containsLiteral( CPath.of( "val" ) ) );
        Assert.assertTrue( l_view.containsLiteral( CPath.of( "obj/name" ) ) );
        Assert.assertFalse( l_view.containsLiteral( CPath.of( "not/exists" ) ) );
    }

    /**
     * test contains view
     */
    @Test
    public void containsview()
    {
        Assume.assumeNotNull( m_data );
        final IView l_view = new CViewMap( "main", m_data );

        Assert.assertFalse( l_view.containsView( CPath.of( "not/exists" ) ) );
        Assert.assertTrue( l_view.containsView( CPath.of( "obj" ) ) );
    }

    /**
     * test in-agent definition
     *
     * @throws Exception is thrown on execute error
     * @todo must be enabled with bool-action
     */
    @Ignore
    @Test
    public void inagent() throws Exception
    {
        Assume.assumeNotNull( m_data );

        final IAgent<?> l_agent = new CAgent.CAgentGenerator(
            "!main. +!main <- "
            + ">>map/str(X); "
            + ".generic/print('string-value:', X); "
            + ".test/result( .bool/equal(X, 'text value'), 'unified value incorrect' ). "
            + "-!main <- .test/result( fail, 'unification wrong').",
            m_data,
            m_actions
        ).generatesingle().call().call();
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
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
        )
        {
            return Stream.of();
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
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            m_testlog.add(
                new ImmutablePair<>(
                    p_argument.get( 0 ).<Boolean>raw(),
                    p_argument.size() > 1
                    ? p_argument.get( 1 ).raw()
                    : ""
                )
            );

            return p_argument.get( 0 ).<Boolean>raw()
                   ? p_context.agent().fuzzy().membership().success()
                   : p_context.agent().fuzzy().membership().fail();
        }
    }

    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<IAgent<?>>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -2312863050588218178L;


        /**
         * ctor
         *
         * @param p_configuration agent configuration
         * @param p_map belief map
         */
        private CAgent( final IAgentConfiguration<IAgent<?>> p_configuration, final Map<String, Object> p_map )
        {
            super( p_configuration );
            m_beliefbase.add( new CViewMap( "map", p_map, m_beliefbase ) );
        }

        /**
         * agent generator class
         */
        private static final class CAgentGenerator extends IBaseAgentGenerator<IAgent<?>>
        {
            /**
             * belief map
             */
            private final Map<String, Object> m_map;

            /**
             * ctor
             *
             * @param p_asl asl string code
             * @param p_map belief map
             * @param p_actions actions
             * @throws Exception thrown on error
             */
            CAgentGenerator( @Nonnull final String p_asl, @Nonnull final Map<String, Object> p_map, @Nonnull final Collection<IAction> p_actions ) throws Exception
            {
                super(
                    IOUtils.toInputStream( p_asl, "UTF-8" ),
                    new CActionStaticGenerator( p_actions ),
                    new CLambdaStreamingStaticGenerator( CCommon.lambdastreamingFromPackage() )
                );
                m_map = p_map;
            }

            @Override
            public IAgent<?> generatesingle( final Object... p_data )
            {
                return new CAgent( m_configuration, m_map );
            }
        }

    }
}
