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

import lightjason.agent.action.IAction;
import lightjason.grammar.AgentLexer;
import lightjason.grammar.AgentParser;
import lightjason.grammar.CASTVisitorAgent;
import lightjason.grammar.CErrorListener;
import lightjason.grammar.IGenericParser;
import org.antlr.v4.runtime.ANTLRErrorListener;

import java.io.InputStream;
import java.util.Set;


/**
 * default agent and planbundle parser
 */
public final class CDefaultParser extends IGenericParser<CASTVisitorAgent, AgentLexer, AgentParser>
{

    /**
     * ctor
     *
     * @param p_actions agent actions
     */
    public CDefaultParser( final Set<IAction> p_actions )
    {
        this( new CASTVisitorAgent( p_actions ), new CErrorListener() );
    }

    /**
     * ctor
     *
     * @param p_visitor visitor instance
     * @param p_errorlistener listener instance
     */
    protected CDefaultParser( final CASTVisitorAgent p_visitor, final ANTLRErrorListener p_errorlistener )
    {
        super( p_visitor, p_errorlistener );
    }

    @Override
    public final CASTVisitorAgent parse( final InputStream p_stream ) throws Exception
    {
        m_visitor.visit( this.getParser( p_stream ).agent() );
        return m_visitor;
    }

    @Override
    protected final Class<AgentLexer> getLexerClass()
    {
        return AgentLexer.class;
    }

    @Override
    protected final Class<AgentParser> getParserClass()
    {
        return AgentParser.class;
    }

}