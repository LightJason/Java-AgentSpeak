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

import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;

import javax.annotation.Nonnull;
import java.io.InputStream;


/**
 * default planbundle parser
 */
public final class CParserPlanBundle extends IBaseParser<IASTVisitorPlanBundle, PlanBundleLexer, PlanBundleParser>
{
    /**
     * action generator
     */
    private final IActionGenerator m_actions;
    /**
     * lambda generator
     */
    private final ILambdaStreamingGenerator m_lambda;

    /**
     * ctor
     *
     * @param p_actions action generator
     * @param p_lambda lambda generator
     */
    public CParserPlanBundle( @Nonnull final IActionGenerator p_actions, @Nonnull final ILambdaStreamingGenerator p_lambda )
    {
        super( new CErrorListener() );
        m_lambda = p_lambda;
        m_actions = p_actions;
    }

    @Nonnull
    @Override
    public IASTVisitorPlanBundle parse( final InputStream p_stream )
    {
        final IASTVisitorPlanBundle l_visitor = new CASTVisitorPlanBundle( m_actions, m_lambda );
        l_visitor.visit( this.parser( p_stream ).planbundle() );
        return l_visitor;
    }

    @Override
    protected Class<PlanBundleLexer> lexerclass()
    {
        return PlanBundleLexer.class;
    }

    @Override
    protected Class<PlanBundleParser> parserclass()
    {
        return PlanBundleParser.class;
    }
}
