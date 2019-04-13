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

import edu.umd.cs.findbugs.annotations.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * test action structure and lambda-streaming
 */
public final class TestCActionLambda extends IBaseTest
{
    /**
     * test action name-by-class
     */
    @Test
    public void namebyclass()
    {
        Assert.assertEquals( "test/testaction", new CTestAction().name().toString() );
    }

    /**
     * test lmabda-object-streaming
     */
    @Test
    public void lambdaobject()
    {
        final ILambdaStreaming<Object> l_stream = new CTestLambdaObject();
        final Object l_object = new Object();

        Assert.assertArrayEquals(
            Stream.of( 1, 2L, 3D, "foo", l_object ).toArray(),
            Stream.of( 1, 2L, 3D, "foo", l_object ).flatMap( l_stream::apply ).toArray()
        );

        Assert.assertTrue( l_stream.assignable().collect( Collectors.toSet() ).contains( Object.class ) );
    }

    /**
     * test lambda-number-streaming
     */
    @Test
    public void lambdanumber()
    {
        final ILambdaStreaming<Number> l_stream = new CTestLambdaNumber();
        final Object[] l_result = Stream.of( 0L, 1L, 2L, 3L ).toArray();

        Assert.assertArrayEquals( l_result, l_stream.apply( 4 ).toArray() );
        Assert.assertArrayEquals( l_result, l_stream.apply( 4.3 ).toArray() );
        Assert.assertArrayEquals( l_result, l_stream.apply( 4.7F ).toArray() );
        Assert.assertArrayEquals( l_result, l_stream.apply( 4L ).toArray() );
    }

    /**
     * test lambda equality
     */
    @Test
    public void lambdaequal()
    {
        Assert.assertEquals( new CTestLambdaObject(), new CTestLambdaObject() );
        Assert.assertEquals( new CTestLambdaNumber(), new CTestLambdaNumber() );
        Assert.assertNotEquals( new CTestLambdaObject(), new CTestLambdaNumber() );
    }

    /**
     * test action
     */
    private static final class CTestAction extends IBaseAction
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 8011510086352157445L;

        @Nonnull
        @Override
        public IPath name()
        {
            return namebyclass( CTestAction.class, "test" );
        }

        @Nonnull
        @Override
        public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
        {
            return Stream.of();
        }
    }

    /**
     * test lambda-streamining
     */
    private static final class CTestLambdaObject extends IBaseLambdaStreaming<Object>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -1099766429334122489L;

        @NonNull
        @Override
        public Stream<Class<?>> assignable()
        {
            return Stream.of( Object.class );
        }

        @Override
        public Stream<?> apply( final Object p_value )
        {
            return Stream.of( p_value );
        }
    }

    /**
     * test number stream lambda
     */
    private static final class CTestLambdaNumber extends IBaseLambdaStreaming<Number>
    {

        @NonNull
        @Override
        public Stream<Class<?>> assignable()
        {
            return Stream.of( Number.class );
        }

        @Override
        public Stream<?> apply( final Number p_number )
        {
            return LongStream.range( 0, Math.abs( p_number.longValue() ) ).boxed();
        }
    }
}
