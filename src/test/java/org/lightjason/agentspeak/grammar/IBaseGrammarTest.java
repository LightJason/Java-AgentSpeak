/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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
import org.junit.jupiter.api.Assertions;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.stream.Stream;


/**
 * abstract class for grammar tests
 */
public abstract class IBaseGrammarTest extends IBaseTest
{

    /**
     * parse a single plan
     *
     * @param p_parser parser
     * @param p_source source
     * @tparam T AST visitor type
     * @return single plan
     * @throws Exception is thrown on any parser error
     */
    @Nonnull
    protected static <T extends IASTVisitorAgentSpeak> IPlan parsesingleplan( @Nonnull final IParser<T> p_parser,
                                                                              @Nonnull final String p_source ) throws Exception
    {
        final IPlan l_plan = parsemultipleplans( p_parser, p_source ).findFirst().orElse( IPlan.EMPTY );
        Assertions.assertNotEquals( IPlan.EMPTY, l_plan );
        return l_plan;
    }

    /**
     * parse all plans
     *
     * @param p_parser parser
     * @param p_source source
     * @tparam T AST visitor type
     * @return plan stream
     * @throws Exception is thrown on any parser error
     */
    @Nonnull
    protected static <T extends IASTVisitorAgentSpeak> Stream<IPlan> parsemultipleplans( @Nonnull final IParser<T> p_parser,
                                                                                         @Nonnull final String p_source ) throws Exception
    {
        return parse( p_parser, p_source ).plans().stream();
    }

    /**
     * parse a single rule
     *
     * @param p_parser parser
     * @param p_source source
     * @tparam T AST visitor type
     * @return single plan
     * @throws Exception is thrown on any parser error
     */
    @Nonnull
    protected static <T extends IASTVisitorAgentSpeak> IRule parsesinglerule( @Nonnull final IParser<T> p_parser,
                                                                              @Nonnull final String p_source ) throws Exception
    {
        final IRule l_rule = parsemultiplerules( p_parser, p_source ).findFirst().orElse( IRule.EMPTY );
        Assertions.assertNotEquals( IRule.EMPTY, l_rule );
        return l_rule;
    }

    /**
     * parse all rules
     *
     * @param p_parser parser
     * @param p_source source
     * @tparam T AST visitor type
     * @return rule stream
     * @throws Exception is thrown on any parser error
     */
    @Nonnull
    protected static <T extends IASTVisitorAgentSpeak> Stream<IRule> parsemultiplerules( @Nonnull final IParser<T> p_parser,
                                                                                         @Nonnull final String p_source ) throws Exception
    {
        return parse( p_parser, p_source ).rules().stream();
    }

    /**
     * parse source
     *
     * @param p_parser parser
     * @param p_source source
     * @tparam T ASR visitor type
     * @return visitor
     * @throws Exception is thrown on any parser error
     */
    private static <T extends IASTVisitorAgentSpeak> T parse( @Nonnull final IParser<T> p_parser,
                                                              @Nonnull final String p_source ) throws Exception
    {
        return p_parser.parse( streamfromstring(  p_source ) );
    }

    /**
     * calculate fibonacci sequence
     *
     * @param p_value value
     * @return result value
     */
    protected static Number fibonacci( @Nonnegative final Number p_value )
    {
        return p_value.intValue() <= 2
               ? 1
               : fibonacci( p_value.intValue() - 1 ).intValue() + fibonacci( p_value.intValue() - 2 ).intValue();
    }

    /**
     * ackermann
     *
     * @param p_nvalue n-value
     * @param p_mvalue m-value
     * @return ackermann function value
     */
    protected static Number ackermann( @Nonnegative final Number p_nvalue, @Nonnegative final Number p_mvalue )
    {
        return p_nvalue.intValue() == 0
               ? p_mvalue.intValue() + 1
               : p_mvalue.intValue() == 0
                 ? ackermann( p_nvalue.intValue() - 1, 1 )
                 : ackermann( p_nvalue.intValue() - 1, ackermann( p_nvalue, p_mvalue.intValue() - 1 ) );
    }


    /**
     * generic default parser
     */
    protected abstract static class ITestParser<T extends IASTVisitor, L extends Lexer, P extends Parser>
    {
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
         */
        public ITestParser()
        {

            try
            {
                m_ctorlexer = this.lexerclass().getConstructor( CharStream.class );
                m_ctorparser = this.parserclass().getConstructor( TokenStream.class );
            }
            catch ( final NoSuchMethodException l_exception )
            {
                throw new RuntimeException( l_exception );
            }
        }

        /**
         * returns a parser component
         *
         * @param p_content content string
         * @return parser (for using in visitor interface)
         */
        public final P parser( @Nonnull final String p_content )
        {
            return this.parser( new ByteArrayInputStream( p_content.getBytes( Charset.forName( "UTF-8" ) ) ) );
        }

        /**
         * returns a parser component
         *
         * @param p_stream input stream
         * @return parser (for using in visitor interface)
         */
        public final P parser( @Nonnull final InputStream p_stream )
        {
            final ANTLRErrorListener l_errorlistener = new CErrorListener();

            try
            {
                final L l_lexer = m_ctorlexer.newInstance( CharStreams.fromStream( p_stream ) );
                l_lexer.removeErrorListeners();
                l_lexer.addErrorListener( l_errorlistener );

                final P l_parser = m_ctorparser.newInstance( new CommonTokenStream( l_lexer ) );
                l_parser.removeErrorListeners();
                l_parser.addErrorListener( l_errorlistener );

                return l_parser;
            }
            catch ( final IllegalAccessException | InstantiationException | InvocationTargetException | IOException l_exception )
            {
                throw new RuntimeException( l_exception );
            }
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
}
