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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action to create a linear program.
 * This action creates the linear program with
 * the objective functions \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + d \f$,
 * the first \f$ n-1 \f$ arguments are the \f$ c_i \f$ values (coefficients) and the last
 * argument is the \f$ d \f$ value (constant) of the objective function
 *
 * {@code LP = .math/linearprogram/create(1,2, [3, [4,5]], 10);}
 *
 * @see https://en.wikipedia.org/wiki/Linear_programming
 * @see http://commons.apache.org/proper/commons-math/userguide/optimization.html
 */
public final class CCreate extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7399100017836837088L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CCreate.class, "math", "linearprogram" );

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
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<Double> l_arguments = CCommon.flatten( p_argument )
                                                .map( i -> i.<Number>raw().doubleValue() )
                                                .collect( Collectors.toList() );

        p_return.add( CRawTerm.of(
            new ImmutablePair<>(
                new LinearObjectiveFunction(
                    l_arguments.stream().limit( l_arguments.size() - 1 ).mapToDouble( i -> i ).toArray(),
                    l_arguments.get( l_arguments.size() - 1 )
                ),
                new HashSet<LinearConstraint>()
            )
        ) );

        return Stream.of();
    }

}
