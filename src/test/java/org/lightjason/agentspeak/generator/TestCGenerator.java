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

import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.binding.CMethodAction;
import org.lightjason.agentspeak.action.binding.IAgentAction;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.agent.IPlanBundle;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.lightjason.agentspeak.testing.IBaseTest;
import org.lightjason.agentspeak.testing.action.CTestIs;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * generator test
 */
public final class TestCGenerator extends IBaseTest
{

    /**
     * test agent generator multiple
     *
     * @throws IOException on stream error
     */
    @Test
    public void agentgeneratormultiple() throws IOException
    {
        Assert.assertEquals( 5, new CAgentGenerator().generatemultiple( 5 ).count() );
    }

    /**
     * test static action generator fail
     */
    @Test( expected = NoSuchElementException.class )
    public void staticactiongeneratorfail()
    {
        new CActionStaticGenerator().apply( CPath.of( "foo" ) );
    }

    /**
     * test lmabda-object-streaming
     */
    @Test
    public void lambdaobject()
    {
        final ILambdaStreaming<Number> l_stream = new CTestLambda();

        Assert.assertArrayEquals(
            Stream.of( 1, 2, 3, 4 ).toArray(),
            Stream.of( 1, 2.1F, 3L, 4.3D ).flatMap( l_stream::apply ).toArray()
        );

        Assert.assertTrue( l_stream.assignable().collect( Collectors.toSet() ).contains( Number.class ) );
    }

    /**
     * empty lambda streaming
     */
    @Test
    public void emptylambdastreamgenerator()
    {
        Assert.assertEquals( ILambdaStreaming.EMPTY, new CLambdaStreamingStaticGenerator( Stream.empty() ).apply( Object.class ) );
        Assert.assertEquals( ILambdaStreaming.EMPTY, new CLambdaStreamingGenerator( CCommon.PACKAGEROOT ).apply( Object.class ) );
        Assert.assertEquals( ILambdaStreaming.EMPTY, new CLambdaStreamingGenerator( false, CCommon.PACKAGEROOT ).apply( Object.class ) );
    }

    /**
     * test lambda streaming
     */
    @Test
    public void lambdastreaming()
    {
        final ITerm l_object = CRawTerm.of( 3.5 );

        // without cache
        Assert.assertEquals(
            l_object.<Number>raw().intValue(),
            new CLambdaStreamingGenerator( false, "org.lightjason.agentspeak.generator" )
                .apply( l_object.raw().getClass() )
                .apply( l_object.raw() )
                .findFirst()
                .get()
        );


        // with caching
        final ILambdaStreamingGenerator l_generator = new CLambdaStreamingGenerator( "org.lightjason.agentspeak.generator" );

        Assert.assertEquals( l_object.<Number>raw().intValue(), l_generator.apply( l_object.raw().getClass() ).apply( l_object.raw() ).findFirst().get() );
        Assert.assertEquals( l_object.<Number>raw().intValue(), l_generator.apply( l_object.raw().getClass() ).apply( l_object.raw() ).findFirst().get() );
    }

    /**
     * test plan-bundle generator
     *
     * @throws IOException on input stream error
     */
    @Test
    public void planbundlegenerator() throws IOException
    {
        final IPlanBundle l_bundle = new CDefaultPlanBundleGenerator(
            IOUtils.toInputStream( "foobar. bar :- success. +!do <- success.", "UTF-8" ),
            IActionGenerator.EMPTY,
            ILambdaStreamingGenerator.EMPTY
        ).generatesingle();

        Assert.assertArrayEquals( Stream.of( CLiteral.of( "foobar" ) ).toArray(), l_bundle.initialbeliefs().toArray() );
        Assert.assertEquals( CTrigger.of( ITrigger.EType.ADDGOAL, CLiteral.of( "do" ) ), l_bundle.plans().stream().findFirst().get().trigger() );
        Assert.assertEquals( CLiteral.of( "bar" ), l_bundle.rules().stream().findFirst().get().identifier() );
    }

    /**
     * test plan-bundle generator multiple
     *
     * @throws IOException on input stream error
     */
    @Test
    public void planbundlemultiply() throws IOException
    {
        final IPlanBundleGenerator l_generator = new CDefaultPlanBundleGenerator(
            IOUtils.toInputStream( "foobar. bar :- success. +!do <- success.", "UTF-8" ),
            IActionGenerator.EMPTY,
            ILambdaStreamingGenerator.EMPTY
        );

        final IPlanBundle l_bundle = l_generator.generatesingle();

        Assert.assertArrayEquals(
            Stream.concat( l_bundle.initialbeliefs().stream(), l_bundle.initialbeliefs().stream() ).toArray(),
            l_generator.generatemultiple( 2 ).flatMap( i -> i.initialbeliefs().stream() ).toArray()
        );

        Assert.assertArrayEquals(
            Stream.concat( l_bundle.plans().stream(), l_bundle.plans().stream() ).toArray(),
            l_generator.generatemultiple( 2 ).flatMap( i -> i.plans().stream() ).toArray()
        );

        Assert.assertArrayEquals(
            Stream.concat( l_bundle.rules().stream(), l_bundle.rules().stream() ).toArray(),
            l_generator.generatemultiple( 2 ).flatMap( i -> i.rules().stream() ).toArray()
        );

    }

    /**
     * test action generator by package
     */
    @Test
    public void actiongeneratorpackages()
    {
        final IAction l_action = new CTestIs();
        final IActionGenerator l_generator = new CActionGenerator( Stream.of( "org.lightjason.agentspeak.testing" ) );

        Assert.assertEquals( l_action, l_generator.apply( l_action.name() ) );
    }

    /**
     * test action generator by class
     */
    @Test
    public void actiongeneratorclass()
    {
        final IActionGenerator l_generator = new CActionGenerator( Stream.empty(), Stream.of( CTestAgent.class ) );

        Assert.assertTrue( l_generator.apply( CPath.of( "agenttest" ) ) instanceof CMethodAction );
        Assert.assertEquals( "agenttest", l_generator.apply( CPath.of( "agenttest" ) ).toString() );
        Assert.assertEquals( 0, l_generator.apply( CPath.of( "agenttest" ) ).minimalArgumentNumber() );
    }

    /**
     * test other generators
     */
    @Test
    public void othergenerators()
    {
        final IAction l_action = new CTestIs();
        final IActionGenerator l_generator = new CActionGenerator();
        final IActionGenerator l_other = new CActionGenerator( Stream.of( "org.lightjason.agentspeak.testing" ) );

        Assert.assertTrue( l_other.contains( l_action.name() ) );
        Assert.assertFalse( l_generator.contains( l_action.name() ) );
        Assert.assertEquals( l_action, l_other.apply( l_action.name() ) );

        Assert.assertTrue( l_generator.add( l_other ).contains( l_action.name() ) );
        Assert.assertEquals( l_action, l_generator.apply( l_action.name() ) );
    }

    @Test
    public void testemptyactiogenerator()
    {
        final IActionGenerator l_generator = IActionGenerator.EMPTY;

        Assert.assertFalse( l_generator.contains( CPath.of( "foo" ) ) );
        Assert.assertEquals( IActionGenerator.EMPTY, l_generator.add( l_generator ) );
        Assert.assertEquals( IActionGenerator.EMPTY, l_generator.remove( l_generator ) );
    }

    /**
     * test action generator error
     */
    @Test( expected = NoSuchElementException.class )
    public void actiongeneratorfail()
    {
        new CActionGenerator( Stream.empty() ).apply( CPath.of( "bar" ) );
    }



    /**
     * test lambda streaming class
     */
    public static final class CTestLambda implements ILambdaStreaming<Number>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 4814323709881633733L;

        @NonNull
        @Override
        public Stream<Class<?>> assignable()
        {
            return Stream.of( Number.class );
        }

        @Override
        public Stream<?> apply( final Number p_object )
        {
            return Stream.of( p_object.intValue() );
        }
    }


    /**
     * test agent
     */
    @IAgentAction( access = IAgentAction.EAccess.WHITELIST )
    public static final class CTestAgent extends IBaseAgent<CTestAgent>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 3414997241586446077L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        public CTestAgent( @Nonnull final IAgentConfiguration<CTestAgent> p_configuration )
        {
            super( p_configuration );
        }

        /**
         * test method
         */
        private void agenttest()
        {

        }
    }
}
