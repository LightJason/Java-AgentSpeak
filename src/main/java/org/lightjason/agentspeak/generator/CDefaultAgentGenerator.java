/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.generator;

import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.CAgent;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IPlanBundle;
import org.lightjason.agentspeak.agent.fuzzy.CBoolFuzzy;
import org.lightjason.agentspeak.agent.fuzzy.IFuzzy;
import org.lightjason.agentspeak.agent.unify.CUnifier;
import org.lightjason.agentspeak.beliefbase.IBeliefBaseUpdate;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.configuration.CDefaultAgentConfiguration;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.grammar.CParserAgent;
import org.lightjason.agentspeak.grammar.IASTVisitorAgent;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.action.unify.IUnifier;
import org.lightjason.agentspeak.language.score.IAggregation;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * agent generator
 */
public class CDefaultAgentGenerator<T extends IAgent<?>> implements IAgentGenerator<T>
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.getLogger( CDefaultAgentGenerator.class );
    /**
     * unification
     */
    protected static final IUnifier UNIFIER = new CUnifier();
    /**
     * fuzzy structure
     */
    protected final IFuzzy<Boolean, T> m_fuzzy = new CBoolFuzzy<>();
    /**
     * configuration of an agent
     */
    protected final IAgentConfiguration<T> m_configuration;


    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with action
     * @param p_aggregation aggregation function
     * @throws Exception thrown on error
     */
    public CDefaultAgentGenerator( final InputStream p_stream, final Set<IAction> p_actions, final IAggregation p_aggregation )
    throws Exception
    {
        this( p_stream, p_actions, p_aggregation, Collections.<IPlanBundle>emptySet(), null, IVariableBuilder.EMPTY );
    }

    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with action
     * @param p_aggregation aggregation function
     * @param p_planbundle set with planbundles
     * @param p_beliefbaseupdate beliefbase updater
     * @param p_variablebuilder variable builder (can be set to null)
     * @throws Exception thrown on error
     */
    public CDefaultAgentGenerator( final InputStream p_stream, final Set<IAction> p_actions,
                                   final IAggregation p_aggregation, final Set<IPlanBundle> p_planbundle,
                                   final IBeliefBaseUpdate<T> p_beliefbaseupdate, final IVariableBuilder p_variablebuilder
    )
    throws Exception
    {
        final IASTVisitorAgent l_visitor = new CParserAgent( p_actions ).parse( p_stream );

        // build configuration (configuration runs cloning of objects if needed)
        m_configuration = new CDefaultAgentConfiguration<>(
            m_fuzzy,

            Stream.concat(
                l_visitor.getInitialBeliefs().stream(),
                p_planbundle.parallelStream().flatMap( i -> i.getInitialBeliefs().stream() )
            ).collect( Collectors.toSet() ),

            p_beliefbaseupdate,

            Stream.concat(
                l_visitor.getPlans().stream(),
                p_planbundle.parallelStream().flatMap( i -> i.getPlans().stream() )
            ).collect( Collectors.toSet() ),

            Stream.concat(
                l_visitor.getRules().stream(),
                p_planbundle.parallelStream().flatMap( i -> i.getRules().stream() )
            ).collect( Collectors.toSet() ),

            l_visitor.getInitialGoal(),

            UNIFIER,

            p_aggregation,

            p_variablebuilder
        );
    }

    /**
     * ctor
     *
     * @param p_configuration any configuration
     */
    protected CDefaultAgentGenerator( final IAgentConfiguration<T> p_configuration )
    {
        m_configuration = p_configuration;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public T generatesingle( final Object... p_data ) throws Exception
    {
        return (T) new CAgent<>( m_configuration );
    }

    @Override
    public final Stream<T> generatemultiple( final int p_number, final Object... p_data )
    {
        return IntStream.range( 0, p_number )
                    .parallel()
                    .mapToObj( i -> {
                        try
                        {
                            return this.generatesingle( p_data );
                        }
                        catch ( final Exception l_exception )
                        {
                            LOGGER.warning( MessageFormat.format( "error with message: {0}", l_exception ) );
                            return null;
                        }
                    } )
                    .filter( i -> i != null );
    }

}
