/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.agent.generator;

import lightjason.agent.CAgent;
import lightjason.agent.IAgent;
import lightjason.agent.action.IAction;
import lightjason.agent.configuration.CDefaultConfiguration;
import lightjason.agent.configuration.IConfiguration;
import lightjason.grammar.AgentLexer;
import lightjason.grammar.AgentParser;
import lightjason.grammar.CASTErrorListener;
import lightjason.grammar.CASTVisitor;
import lightjason.grammar.IAgentVisitor;
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
public class CDefaultGenerator implements IGenerator
{
    /**
     * configuration of an agent
     */
    protected final IConfiguration m_configuration;

    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with actions
     * @throws IOException thrown on error
     */
    public CDefaultGenerator( final InputStream p_stream, final Set<IAction> p_actions ) throws Exception
    {
        // run parsing with default AgentSpeak(L) visitor
        final IAgentVisitor l_visitor = new CASTVisitor( p_actions );
        parse( p_stream, l_visitor );

        // build configuration (configuration runs cloning of objects if needed)
        m_configuration = new CDefaultConfiguration( l_visitor.getPlans(), l_visitor.getInitialGoal(), l_visitor.getInitialBeliefs() );
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
            catch ( final Exception l_exception )
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
     * @throws IOException thrown on IO errors
     */
    protected static void parse( final InputStream p_stream, final IAgentVisitor p_astvisitor ) throws Exception
    {
        final ANTLRErrorListener l_errorlistener = new CASTErrorListener();

        final AgentLexer l_lexer = new AgentLexer( new ANTLRInputStream( p_stream ) );
        l_lexer.removeErrorListeners();
        l_lexer.addErrorListener( l_errorlistener );

        final AgentParser l_parser = new AgentParser( new CommonTokenStream( l_lexer ) );
        l_parser.removeErrorListeners();
        l_parser.addErrorListener( l_errorlistener );

        p_astvisitor.visit( l_parser.agent() );
    }

}
