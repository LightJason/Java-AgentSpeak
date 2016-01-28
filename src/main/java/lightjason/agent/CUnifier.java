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

package lightjason.agent;

import lightjason.language.ILiteral;
import lightjason.language.execution.IUnifier;
import lightjason.language.execution.expression.IExpression;


/**
 * unification algorithm
 */
public final class CUnifier implements IUnifier
{
    @Override
    public final <R, T extends IAgent> R parallelunify( final T p_agent, final ILiteral p_literal, final IExpression p_expression )
    {
        return null;
    }

    @Override
    public final <R, T extends IAgent> R sequentialunify( final T p_agent, final ILiteral p_literal, final IExpression p_expression )
    {
        return null;
    }
}
