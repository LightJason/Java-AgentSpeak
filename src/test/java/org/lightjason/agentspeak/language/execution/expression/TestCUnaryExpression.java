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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@RunWith( DataProviderRunner.class )
public final class TestCUnaryExpression extends IBaseTest
{
    /**
     * assignment operator
     *
     * @return 3-tuple as array, first & second input values, 3rd operator, 4th result
     */
    @DataProvider
    public static Object[] operator()
    {
        return Stream.of(

            testcase( true, EUnaryOperator.NEGATION, false ),
            testcase( false, EUnaryOperator.NEGATION, true )

        ).toArray();
    }

    /**
     * test-case generator
     *
     * @param p_value argument
     * @param p_operator operator
     * @param p_result result
     * @return test-case
     */
    private static Object testcase( @Nonnull final Object p_value, @Nonnull final EUnaryOperator p_operator, @Nonnull final Object p_result )
    {
        return Stream.of( p_value, p_operator, p_result ).toArray();
    }

    /**
     * test with variable
     */
    @Test
    @UseDataProvider( "operator" )
    @SuppressWarnings( "unchecked" )
    public void variable( @Nonnull final Object[] p_data )
    {
        Assume.assumeTrue( p_data.length == 3 );

        final List<ITerm> l_return = new ArrayList<>();

        final IVariable<Object> l_var = new CVariable<>( "Value" );
        l_var.set( p_data[0] );

        Assert.assertTrue(
            execute(
                new CUnaryExpression(
                    (EUnaryOperator) p_data[1],
                    new CPassVariable( l_var )
                ),
                false,
                Collections.emptyList(),
                l_return,
                l_var
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( p_data[2], l_return.get( 0 ).raw() );
        Assert.assertEquals( p_data[0], l_var.raw() );
    }

    /**
     * test with native data
     */
    @Test
    @UseDataProvider( "operator" )
    @SuppressWarnings( "unchecked" )
    public void raw( @Nonnull final Object[] p_data )
    {
        Assume.assumeTrue( p_data.length == 3 );

        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            execute(
                new CUnaryExpression(
                    (EUnaryOperator) p_data[1],
                    new CPassRaw<>( p_data[0] )
                ),
                false,
                Collections.emptyList(),
                l_return
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( p_data[2], l_return.get( 0 ).raw() );
    }


}
