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

package org.lightjason.agentspeak;

import org.apache.commons.io.IOUtils;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;


/**
 * base test class with helpers
 */
public abstract class IBaseTest
{
    /**
     * enable printing of test-data
     */
    protected static final boolean PRINTENABLE = Files.exists( Paths.get( "agentprinting.conf" ) );


    /**
     * generator of empty agents
     */
    protected static final class CAgentGenerator extends IBaseAgentGenerator<IAgent<?>>
    {
        /**
         * ctor
         *
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator() throws Exception
        {
            this( "", Collections.emptySet() );
        }

        /**
         * ctor
         *
         * @param p_asl asl code
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator( @Nonnull final String p_asl ) throws Exception
        {
            this( p_asl, Collections.emptySet() );
        }

        /**
         * ctor
         *
         * @param p_asl asl code
         * @param p_action actions
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator( @Nonnull final String p_asl, @Nonnull final Collection<IAction> p_action ) throws Exception
        {
            super( IOUtils.toInputStream( p_asl, "UTF-8" ), Collections.emptySet() );
        }

        @Nullable
        @Override
        public final IAgent<?> generatesingle( @Nullable final Object... p_data )
        {
            return new CAgent( m_configuration );
        }
    }

    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<IAgent<?>>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 3961697445753327536L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CAgent( @Nonnull final IAgentConfiguration<IAgent<?>> p_configuration )
        {
            super( p_configuration );
        }
    }
}
