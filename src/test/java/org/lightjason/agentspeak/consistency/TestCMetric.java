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

package org.lightjason.agentspeak.consistency;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.CBeliefbase;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.beliefbase.view.IViewGenerator;
import org.lightjason.agentspeak.consistency.filter.CAll;
import org.lightjason.agentspeak.consistency.filter.IFilter;
import org.lightjason.agentspeak.consistency.metric.CSymmetricDifference;
import org.lightjason.agentspeak.consistency.metric.CWeightedDifference;
import org.lightjason.agentspeak.consistency.metric.IMetric;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * metric tests
 */
public final class TestCMetric extends IBaseTest
{
    /**
     * agent generator
     */
    private CAgentGenerator m_agentgenerator;
    /**
     * literal view generator
     */
    private IViewGenerator m_generator;
    /**
     * set with testing literals
     */
    private Set<ILiteral> m_literals;

    /**
     * test initialize
     *
     * @throws Exception on any parsing error
     */
    @Before
    public void initialize() throws Exception
    {
        m_generator = new CGenerator();
        m_agentgenerator = new CAgentGenerator();

        m_literals = Stream.of(
            CLiteral.of( "toplevel" ),
            CLiteral.of( "first/sub1" ),
            CLiteral.of( "first/sub2" ),
            CLiteral.of( "second/sub3" ),
            CLiteral.of( "second/sub4" ),
            CLiteral.of( "second/sub/sub5" )
        ).collect( Collectors.toSet() );
    }



    /**
     * test symmetric weight metric equality
     */
    @Test
    public void symmetricweightequality()
    {
        Assume.assumeNotNull( m_literals );
        Assume.assumeFalse( "testing literals are empty", m_literals.isEmpty() );
        this.check(
            "symmetric difference equality",
            new CAll(), new CSymmetricDifference(),
            m_literals,
            m_literals,
            0, 0
        );
    }


    /**
     * test symmetric weight metric inequality
     */
    @Test
    public void symmetricweightinequality()
    {
        Assume.assumeNotNull( m_literals );
        Assume.assumeFalse( "testing literals are empty", m_literals.isEmpty() );
        this.check(
            "symmetric difference inequality",
            new CAll(), new CSymmetricDifference(),
            m_literals,
            Stream.concat( m_literals.stream(), Stream.of( CLiteral.of( "diff" ) ) ).collect( Collectors.toSet() ),
            1, 0
        );
    }


    /**
     * test symmetric metric equality
     */
    @Test
    public void weightequality()
    {
        Assume.assumeNotNull( m_literals );
        Assume.assumeFalse( "testing literals are empty", m_literals.isEmpty() );

        this.check(
            "weight difference equality",
            new CAll(), new CWeightedDifference(),
            m_literals,
            m_literals,
            24, 0
        );
    }


    /**
     * test symmetric metric equality
     */
    @Test
    public void weightinequality()
    {
        Assume.assumeNotNull( m_literals );
        Assume.assumeFalse( "testing literals are empty", m_literals.isEmpty() );

        this.check(
            "weight difference inequality",
            new CAll(),
            new CWeightedDifference(),
            m_literals,
            Stream.concat( m_literals.stream(), Stream.of( CLiteral.of( "diff" ) ) ).collect( Collectors.toSet() ),
            28 + 1.0 / 6, 0
        );
    }


    /**
     * runs the check
     *
     * @param p_message error / successful message
     * @param p_filter agent filter
     * @param p_metric metric value
     * @param p_belief1 belief set 1
     * @param p_belief2 belief set 2
     * @param p_excepted expected value
     * @param p_delta delta
     */
    private void check( final String p_message, final IFilter p_filter, final IMetric p_metric, final Collection<ILiteral> p_belief1,
                        final Collection<ILiteral> p_belief2, final double p_excepted, final double p_delta )
    {
        final double l_value = p_metric.apply(
            p_filter.apply( this.agent( p_belief1 ) ),
            p_filter.apply( this.agent( p_belief2 ) )
        );
        Assert.assertEquals( p_message, p_excepted, l_value, p_delta );
    }


    /**
     * generates an agent
     *
     * @param p_literals literal collection
     * @return agent
     */
    private IAgent<?> agent( final Collection<ILiteral> p_literals )
    {
        Assume.assumeNotNull( m_generator );

        final IAgent<?> l_agent = m_agentgenerator.generatesingle();
        p_literals.forEach( i -> l_agent.beliefbase().generate( m_generator, i.functorpath() ).add( i ) );
        return l_agent;
    }


    /**
     * test belief generator
     */
    private static final class CGenerator implements IViewGenerator
    {
        @Override
        public IView apply( final String p_name, final IView p_parent )
        {
            return new CBeliefbase( new CMultiStorage<>() ).create( p_name, p_parent );
        }
    }

}
