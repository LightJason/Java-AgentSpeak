/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.passing.CPassRaw;
import org.lightjason.agentspeak.language.execution.passing.CPassVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


/**
 * test binary expression
 */
public final class TestCBinaryExpression extends IBaseTest
{
    /**
     * assignment operator
     *
     * @return 4-tuple as array, first & second input values, 3rd operator, 4th result
     */
    public static Stream<Arguments> operator()
    {
        final Object l_object = new Object();

        final Object l_compare1 = new CCompare( "foo" );
        final Object l_compare2 = new CCompare( "bar" );

        return Stream.of(

            Arguments.of( 5, 2, EBinaryOperator.PLUS, 7.0 ),
            Arguments.of( 3, 5, EBinaryOperator.MINUS, -2.0 ),

            Arguments.of( 12, 10, EBinaryOperator.MULTIPLY, 120.0 ),
            Arguments.of( 360, 12, EBinaryOperator.DIVIDE, 30.0 ),
            Arguments.of( 2, 8, EBinaryOperator.POWER, 256.0 ),

            Arguments.of( 21, 17, EBinaryOperator.MODULO, 4L ),
            Arguments.of( -1, 17, EBinaryOperator.MODULO, 16L ),
            Arguments.of( -18, 17, EBinaryOperator.MODULO,  1L ),

            Arguments.of( 1, 2, EBinaryOperator.LESS,  true ),
            Arguments.of( 2, 3, EBinaryOperator.LESSEQUAL,  true ),
            Arguments.of( 3, 3, EBinaryOperator.LESSEQUAL,  true ),
            Arguments.of( 3, 2, EBinaryOperator.LESSEQUAL,  false ),

            Arguments.of( 8, 6, EBinaryOperator.GREATER,  true ),
            Arguments.of( 8, 6, EBinaryOperator.GREATEREQUAL,  true ),
            Arguments.of( 8, 8, EBinaryOperator.GREATEREQUAL,  true ),
            Arguments.of( 6, 8, EBinaryOperator.GREATEREQUAL,  false ),

            Arguments.of( l_compare2, l_compare1, EBinaryOperator.LESS,  true ),
            Arguments.of( l_compare1, l_compare2, EBinaryOperator.LESS,  false ),
            Arguments.of( l_compare2, l_compare1, EBinaryOperator.LESSEQUAL,  true ),
            Arguments.of( l_compare2, l_compare2, EBinaryOperator.LESSEQUAL,  true ),

            Arguments.of( l_compare1, l_compare2, EBinaryOperator.GREATER,  true ),
            Arguments.of( l_compare2, l_compare1, EBinaryOperator.GREATER,  false ),
            Arguments.of( l_compare1, l_compare2, EBinaryOperator.GREATEREQUAL,  true ),
            Arguments.of( l_compare1, l_compare1, EBinaryOperator.GREATEREQUAL,  true ),

            Arguments.of( new Object(), new Object(), EBinaryOperator.EQUAL,  false ),
            Arguments.of( l_object, l_object, EBinaryOperator.EQUAL,  true ),
            Arguments.of( new Object(), new Object(), EBinaryOperator.NOTEQUAL,  true ),
            Arguments.of( l_object, l_object, EBinaryOperator.NOTEQUAL,  false ),

            Arguments.of( 5, 5, EBinaryOperator.EQUAL,  true ),
            Arguments.of( 5.0, 5, EBinaryOperator.EQUAL,  true ),
            Arguments.of( 5.0000, 5.0000001, EBinaryOperator.NOTEQUAL,  true ),

            Arguments.of( true, true, EBinaryOperator.OR, true ),
            Arguments.of( true, false, EBinaryOperator.OR, true ),
            Arguments.of( false, true, EBinaryOperator.OR, true ),
            Arguments.of( false, false, EBinaryOperator.OR, false ),

            Arguments.of( true, true, EBinaryOperator.AND, true ),
            Arguments.of( true, false, EBinaryOperator.AND, false ),
            Arguments.of( false, true, EBinaryOperator.AND, false ),
            Arguments.of( false, false, EBinaryOperator.AND, false ),

            Arguments.of( true, true, EBinaryOperator.XOR, false ),
            Arguments.of( true, false, EBinaryOperator.XOR, true ),
            Arguments.of( false, true, EBinaryOperator.XOR, true ),
            Arguments.of( false, false, EBinaryOperator.XOR, false )

        );
    }

    /**
     * test assignment operator with variables
     *
     * @param p_lhs left-hand-side argument
     * @param p_rhs right-hand-side argument
     * @param p_operator operator
     * @param p_result result
     */
    @ParameterizedTest
    @MethodSource( "operator" )
    @SuppressWarnings( "unchecked" )
    public void variable( @Nonnull final Object p_lhs, @Nonnull final Object p_rhs, @Nonnull final EBinaryOperator p_operator, @Nonnull final Object p_result )
    {
        final List<ITerm> l_return = new ArrayList<>();

        final IVariable<Object> l_lhs = new CVariable<>( "Lhs" ).set( p_lhs );
        final IVariable<Object> l_rhs = new CVariable<>( "Rhs" ).set( p_rhs );

        Assertions.assertTrue(
            execute(
                new CBinaryExpression(
                    p_operator,
                    new CPassVariable( l_lhs ),
                    new CPassVariable( l_rhs )
                ),
                false,
                Collections.emptyList(),
                l_return,
                l_lhs,
                l_rhs
            )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( p_lhs, l_lhs.raw() );
        Assertions.assertEquals( p_rhs,  l_rhs.raw() );
        Assertions.assertEquals( p_result, l_return.get( 0 ).raw() );
    }

    /**
     * test assignment operator with native data
     *
     * @param p_lhs left-hand-side argument
     * @param p_rhs right-hand-side argument
     * @param p_operator operator
     * @param p_result result
     */
    @ParameterizedTest
    @MethodSource( "operator" )
    @SuppressWarnings( "unchecked" )
    public void raw( @Nonnull final Object p_lhs, @Nonnull final Object p_rhs, @Nonnull final EBinaryOperator p_operator, @Nonnull final Object p_result )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CBinaryExpression(
                    p_operator,
                    new CPassRaw<>( p_lhs ),
                    new CPassRaw<>( p_result )
                ),
                false,
                Collections.emptyList(),
                l_return
            )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( p_result, l_return.get( 0 ).raw() );
    }

    /**
     * test comparable
     */
    private static final class CCompare implements Comparable<CCompare>
    {
        /**
         * data
         */
        private final String m_data;

        /**
         * ctor
         *
         * @param p_data string data
         */
        CCompare( @Nonnull final String p_data )
        {
            m_data = p_data;
        }

        @Override
        public int compareTo( @Nonnull final CCompare p_object )
        {
            return this.m_data.compareTo( p_object.m_data );
        }
    }

}
