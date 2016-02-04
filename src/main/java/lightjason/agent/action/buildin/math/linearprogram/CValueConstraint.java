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

package lightjason.agent.action.buildin.math.linearprogram;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;

import java.util.HashSet;
import java.util.List;


/**
 * adds an linear value constraint to
 * the LP with the definition
 * \f$ \sum_{i=1} c_i \cdot x_i = v \f$,
 * \f$ \sum_{i=1} c_i \cdot x_i \geq v \f$
 * \f$ \sum_{i=1} c_i \cdot x_i \leq v \f$
 *
 * @todo add equation constraints https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/optim/linear/LinearConstraint.html
 */
public final class CValueConstraint extends IBuildinAction
{

    /**
     * ctor
     */
    public CValueConstraint()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 4;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final Relationship l_relationship;
        final String l_symbol = CCommon.<String, ITerm>getRawValue( p_argument.get( p_argument.size() - 2 ) ).trim();
        switch ( l_symbol )
        {
            case "<":
            case "<=":
                l_relationship = Relationship.LEQ;
                break;

            case ">":
            case ">=":
                l_relationship = Relationship.GEQ;
                break;

            case "=":
            case "==":
                l_relationship = Relationship.EQ;
                break;

            default:
                throw new CIllegalArgumentException( lightjason.common.CCommon.getLanguageString( this, "relation", l_symbol ) );
        }


        CCommon.<Pair<LinearObjectiveFunction, HashSet<LinearConstraint>>, ITerm>getRawValue( p_argument.get( 0 ) ).getRight().add(
                new LinearConstraint(
                        p_argument.subList( 2, p_argument.size() - 2 ).stream().mapToDouble( i -> CCommon.<Number, ITerm>getRawValue( i ).doubleValue() )
                                  .toArray(),
                        l_relationship,
                        CCommon.<Number, ITerm>getRawValue( p_argument.get( p_argument.size() - 1 ) ).doubleValue()
                )
        );

        return CBoolean.from( true );
    }

}
