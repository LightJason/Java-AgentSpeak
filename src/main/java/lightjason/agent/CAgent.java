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

import lightjason.agent.score.IAgentPlanScore;
import lightjason.beliefbase.IBeliefBase;
import lightjason.error.CSyntaxErrorException;
import lightjason.grammar.AgentLexer;
import lightjason.grammar.CAgentVisitor;
import lightjason.grammar.IAgentVisitor;
import lightjason.language.event.IEvent;
import lightjason.language.plan.IPlan;
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
import java.text.MessageFormat;
import java.util.BitSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * agent class
 */
public class CAgent implements IAgent
{
    /**
     * agent name
     */
    protected final String m_name;
    /**
     * thread-safe map with all existing plans
     *
     * @note plan list must be a linked-hashset
     * to store the execution order of the plans
     */
    protected final Map<String, Set<IPlan>> m_plans = new ConcurrentHashMap<>();
    /**
     * beliefbase of the agent
     */
    protected final IBeliefBase m_beliefbase;
    /**
     * curent agent cycle
     */
    protected int m_cycle = 0;
    /**
     * score sum of the actions
     */
    protected final IAgentPlanScore m_score;
    /**
     * suspending state
     */
    private volatile boolean m_suspend = false;


    /**
     * ctor
     *
     * @param p_stream input stream with agent-speak source
     * @param p_action map with all usable actions
     * @throws IOException is throwing on parsing error
     */
    public CAgent( final InputStream p_stream, final Map<String, IAction> p_action ) throws IOException
    {
        this( p_stream, p_action, null, null, new CAgentVisitor() );
    }

    /**
     * ctor
     *
     * @param p_stream input stream with agent-speak source
     * @param p_action map with all usable actions
     * @param p_beliefbase beliefbase
     * @throws IOException is throwing on parsing error
     */
    public CAgent( final InputStream p_stream, final Map<String, IAction> p_action, final IBeliefBase p_beliefbase ) throws IOException
    {
        this( p_stream, p_action, p_beliefbase, null, new CAgentVisitor() );
    }

    /**
     * ctor
     *
     * @param p_stream input stream with agent-speak source
     * @param p_action map with all usable actions
     * @param p_name agent name
     * @throws IOException is throwing on parsing error
     */
    public CAgent( final InputStream p_stream, final Map<String, IAction> p_action, final String p_name ) throws IOException
    {
        this( p_stream, p_action, null, p_name, new CAgentVisitor() );
    }

    /**
     * ctor
     *
     * @param p_stream input stream with agent-speak source
     * @param p_action map with all usable actions
     * @param p_beliefbase beliefbase
     * @param p_name agent name
     * @param p_astvisitor visitor object of the AST
     * @throws IOException is throwing on parsing error
     * @bug remove test plan execution
     */
    public CAgent( final InputStream p_stream, final Map<String, IAction> p_action, final IBeliefBase p_beliefbase, final String p_name,
            final IAgentVisitor p_astvisitor
    ) throws IOException
    {
        // initialize agent
        m_beliefbase = p_beliefbase == null ? null : p_beliefbase;
        m_name = ( p_name == null ) || ( p_name.isEmpty() ) ? super.toString() : p_name;
        m_score = null;

        parse( p_stream, p_astvisitor );

        //System.out.println( p_astvisitor.getInitialGoal() );
        //System.out.println( p_astvisitor.getInitialBeliefs() );
        //System.out.println( p_astvisitor.getPlans() );

        p_astvisitor.getPlans().values().stream().forEach( i -> {
            System.out.println( i );
            //System.out.println( i.evaluate( null, null ) );
        } );

    }

    @Override
    public final int getCycle()
    {
        return m_cycle;
    }

    @Override
    public final String getName()
    {
        return m_name;
    }

    @Override
    public IBeliefBase getBeliefBase()
    {
        return m_beliefbase;
    }

    @Override
    public void trigger( final IEvent<?> p_event )
    {

    }

    @Override
    public final synchronized void suspend()
    {
        m_suspend = true;
    }

    @Override
    public final boolean isSuspending()
    {
        return m_suspend;
    }

    @Override
    public final synchronized void resume()
    {
        m_suspend = false;
    }

    @Override
    public Set<IPlan> getCurrentPlans()
    {
        return null;
    }

    @Override
    public IAgent clone( final IBeliefBase p_beliefbase )
    {
        return null;
    }

    @Override
    public IAgent clone()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0} ( Cycle: {1} / Beliefbase: {2} / Plans: {3} )", m_name, m_cycle, m_beliefbase, m_plans );
    }

    @Override
    public IAgent call() throws Exception
    {
        // run beliefbase update, because environment can be changed
        m_beliefbase.update();
        if ( m_suspend )
            return this;

        // increment cycle
        m_cycle++;

        return this;
    }

    /**
     * parsing ASL code
     *
     * @param p_stream input stream
     * @param p_astvisitor AST visitor object
     * @throws IOException thrown on IO errors
     */
    private static void parse( final InputStream p_stream, final IAgentVisitor p_astvisitor ) throws IOException
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
