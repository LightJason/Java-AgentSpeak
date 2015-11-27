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

package lightjason.language.plan.unaryoperator;

import lightjason.language.IVariable;
import lightjason.language.plan.IBodyOperation;


/**
 * defines an unary operation
 */
public class COperation<T extends Number> implements IBodyOperation
{
    /**
     * variable
     */
    private final IVariable<T> m_variable;
    /**
     * operator
     */
    private final IOperator<T> m_operation;

    /**
     * ctor
     *
     * @param p_variable variable
     * @param p_operator operator
     */
    public COperation( final IVariable<T> p_variable, final IOperator<T> p_operator )
    {
        m_variable = p_variable;
        m_operation = p_operator;
    }


    @Override
    public boolean evaluate()
    {
        m_operation.evaluate( m_variable );
        return true;
    }
}
