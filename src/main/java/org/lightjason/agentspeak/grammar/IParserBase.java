/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
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
     * ctor lexer reference
     */
    private final Constructor<L> m_ctorlexer;
    /**
     * ctor parser reference
     */
    private final Constructor<P> m_ctorparser;


    /**
     * ctor
     *
     * @param p_errorlistener listener instance
     * @throws NoSuchMethodException on ctor-method call
     */
    protected IParserBase( @Nonnull final ANTLRErrorListener p_errorlistener ) throws NoSuchMethodException
    {
        m_errorlistener = p_errorlistener;
        m_ctorlexer = this.lexerclass().getConstructor( CharStream.class );
        m_ctorparser = this.parserclass().getConstructor( TokenStream.class );
    }

    /**
     * returns a parser component
     *
     * @param p_stream input stream
     * @return parser (for using in visitor interface)
     *
     * @throws IOException on io-stream errors
     * @throws IllegalAccessException on lexer / parser method access error
     * @throws InvocationTargetException on lexer / parser invocation error
     * @throws InstantiationException on lexer / parser instantiation error
     */
    protected final P parser( @Nonnull final InputStream p_stream ) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        final L l_lexer = m_ctorlexer.newInstance( CharStreams.fromStream( p_stream ) );
        l_lexer.removeErrorListeners();
        l_lexer.addErrorListener( m_errorlistener );

        final P l_parser = m_ctorparser.newInstance( new CommonTokenStream( l_lexer ) );
        l_parser.removeErrorListeners();
        l_parser.addErrorListener( m_errorlistener );

        return l_parser;
    }

    /**
     * returns the lexer class reference
     *
     * @return class of lexer
     */
    protected abstract Class<L> lexerclass();

    /**
     * returns the parser class reference
     *
     * @return class of parser
     */
    protected abstract Class<P> parserclass();
}
