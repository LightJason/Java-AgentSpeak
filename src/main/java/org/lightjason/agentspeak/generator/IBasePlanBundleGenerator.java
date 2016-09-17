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

package org.lightjason.agentspeak.generator;

import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IPlanBundle;
import org.lightjason.agentspeak.configuration.CDefaultPlanBundleConfiguration;
import org.lightjason.agentspeak.configuration.IPlanBundleConfiguration;
import org.lightjason.agentspeak.grammar.CParserPlanBundle;
import org.lightjason.agentspeak.grammar.IASTVisitorPlanBundle;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * plan bundle generator
 */
public abstract class IBasePlanBundleGenerator implements IPlanBundleGenerator
{
    /**
     * configuration
     */
    protected final IPlanBundleConfiguration m_configuration;


    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with actions
     * @throws Exception thrown on error
     */
    public IBasePlanBundleGenerator( final InputStream p_stream, final Set<IAction> p_actions ) throws Exception
    {
        final IASTVisitorPlanBundle l_visitor = new CParserPlanBundle( p_actions ).parse( p_stream );

        m_configuration = new CDefaultPlanBundleConfiguration(
            l_visitor.plans(),
            l_visitor.rules(),
            l_visitor.initialbeliefs()
        );
    }

    /**
     * ctor
     *
     * @param p_configuration configuration
     */
    protected IBasePlanBundleGenerator( final IPlanBundleConfiguration p_configuration )
    {
        m_configuration = p_configuration;
    }

    @Override
    public final Stream<IPlanBundle> generatemultiple( final int p_number, final Object... p_data )
    {
        return IntStream.range( 0, p_number )
                    .parallel()
                    .mapToObj( i -> this.generatesingle( p_data ) );
    }

}
