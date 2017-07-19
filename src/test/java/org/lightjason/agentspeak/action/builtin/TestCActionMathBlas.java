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

package org.lightjason.agentspeak.action.builtin;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;

import org.lightjason.agentspeak.action.builtin.math.blas.CElementWise;
import org.lightjason.agentspeak.action.builtin.math.blas.CSize;
import org.lightjason.agentspeak.action.builtin.math.blas.CMultiply;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * test math blas functions
 */
public final class TestCActionMathBlas extends IBaseTest
{
    /**
     * testing matrix
     */
    private DoubleMatrix2D m_matrix1;

    /**
     * testing matrix
     */
    private DoubleMatrix2D m_matrix2;

    /**
     * testing vector
     */
    private DoubleMatrix1D m_vector;


    /**
     * test initialize
     */
    @Before
    public final void initialize()
    {
        m_matrix1 = new DenseDoubleMatrix2D( new double[][]{{2, 6}, {3, 8}} );
        m_matrix2 = new DenseDoubleMatrix2D( new double[][]{{2, 2}, {3, 1}} );
        m_vector = new DenseDoubleMatrix1D( new double[]{2, 5} );
    }

    /**
     * test size
     */
    @Test
    public final void size()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSize().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 4L );
    }

    /**
     * test multiply
     */
    @Test
    public final void multiply()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMultiply().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( m_matrix1, m_matrix2, m_matrix1, m_vector, m_vector, m_vector, m_vector, m_matrix2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertArrayEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{22.0, 10.0}, {30.0, 14.0}} );
        Assert.assertArrayEquals( l_return.get( 1 ).<DoubleMatrix1D>raw().toArray(), new double[]{34.0, 46.0}, 0 );
        Assert.assertArrayEquals( l_return.get( 2 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{4.0, 10.0}, {10.0, 25.0}} );
        Assert.assertArrayEquals( l_return.get( 3 ).<DoubleMatrix1D>raw().toArray(), new double[]{14.0, 11.0}, 0 );
    }

    /**
     * test elementwise for matrix
     */
    @Test
    public final void elementwisematrix()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CElementWise().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                    m_matrix1, "+", m_matrix2,
                    m_matrix1, "+", 5,
                    m_matrix1, "-", 5,
                    m_matrix1, "*", 5,
                    m_matrix1, "/", 2,
                    m_matrix1, "|+|", m_matrix2,
                    m_matrix1, "-", m_matrix2,
                    m_matrix1, "*", m_matrix2,
                    m_matrix1, "|+|", -9
                ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 9 );
        Assert.assertArrayEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{4.0, 8.0}, {6.0, 9.0}} );
        Assert.assertArrayEquals( l_return.get( 1 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{7.0, 11.0}, {8.0, 13.0}} );
        Assert.assertArrayEquals( l_return.get( 2 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{-3.0, 1.0}, {-2.0, 3.0}} );
        Assert.assertArrayEquals( l_return.get( 3 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{10.0, 30.0}, {15.0, 40.0}} );
        Assert.assertArrayEquals( l_return.get( 4 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{1.0, 3.0}, {1.5, 4.0}} );
        Assert.assertArrayEquals( l_return.get( 5 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{4.0, 8.0}, {6.0, 9.0}} );
        Assert.assertArrayEquals( l_return.get( 6 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{0.0, 4.0}, {0.0, 7.0}} );
        Assert.assertArrayEquals( l_return.get( 7 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{4.0, 12.0}, {9.0, 8.0}} );
        Assert.assertArrayEquals( l_return.get( 8 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{7.0, 3.0}, {6.0, 1.0}} );

    }

    /**
     * test elementwise for vector
     */
    @Test
    public final void elementwisevector()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CElementWise().execute(
            false, IContext.EMPTYPLAN,
            Stream.of(
                    m_vector, "+", m_vector,
                    m_vector, "+", 5,
                    m_vector, "-", 5,
                    m_vector, "*", 5,
                    m_vector, "/", 2,
                    m_vector, "-", m_vector,
                    m_vector, "*", m_vector,
                    m_vector, "|+|", -5
                ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 8 );
        Assert.assertArrayEquals( l_return.get( 0 ).<DoubleMatrix1D>raw().toArray(), new double[]{4.0, 10.0}, 0 );
        Assert.assertArrayEquals( l_return.get( 1 ).<DoubleMatrix1D>raw().toArray(), new double[]{7.0, 10.0}, 0 );
        Assert.assertArrayEquals( l_return.get( 2 ).<DoubleMatrix1D>raw().toArray(), new double[]{-3.0, 0.0}, 0 );
        Assert.assertArrayEquals( l_return.get( 3 ).<DoubleMatrix1D>raw().toArray(), new double[]{10.0, 25.0}, 0 );
        Assert.assertArrayEquals( l_return.get( 4 ).<DoubleMatrix1D>raw().toArray(), new double[]{1.0, 2.5}, 0 );
        Assert.assertArrayEquals( l_return.get( 5 ).<DoubleMatrix1D>raw().toArray(), new double[]{0.0, 0.0}, 0 );
        Assert.assertArrayEquals( l_return.get( 6 ).<DoubleMatrix1D>raw().toArray(), new double[]{4.0, 25.0}, 0 );
        Assert.assertArrayEquals( l_return.get( 7 ).<DoubleMatrix1D>raw().toArray(), new double[]{3.0, 0.0}, 0 );

    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionMathBlas().invoketest();
    }
}
