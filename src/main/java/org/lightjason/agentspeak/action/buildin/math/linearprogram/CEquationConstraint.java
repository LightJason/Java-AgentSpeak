/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

import com.codepoetics.protonpack.StreamUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


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
 * elements must be set and the last argument is the \f$ r_{const} \f$, the action fails on wrong input
 *
 * @code math/linearprogram/equationconstraint( LP, [2,7,[7,12,[19]]], "<", [1,2],3,5 ) @endcode
 * @see https://en.wikipedia.org/wiki/Linear_programming
 * @see http://commons.apache.org/proper/commons-math/userguide/optimization.html
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
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, final IContext p_context, final List<ITerm> p_argument,
                                               final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        // create left-hand-side and right-hand-side with operator lists
        final List<Number> l_lhs = StreamUtils.takeWhile( l_arguments.stream().skip( 1 ), i -> !CCommon.rawvalueAssignableTo( i, String.class ) )
                                              .map( ITerm::<Number>raw )
                                              .collect( Collectors.toList() );

        final List<ITerm> l_rhs = l_arguments.stream()
                                             .skip( l_lhs.size() + 1 )
                                             .collect( Collectors.toList() );

        // test content
        if ( ( l_lhs.size() < 2 ) || ( l_rhs.size() < 3 ) || ( !CCommon.rawvalueAssignableTo( l_rhs.get( 0 ), String.class ) ) )
            return CFuzzyValue.from( false );

        // create constraint
        l_arguments.get( 0 ).<Pair<LinearObjectiveFunction, Collection<LinearConstraint>>>raw().getRight().add(
            new LinearConstraint(

                // c_i values
                l_lhs.stream()
                     .limit( l_lhs.size() - 1 )
                     .mapToDouble( Number::doubleValue )
                     .toArray(),

                // c_const value
                l_lhs.get( l_lhs.size() - 1 )
                     .doubleValue(),

                // relation symbol
                this.getRelation( l_rhs.get( 0 ).<String>raw() ),

                // r_i values
                l_rhs.stream()
                     .limit( l_rhs.size() - 1 )
                     .skip( 1 )
                     .map( ITerm::<Number>raw )
                     .mapToDouble( Number::doubleValue )
                     .toArray(),

                // r_const value
                l_rhs.get( l_rhs.size() - 1 )
                     .<Number>raw()
                     .doubleValue()

            )
        );

        return CFuzzyValue.from( true );
    }

}
