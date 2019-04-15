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
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.lightjason.agentspeak.testing.IBaseTest;

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
}
