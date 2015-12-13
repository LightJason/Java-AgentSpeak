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

import lightjason.common.CCommon;
import lightjason.error.CIllegalArgumentException;

import java.util.List;


/**
 * plus operator
 */
public final class CPlus implements IArithmeticOperator
{
    @Override
    public final String getToken()
    {
        return "+";
    }

    @Override
    public final int getNumberOfArguments()
    {
        return 2;
    }

    @Override
    public final Number execution( final List<Number> p_arguments )
    {
        if ( ( p_arguments.get( 0 ) instanceof Double ) || ( p_arguments.get( 1 ) instanceof Double ) )
            return new Double( p_arguments.get( 0 ).doubleValue() + p_arguments.get( 1 ).doubleValue() );

        if ( ( p_arguments.get( 0 ) instanceof Long ) || ( p_arguments.get( 1 ) instanceof Long ) )
            return new Double( p_arguments.get( 0 ).longValue() + p_arguments.get( 1 ).longValue() );

        if ( ( p_arguments.get( 0 ) instanceof Float ) || ( p_arguments.get( 1 ) instanceof Float ) )
            return new Double( p_arguments.get( 0 ).floatValue() + p_arguments.get( 1 ).floatValue() );

        if ( ( p_arguments.get( 0 ) instanceof Integer ) || ( p_arguments.get( 1 ) instanceof Integer ) )
            return new Double( p_arguments.get( 0 ).intValue() + p_arguments.get( 1 ).intValue() );

        if ( ( p_arguments.get( 0 ) instanceof Short ) || ( p_arguments.get( 1 ) instanceof Short ) )
            return new Double( p_arguments.get( 0 ).shortValue() + p_arguments.get( 1 ).shortValue() );

        if ( ( p_arguments.get( 0 ) instanceof Byte ) || ( p_arguments.get( 1 ) instanceof Byte ) )
            return new Double( p_arguments.get( 0 ).byteValue() + p_arguments.get( 1 ).byteValue() );

        throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notdefined", this.getToken(), p_arguments.get( 0 ).getClass(),
                                                                        p_arguments.get( 1 ).getClass()
        )
        );
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
