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

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.algo.DoubleFormatter;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import cern.colt.matrix.tobject.impl.DenseObjectMatrix2D;
import cern.colt.matrix.tobject.impl.SparseObjectMatrix2D;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.grid.CDenseGrid;
import org.lightjason.agentspeak.action.builtin.grid.CIsEmpty;
import org.lightjason.agentspeak.action.builtin.grid.CRemove;
import org.lightjason.agentspeak.action.builtin.grid.CSet;
import org.lightjason.agentspeak.action.builtin.grid.CSparseGrid;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDirection;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDistance;
import org.lightjason.agentspeak.action.builtin.grid.routing.star.CAStar;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test grid actions
 */
public final class TestCActionGrid extends IBaseTest
{

    /**
     * test dense-grid generating
     */
    @Test
    public void dense()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CDenseGrid().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( 2, 2, 3, 5 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );

        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DenseObjectMatrix2D );
        Assert.assertEquals( 2, l_return.get( 0 ).<ObjectMatrix2D>raw().rows() );
        Assert.assertEquals( 2, l_return.get( 0 ).<ObjectMatrix2D>raw().columns() );

        Assert.assertTrue( l_return.get( 1 ).raw() instanceof DenseObjectMatrix2D );
        Assert.assertEquals( 3, l_return.get( 1 ).<ObjectMatrix2D>raw().rows() );
        Assert.assertEquals( 5, l_return.get( 1 ).<ObjectMatrix2D>raw().columns() );
    }

    /**
     * test sparse-grid generating
     */
    @Test
    public void sparse()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSparseGrid().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( 4, 7, 1, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );

        Assert.assertTrue( l_return.get( 0 ).raw() instanceof SparseObjectMatrix2D );
        Assert.assertEquals( 4, l_return.get( 0 ).<ObjectMatrix2D>raw().rows() );
        Assert.assertEquals( 7, l_return.get( 0 ).<ObjectMatrix2D>raw().columns() );

        Assert.assertTrue( l_return.get( 1 ).raw() instanceof SparseObjectMatrix2D );
        Assert.assertEquals( 1, l_return.get( 1 ).<ObjectMatrix2D>raw().rows() );
        Assert.assertEquals( 1, l_return.get( 1 ).<ObjectMatrix2D>raw().columns() );
    }

    /**
     * test is-empty action
     */
    @Test
    public void isempty()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final ObjectMatrix2D l_grid = new SparseObjectMatrix2D( 3, 3 );
        l_grid.setQuick( 0, 1, new Object() );

        new CIsEmpty().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( l_grid, 1, 1, 0, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() );
        Assert.assertFalse( l_return.get( 1 ).raw() );
    }

    /**
     * test set action
     */
    @Test
    public void set()
    {
        final ObjectMatrix2D l_grid = new SparseObjectMatrix2D( 3, 3 );

        execute(
            new CSet(),
            false,
            Stream.of( l_grid, 2, 1, new Object(), 0, 0, new Object() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertTrue( Objects.nonNull( l_grid.getQuick( 2, 1 ) ) );
        Assert.assertTrue( Objects.nonNull( l_grid.getQuick( 0, 0 ) ) );
    }

    /**
     * test set action with avoid set
     */
    @Test
    public void setwithcheck()
    {
        final ObjectMatrix2D l_grid = new SparseObjectMatrix2D( 3, 3 );

        execute(
            new CSet( ( g, r, c ) -> r.intValue() == 1 && c.intValue() == 1 ),
            false,
            Stream.of( l_grid, 1, 2, new Object(), 1, 1, new Object() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertTrue( Objects.nonNull( l_grid.getQuick( 1, 2 ) ) );
        Assert.assertFalse( Objects.nonNull( l_grid.getQuick( 1, 1 ) ) );
    }

    /**
     * test remove action
     */
    @Test
    public void remove()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final ObjectMatrix2D l_grid = new SparseObjectMatrix2D( 3, 3 );
        l_grid.setQuick( 1, 1,  999 );

        execute(
            new CRemove(),
            false,
            Stream.of( l_grid, 1, 1 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( Objects.isNull( l_grid.getQuick( 1, 1 ) ) );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Number );
        Assert.assertEquals( 999, l_return.get( 0 ).<Number>raw() );
    }

    /**
     * test remove action with check
     */
    @Test
    public void removewithcheck()
    {
        final List<ITerm> l_return = new ArrayList<>();
        final ObjectMatrix2D l_grid = new SparseObjectMatrix2D( 3, 3 );
        l_grid.setQuick( 1, 1,  777 );

        execute(
            new CRemove( ( g, r, c ) -> r.intValue() == 1 && c.intValue() == 1 ),
            false,
            Stream.of( l_grid, 1, 1, 2, 2 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertTrue( Objects.nonNull( l_grid.getQuick( 1, 1 ) ) );
        Assert.assertTrue( Objects.isNull( l_return.get( 0 ).raw() ) );
    }

    /**
     * test directions
     */
    @Test
    public void direction()
    {
        final DoubleMatrix1D l_position = new DenseDoubleMatrix1D( 2 );

        Assert.assertArrayEquals(
            EDirection.NORTH.apply( l_position ).toArray(),
            new double[]{-1, 0},
            0
        );

        Assert.assertArrayEquals(
            EDirection.EAST.apply( l_position ).toArray(),
            new double[]{0, 1},
            0
        );

        Assert.assertArrayEquals(
            EDirection.SOUTH.apply( l_position ).toArray(),
            new double[]{1, 0},
            0
        );

        Assert.assertArrayEquals(
            EDirection.WEST.apply( l_position ).toArray(),
            new double[]{0, -1},
            0
        );

        Assert.assertArrayEquals(
            EDirection.NORTHEAST.apply( l_position ).toArray(),
            new double[]{-1, 1},
            0
        );

        Assert.assertArrayEquals(
            EDirection.NORTHWEST.apply( l_position ).toArray(),
            new double[]{-1, -1},
            0
        );

        Assert.assertArrayEquals(
            EDirection.SOUTHEAST.apply( l_position ).toArray(),
            new double[]{1, 1},
            0
        );

        Assert.assertArrayEquals(
            EDirection.SOUTHWEST.apply( l_position ).toArray(),
            new double[]{1, -1},
            0
        );
    }

    /**
     * distance algorithms
     */
    @Test
    public void distance()
    {
        final DoubleMatrix1D l_value1 = new DenseDoubleMatrix1D( new double[]{3, 4} );
        final DoubleMatrix1D l_value2 = new DenseDoubleMatrix1D( new double[]{7, 2} );

        Assert.assertEquals( 6D, EDistance.MANHATTAN.apply( l_value1, l_value2 ) );
        Assert.assertEquals( 4.47213595499958, EDistance.EUCLIDEAN.apply( l_value1, l_value2 ) );
        Assert.assertEquals( 4D, EDistance.CHEBYSHEV.apply( l_value1, l_value2 ) );
        Assert.assertEquals( 4.82842712474619, EDistance.OCTILE.apply( l_value1, l_value2 ) );
    }

    /**
     * test a-star test
     */
    @Test
    public void astar()
    {
        final DoubleFormatter l_formatter = new DoubleFormatter();
        l_formatter.setRowSeparator( "; " );
        l_formatter.setColumnSeparator( "," );
        l_formatter.setPrintShape( false );

        final int[][] l_matrix = new int[][]{{0, 0, 0, 1, 0}, {1, 0, 0, 0, 1}, {0, 0, 1, 0, 0}};
        final ObjectMatrix2D l_grid = new SparseObjectMatrix2D( l_matrix.length, l_matrix[0].length );

        IntStream.range( 0, l_matrix.length )
                 .forEach( r -> IntStream.range( 0, l_matrix[r].length )
                                         .filter( c -> l_matrix[r][c] == 1 )
                                         .forEach( c -> l_grid.set( r, c, new Object() ) ) );

        new CAStar().apply(
            l_grid,
            new DenseDoubleMatrix1D( new double[]{2, 1} ),
            new DenseDoubleMatrix1D( new double[]{2, 4} )
        ).map( l_formatter::toString ).forEach( System.out::println );
    }

}
