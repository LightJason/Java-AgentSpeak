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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.language.execution.passing.CPassRaw;
import org.lightjason.agentspeak.language.execution.passing.CPassVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.stream.Stream;


/**
 * test single assignment
 */
@RunWith( DataProviderRunner.class )
public final class TestCSingleAssignment extends IBaseTest
{

    /**
     * assignment operator
     *
     * @return 4-tuple as array, first & second input values, 3rd operator, 4th result
     */
    @DataProvider
    public static Object[] operator()
    {
        return Stream.of(

            testcase( 5, 2, EAssignOperator.INCREMENT, 7.0 ),
            testcase( 7, 3, EAssignOperator.DECREMENT, 4.0 ),

            testcase( 11, 11, EAssignOperator.MULTIPLY, 121.0 ),
            testcase( 33, 3, EAssignOperator.DIVIDE, 11.0 ),

            testcase( 21, 17, EAssignOperator.MODULO, 4L ),
            testcase( -1, 17, EAssignOperator.MODULO, 16L ),
            testcase( -18, 17, EAssignOperator.MODULO, 1L ),

            testcase( 2, 3, EAssignOperator.POWER, 8.0 ),
            testcase( 9, 0.5, EAssignOperator.POWER, 3.0 ),

            testcase( 2, 3, EAssignOperator.ASSIGN,  3 )

        ).toArray();
    }

    /**
     * test-case generator
     *
     * @param p_lhs left-hand-side argument
     * @param p_rhs right-hand-side argument
     * @param p_operator operator
     * @param p_result result
     * @return test-case
     */
    private static Object testcase( @Nonnull final Object p_lhs, @Nonnull final Object p_rhs,
                                    @Nonnull final EAssignOperator p_operator, @Nonnull final Object p_result )
    {
        return Stream.of( p_lhs, p_rhs, p_operator, p_result ).toArray();
    }


    /**
     * test assignment operator with variables
     * @param p_data input data
     */
    @Test
    @SuppressWarnings( "unchecked" )
    @UseDataProvider( "operator" )
    public void assignvariable( @Nonnull final Object[] p_data )
    {
        Assume.assumeTrue( p_data.length == 4 );

        final IVariable<Object> l_lhs = new CVariable<>( "Lhs" ).set( p_data[0] );
        final IVariable<Object> l_rhs = new CVariable<>( "Rhs" ).set( p_data[1] );

        Assert.assertTrue(
            new CSingleAssignment(
                (EAssignOperator) p_data[2], l_lhs,
                new CPassVariable( l_rhs )
            ).execute(
                false,
                new CLocalContext( l_lhs, l_rhs ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertEquals( p_data[3], l_lhs.raw() );
        Assert.assertEquals( p_data[1],  l_rhs.raw() );
    }

    /**
     * test assignment operator with native data
     * @param p_data input data
     */
    @Test
    @SuppressWarnings( "unchecked" )
    @UseDataProvider( "operator" )
    public void assignraw( @Nonnull final Object[] p_data )
    {
        Assume.assumeTrue( p_data.length == 4 );

        final IVariable<Object> l_lhs = new CVariable<>( "Lhs" ).set( p_data[0] );

        Assert.assertTrue(
            new CSingleAssignment(
                (EAssignOperator) p_data[2], l_lhs,
                new CPassRaw<>( p_data[1] )
            ).execute(
                false,
                new CLocalContext( l_lhs ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );

        Assert.assertEquals( p_data[3], l_lhs.raw() );
    }
}
