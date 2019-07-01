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

package org.lightjason.agentspeak.action;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.binding.IAgentAction;
import org.lightjason.agentspeak.action.binding.IAgentActionFilter;
import org.lightjason.agentspeak.action.binding.IAgentActionName;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
            ).map( i -> i.name().toString() ).sorted().toArray(),

            Stream.of( "methodwhitelist", "methodpass", "methodbyname", "methodfail" ).sorted().toArray()
        );
    }


    /**
     * test method default binding
     */
    @Test
    public void methoddefault()
    {
        Assert.assertArrayEquals(
            CCommon.actionsFromAgentClass( CMethodBindingDefault.class ).map( i -> i.name().toString() ).toArray(),
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
            CCommon.actionsFromAgentClass( CMethodBindingBlacklist.class ).map( i -> i.name().toString() ).toArray(),
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
            CCommon.actionsFromAgentClass( CMethodBindingWhitelist.class ).map( i -> i.name().toString() ).toArray(),
            Stream.of( "methodnotannotate" ).toArray()
        );
    }

    /**
     * test binding methods calls
     *
     * @throws Exception on agent instantiation
     */
    @Test
    public void methodcalls() throws Exception
    {
        final IAgent<?> l_agent = new CClassBindingWhiteListGenerator().generatesingle();
        final List<IAction> l_actions = CCommon.actionsFromAgentClass( CClassBindingWhitelist.class )
                                               .collect( Collectors.toList() );

        Assert.assertEquals( 4, l_actions.size() );

        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertEquals( 0, l_actions.get( 0 ).minimalArgumentNumber() );
        Assert.assertTrue(
            execute(
                l_actions.get( 0 ),
                false,
                Collections.emptyList(),
                l_return,
                l_agent
            )
        );

        Assert.assertEquals( 1, l_actions.get( 1 ).minimalArgumentNumber() );
        Assert.assertTrue(
            execute(
                l_actions.get( 1 ),
                false,
                Stream.of( 111 ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return,
                l_agent
            )
        );

        Assert.assertEquals( 0, l_actions.get( 2 ).minimalArgumentNumber() );
        Assert.assertTrue(
            execute(
                l_actions.get( 2 ),
                false,
                Collections.emptyList(),
                l_return,
                l_agent
            )
        );

        Assert.assertEquals( 0, l_actions.get( 3 ).minimalArgumentNumber() );
        Assert.assertFalse(
            execute(
                l_actions.get( 3 ),
                false,
                Collections.emptyList(),
                l_return,
                l_agent
            )
        );

        Assert.assertArrayEquals(
            Stream.of( 2, 111, 3 ).toArray(),
            l_return.stream().map( ITerm::raw ).toArray()
        );
    }

    /**
     * test serializable action
     *
     * @throws Exception on serialize or agent error
     */
    @Test
    public void serialize() throws Exception
    {
        final List<ITerm> l_return = new ArrayList<>();

        final IAgent<?> l_agent = new CClassBindingWhiteListGenerator().generatesingle();
        final IAction l_origin = CCommon.actionsFromAgentClass( CClassBindingWhitelist.class ).findFirst().get();
        final IAction l_copy = SerializationUtils.clone( l_origin );

        Assert.assertEquals( l_origin, l_copy );
        Assert.assertTrue(
            execute(
                l_origin,
                false,
                Collections.emptyList(),
                l_return,
                l_agent
            )
        );
        Assert.assertTrue(
            execute(
                l_copy,
                false,
                Collections.emptyList(),
                l_return,
                l_agent
            )
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( l_return.get( 0 ), l_return.get( 1 ) );
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
         *
         * @return int value
         */
        private int methoddefault()
        {
            return 0;
        }
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
         *
         * @return int value
         */
        private int methodblacklist()
        {
            return 1;
        }
    }

    /**
     * test class whitelist agent generator
     */
    private static class CClassBindingWhiteListGenerator extends IBaseAgentGenerator<CClassBindingWhitelist>
    {

        CClassBindingWhiteListGenerator() throws Exception
        {
            super( InputStream.nullInputStream(), IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY );
        }

        @Nonnull
        @Override
        public CClassBindingWhitelist generatesingle( @Nullable final Object... p_data )
        {
            return new CClassBindingWhitelist( m_configuration );
        }
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
         *
         * @return int value
         */
        private int methodwhitelist()
        {
            return 2;
        }

        /**
         * test binding method with name
         *
         * @return int value
         */
        @IAgentActionName( name = "methodbyname" )
        private int methodbyname()
        {
            return 3;
        }

        /**
         * test binding method with parameter
         *
         * @param p_value input value
         * @return int value
         */
        private int methodpass( final int p_value )
        {
            return p_value;
        }

        /**
         * test binding method failing
         */
        private void methodfail()
        {
            throw new RuntimeException();
        }
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
         *
         * @return int value
         */
        @IAgentActionFilter
        private int methodannotate()
        {
            return 4;
        }

        /**
         * test binding method without annotation
         *
         * @return int value
         */
        private int methodnotannotate()
        {
            return 5;
        }
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
         *
         * @return int value
         */
        @IAgentActionFilter
        private int methodannotate()
        {
            return 6;
        }

        /**
         * test binding method without annotation
         *
         * @return int value
         */
        private int methodnotannotate()
        {
            return 7;
        }
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
         *
         * @return int value
         */
        @IAgentActionFilter
        private int methodannotate()
        {
            return 8;
        }

        /**
         * test binding method without annotation
         *
         * @return int value
         */
        private int methodnotannotate()
        {
            return 9;
        }
    }


}
