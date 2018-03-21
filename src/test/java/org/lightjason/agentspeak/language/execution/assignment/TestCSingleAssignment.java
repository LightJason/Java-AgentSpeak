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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.passing.CPassVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test single assignment
 */
@RunWith( DataProviderRunner.class )
public final class TestCSingleAssignment
{

    /**
     * assignment operator
     *
     * @return 4-tuples as array, first & second input values, 3rd operator, 4th result
     */
    @DataProvider
    public static Object[] assignmentoperator()
    {
        return Stream.of(

            testcase( 5, 2, EAssignOperator.ASSIGNINCREMENT, 7.0 ),
            testcase( 7, 3, EAssignOperator.ASSIGNDECREMENT, 4.0 ),
            testcase( 11, 11, EAssignOperator.ASSIGNMULTIPLY, 121.0 ),
            testcase( 33, 3, EAssignOperator.ASSIGNDIVIDE, 11.0 )


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
    private static Object testcase( @Nonnull final Object p_lhs, @Nonnull final Object p_rhs, @Nonnull EAssignOperator p_operator, @Nonnull Object p_result )
    {
        return Stream.of( p_lhs, p_rhs, p_operator, p_result ).toArray();
    }


    /**
     * test assignment operator with variables
     * @param p_data input data
     */
    @Test
    @UseDataProvider( "assignmentoperator" )
    @SuppressWarnings( "unchecked" )
    public final void assignvariable( @Nonnull final Object[] p_data )
    {
        Assert.assertEquals( p_data.length, 4 );

        final IVariable<Object> l_lhs = new CVariable<>( "Lhs" );
        final IVariable<Object> l_rhs = new CVariable<>( "Rhs" );

        l_lhs.set( p_data[0] );
        l_rhs.set( p_data[1] );

        new CSingleAssignment(
            l_lhs,
            new CPassVariable( l_rhs ),
            (EAssignOperator) p_data[2]
        ).execute(
            false,
            new CContext( l_lhs, l_rhs ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( p_data[3], l_lhs.raw() );
        Assert.assertEquals( p_data[1],  l_rhs.raw() );
    }

    /**
    public final void modulopositivevariable()
    {
        final IVariable<Number> l_lhs = new CVariable<>( "Lhs" );
        final IVariable<Number> l_rhs = new CVariable<>( "Rhs" );

        l_lhs.set( 21 );
        l_rhs.set( 17 );

        new CSingleAssignment(
            l_lhs,
            new CPassVariable( l_rhs ),
            EAssignOperator.ASSIGNMODULO
        ).execute(
            false,
            new CContext( l_lhs, l_rhs ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_lhs.<Number>raw(), 4L );
        Assert.assertEquals( l_rhs.<Number>raw(), 17 );
    }

    public final void modulonegativevariable()
    {
        final IVariable<Number> l_lhs = new CVariable<>( "Lhs" );
        final IVariable<Number> l_rhs = new CVariable<>( "Rhs" );

        l_lhs.set( -1 );
        l_rhs.set( 17 );

        new CSingleAssignment(
            l_lhs,
            new CPassVariable( l_rhs ),
            EAssignOperator.ASSIGNMODULO
        ).execute(
            false,
            new CContext( l_lhs, l_rhs ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_lhs.<Number>raw(), 16L );
        Assert.assertEquals( l_rhs.<Number>raw(), 17 );


        l_lhs.set( -20 );
        l_rhs.set( 17 );

        new CSingleAssignment(
            l_lhs,
            new CPassVariable( l_rhs ),
            EAssignOperator.ASSIGNMODULO
        ).execute(
            false,
            new CContext( l_lhs, l_rhs ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        System.out.println( l_lhs );
    }
    */

    /**
     * local context
     */
    private static final class CContext implements IContext
    {
        /**
         * variable map
         */
        private final Map<IPath, IVariable<?>> m_variables;

        /**
         * ctor
         *
         * @param p_variables variables
         */
        CContext( @Nonnull final IVariable<?>... p_variables )
        {
            m_variables = Arrays.stream( p_variables )
                                .collect( Collectors.toMap( ITerm::fqnfunctor, i -> i ) );
        }

        @Nonnull
        @Override
        public final IAgent<?> agent()
        {
            return IAgent.EMPTY;
        }

        @Nonnull
        @Override
        public final IInstantiable instance()
        {
            return IPlan.EMPTY;
        }

        @Nonnull
        @Override
        public final Map<IPath, IVariable<?>> instancevariables()
        {
            return m_variables;
        }

        @Nonnull
        @Override
        public final IContext duplicate()
        {
            return this;
        }
    }
}
