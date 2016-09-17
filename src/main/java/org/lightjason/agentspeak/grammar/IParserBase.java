/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.grammar;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;


/**
 * generic default parser
 */
public abstract class IParserBase<T extends IASTVisitor, L extends Lexer, P extends Parser> implements IParser<T>
{
    /**
     * error listener
     */
    private final ANTLRErrorListener m_errorlistener;


    /**
     * ctor
     *
     * @param p_errorlistener listener instance
     */
    protected IParserBase( final ANTLRErrorListener p_errorlistener )
    {
        m_errorlistener = p_errorlistener;
    }

    /**
     * returns a parser component
     *
     * @param p_stream input stream
     * @return parser (for using in visitor interface)
     *
     * @throws IOException on io-stream errors
     * @throws NoSuchMethodException on ctor-method call
     * @throws IllegalAccessException on lexer / parser method access error
     * @throws InvocationTargetException on lexer / parser invocation error
     * @throws InstantiationException on lexer / parser instantiation error
     */
    protected final P getParser( final InputStream p_stream )
    throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        final L l_lexer = this.getLexerClass().getConstructor( CharStream.class ).newInstance( new ANTLRInputStream( p_stream ) );
        l_lexer.removeErrorListeners();
        l_lexer.addErrorListener( m_errorlistener );

        final P l_parser = this.getParserClass().getConstructor( TokenStream.class ).newInstance( new CommonTokenStream( l_lexer ) );
        l_parser.removeErrorListeners();
        l_parser.addErrorListener( m_errorlistener );

        return l_parser;
    }

    /**
     * returns the lexer class reference
     *
     * @return class of lexer
     */
    protected abstract Class<L> getLexerClass();

    /**
     * returns the parser class reference
     *
     * @return class of parser
     */
    protected abstract Class<P> getParserClass();
}
