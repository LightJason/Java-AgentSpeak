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

package lightjason.agent;

import lightjason.common.CPath;
import lightjason.error.CSyntaxErrorException;
import lightjason.grammar.AgentLexer;
import lightjason.grammar.CAgentVisitor;
import lightjason.grammar.IAgentVisitor;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * agent generator
 */
public class CAgentGenerator implements IAgentGenerator
{
    /**
     * visitor to store parsed data
     */
    private final IAgentVisitor m_visitor;
    /**
     * actions
     */
    private final Map<CPath, IAction> m_actions;

    /**
     * ctor
     *
     * @param p_stream input stream
     * @throws IOException thrown on error
     */
    public CAgentGenerator( final InputStream p_stream, final Map<CPath, IAction> p_actions ) throws IOException
    {
        m_visitor = new CAgentVisitor();
        m_actions = p_actions;
        parse( p_stream, m_visitor );

        // replace all literals within the plans with actions
        m_visitor.getPlans().values().parallelStream().forEach(
                i -> {

                    i.getBodyActions()

                } );
    }

    @Override
    public <T> IAgent generate( final T... p_data ) throws IOException
    {
        return new CAgent( new CAgentConfiguration( CPath.createPath( "agent" ), m_visitor.getPlans() ) );
    }

    @Override
    public <T> Set<IAgent> generate( final int p_number, final T... p_data )
    {
        return IntStream.range( 0, p_number ).parallel().mapToObj( i -> {
            try
            {
                return this.generate( p_data );
            }
            catch ( final IOException l_exception )
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
    protected static void parse( final InputStream p_stream, final IAgentVisitor p_astvisitor ) throws IOException
    {
        final AgentLexer l_lexer = new lightjason.grammar.AgentLexer( new ANTLRInputStream( p_stream ) );
        l_lexer.removeErrorListeners();
        l_lexer.addErrorListener(
                new ANTLRErrorListener()
                {
                    @Override
                    public void syntaxError( final Recognizer<?, ?> p_recognizer, final Object p_symbol, final int p_line, final int p_charposition,
                            final String p_message,
                            final RecognitionException p_exception
                    )
                    {
                        throw new CSyntaxErrorException( p_message, p_exception );
                    }

                    @Override
                    public void reportAmbiguity( final Parser p_parser, final DFA p_dfa, final int p_startindex, final int p_stopindex, final boolean p_exact,
                            final BitSet p_alternatives,
                            final ATNConfigSet p_configuration
                    )
                    {

                    }

                    @Override
                    public void reportAttemptingFullContext( final Parser p_parser, final DFA p_dfa, final int p_i, final int p_i1, final BitSet p_bitSet,
                            final ATNConfigSet p_configuration
                    )
                    {

                    }

                    @Override
                    public void reportContextSensitivity( final Parser p_parser, final DFA p_dfa, final int p_startindex, final int p_stopindex,
                            final int p_prediction,
                            final ATNConfigSet p_configuration
                    )
                    {

                    }
                }
        );
        p_astvisitor.visit( new lightjason.grammar.AgentParser( new CommonTokenStream( l_lexer ) ).agent() );
    }
}
