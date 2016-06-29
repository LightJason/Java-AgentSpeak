/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
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

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;


/**
 * AntLR error lister to catch parser and lexer errors
 * with individual error handling and language translating
 */
public final class CErrorListener implements ANTLRErrorListener
{
    @Override
    public void syntaxError( final Recognizer<?, ?> p_recognizer, final Object p_symbol, final int p_line, final int p_charposition,
                             final String p_message,
                             final RecognitionException p_exception
    )
    {
        throw new CSyntaxErrorException( CCommon.languagestring( this, "syntax", p_line, p_charposition ) );
    }

    @Override
    public void reportAmbiguity( final Parser p_parser, final DFA p_dfa, final int p_startindex, final int p_stopindex, final boolean p_exact,
                                 final BitSet p_alternatives,
                                 final ATNConfigSet p_configuration
    )
    {

    }

    @Override
    public void reportAttemptingFullContext( final Parser p_parser, final DFA p_dfa, final int p_startindex, final int p_stopindex,
                                             final BitSet p_bitSet,
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
