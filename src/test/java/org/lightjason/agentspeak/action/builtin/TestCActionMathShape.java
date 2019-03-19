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

package org.lightjason.agentspeak.action.builtin;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.shape.CInCircle;
import org.lightjason.agentspeak.action.shape.CInRectangle;
import org.lightjason.agentspeak.action.shape.CInTriangle;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * test math shape functions
 */
public final class TestCActionMathShape extends IBaseTest
{

    /**
     * test in circle
     */
    @Test
    public void incircle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInCircle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 1, 1, 2, 2.5, 0.5, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertFalse( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertTrue( l_return.get( 1 ).<Boolean>raw() );
    }

    /**
     * test in rechtangle
     */
    @Test
    public void inrechtangle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInRectangle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 0, 0, 100, 100, 40, 55, 100, 120 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertFalse( l_return.get( 1 ).<Boolean>raw() );
    }

    /**
     * test in triangle
     */
    @Test
    public void intriangle()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CInTriangle().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 250, 220, 25, 275, 40, 55, 60, 170, 310, 129 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
        Assert.assertFalse( l_return.get( 1 ).<Boolean>raw() );
    }

}
