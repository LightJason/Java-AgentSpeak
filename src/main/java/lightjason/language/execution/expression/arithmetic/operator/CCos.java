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
 * cos-function operator
 */
public final class CCos implements IArithmeticOperator
{
    @Override
    public final String getToken()
    {
        return "cos";
    }

    @Override
    public final int getNumberOfArguments()
    {
        return 1;
    }

    @Override
    public final Number execution( final List<Number> p_arguments )
    {
        return Math.cos( p_arguments.get( 0 ).doubleValue() );
    }

    @Override
    public final int hashCode()
    {
        return getToken().hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.getToken().equals( p_object );
    }
}
