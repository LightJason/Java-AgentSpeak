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

package org.lightjason.agentspeak.action.linearprogram;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * add a linear value constraint to the LP.
 * The action create a value constaint with one of
 * the definitions:
 *
 * + \f$ \sum_{i=1} c_i \cdot x_i   =      v \f$
 * + \f$ \sum_{i=1} c_i \cdot x_i   \geq   v \f$
 * + \f$ \sum_{i=1} c_i \cdot x_i   \leq   v \f$
 *
 * the first \f$ n-2 \f$ arguments are the \f$ c_i \f$,
 * the \f$ n-1 \f$ argument ist the relation symbol
 * (\f$ = \f$, \f$ \geq \f$ or \f$ \leq \f$) as string
 * and the last value is the \f$ v \f$ value
 *
 * {@code .math/linearprogram/valueconstaint( LP, [2,5,[7,8,[9]]], "<", 100 );}
 *
 * @see https://en.wikipedia.org/wiki/Linear_programming
 * @see http://commons.apache.org/proper/commons-math/userguide/optimization.html
 */
public final class CValueConstraint extends IConstraint
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 2316665584839205362L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CValueConstraint.class, "math", "linearprogram" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 4;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        // create linear constraint based on a value
        l_arguments.get( 0 ).<Pair<LinearObjectiveFunction, Collection<LinearConstraint>>>raw().getRight().add(
            new LinearConstraint(
                l_arguments.stream()
                           .limit( l_arguments.size() - 2 )
                           .skip( 1 )
                           .mapToDouble( i -> i.<Number>raw().doubleValue() )
                           .toArray(),
                this.getRelation( l_arguments.get( l_arguments.size() - 2 ).raw() ),
                l_arguments.get( l_arguments.size() - 1 ).<Number>raw().doubleValue()
            )
        );

        return Stream.of();
    }

}
