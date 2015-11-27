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

import lightjason.common.CCommon;
import lightjason.language.IVariable;


/**
 * unary increment
 */
public final class CIncrement<T extends Number> implements IOperator<T>
{
    @Override
    @SuppressWarnings( "unchecked" )
    public final IVariable<T> evaluate( final IVariable<T> p_variable )
    {
        if ( !p_variable.isAllocated() )
            throw new IllegalArgumentException( CCommon.getLanguageString( this, "notallocated", p_variable ) );

        if ( p_variable.isValueAssignableFrom( Double.class ) )
            return p_variable.set( (T) new Double( p_variable.get().doubleValue() + 1 ) );
        if ( p_variable.isValueAssignableFrom( Long.class ) )
            return p_variable.set( (T) new Long( p_variable.get().longValue() + 1 ) );
        if ( p_variable.isValueAssignableFrom( Float.class ) )
            return p_variable.set( (T) new Float( p_variable.get().floatValue() + 1 ) );
        if ( p_variable.isValueAssignableFrom( Integer.class ) )
            return p_variable.set( (T) new Integer( p_variable.get().intValue() + 1 ) );

        return p_variable;
    }
}
