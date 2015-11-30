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
    /**
     * variable
     */
    private final IVariable<T> m_variable;

    /**
     * ctor
     *
     * @param p_variable variable
     */
    public CIncrement( final IVariable<T> p_variable )
    {
        m_variable = p_variable;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IVariable<T> evaluate()
    {
        if ( !m_variable.isAllocated() )
            throw new IllegalArgumentException( CCommon.getLanguageString( this, "notallocated", m_variable ) );

        if ( m_variable.isValueAssignableFrom( Double.class ) )
            return m_variable.set( (T) new Double( m_variable.get().doubleValue() + 1 ) );
        if ( m_variable.isValueAssignableFrom( Long.class ) )
            return m_variable.set( (T) new Long( m_variable.get().longValue() + 1 ) );
        if ( m_variable.isValueAssignableFrom( Float.class ) )
            return m_variable.set( (T) new Float( m_variable.get().floatValue() + 1 ) );
        if ( m_variable.isValueAssignableFrom( Integer.class ) )
            return m_variable.set( (T) new Integer( m_variable.get().intValue() + 1 ) );

        return m_variable;
    }
}
