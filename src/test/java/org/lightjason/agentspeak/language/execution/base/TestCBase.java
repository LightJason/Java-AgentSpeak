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

package org.lightjason.agentspeak.language.execution.base;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.expression.IExpression;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * test base executions
 */
public final class TestCBase extends IBaseTest
{

    /**
     * test tenary-operator failing expression
     */
    @Test
    public void ternaryfailexpression()
    {
        execute(
            new CTernaryOperation(
                new IExpression()
                {
                    @Nonnull
                    @Override
                    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                           @Nonnull final List<ITerm> p_return )
                    {
                        return p_context.agent().fuzzy().membership().fail();
                    }

                    @Nonnull
                    @Override
                    public Stream<IVariable<?>> variables()
                    {
                        return Stream.of();
                    }
                },
                new IExecution()
                {
                    @Nonnull
                    @Override
                    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                           @Nonnull final List<ITerm> p_return )
                    {
                        Assert.fail();
                        return Stream.of();
                    }

                    @Nonnull
                    @Override
                    public Stream<IVariable<?>> variables()
                    {
                        return Stream.of();
                    }
                },
                new IExecution()
                {
                    @Nonnull
                    @Override
                    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                           @Nonnull final List<ITerm> p_return )
                    {
                        Assert.assertTrue( true );
                        return Stream.of();
                    }

                    @Nonnull
                    @Override
                    public Stream<IVariable<?>> variables()
                    {
                        return Stream.of();
                    }
                }
            ),
            false,
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

    /**
     * test tenary-operator wrong argument number
     */
    @Test( expected = IllegalStateException.class )
    public void ternaryargumenterror()
    {
        execute(
            new CTernaryOperation(
                new IExpression()
                {
                    @Nonnull
                    @Override
                    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                           @Nonnull final List<ITerm> p_return )
                    {
                        return p_context.agent().fuzzy().membership().success();
                    }

                    @Nonnull
                    @Override
                    public Stream<IVariable<?>> variables()
                    {
                        return Stream.of();
                    }
                },
                new IExecution()
                {
                    @Nonnull
                    @Override
                    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                           @Nonnull final List<ITerm> p_return )
                    {
                        Assert.fail();
                        return Stream.of();
                    }

                    @Nonnull
                    @Override
                    public Stream<IVariable<?>> variables()
                    {
                        return Stream.of();
                    }
                },
                new IExecution()
                {
                    @Nonnull
                    @Override
                    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                                           @Nonnull final List<ITerm> p_return )
                    {
                        Assert.fail();
                        return Stream.of();
                    }

                    @Nonnull
                    @Override
                    public Stream<IVariable<?>> variables()
                    {
                        return Stream.of();
                    }
                }
            ),
            false,
            Collections.emptyList(),
            Collections.emptyList()
        );
    }

}
