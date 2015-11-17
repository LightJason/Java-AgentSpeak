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

package lightjason.language.arithmetic.operator;

import java.util.List;


/**
 * abs-function operator
 */
public class CAbs implements IArithmeticOperator
{
    @Override
    public String getToken()
    {
        return "abs";
    }

    @Override
    public int getNumberOfArguments()
    {
        return 1;
    }

    @Override
    public Number execution( final List<Number> p_arguments )
    {
        if ( p_arguments.get( 0 ) instanceof Long )
            return Math.abs( p_arguments.get( 0 ).longValue() );

        if ( p_arguments.get( 0 ) instanceof Integer )
            return Math.abs( p_arguments.get( 0 ).intValue() );

        if ( p_arguments.get( 0 ) instanceof Float )
            return Math.abs( p_arguments.get( 0 ).floatValue() );

        return Math.abs( p_arguments.get( 0 ).doubleValue() );
    }

    @Override
    public int hashCode()
    {
        return getToken().hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return this.getToken().equals( p_object );
    }
}
