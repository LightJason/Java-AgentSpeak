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

package org.lightjason.agentspeak.generator;

import org.lightjason.agentspeak.agent.CDefaultPlanBundle;
import org.lightjason.agentspeak.agent.IPlanBundle;
import org.lightjason.agentspeak.configuration.IPlanBundleConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;


/**
 * default plan-bundle generator
 */
public final class CDefaultPlanBundleGenerator extends IBasePlanBundleGenerator
{
    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions action generator
     * @param p_lambda lambda streaming generator
     */
    public CDefaultPlanBundleGenerator( @Nonnull final InputStream p_stream,
                                        @Nonnull final IActionGenerator p_actions,
                                        @Nonnull final ILambdaStreamingGenerator p_lambda )
    {
        super( p_stream, p_actions, p_lambda );
    }

    /**
     * ctor
     *
     * @param p_configuration plan-bundle configuration
     */
    public CDefaultPlanBundleGenerator( @Nonnull final IPlanBundleConfiguration p_configuration )
    {
        super( p_configuration );
    }

    @Nonnull
    @Override
    public IPlanBundle generatesingle( @Nullable final Object... p_data )
    {
        return new CDefaultPlanBundle( m_configuration );
    }
}
