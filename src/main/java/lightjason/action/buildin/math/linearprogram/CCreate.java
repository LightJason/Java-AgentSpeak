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

package lightjason.action.buildin.math.linearprogram;

import lightjason.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;

import java.util.HashSet;
import java.util.List;


/**
 * action to create a linear program with an
 * objective functions \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + d \f$
 */
public final class CCreate extends IBuildinAction
{

    /**
     * ctor
     */
    public CCreate()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 3;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        p_return.add( CRawTerm.from(
            new ImmutablePair<>(
                new LinearObjectiveFunction(
                    p_argument.subList( 0, p_argument.size() - 1 ).stream()
                              .mapToDouble( i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue() ).toArray(),
                    CCommon.<Number, ITerm>getRawValue( p_argument.get( p_argument.size() - 1 ) ).doubleValue()
                ),
                new HashSet<LinearConstraint>()
            )
        ) );

        return CFuzzyValue.from( true );
    }

}
