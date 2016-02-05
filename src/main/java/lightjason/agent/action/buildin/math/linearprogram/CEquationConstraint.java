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
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * adds an linear value constraint to
 * the LP with the definition
 * \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    =      \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$,
 * \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    \geq   \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$
 * \f$ \left( \sum_{i=1} c_i \cdot x_i \right) + c_{const}    \leq   \left( \sum_{i=1} r_i \cdot x_i \right) + r_{const} \f$
 */
public final class CEquationConstraint extends IBuildinAction
{

    /**
     * ctor
     */
    public CEquationConstraint()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 6;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first search the relation symbol
/*
        p_argument.subList( 1, p_argument.size() ).stream().filter( i -> CCommon.isRawValueAssignableTo( i, String.class ) ).findFirst().orElseThrow( () -> new CIllegalArgumentException() );

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
*/
        return CBoolean.from( true );
    }

}
