/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agentspeak.action.buildin.math.linearprogram;

import lightjason.agentspeak.error.CIllegalArgumentException;
import lightjason.agentspeak.language.CCommon;
import lightjason.agentspeak.language.ITerm;
import lightjason.agentspeak.language.execution.IContext;
import lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;


/**
 * add a linear value constraint to the LP with the definition
 * \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    =      \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$,
 * \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    \geq   \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$
 * \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    \leq   \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$
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
    public final int getMinimalArgumentNumber()
    {
        return 6;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first search the relation symbol and create spliting lists
        final int l_index = IntStream.range( 1, p_argument.size() )
                                     .boxed()
                                     .mapToInt( i -> CCommon.isRawValueAssignableTo( p_argument.get( i ), String.class ) ? i : -1 )
                                     .filter( i -> i > -1 )
                                     .findFirst()
                                     .orElseThrow( () -> new CIllegalArgumentException( lightjason.agentspeak.common.CCommon.getLanguageString( this, "relation" ) ) );


        // create linear constraint based on an equation
        CCommon.<Pair<LinearObjectiveFunction, Collection<LinearConstraint>>, ITerm>getRawValue( p_argument.get( 0 ) ).getRight().add(
            new LinearConstraint(
                p_argument.subList( 1, l_index - 2 ).stream()
                          .mapToDouble( i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue() )
                          .toArray(),
                CCommon.<Number, ITerm>getRawValue( p_argument.get( l_index - 1 ) ).doubleValue(),

                this.getRelation( CCommon.getRawValue( p_argument.get( l_index ) ) ),

                p_argument.subList( l_index + 1, p_argument.size() ).stream()
                          .mapToDouble( i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue() )
                          .toArray(),
                CCommon.<Number, ITerm>getRawValue( p_argument.get( p_argument.size() - 1 ) ).doubleValue()
            )
        );

        return CFuzzyValue.from( true );
    }

}
