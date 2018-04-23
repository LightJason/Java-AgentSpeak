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


import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiFunction;


/**
 * expression binary operator
 */
public enum EBinaryOperator implements BiFunction<ITerm, ITerm, Object>
{
    PLUS( "+" ),
    MINUS( "-" ),

    MULTIPLY( "*" ),
    DIVIDE( "/" ),
    MODULO( "%", "mod" ),
    POWER( "**" ),

    OR( "||", "or" ),
    AND( "&&", "and" ),
    XOR( "^", "xor" ),

    EQUAL( "==" ),
    NOTEQUAL( "\\==", "!=" ),

    LESS( "<" ),
    LESSEQUAL( "<=" ),
    GREATER( ">" ),
    GREATEREQUAL( ">=" );



    /**
     * text name of the enum
     */
    private final String[] m_operator;

    /**
     * ctor
     *
     * @param p_operator text name
     */
    EBinaryOperator( @Nonnull final String... p_operator )
    {
        m_operator = p_operator;
    }

    @Override
    public String toString()
    {
        return m_operator[0];
    }

    @Override
    public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
    {
        switch ( this )
        {
            case PLUS:
                return p_lhs.<Number>raw().doubleValue() + p_rhs.<Number>raw().doubleValue();

            case MINUS:
                return p_lhs.<Number>raw().doubleValue() - p_rhs.<Number>raw().doubleValue();

            case MULTIPLY:
                return p_lhs.<Number>raw().doubleValue() * p_rhs.<Number>raw().doubleValue();

            case DIVIDE:
                return p_lhs.<Number>raw().doubleValue() / p_rhs.<Number>raw().doubleValue();

            case MODULO:
                return CCommon.modulo( p_lhs.raw(), p_rhs.raw() );

            case POWER:
                return Math.pow( p_lhs.<Number>raw().doubleValue(), p_rhs.<Number>raw().doubleValue() );



            case OR:
                return p_lhs.<Boolean>raw() || p_rhs.<Boolean>raw();

            case AND:
                return p_lhs.<Boolean>raw() && p_rhs.<Boolean>raw();

            case XOR:
                return p_lhs.<Boolean>raw() ^ p_rhs.<Boolean>raw();



            case EQUAL:
                return checkequal( p_lhs, p_rhs );

            case NOTEQUAL:
                return !checkequal( p_lhs, p_rhs );



            case GREATER:
                return compare( p_lhs, p_rhs ) > 0;

            case GREATEREQUAL:
                return compare( p_lhs, p_rhs ) >= 0;

            case LESS:
                return compare( p_lhs, p_rhs ) < 0;

            case LESSEQUAL:
                return compare( p_lhs, p_rhs ) <= 0;



            default:
                throw new CIllegalArgumentException( org.lightjason.agentspeak.common.CCommon.languagestring( this, "unknownoperator", this ) );
        }
    }


    /**
     * equal method with number handling
     *
     * @param p_value1 term value
     * @param p_value2 term value
     * @return equality flag
     */
    @SuppressWarnings( "unchecked" )
    private static boolean checkequal( @Nonnull final ITerm p_value1, @Nonnull final ITerm p_value2 )
    {
        return ( p_value1.raw() instanceof Number ) && ( p_value2.raw() instanceof Number )
               ? Double.valueOf( p_value1.<Number>raw().doubleValue() ).equals( p_value2.<Number>raw().doubleValue() )
               : p_value1.raw().equals( p_value2.raw() );
    }


    /**
     * compares term types
     *
     * @param p_left left argument
     * @param p_right right argument
     * @return default compare values [-1,1]
     */
    private static int compare( @Nonnull final ITerm p_left, @Nonnull final ITerm p_right )
    {
        return CCommon.isssignableto( p_left.raw(), Number.class ) && CCommon.isssignableto( p_right.raw(), Number.class )
               ? comparenumber( map( p_left.raw() ), map( p_right.raw() ) )
               : compareobject( map( p_left.raw() ), map( p_right.raw() ) );
    }

    /**
     * compare method for any number type
     *
     * @param p_left left argument
     * @param p_right right argument
     * @return default compare values [-1,1]
     *
     * @tparam T number type
     */
    private static <T extends Number & Comparable<T>> int comparenumber( @Nonnull final T p_left, @Nonnull final T p_right )
    {
        return Double.compare( p_left.doubleValue(), p_right.doubleValue() );
    }


    /**
     * compare method for any object type
     *
     * @param p_left left argument
     * @param p_right right argument
     * @return default compare values [-1,1]
     *
     * @tparam T object type
     */
    private static <T extends Comparable<T>> int compareobject( @Nonnull final T p_left, @Nonnull final T p_right )
    {
        return p_left.compareTo( p_right );
    }

    /**
     * type mapping method
     *
     * @param p_value value
     * @tparam N return type
     * @tparam M value type
     * @return casted value
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    private static <N, M> N map( @Nonnull final M p_value )
    {
        return (N) p_value;
    }

    /**
     * returns operator of a string
     *
     * @param p_value string
     * @return operator
     */
    @Nonnull
    public static EBinaryOperator of( @Nonnull final String p_value )
    {
        return Arrays.stream( EBinaryOperator.values() )
                     .filter( i -> Arrays.stream( i.m_operator ).anyMatch( j -> j.equals( p_value ) ) )
                     .findFirst()
                     .orElseThrow( () -> new CIllegalArgumentException(
                         org.lightjason.agentspeak.common.CCommon.languagestring( EBinaryOperator.class, "unknownoperator", p_value ) )
                     );
    }

}
