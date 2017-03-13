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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.action.buildin.bool.CAllMatch;
import org.lightjason.agentspeak.action.buildin.bool.CAnd;
import org.lightjason.agentspeak.action.buildin.bool.CAnyMatch;
import org.lightjason.agentspeak.action.buildin.bool.COr;
import org.lightjason.agentspeak.action.buildin.bool.CXor;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for boolean actions
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionBool
{

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.of(
            Stream.of( true, false, true, false, false, true ).collect( Collectors.toList() )
        ).toArray();
    }


    /**
     * test all-match
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void allmatch( final List<Boolean> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAllMatch().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertFalse( l_return.get( 0 ).<Boolean>raw() );
    }


    /**
     * test and
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void and( final List<Boolean> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAnd().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertFalse( l_return.get( 0 ).<Boolean>raw() );
    }


    /**
     * test any-match
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void anymatch( final List<Boolean> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CAnyMatch().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
    }


    /**
     * test or
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void or( final List<Boolean> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new COr().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
    }


    /**
     * test xor
     *
     * @param p_input test arguments
     */
    @Test
    @UseDataProvider( "generate" )
    public final void xor( final List<Boolean> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CXor().execute(
            null,
            false,
            p_input.stream().map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertTrue( l_return.get( 0 ).<Boolean>raw() );
    }


    /**
     * main test call
     *
     * @param p_args arguments
     */
    @SuppressWarnings( "unchecked" )
    public static void main( final String[] p_args )
    {
        final TestCActionBool l_test = new TestCActionBool();

        Arrays.stream( TestCActionBool.generate() )
              .map( i -> (List<Boolean>) i )
              .forEach( i -> {
                  l_test.allmatch( i );
                  l_test.and( i );
                  l_test.anymatch( i );
                  l_test.or( i );
                  l_test.xor( i );
              } );
    }
}
