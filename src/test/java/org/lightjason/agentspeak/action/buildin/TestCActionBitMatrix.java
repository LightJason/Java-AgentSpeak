/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

import cern.colt.bitvector.BitMatrix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CCreate;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CNAnd;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CAnd;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CColumns;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CCopy;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CNot;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CHammingDistance;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CBoolValue;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CDimension;
import org.lightjason.agentspeak.action.buildin.math.bit.matrix.CFalseCount;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * test math bit matrix functions
 */
public class TestCActionBitMatrix extends IBaseTest
{
    private final BitMatrix m_matrix = new BitMatrix( 2, 2 );
    private final BitMatrix m_matrix1 = new BitMatrix( 2, 2 );

    /**
     * initialize class with static data for routing algorithm test
     */
    @Before
    public void initialize()
    {
        m_matrix.put( 0, 1, false );
        m_matrix.put( 1, 0, false );
        m_matrix.put( 1, 1, true );
        m_matrix.put( 0, 0, true );

        m_matrix1.put( 0, 1, true );
        m_matrix1.put( 1, 0, true );
        m_matrix1.put( 1, 1, true );
        m_matrix1.put( 0, 0, false );

    }


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
                Stream.of( 2, 2 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<BitMatrix>raw(), new BitMatrix( 2, 2 ) );
    }

    /**
     * test and
     */
    @Test
    public final void and()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAnd().execute(
                null,
                false,
                Stream.of( m_matrix, m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertEquals( m_matrix1.get( 0, 1 ), false );
        Assert.assertEquals( m_matrix1.get( 1, 0 ), false );
        Assert.assertEquals( m_matrix1.get( 1, 1 ), true );
        Assert.assertEquals( m_matrix1.get( 0, 0 ), false );
    }

    /**
     * test boolean value
     */
    @Test
    public final void boolValue()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBoolValue().execute(
                null,
                false,
                Stream.of( m_matrix1, 0, 0 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Boolean>raw(), false );

    }


    /**
     * test copy
     */
    @Test
    public final void copy()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCopy().execute(
                null,
                false,
                Stream.of( m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<BitMatrix>raw(), m_matrix1  );

    }

    /**
     * test columns
     */
    @Test
    public final void columns()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CColumns().execute(
                null,
                false,
                Stream.of( m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 2L  );
    }

    /**
     * test dimension
     */
    @Test
    public final void dimension()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CDimension().execute(
                null,
                false,
                Stream.of( m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 2L  );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 2L  );
    }

    /**
     * test false count
     */
    @Test
    public final void falseCount()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CFalseCount().execute(
                null,
                false,
                Stream.of( m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 1L  );

    }

    /**
     * test HammingDistance
     */
    @Test
    public final void hammingDistance()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CHammingDistance().execute(
                null,
                false,
                Stream.of( m_matrix1, m_matrix ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 3L  );

    }

    /**
     * test nand
     */
    @Test
    @Ignore
    public final void nand()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CNAnd().execute(
                null,
                false,
                Stream.of( m_matrix, m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertEquals( m_matrix1.get( 0, 1 ), true );
        Assert.assertEquals( m_matrix1.get( 1, 0 ), true );
        Assert.assertEquals( m_matrix1.get( 1, 1 ), false );
        Assert.assertEquals( m_matrix1.get( 0, 0 ), true );

    }

    /**
     * test not
     */
    @Test
    public final void not()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CNot().execute(
                null,
                false,
                Stream.of( m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 0 );
        Assert.assertEquals( m_matrix1.get( 0, 1 ), false );
        Assert.assertEquals( m_matrix1.get( 1, 0 ), false );
        Assert.assertEquals( m_matrix1.get( 1, 1 ), false );
        Assert.assertEquals( m_matrix1.get( 0, 0 ), true );

    }



    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionBitMatrix().invoketest();
    }
}
