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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;

import org.lightjason.agentspeak.action.buildin.math.shape.CInCircle;
import org.lightjason.agentspeak.action.buildin.math.shape.CInRectangle;
import org.lightjason.agentspeak.action.buildin.math.shape.CInTriangle;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * test math shape functions
 */
public class TestCActionMathShape extends IBaseTest
{

    /**
     * test in circle
     */
    @Test
    public final void incircle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInCircle().execute(
                null,
                false,
                Stream.of( 1, 1, 1, 2, 2.5 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).raw(), false );
    }

    /**
     * test in circle error
     * @bug l_return size should be 2l_return size should be 2
     */
    @Test
    @Ignore
    public final void incircleerror()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInCircle().execute(
                null,
                false,
                Stream.of( 1, 1, 1, 2, 2.5, 1, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
    }

    /**
     * test in rechtangle
     */
    @Test
    public final void inrechtangle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInRectangle().execute(
                null,
                false,
                Stream.of( 0, 0, 100, 100, 40, 55 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).raw(), true );
    }

    /**
     * test in rechtangle error
     * @bug l_return size should be 2
     */
    @Test
    @Ignore
    public final void inrechtangleerror()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInRectangle().execute(
                null,
                false,
                Stream.of( 10, 100, 100, 200, 40, 55, 100, 112 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
    }

    /**
     * test in triangle
     */
    @Test
    public final void intriangle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInTriangle().execute(
                null,
                false,
                Stream.of( 250, 220, 25, 275, 40, 55, 60, 170 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).raw(), true );
    }

    /**
     * test in intriangle error
     * @bug l_return size should be 2
     */
    @Test
    @Ignore
    public final void intriangleerror()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInTriangle().execute(
                null,
                false,
                Stream.of( 250, 220, 25, 275, 40, 55, 60, 170, 40, 100 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 2 );
    }

    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionMathShape().invoketest();
    }
}
