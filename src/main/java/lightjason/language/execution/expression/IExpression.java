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

package lightjason.language.execution.expression;

import lightjason.common.CPath;
import lightjason.language.IVariable;

import java.util.Map;


/**
 * interface of any expression type
 *
 * @todo add action interface
 */
public interface IExpression<T>
{

    /**
     * returns a map of all variables of the expression
     *
     * @return unmodifiable map with variables
     */
    public Map<CPath, IVariable<T>> getVariables();


    /**
     * adds operators
     *
     * @param p_operator operators
     * @return expression
     */
    public IExpression<T> push( final String... p_operator );


    /**
     * adds variables
     *
     * @param p_variable variables
     * @return expression
     */
    public IExpression<T> push( final IVariable<T>... p_variable );


    /**
     * adds values
     *
     * @param p_value values
     * @return expression
     */
    public IExpression<T> push( final T... p_value );


    /**
     * evaluates expression
     *
     * @return result
     */
    public T evaluate();

}
