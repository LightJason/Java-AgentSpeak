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

package org.lightjason.agentspeak.action.buildin;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.binding.IAgentAction;
import org.lightjason.agentspeak.action.binding.IAgentActionFilter;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;

import java.util.stream.Stream;


/**
 * test action binding
 */
public final class TestCActionBinding extends IBaseTest
{

    /**
     * test class binding
     */
    @Test
    public final void classbinding()
    {
        Assert.assertArrayEquals(

            CCommon.actionsFromAgentClass(
                CClassBindingDefault.class,
                CClassBindingBlacklist.class,
                CClassBindingWhitelist.class
            ).map( i -> i.name().getPath() ).toArray(),

            Stream.of( "methodwhitelist" ).toArray()
        );
    }


    /**
     * test method default binding
     */
    @Test
    public final void methoddefault()
    {
        Assert.assertArrayEquals(
            CCommon.actionsFromAgentClass( CMethodBindingDefault.class ).map( i -> i.name().getPath() ).toArray(),
            Stream.of( "methodannotate" ).toArray()
        );
    }


    /**
     * test method blacklist binding
     */
    @Test
    public final void methodblacklist()
    {
        Assert.assertArrayEquals(
            CCommon.actionsFromAgentClass( CMethodBindingBlacklist.class ).map( i -> i.name().getPath() ).toArray(),
            Stream.of( "methodannotate" ).toArray()
        );
    }


    /**
     * test method whitelist binding
     */
    @Test
    public final void methodwhitelist()
    {
        Assert.assertArrayEquals(
            CCommon.actionsFromAgentClass( CMethodBindingWhitelist.class ).map( i -> i.name().getPath() ).toArray(),
            Stream.of( "methodnotannotate" ).toArray()
        );
    }


    /**
     * main test call
     *
     * @param p_args command line arguments
     *
     * @throws Exception on any error
     */
    public static void main( final String[] p_args ) throws Exception
    {
        new TestCActionBinding().invoketest();
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * test class with default binding
     */
    @IAgentAction
    private static final class CClassBindingDefault extends IBaseAgent<CClassBindingDefault>
    {
        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        public CClassBindingDefault(
            final IAgentConfiguration<CClassBindingDefault> p_configuration
        )
        {
            super( p_configuration );
        }

        /**
         * test binding method
         */
        private void methoddefault()
        {
        }
    }

    /**
     * test class with blacklist binding
     */
    @IAgentAction( access = IAgentAction.EAccess.BLACKLIST )
    private static final class CClassBindingBlacklist extends IBaseAgent<CClassBindingBlacklist>
    {
        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        public CClassBindingBlacklist(
            final IAgentConfiguration<CClassBindingBlacklist> p_configuration
        )
        {
            super( p_configuration );
        }

        /**
         * test binding method
         */
        private void methodblacklist()
        {
        }
    }

    /**
     * test class with whitelist binding
     */
    @IAgentAction( access = IAgentAction.EAccess.WHITELIST )
    private static final class CClassBindingWhitelist extends IBaseAgent<CClassBindingWhitelist>
    {
        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        public CClassBindingWhitelist(
            final IAgentConfiguration<CClassBindingWhitelist> p_configuration
        )
        {
            super( p_configuration );
        }

        /**
         * test binding method
         */
        private void methodwhitelist()
        {
        }
    }


    /**
     * test class with default binding
     */
    @IAgentAction
    private static final class CMethodBindingDefault extends IBaseAgent<CMethodBindingDefault>
    {

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        public CMethodBindingDefault(
            final IAgentConfiguration<CMethodBindingDefault> p_configuration
        )
        {
            super( p_configuration );
        }

        /**
         * test binding method with annotation
         */
        @IAgentActionFilter
        private void methodannotate()
        {
        }

        /**
         * test binding method without annotation
         */
        private void methodnotannotate()
        {
        }
    }

    /**
     * test class with blacklist binding
     */
    @IAgentAction( access = IAgentAction.EAccess.BLACKLIST )
    private static final class CMethodBindingBlacklist extends IBaseAgent<CMethodBindingBlacklist>
    {

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        public CMethodBindingBlacklist(
            final IAgentConfiguration<CMethodBindingBlacklist> p_configuration
        )
        {
            super( p_configuration );
        }

        /**
         * test binding method with annotation
         */
        @IAgentActionFilter
        private void methodannotate()
        {
        }

        /**
         * test binding method without annotation
         */
        private void methodnotannotate()
        {
        }
    }


    /**
     * test class with whitelist binding
     */
    @IAgentAction( access = IAgentAction.EAccess.WHITELIST )
    private static final class CMethodBindingWhitelist extends IBaseAgent<CMethodBindingWhitelist>
    {

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        public CMethodBindingWhitelist(
            final IAgentConfiguration<CMethodBindingWhitelist> p_configuration
        )
        {
            super( p_configuration );
        }

        /**
         * test binding method with annotation
         */
        @IAgentActionFilter
        private void methodannotate()
        {
        }

        /**
         * test binding method without annotation
         */
        private void methodnotannotate()
        {
        }
    }


}
