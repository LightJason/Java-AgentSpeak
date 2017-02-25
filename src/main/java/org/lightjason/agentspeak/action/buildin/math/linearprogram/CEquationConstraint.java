/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.math.linearprogram;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * add a linear equation constraint to the LP.
 * The arguments of the action contains the left and right side of the equation:
 *
 * + \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    =      \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$
 * + \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    \geq   \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$
 * + \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    \leq   \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$
 *
 * The first arguments is the LP object, the following arguments are the \f$ c_i \f$ values, after that the \f$ c_{const} \f$ value must be added, in the middle
 * of the arguments the relation symbol (\f$ = \f$, \f$ \geq \f$ or \f$ \leq \f$) must be set as string, after that all \f$ r_i \f$
 * elements must be set and the last argument is the \f$ r_{const} \f$, the action never fails
 * @code math/linearprogram/equationconstraint( LP, [2,7,[7,12,[19]]], "<", [1,2],3,5 ) @endcode
 *
 * @warning the action throws an exception if the relation symbol is not found
 * @see https://en.wikipedia.org/wiki/Linear_programming
 * @see http://commons.apache.org/proper/commons-math/userguide/optimization.html
 *
 */
public final class CEquationConstraint extends IConstraint
{

    /**
     * ctor
     */
    public CEquationConstraint()
    {
        super();
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 6;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        // first search the relation symbol and create splitting lists
        final int l_index = IntStream.range( 1, l_arguments.size() )
                                     .boxed()
                                     .mapToInt( i -> CCommon.rawvalueAssignableTo( l_arguments.get( i ), String.class ) ? i : -1 )
                                     .filter( i -> i > -1 )
                                     .findFirst()
                                     .orElseThrow( () -> new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon
                                                                                            .languagestring( this, "relation" ) ) );


        // create linear constraint based on an equation
        l_arguments.get( 0 ).<Pair<LinearObjectiveFunction, Collection<LinearConstraint>>>raw().getRight().add(
            new LinearConstraint(

                // c_i values
                l_arguments.stream()
                    .limit( l_index - 2 )
                    .skip( 1 )
                    .mapToDouble( i -> i.<Number>raw().doubleValue() )
                    .toArray(),

                // c_const value
                l_arguments.get( l_index - 1 ).<Number>raw().doubleValue(),

                // relation symbol
                this.getRelation( p_argument.get( l_index ).<String>raw() ),

                // r_i values
                l_arguments.stream()
                    .skip( l_index + 2 )
                    .mapToDouble( i -> i.<Number>raw().doubleValue() )
                    .toArray(),

                // r_const value
                l_arguments.get( p_argument.size() - 1 ).<Number>raw().doubleValue()
            )
        );

        return CFuzzyValue.from( true );
    }

}
