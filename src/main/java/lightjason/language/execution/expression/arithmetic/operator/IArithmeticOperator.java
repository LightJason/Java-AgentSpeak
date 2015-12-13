/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.execution.expression.arithmetic.operator;


import java.util.List;


/**
 * interface to describe an arithmetic operator
 */
public interface IArithmeticOperator
{
    /**
     * token for matching
     */
    public String getToken();

    /**
     * number of arguments of the operator
     *
     * @return argument number [1,infinty)
     */
    public int getNumberOfArguments();

    /**
     * operator execution
     *
     * @param p_arguments arguments of the operator
     * @return calculated number
     */
    public Number execution( final List<Number> p_arguments );
}
