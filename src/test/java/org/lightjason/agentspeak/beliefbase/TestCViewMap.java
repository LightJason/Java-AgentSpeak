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

package org.lightjason.agentspeak.beliefbase;

import com.codepoetics.protonpack.StreamUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.beliefbase.view.CViewMap;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.beliefbase.view.IViewGenerator;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.CLambdaStreamingStaticGenerator;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.testing.IBaseTest;
import org.lightjason.agentspeak.testing.action.CTestEqual;
import org.lightjason.agentspeak.testing.action.CTestPrint;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        Stream.of( new CTestResult(), new CTestPrint( PRINTENABLE ), new CTestEqual() ),
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
    @BeforeEach
    public void initialize() throws IOException
    {
        m_testlog = Collections.synchronizedList( new ArrayList<>() );

        m_data = new HashMap<>();
        m_data.put( "val", 123 );
        m_data.put( "str", "text value" );
        m_data.put( "logic", true );

        final Map<String, Object> l_data = new HashMap<>();
        m_data.put( "obj", l_data );

        l_data.put( "name", "abcdef" );
        l_data.put( "val", 357 );
        l_data.put( "ar", Stream.of( 1, 3, 5 ).collect( Collectors.toList() ) );
    }

    /**
     * test stream
     */
    @Test
    public void stream()
    {
        Assumptions.assumeTrue( Objects.nonNull( m_data ) );

        Assertions.assertTrue(
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
        Assumptions.assumeTrue( Objects.nonNull( m_data ) );
        final IView l_view = new CViewMap( "main", m_data );

        Assertions.assertTrue( l_view.containsliteral( CPath.of( "val" ) ) );
        Assertions.assertTrue( l_view.containsliteral( CPath.of( "obj/name" ) ) );
        Assertions.assertFalse( l_view.containsliteral( CPath.of( "not/exists" ) ) );
    }

    /**
     * test contains view
     */
    @Test
    public void containsview()
    {
        Assumptions.assumeTrue( Objects.nonNull( m_data ) );
        final IView l_view = new CViewMap( "main", m_data );

        Assertions.assertFalse( l_view.containsview( CPath.of( "not/exists" ) ) );
        Assertions.assertTrue( l_view.containsview( CPath.of( "obj" ) ) );
    }

    /**
     * test add & remove literal
     */
    @Test
    public void addremoveliteral()
    {
        final Map<String, Object> l_data = new HashMap<>();

        final IView l_view = new CViewMap( "main", l_data )
            .add(
                CLiteral.of( "top", CRawTerm.of( 1 ) ),
                CLiteral.of( "top/suba", CRawTerm.of( 2 ) ),
                CLiteral.of( "top/sub/subb", CRawTerm.of( 3 ) )
            );

        Assertions.assertFalse( l_view.isempty() );

        Assertions.assertEquals( l_data.size(), l_view.size() );
        Assertions.assertEquals( 1, l_data.get( "top" ) );
        Assertions.assertEquals( 2, l_data.get( "suba" ) );
        Assertions.assertEquals( 3, l_data.get( "subb" ) );

        l_view.remove( CLiteral.of( "top/suba", CRawTerm.of( 2 ) ) );

        Assertions.assertEquals( l_data.size(), l_view.size() );
        Assertions.assertEquals( 1, l_data.get( "top" ) );
        Assertions.assertEquals( 3, l_data.get( "subb" ) );

        l_view.clear();
        Assertions.assertEquals( l_data.size(), l_view.size() );
        Assertions.assertTrue( l_view.isempty() );
    }

    /**
     * test add & remove view
     */
    @Test
    public void addremoveview()
    {
        final Map<String, Object> l_datatop = new HashMap<>();
        final Map<String, Object> l_datasuba = new HashMap<>();
        final Map<String, Object> l_datasubb = new HashMap<>();

        final IView l_suba = new CViewMap( "suba", l_datasuba );
        final IView l_view = new CViewMap( "main", l_datatop )
            .add(
                l_suba,
                new CViewMap( "subb", l_datasubb )
            );

        Assertions.assertFalse( l_view.isempty() );
        Assertions.assertEquals( 2, l_datatop.size() );
        Assertions.assertNotNull( l_datatop.get( "suba" ) );
        Assertions.assertNotNull( l_datatop.get( "subb" ) );

        l_view.remove( l_suba );

        Assertions.assertEquals( 1, l_datatop.size() );
        Assertions.assertNotNull( l_datatop.get( "subb" ) );

        l_view.clear();
        Assertions.assertTrue( l_view.isempty() );
    }

    /**
     * test empty view
     */
    @Test
    public void emptyview()
    {
        Assertions.assertEquals( 0, IView.EMPTY.walk( IPath.EMPTY ).count() );
        Assertions.assertEquals( IView.EMPTY, IView.EMPTY.generate( IViewGenerator.EMPTY ) );
        Assertions.assertEquals( 0, IView.EMPTY.root().count() );
        Assertions.assertEquals( IBeliefbase.EMPY, IView.EMPTY.beliefbase() );
        Assertions.assertEquals( IPath.EMPTY, IView.EMPTY.path() );
        Assertions.assertTrue( IView.EMPTY.name().isEmpty() );
        Assertions.assertNull( IView.EMPTY.parent() );
        Assertions.assertFalse( IView.EMPTY.hasparent() );
        Assertions.assertEquals( 0, IView.EMPTY.trigger().count() );
        Assertions.assertEquals( 0, IView.EMPTY.stream( true ).count() );
        Assertions.assertEquals( IView.EMPTY, IView.EMPTY.clear() );
        Assertions.assertEquals( IView.EMPTY, IView.EMPTY.add( Stream.empty() ) );
        Assertions.assertEquals( IView.EMPTY, IView.EMPTY.remove( Stream.empty() ) );
        Assertions.assertFalse( IView.EMPTY.containsliteral( IPath.EMPTY ) );
        Assertions.assertFalse( IView.EMPTY.containsview( IPath.EMPTY ) );
        Assertions.assertTrue( IView.EMPTY.isempty() );
        Assertions.assertEquals( 0, IView.EMPTY.size() );
        Assertions.assertEquals( 0, IView.EMPTY.stream( IPath.EMPTY ).count() );
        Assertions.assertEquals( IView.EMPTY, IView.EMPTY.add( IView.EMPTY ) );
        Assertions.assertEquals( IView.EMPTY, IView.EMPTY.add( ILiteral.EMPTY ) );
        Assertions.assertEquals( IView.EMPTY, IView.EMPTY.remove( IView.EMPTY ) );
        Assertions.assertEquals( IView.EMPTY, IView.EMPTY.remove( ILiteral.EMPTY ) );
        Assertions.assertEquals( IAgent.EMPTY, IView.EMPTY.update( IAgent.EMPTY ) );
    }

    /**
     * test in-agent definition
     *
     * @throws Exception is thrown on execute error
     */
    @Test
    public void inagent() throws Exception
    {
        Assumptions.assumeTrue( Objects.nonNull( m_data ) );

        final IAgent<?> l_agent = new CAgent.CAgentGenerator(
            "!main. +!main <- "
            + ">>map/str(X); "
            + ".test/print('string-value:', X); "
            + ".test/result( .test/equal(X, 'text value'), 'unified value incorrect' ). "
            + "-!main <- .test/result( fail, 'unification wrong').",
            m_data,
            m_actions
        ).generatesingle().call().call();
        Assertions.assertTrue(
            m_testlog.stream().anyMatch( Pair::getLeft ),
            MessageFormat.format( "{0}", m_testlog.stream().filter( i -> !i.getLeft() ).map( Pair::getRight ).collect( Collectors.toList() ) )
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


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

            @Nonnull
            @Override
            public IAgent<?> generatesingle( final Object... p_data )
            {
                return new CAgent( m_configuration, m_map );
            }
        }

    }
}
