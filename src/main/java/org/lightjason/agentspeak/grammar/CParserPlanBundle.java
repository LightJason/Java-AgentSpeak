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

import org.lightjason.agentspeak.action.IAction;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.Set;


/**
 * default planbundle parser
 */
public final class CParserPlanBundle extends IBaseParser<IASTVisitorPlanBundle, PlanBundleLexer, PlanBundleParser>
{
    /**
     * set with actions
     */
    private final Set<IAction> m_actions;

    /**
     * ctor
     *
     * @param p_actions agent actions
     * @throws NoSuchMethodException on ctor-method call
     */
    public CParserPlanBundle( @Nonnull final Set<IAction> p_actions ) throws NoSuchMethodException
    {
        super( new CErrorListener() );
        m_actions = p_actions;
    }

    @Nonnull
    @Override
    public final IASTVisitorPlanBundle parse( final InputStream p_stream ) throws Exception
    {
        final IASTVisitorPlanBundle l_visitor = new CASTVisitorPlanBundle( m_actions );
        l_visitor.visit( this.parser( p_stream ).planbundle() );
        return l_visitor;
    }

    @Override
    protected final Class<PlanBundleLexer> lexerclass()
    {
        return PlanBundleLexer.class;
    }

    @Override
    protected final Class<PlanBundleParser> parserclass()
    {
        return PlanBundleParser.class;
    }
}
