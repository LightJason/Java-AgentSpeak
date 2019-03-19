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

package org.lightjason.agentspeak.action;

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
    public void classbinding()
    {
        Assert.assertArrayEquals(

            CCommon.actionsFromAgentClass(
                CClassBindingDefault.class,
                CClassBindingBlacklist.class,
                CClassBindingWhitelist.class
            ).map( i -> i.name().path() ).toArray(),

            Stream.of( "methodwhitelist" ).toArray()
        );
    }


    /**
     * test method default binding
     */
    @Test
    public void methoddefault()
    {
        Assert.assertArrayEquals(
            CCommon.actionsFromAgentClass( CMethodBindingDefault.class ).map( i -> i.name().path() ).toArray(),
            Stream.of( "methodannotate" ).toArray()
        );
    }


    /**
     * test method blacklist binding
     */
    @Test
    public void methodblacklist()
    {
        Assert.assertArrayEquals(
            CCommon.actionsFromAgentClass( CMethodBindingBlacklist.class ).map( i -> i.name().path() ).toArray(),
            Stream.of( "methodannotate" ).toArray()
        );
    }


    /**
     * test method whitelist binding
     */
    @Test
    public void methodwhitelist()
    {
        Assert.assertArrayEquals(
            CCommon.actionsFromAgentClass( CMethodBindingWhitelist.class ).map( i -> i.name().path() ).toArray(),
            Stream.of( "methodnotannotate" ).toArray()
        );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * test class with default binding
     */
    @IAgentAction
    private static final class CClassBindingDefault extends IBaseAgent<CClassBindingDefault>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 3345768520232804575L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CClassBindingDefault( final IAgentConfiguration<CClassBindingDefault> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * test binding method
         */
        private void methoddefault()
        {}
    }

    /**
     * test class with blacklist binding
     */
    @IAgentAction( access = IAgentAction.EAccess.BLACKLIST )
    private static final class CClassBindingBlacklist extends IBaseAgent<CClassBindingBlacklist>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 7111629511993393960L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CClassBindingBlacklist( final IAgentConfiguration<CClassBindingBlacklist> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * test binding method
         */
        private void methodblacklist()
        {}
    }

    /**
     * test class with whitelist binding
     */
    @IAgentAction( access = IAgentAction.EAccess.WHITELIST )
    private static final class CClassBindingWhitelist extends IBaseAgent<CClassBindingWhitelist>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -7816405802996290926L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CClassBindingWhitelist( final IAgentConfiguration<CClassBindingWhitelist> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * test binding method
         */
        private void methodwhitelist()
        {}
    }


    /**
     * test class with default binding
     */
    @IAgentAction
    private static final class CMethodBindingDefault extends IBaseAgent<CMethodBindingDefault>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -2286148952945672902L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CMethodBindingDefault( final IAgentConfiguration<CMethodBindingDefault> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * test binding method with annotation
         */
        @IAgentActionFilter
        private void methodannotate()
        {}

        /**
         * test binding method without annotation
         */
        private void methodnotannotate()
        {}
    }

    /**
     * test class with blacklist binding
     */
    @IAgentAction( access = IAgentAction.EAccess.BLACKLIST )
    private static final class CMethodBindingBlacklist extends IBaseAgent<CMethodBindingBlacklist>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 2803896156449016208L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CMethodBindingBlacklist( final IAgentConfiguration<CMethodBindingBlacklist> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * test binding method with annotation
         */
        @IAgentActionFilter
        private void methodannotate()
        {}

        /**
         * test binding method without annotation
         */
        private void methodnotannotate()
        {}
    }


    /**
     * test class with whitelist binding
     */
    @IAgentAction( access = IAgentAction.EAccess.WHITELIST )
    private static final class CMethodBindingWhitelist extends IBaseAgent<CMethodBindingWhitelist>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -9187345129201192928L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CMethodBindingWhitelist( final IAgentConfiguration<CMethodBindingWhitelist> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * test binding method with annotation
         */
        @IAgentActionFilter
        private void methodannotate()
        {}

        /**
         * test binding method without annotation
         */
        private void methodnotannotate()
        {}
    }


}
