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
import lightjason.grammar.IASTVisitorAgent;
import lightjason.grammar.IGenericParser;
import lightjason.language.instantiable.rule.IRule;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;


/**
 * default agent parser
 */
public final class CDefaultAgentParser extends IGenericParser<IASTVisitorAgent, AgentLexer, AgentParser>
{
    /**
     * set with actions
     */
    private final Set<IAction> m_actions;

    /**
     * ctor
     *
     * @param p_actions agent actions
     */
    public CDefaultAgentParser( final Set<IAction> p_actions )
    {
        super( new CErrorListener() );
        m_actions = p_actions;
    }

    @Override
    public final IASTVisitorAgent parse( final InputStream p_stream ) throws Exception
    {
        final IASTVisitorAgent l_visitor = new CASTVisitorAgent( m_actions, Collections.<IRule>emptySet() );
        l_visitor.visit( this.getParser( p_stream ).agent() );
        return l_visitor;
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
