/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;

import java.util.Collection;
import java.util.List;


/**
 * add a linear value constraint to the LP with the definition
 * \f$ \sum_{i=1} c_i \cdot x_i   =      v \f$,
 * \f$ \sum_{i=1} c_i \cdot x_i   \geq   v \f$
 * \f$ \sum_{i=1} c_i \cdot x_i   \leq   v \f$
 */
public final class CValueConstraint extends IConstraint
{

    /**
     * ctor
     */
    public CValueConstraint()
    {
        super();
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 4;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // create linear constraint based on a value
        CCommon.<Pair<LinearObjectiveFunction, Collection<LinearConstraint>>, ITerm>getRawValue( p_argument.get( 0 ) ).getRight().add(
            new LinearConstraint(
                p_argument.subList( 1, p_argument.size() - 2 ).stream()
                          .mapToDouble( i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue() )
                          .toArray(),
                this.getRelation( CCommon.getRawValue( p_argument.get( p_argument.size() - 2 ) ) ),
                CCommon.<Number, ITerm>getRawValue( p_argument.get( p_argument.size() - 1 ) ).doubleValue()
            )
        );

        return CFuzzyValue.from( true );
    }

}
