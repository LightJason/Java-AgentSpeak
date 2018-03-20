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

import org.junit.Assert;
import org.junit.Test;
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


/**
 * test single assignment
 */
public final class TestCSingleAssignment
{

    /**
     * plus assignment
     */
    @Test
    public final void plusvariable()
    {
        final IVariable<Number> l_lhs = new CVariable<>( "Lhs" );
        final IVariable<Number> l_rhs = new CVariable<>( "Rhs" );

        l_lhs.set( 5 );
        l_rhs.set( 2 );

        new CSingleAssignment(
            l_lhs,
            new CPassVariable( l_rhs ),
            EAssignOperator.ASSIGNINCREMENT
        ).execute(
            false,
            new CContext( l_lhs, l_rhs ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_lhs.<Number>raw(), 7.0 );
        Assert.assertEquals( l_rhs.<Number>raw(), 2 );
    }

    /**
     * minus assignment
     */
    @Test
    public final void minusvariable()
    {
        final IVariable<Number> l_lhs = new CVariable<>( "Lhs" );
        final IVariable<Number> l_rhs = new CVariable<>( "Rhs" );

        l_lhs.set( 7 );
        l_rhs.set( 3 );

        new CSingleAssignment(
            l_lhs,
            new CPassVariable( l_rhs ),
            EAssignOperator.ASSIGNDECREMENT
        ).execute(
            false,
            new CContext( l_lhs, l_rhs ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_lhs.<Number>raw(), 4.0 );
        Assert.assertEquals( l_rhs.<Number>raw(), 3 );
    }

    /**
     * multiply assignment
     */
    @Test
    public final void multiplyvariable()
    {
        final IVariable<Number> l_lhs = new CVariable<>( "Lhs" );
        final IVariable<Number> l_rhs = new CVariable<>( "Rhs" );

        l_lhs.set( 11 );
        l_rhs.set( 11 );

        new CSingleAssignment(
            l_lhs,
            new CPassVariable( l_rhs ),
            EAssignOperator.ASSIGNMULTIPLY
        ).execute(
            false,
            new CContext( l_lhs, l_rhs ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_lhs.<Number>raw(), 121.0 );
        Assert.assertEquals( l_rhs.<Number>raw(), 11 );
    }

    /**
     * divide assignment
     */
    @Test
    public final void dividevariable()
    {
        final IVariable<Number> l_lhs = new CVariable<>( "Lhs" );
        final IVariable<Number> l_rhs = new CVariable<>( "Rhs" );

        l_lhs.set( 33 );
        l_rhs.set( 3 );

        new CSingleAssignment(
            l_lhs,
            new CPassVariable( l_rhs ),
            EAssignOperator.ASSIGNDIVIDE
        ).execute(
            false,
            new CContext( l_lhs, l_rhs ),
            Collections.emptyList(),
            Collections.emptyList()
        );

        Assert.assertEquals( l_lhs.<Number>raw(), 11.0 );
        Assert.assertEquals( l_rhs.<Number>raw(), 3 );
    }

    /**
     * divide assignment
     */
    @Test
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

    /**
     * divide assignment
     */
    @Test
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
