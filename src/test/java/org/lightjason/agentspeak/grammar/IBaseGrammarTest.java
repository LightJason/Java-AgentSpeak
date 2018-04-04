/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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

import org.junit.Assert;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
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
        Assert.assertNotEquals( IPlan.EMPTY, l_plan );
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
        Assert.assertNotEquals( IRule.EMPTY, l_rule );
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
    protected static Number fibonacci( @Nonnegative Number p_value )
    {
        return p_value.intValue() <= 2
               ? 1
               : fibonacci( p_value.intValue() - 1 ).intValue() + fibonacci( p_value.intValue() - 2 ).intValue();
    }
}
