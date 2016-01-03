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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.agent.generator;

import lightjason.agent.IPlanBundle;
import lightjason.agent.action.IAction;
import lightjason.grammar.CASTErrorListener;
import lightjason.grammar.CASTVisitor;
import lightjason.grammar.IPlanBundleVisitor;
import lightjason.grammar.PlanBundleLexer;
import lightjason.grammar.PlanBundleParser;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;


/**
 * plan bundle generator
 */
public class CDefaultPlanBundleGenerator implements IPlanBundleGenerator
{

    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with actions
     * @throws IOException thrown on error
     */
    public CDefaultPlanBundleGenerator( final InputStream p_stream, final Set<IAction> p_actions ) throws Exception
    {
        // run parsing with default AgentSpeak(L) visitor
        final IPlanBundleVisitor l_visitor = this.parse( p_stream, new CASTVisitor( p_actions ) );
    }


    @Override
    public <T> IPlanBundle generate( final T... p_data ) throws Exception
    {
        return null;
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
    protected IPlanBundleVisitor parse( final InputStream p_stream, final IPlanBundleVisitor p_astvisitor ) throws Exception
    {
        final ANTLRErrorListener l_errorlistener = new CASTErrorListener();

        final PlanBundleLexer l_lexer = new PlanBundleLexer( new ANTLRInputStream( p_stream ) );
        l_lexer.removeErrorListeners();
        l_lexer.addErrorListener( l_errorlistener );

        final PlanBundleParser l_parser = new PlanBundleParser( new CommonTokenStream( l_lexer ) );
        l_parser.removeErrorListeners();
        l_parser.addErrorListener( l_errorlistener );

        p_astvisitor.visit( l_parser.planbundle() );
        return p_astvisitor;
    }
}
