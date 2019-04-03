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

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.testing.IBaseTest;
import org.lightjason.agentspeak.testing.action.CTestPrint;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;


/**
 * test hierarchical plan structure
 */
public final class TestCHierarchicalPlan extends IBaseTest
{
    /**
     * test strict plan matching
     *
     * @throws Exception on parsing error
     */
    @Test
    public void matchstrict() throws Exception
    {
        final List<Object> l_log = new ArrayList<>();
        final IAgent<?> l_agent = agent( "+!foo <- .test/log(1). +!foo/bar <- .test/log(2).", l_log );

        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foo/bar" ) ), true ).forEach( i ->
        {
        } );
        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foo" ) ), true ).forEach( i ->
        {
        } );
        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "xxx" ) ), true ).forEach( i ->
        {
        } );

        Assert.assertArrayEquals( Stream.of( 2D, 1D ).toArray(), l_log.toArray() );
    }

    /**
     * test hierarchical plan matching
     *
     * @throws Exception on parsing error
     */
    @Test
    public void matchhierarchical() throws Exception
    {
        final List<Object> l_log = new ArrayList<>();
        final IAgent<?> l_agent = agent( "+!foo <- .test/log(1). +!foo/bar <- .test/log(2).", l_log );


        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foo/xxx" ) ), true ).forEach( i ->
        {
        } );
        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foo/bar/xxx" ) ), true ).forEach( i ->
        {
        } );

        Assert.assertArrayEquals( Stream.of( 1D, 2D ).toArray(), l_log.toArray() );
    }

    /**
     * test plan on condition fail
     *
     * @throws Exception on parsing error
     */
    @Test
    public void hierarchicalfail() throws Exception
    {
        final List<Object> l_log = new ArrayList<>();
        final IAgent<?> l_agent = agent( "+!foo <- .test/log(1). +!foo/bar : false <- .test/log(2).", l_log );

        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foo/bar" ) ), true ).forEach( i ->
        {
        } );

        Assert.assertArrayEquals( Stream.of( 1D ).toArray(), l_log.toArray() );
    }

    /**
     * test plan on condition fail
     *
     * @throws Exception on parsing error
     */
    @Test
    public void multiplehierarchicalfail() throws Exception
    {
        final List<Object> l_log = new ArrayList<>();
        final IAgent<?> l_agent = agent( "+!foo <- .test/log(1). +!foo/bar : false <- .test/log(2) <- .test/log(3).", l_log );

        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foo/bar" ) ), true ).forEach( i ->
        {
        } );

        Assert.assertArrayEquals( Stream.of( 1D, 3D ).toArray(), l_log.toArray() );
    }

    /**
     * test plan on condition fail
     *
     * @throws Exception on parsing error
     */
    @Test
    public void multiplehierarchicalfailparameter() throws Exception
    {
        // needs sorting, because plan execution is run in parallel

        final List<Object> l_log = new ArrayList<>();
        final IAgent<?> l_agent = agent( "+!foo(X) <- .test/log(X). +!foo/bar(X) : X > 3 <- X *= 10; .test/log(X) : X <= 3 <- X *= 100; .test/log(X).", l_log );

        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foo/bar", CRawTerm.of( 5D ) ) ), true ).forEach( i ->
        {
        } );

        l_log.sort( Comparator.comparingInt( Object::hashCode ) );
        Assert.assertArrayEquals( Stream.of( 5D, 50D ).toArray(), l_log.toArray() );


        l_log.clear();
        l_agent.trigger( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "foo/bar", CRawTerm.of( 2D ) ) ), true ).forEach( i ->
        {
        } );

        l_log.sort( Comparator.comparingInt( Object::hashCode ) );
        Assert.assertArrayEquals( Stream.of( 2D, 200D ).toArray(), l_log.toArray() );
    }


    /**
     * generates a single agent
     *
     * @param p_asl asl
     * @param p_log log structure
     * @return agent
     * @throws Exception on parsing error
     */
    private static IAgent<?> agent( @Nonnull final String p_asl, @Nonnull final List<Object> p_log ) throws Exception
    {
        return new CAgentGenerator( p_asl,
            new CActionStaticGenerator( Stream.of( new CTestPrint( true ), new CLog( p_log ) ) ),
            ILambdaStreamingGenerator.EMPTY
        ).generatesingle();
    }


    /**
     * log action
     */
    private static final class CLog extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 4536335097194230205L;
        /**
         * log structure
         */
        private final List<Object> m_log;

        /**
         * ctor
         *
         * @param p_log log structure
         */
        CLog( @Nonnull final List<Object> p_log )
        {
            m_log = p_log;
        }


        @Nonnull
        @Override
        public IPath name()
        {
            return CPath.of( "test/log" );
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
            p_argument.stream().map( ITerm::raw ).forEach( m_log::add );
            return Stream.of();
        }
    }
}
