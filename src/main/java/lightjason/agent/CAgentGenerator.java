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
import lightjason.grammar.AgentLexer;
import lightjason.grammar.AgentParser;
import lightjason.grammar.CASTErrorListener;
import lightjason.grammar.CAgentVisitor;
import lightjason.grammar.IAgentVisitor;
import lightjason.language.plan.IBodyAction;
import lightjason.language.plan.action.CRawAction;
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
public class CAgentGenerator implements IAgentGenerator
{
    /**
     * visitor to store parsed data
     */
    private final IAgentVisitor m_visitor;

    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with actions
     * @throws IOException thrown on error
     */
    @SuppressWarnings( "unchecked" )
    public CAgentGenerator( final InputStream p_stream, final Set<IAction> p_actions ) throws Exception
    {
        // run parsing with default AgentSpeak(L) visitor
        m_visitor = new CAgentVisitor( p_actions );
        parse( p_stream, m_visitor );

        m_visitor.getPlans().values().stream().forEach(

                i -> {
                    System.out.println( "------------------------------------" );
                    for ( IBodyAction l_item : i.getBodyActions() )
                    {
                        System.out.print( l_item + "\t" + l_item.getClass() );
                        if ( l_item.getClass().equals( CRawAction.class ) )
                        {
                            final CRawAction<?> l_raw = ( (CRawAction) l_item );
                            System.out.print( "\t" + l_raw.getValue().getClass() );
                        }
                        System.out.println();
                    }

                } );
        System.out.println( "------------------------------------" );
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
