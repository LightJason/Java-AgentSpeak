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
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.collection.tuple.CCreate;
import org.lightjason.agentspeak.action.buildin.collection.tuple.CFlat;
import org.lightjason.agentspeak.action.buildin.collection.tuple.CSet;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test collection tuple
 */
public final class TestCActionCollectionTuple extends IBaseTest
{

    /**
     * test tuple creating
     */
    @Test
    public final void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, null,
            Stream.of( "abcd", 123, "foobar", true ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 2 );

        Assert.assertEquals( l_return.get( 0 ).<AbstractMap.Entry<String, ?>>raw().getKey(), "abcd" );
        Assert.assertEquals( l_return.get( 0 ).<AbstractMap.Entry<?, Number>>raw().getValue(), 123 );

        Assert.assertEquals( l_return.get( 1 ).<AbstractMap.Entry<String, ?>>raw().getKey(), "foobar" );
        Assert.assertTrue( l_return.get( 1 ).<AbstractMap.Entry<?, Boolean>>raw().getValue() );
    }

    /**
     * test tuple creating error
     */
    @Test
    public final void createerror()
    {
        Assert.assertFalse(
            new CCreate().execute(
                false, null,
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }


    /**
     * test tuple set
     */
    @Test
    public final void set()
    {
        final AbstractMap.Entry<String, String> l_data = new AbstractMap.SimpleEntry<>( "foo", "bar" );

        new CSet().execute(
            false, null,
            Stream.of( "blubblub", l_data ).map( CRawTerm::from ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( l_data.getValue(), "blubblub" );
    }


    /**
     * test tuple flat
     */
    @Test
    public final void flat()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CFlat().execute(
            false, null,
            Stream.of( new AbstractMap.SimpleEntry<>( "foo", "bar" ), new AbstractMap.SimpleEntry<>( 1, 2 ) ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( l_return.size(), 4 );

        Assert.assertEquals( l_return.get( 0 ).raw(), "foo" );
        Assert.assertEquals( l_return.get( 1 ).raw(), "bar" );

        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 1 );
        Assert.assertEquals( l_return.get( 3 ).<Number>raw(), 2 );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionCollectionTuple().invoketest();
    }

}
