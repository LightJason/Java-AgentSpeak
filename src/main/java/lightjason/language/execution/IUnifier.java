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

package lightjason.language.execution;

import lightjason.language.ILiteral;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.IFuzzyValue;


/**
 * interface of an unification algorithm
 */
public interface IUnifier
{

    /**
     * unifies a literal in parallel
     *
     *
     * @param p_context running context
     * @param p_literal literal
     * @param p_expression expression can be null iif expression is not set
     * @return boolean if a unify can be done
     */
    IFuzzyValue<Boolean> parallelunify( final IContext<?> p_context, final ILiteral p_literal, final IExpression p_expression );

    /**
     * unifies a literal in parallel
     *
     *
     * @param p_context running context
     * @param p_literal literal
     * @param p_expression expression can be null iif expression is not set
     * @return boolean if a unify can be done
     */
    IFuzzyValue<Boolean> sequentialunify( final IContext<?> p_context, final ILiteral p_literal, final IExpression p_expression );

}
