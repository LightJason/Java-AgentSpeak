/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;

import org.lightjason.agentspeak.action.buildin.math.linearprogram.CCreate;
import org.lightjason.agentspeak.action.buildin.math.linearprogram.CSolve;
import org.lightjason.agentspeak.action.buildin.math.linearprogram.CValueConstraint;
import org.lightjason.agentspeak.action.buildin.math.linearprogram.CEquationConstraint;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * test math linear program functions
 */
public class TestCActionMathLinearprogram extends IBaseTest
{
    /**
     * testing linear program
     */
    private ImmutablePair<LinearObjectiveFunction, Collection<LinearConstraint>> m_linearprogram =
            new ImmutablePair( new LinearObjectiveFunction( new double[]{}, 0.0 ), new HashSet<LinearConstraint>() );

    /**
     * testing linear program
     */
    private ImmutablePair<LinearObjectiveFunction, Collection<LinearConstraint>> m_linearprogram1 =
            new ImmutablePair( new LinearObjectiveFunction( new double[]{2, 3}, 5.0 ), new HashSet<LinearConstraint>() );

    /**
     * test create
     */
    @Test
    public final void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
                null,
                false,
                Stream.of( 2, 3, 4, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        assertTrue( l_return.get( 0 ).<ImmutablePair>raw().getLeft() instanceof LinearObjectiveFunction );
    }

    /**
     * test equation constraint
     * @bug in getcoefficients
     */
    @Test
    @Ignore
    public final void equationconstraint()
    {
        final List<ITerm> l_return = new ArrayList<>();
        LinearConstraint l_result = new LinearConstraint( new double[]{2, 7, 12}, 19.0, Relationship.EQ, new double[]{1, 2, 3}, 5.0 );

        new CEquationConstraint().execute(
                null,
                false,
                Stream.of( m_linearprogram, 2.0, 7.0, 12.0, 19.0, "=", 1.0, 2.0, 3.0, 5.0 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertEquals( m_linearprogram.getRight().size(), 1 );

        Assert.assertEquals( l_result.getValue(),  m_linearprogram.getRight().iterator().next().getValue(), 0 );
        Assert.assertEquals( l_result.getRelationship(), m_linearprogram.getRight().iterator().next().getRelationship() );
        Assert.assertEquals( l_result.getCoefficients(),  m_linearprogram.getRight().iterator().next().getCoefficients() );

    }

    /**
     * test solve error
     * @bug unbound solution exception
     */
    @Test
    @Ignore
    public final void solveerror()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSolve().execute(
                null,
                false,
                Stream.of( m_linearprogram1, "maximize" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

    }

    /**
     * test solve
     */
    @Test
    public final void solve()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSolve().execute(
                null,
                false,
                Stream.of( m_linearprogram1, "minimize", "non-negative" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertArrayEquals( l_return.stream().map( ITerm::raw ).toArray(), Stream.of( 5.0, 2, 0.0, 0.0 ).toArray() );

    }

    /**
     * test value constraint
     */
    @Test
    public final void valueconstraint()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CValueConstraint().execute(
                null,
                false,
                Stream.of( m_linearprogram, 2.0, 2.0, 12.0, 19.0, "=", 11.0 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertEquals( m_linearprogram.getRight().size(), 1 );

        Assert.assertEquals( 11.0,  m_linearprogram.getRight().iterator().next().getValue(), 0 );
    }




    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionMathLinearprogram().invoketest();
    }
}
