/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.execution.assignment;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.error.CSyntaxErrorException;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiFunction;


/**
 * binary operator
 */
public enum EAssignOperator implements BiFunction<ITerm, ITerm, Object>
{
    INCREMENT( "+=" ),
    DECREMENT( "-=" ),

    MULTIPLY( "*=" ),
    DIVIDE( "/=" ),
    MODULO( "%=" ),

    POWER( "^=" ),

    ASSIGN( "=" );

    /**
     * operator
     */
    private final String m_operator;

    /**
     * ctor
     *
     * @param p_operator operator string
     */
    EAssignOperator( final String p_operator )
    {
        m_operator = p_operator;
    }

    @Override
    public Object apply( final ITerm p_lhs, final ITerm p_rhs )
    {
        switch ( this )
        {
            case INCREMENT:
                return p_lhs.<Number>raw().doubleValue() + p_rhs.<Number>raw().doubleValue();

            case DECREMENT:
                return p_lhs.<Number>raw().doubleValue() - p_rhs.<Number>raw().doubleValue();

            case MULTIPLY:
                return p_lhs.<Number>raw().doubleValue() * p_rhs.<Number>raw().doubleValue();

            case DIVIDE:
                return p_lhs.<Number>raw().doubleValue() / p_rhs.<Number>raw().doubleValue();

            case MODULO:
                return org.lightjason.agentspeak.language.CCommon.modulo( p_lhs.raw(), p_rhs.raw() );

            case POWER:
                return Math.pow( p_lhs.<Number>raw().doubleValue(), p_rhs.<Number>raw().doubleValue() );

            case ASSIGN:
                return p_rhs.raw();

            default:
                throw new CSyntaxErrorException( CCommon.languagestring( this, "unknownoperator", this ) );
        }
    }

    @Override
    public String toString()
    {
        return m_operator;
    }

    /**
     * returns operator of a string
     *
     * @param p_value string
     * @return operator
     */
    @Nonnull
    public static EAssignOperator of( @Nonnull final String p_value )
    {
        return Arrays.stream( EAssignOperator.values() )
                     .filter( i -> i.m_operator.equals( p_value ) )
                     .findFirst()
                     .orElseThrow( () -> new CIllegalArgumentException( CCommon.languagestring( EAssignOperator.class, "unknown", p_value ) ) );
    }
}
