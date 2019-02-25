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

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionNewtonForm;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.math.interpolate.CCreate;
import org.lightjason.agentspeak.action.builtin.math.interpolate.CMultipleInterpolate;
import org.lightjason.agentspeak.action.builtin.math.interpolate.CSingleInterpolate;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * test for math interpolate
 */
public final class TestCActionMathInterpolate extends IBaseTest
{

    /**
     * test create
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "linear", 2, 3, 8, 11, 13, 20 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "divideddifference", 2, 3, 8, 11, 13, 20 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "neville", 2, 3, 8, 11, 13, 20 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "akima", 42, 65, 78, 87, 100, 150, 41, 63, 82, 98, 110, 200 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "loess", 42, 65, 78, 87, 100, 150, 300, 400, 500, 41, 63, 82, 98, 110, 200, 400, 600, 800 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 5, l_return.size() );
        assertTrue( l_return.get( 0 ).raw() instanceof PolynomialSplineFunction );
        assertTrue( l_return.get( 1 ).raw() instanceof PolynomialFunctionNewtonForm );
        assertTrue( l_return.get( 2 ).raw() instanceof PolynomialFunctionLagrangeForm );
        assertTrue( l_return.get( 3 ).raw() instanceof PolynomialSplineFunction );
        assertTrue( l_return.get( 4 ).raw() instanceof PolynomialSplineFunction );
    }

    /**
     * test single interpolate
     */
    @Test
    public void singleinterpolate()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSingleInterpolate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                    new LinearInterpolator().interpolate( new double[]{3, 6}, new double[]{11, 13} ), 3, 4
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );
        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( 11.0, l_return.get( 0 ).<Number>raw() );
        Assert.assertEquals( 11.666666666666666, l_return.get( 1 ).<Number>raw() );
    }

    /**
     * test multiple interpolate
     */
    @Test
    public void multipleinterpolate()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMultipleInterpolate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                    5,
                    new LinearInterpolator().interpolate( new double[]{3, 6}, new double[]{11, 13} ),
                    new NevilleInterpolator().interpolate( new double[]{2, 3, 8}, new double[]{11, 13, 20} )
                ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( 12.333333333333334, l_return.get( 0 ).<Number>raw() );
        Assert.assertEquals( 16.400000000000002, l_return.get( 1 ).<Number>raw() );
    }

}
