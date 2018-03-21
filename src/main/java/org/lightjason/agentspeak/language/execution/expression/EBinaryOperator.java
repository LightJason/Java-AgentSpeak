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

package org.lightjason.agentspeak.language.execution.expression;


import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;


/**
 * expression operator
 */
public enum EBinaryOperator implements BiFunction<ITerm, ITerm, Object>
{
    PLUS( "+" ),
    MINUS( "-" ),

    MULTIPLY( "*" ),
    DIVIDE( "/" ),
    MODULO( "%" ),
    POWER( "**" ),

    OR( "||" ),
    AND( "&&" ),
    XOR( "^" ),

    EQUAL( "==" ),
    NOTEQUAL( "\\==" ),

    LESS( "<" ),
    LESSEQUAL( "<=" ),
    GREATER( ">" ),
    GREATEREQUAL( ">=" );



    /**
     * text name of the enum
     */
    private final String m_name;

    /**
     * ctor
     *
     * @param p_name text name
     */
    EBinaryOperator( final String p_name )
    {
        m_name = p_name;
    }

    @Override
    public final String toString()
    {
        return m_name;
    }

    @Override
    public final Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
    {
        switch ( this )
        {
            case PLUS:
                return p_lhs.<Number>raw().doubleValue() + p_rhs.<Number>raw().doubleValue();

            case MINUS:
                return return p_lhs.<Number>raw().doubleValue() - p_rhs.<Number>raw().doubleValue();

            case MULTIPLY:
                return p_lhs.<Number>raw().doubleValue() * p_rhs.<Number>raw().doubleValue();

            case DIVIDE:
                return p_lhs.<Number>raw().doubleValue() / p_rhs.<Number>raw().doubleValue();

            case MODULO:
                return return p_lhs.<Number>raw().longValue() < 0
                              ? Math.abs( ( p_rhs.<Number>raw().longValue() + p_lhs.<Number>raw().longValue() ) % p_rhs.<Number>raw().longValue() )
                              : p_lhs.<Number>raw().longValue() % p_rhs.<Number>raw().longValue();


            case POWER:
                return Math.pow( p_lhs.<Number>raw().doubleValue(), p_rhs.<Number>raw().doubleValue() );

            case OR:
                return p_lhs.<Boolean>raw() || p_rhs.<Boolean>raw();

            case AND:
                return p_lhs.<Boolean>raw() && p_rhs.<Boolean>raw();

            case XOR:
                return p_lhs.<Boolean>raw() ^ p_rhs.<Boolean>raw();


            case EQUAL:
                return p_lhs.raw().equals( p_rhs.raw() );

            case NOTEQUAL:
                return !p_lhs.raw().equals( p_rhs.raw() );





            default:
                throw new CIllegalArgumentException( CCommon.languagestring( this, "operator", this ) );
        }
    }
}
