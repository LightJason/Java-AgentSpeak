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

package org.lightjason.agentspeak.language.execution.lambda;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.passing.CPassRaw;
import org.lightjason.agentspeak.language.execution.passing.CPassVariable;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * test lambda-streaming
 */
public final class TestCLambda extends IBaseTest
{

    /**
     * test lambda-initialize with value
     */
    @Test
    public void initializevalue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CLambdaInitializeRange( Stream.of( new CPassRaw<>( 3 ) ) ),
                false,
                Collections.emptyList(),
                l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertArrayEquals(
            LongStream.range( 0, 3 ).boxed().toArray(),
            l_return.get( 0 ).<Stream<?>>raw().toArray()
        );
    }

    /**
     * test lambda-initialize with range
     */
    @Test
    public void initializerange()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CLambdaInitializeRange( Stream.of( new CPassRaw<>( 4 ), new CPassRaw<>( 12 ) ) ),
                false,
                Collections.emptyList(),
                l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertArrayEquals(
            LongStream.range( 4, 12 ).boxed().toArray(),
            l_return.get( 0 ).<Stream<?>>raw().toArray()
        );
    }

    /**
     * test lambda-initialize with range skip
     */
    @Test
    public void initializerangeskip()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CLambdaInitializeRange( Stream.of( new CPassRaw<>( 5 ), new CPassRaw<>( 12 ), new CPassRaw<>( 2 ) ) ),
                false,
                Collections.emptyList(),
                l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertArrayEquals(
            Stream.of( 5L, 7L, 9L, 11L ).toArray(),
            l_return.get( 0 ).<Stream<?>>raw().toArray()
        );
    }

    /**
     * test lambda-range exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void initializerangeerror()
    {
        execute(
            new CLambdaInitializeRange(
                Stream.of(
                    new IExecution()
                        {
                            @Nonnull
                            @Override
                            public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                                   @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
                            {
                                return Stream.of();
                            }

                            @Nonnull
                            @Override
                            public Stream<IVariable<?>> variables()
                            {
                                return Stream.of();
                            }
                        }
                )
            ),
            false,
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    /**
     * test execution fail
     */
    @Test
    public void initializefail()
    {
        Assert.assertFalse(
            execute(
                new CLambdaInitializeRange(
                    Stream.of(
                        new IExecution()
                        {
                            @Nonnull
                            @Override
                            public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                                   @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
                            {
                                return p_context.agent().fuzzy().membership().fail();
                            }

                            @Nonnull
                            @Override
                            public Stream<IVariable<?>> variables()
                            {
                                return Stream.of();
                            }
                        }
                    )
                ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test initialize range variables
     */
    @Test
    public void rangevariables()
    {
        final IVariable<?> l_variable = new CVariable<>( "X" );
        Assert.assertEquals( l_variable, new CLambdaInitializeRange( Stream.of( new CPassVariable( l_variable ) ) ).variables().findFirst().get() );
    }
}
