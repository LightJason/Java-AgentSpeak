/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agent.generator;

import lightjason.agent.CAgent;
import lightjason.agent.IAgent;
import lightjason.agent.action.IAction;
import lightjason.agent.configuration.CDefaultAgentConfiguration;
import lightjason.agent.configuration.IAgentConfiguration;
import lightjason.grammar.AgentLexer;
import lightjason.grammar.AgentParser;
import lightjason.grammar.CErrorListener;
import lightjason.grammar.CParser;
import lightjason.grammar.IParseAgent;
import lightjason.language.score.IAggregation;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * agent generator
 */
public class CDefaultAgentGenerator implements IAgentGenerator
{
    /**
     * configuration of an agent
     */
    protected final IAgentConfiguration m_configuration;

    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with actions
     * @param p_aggregation aggregation function
     * @throws IOException thrown on error
     */
    public CDefaultAgentGenerator( final InputStream p_stream, final Set<IAction> p_actions, final IAggregation p_aggregation )
    throws Exception
    {
        // run parsing with default AgentSpeak(L) visitor
        final IParseAgent l_visitor = this.parse( p_stream, new CParser( p_actions ) );

        // build configuration (configuration runs cloning of objects if needed)
        m_configuration = new CDefaultAgentConfiguration(
                p_aggregation, l_visitor.getPlans(), l_visitor.getInitialBeliefs(), l_visitor.getInitialGoal() );
    }

    @Override
    public <T> IAgent generate( final T... p_data ) throws Exception
    {
        return new CAgent( m_configuration );
    }

    @Override
    public <T> Set<IAgent> generate( final int p_number, final T... p_data )
    {
        return IntStream.range( 0, p_number ).parallel().mapToObj( i -> {
            try
            {
                return this.generate( p_data );
            }
            catch ( final Exception p_exception )
            {
                return null;
            }
        } ).filter( i -> i != null ).collect( Collectors.toSet() );
    }


    /**
     * parsing ASL code
     *
     * @param p_stream input stream
     * @param p_astvisitor AST visitor object
     * @return visitor instance
     *
     * @throws IOException thrown on IO errors
     */
    protected IParseAgent parse( final InputStream p_stream, final IParseAgent p_astvisitor ) throws Exception
    {
        final ANTLRErrorListener l_errorlistener = new CErrorListener();

        final AgentLexer l_lexer = new AgentLexer( new ANTLRInputStream( p_stream ) );
        l_lexer.removeErrorListeners();
        l_lexer.addErrorListener( l_errorlistener );

        final AgentParser l_parser = new AgentParser( new CommonTokenStream( l_lexer ) );
        l_parser.removeErrorListeners();
        l_parser.addErrorListener( l_errorlistener );

        p_astvisitor.visit( l_parser.agent() );
        return p_astvisitor;
    }

}
