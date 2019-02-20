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


import org.lightjason.agentspeak.error.CNoSuchElementException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;


/**
 * expression binary operator
 */
public enum EBinaryOperator implements BiFunction<ITerm, ITerm, Object>
{
    PLUS( "+" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return p_lhs.<Number>raw().doubleValue() + p_rhs.<Number>raw().doubleValue();
        }
    },
    MINUS( "-" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return p_lhs.<Number>raw().doubleValue() - p_rhs.<Number>raw().doubleValue();
        }
    },
    MULTIPLY( "*" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return p_lhs.<Number>raw().doubleValue() * p_rhs.<Number>raw().doubleValue();
        }
    },
    DIVIDE( "/" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return p_lhs.<Number>raw().doubleValue() / p_rhs.<Number>raw().doubleValue();
        }
    },
    MODULO( "%", "mod" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return CCommon.modulo( p_lhs.raw(), p_rhs.raw() );
        }
    },
    POWER( "**" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return Math.pow( p_lhs.<Number>raw().doubleValue(), p_rhs.<Number>raw().doubleValue() );
        }
    },
    OR( "||", "or" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return p_lhs.<Boolean>raw() || p_rhs.<Boolean>raw();
        }
    },
    AND( "&&", "and" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return p_lhs.<Boolean>raw() && p_rhs.<Boolean>raw();
        }
    },
    XOR( "^", "xor" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return p_lhs.<Boolean>raw() ^ p_rhs.<Boolean>raw();
        }
    },


    EQUAL( "==" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return checkequal( p_lhs, p_rhs );
        }
    },
    NOTEQUAL( "\\==", "!=" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return !checkequal( p_lhs, p_rhs );
        }
    },


    LESS( "<" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return compare( p_lhs, p_rhs ) < 0;
        }
    },
    LESSEQUAL( "<=" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return compare( p_lhs, p_rhs ) <= 0;
        }
    },
    GREATER( ">" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return compare( p_lhs, p_rhs ) > 0;
        }
    },
    GREATEREQUAL( ">=" )
    {
        @Override
        public Object apply( @Nonnull final ITerm p_lhs, @Nonnull final ITerm p_rhs )
        {
            return compare( p_lhs, p_rhs ) >= 0;
        }
    };



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
               : Objects.isNull( p_value1.raw() ) && Objects.isNull( p_value2.raw() )
                 || Objects.nonNull( p_value1.raw() ) && p_value1.raw().equals( p_value2.raw() );
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
     * @return casted value
     *
     * @tparam N return type
     * @tparam M value type
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
                     .orElseThrow( () -> new CNoSuchElementException(
                         org.lightjason.agentspeak.common.CCommon.languagestring( EBinaryOperator.class, "unknownoperator", p_value ) )
                     );
    }

}
