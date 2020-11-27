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

package org.lightjason.agentspeak.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.action.binding.IAgentAction;
import org.lightjason.agentspeak.action.binding.IAgentActionName;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;


/**
 * test common structure
 */
public final class TestCCommon extends IBaseTest
{
    /**
     * test empty agent actions
     */
    @Test
    public void emptyagentaction()
    {
        Assertions.assertEquals( 0, CCommon.actionsFromAgentClass().count() );
    }

    @Test
    public void agentactionerror()
    {
        Assertions.assertEquals( 0, CCommon.actionsFromAgentClass( CWrongAgent.class ).count() );
    }

    /**
     * wrong method definition
     */
    @IAgentAction( access = IAgentAction.EAccess.WHITELIST )
    private static final class CWrongAgent extends IBaseAgent<CWrongAgent>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -2220693400826421455L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        public CWrongAgent( @Nonnull final IAgentConfiguration<CWrongAgent> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * empty name
         */
        @IAgentActionName( name = "" )
        private void emptyname()
        {
        }

        /**
         * name case error
         */
        @IAgentActionName( name = "ABCD" )
        public void caseerror()
        {
        }

        /**
         * number name
         */
        @IAgentActionName( name = "123" )
        private void numbername()
        {
        }
    }
}
