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
 * test unary expression
 */
public final class TestCUnaryExpression extends IBaseTest
{
    /**
     * assignment operator
     *
     * @return 3-tuple as array, first & second input values, 3rd operator, 4th result
     */
    public static Stream<Arguments> operator()
    {
        return Stream.of(
            Arguments.of( true, EUnaryOperator.NEGATION, false ),
            Arguments.of( false, EUnaryOperator.NEGATION, true )
        );
    }

    /**
     * test with variable
     *
     * @param p_value argument
     * @param p_operator operator
     * @param p_result result
     */
    @ParameterizedTest
    @MethodSource( "operator" )
    @SuppressWarnings( "unchecked" )
    public void variable( final boolean p_value, @Nonnull final EUnaryOperator p_operator, final boolean p_result )
    {
        final List<ITerm> l_return = new ArrayList<>();

        final IVariable<Object> l_var = new CVariable<>( "Value" );
        l_var.set( p_value );

        Assertions.assertTrue(
            execute(
                new CUnaryExpression(
                    p_operator,
                    new CPassVariable( l_var )
                ),
                false,
                Collections.emptyList(),
                l_return,
                l_var
            )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( p_result, l_return.get( 0 ).raw() );
        Assertions.assertEquals( p_value, l_var.raw() );
    }

    /**
     * test with native data
     */
    @ParameterizedTest
    @MethodSource( "operator" )
    @SuppressWarnings( "unchecked" )
    public void raw( final boolean p_value, @Nonnull final EUnaryOperator p_operator, final boolean p_result )
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assertions.assertTrue(
            execute(
                new CUnaryExpression(
                    p_operator,
                    new CPassRaw<>( p_value )
                ),
                false,
                Collections.emptyList(),
                l_return
            )
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( p_result, l_return.get( 0 ).raw() );
    }

}
