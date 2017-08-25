/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.expressionbinary;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CSyntaxErrorException;

import java.util.function.BiFunction;


/**
 * binary operator
 */
public enum EOperator implements BiFunction<Number, Number, Number>
{
    ASSIGNINCREMENT( "+=" ),
    ASSIGNDECREMENT( "-=" ),
    ASSIGNMULTIPLY( "*=" ),
    ASSIGNDIVIDE( "/=" ),
    ASSIGNMODULO( "%=" ),
    ASSIGNPOW( "^=" );

    /**
     * operator
     */
    private final String m_operator;

    /**
     * ctor
     *
     * @param p_operator operator string
     */
    EOperator( final String p_operator )
    {
        m_operator = p_operator;
    }

    @Override
    public final Number apply( final Number p_lhs, final Number p_rhs )
    {
        switch ( this )
        {
            case ASSIGNINCREMENT:
                return p_lhs.doubleValue() + p_rhs.doubleValue();

            case ASSIGNDECREMENT:
                return p_lhs.doubleValue() - p_rhs.doubleValue();

            case ASSIGNMULTIPLY:
                return p_lhs.doubleValue() * p_rhs.doubleValue();

            case ASSIGNDIVIDE:
                return p_lhs.doubleValue() * p_rhs.doubleValue();

            case ASSIGNMODULO:
                return p_lhs.longValue() % p_rhs.longValue();

            case ASSIGNPOW:
                return Math.pow( p_lhs.doubleValue(), p_rhs.doubleValue() );

            default:
                throw new CSyntaxErrorException( CCommon.languagestring( this, "operatorunknown", this ) );
        }
    }

    @Override
    public final String toString()
    {
        return m_operator;
    }
}
