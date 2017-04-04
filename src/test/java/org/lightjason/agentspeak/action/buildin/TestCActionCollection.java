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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.action.buildin.collection.CSize;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test collection action
 */
@RunWith( DataProviderRunner.class )
public final class TestCActionCollection
{

    /**
     * data provider generator
     * @return data
     */
    @DataProvider
    public static Object[] generate()
    {
        return Stream.of(

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( new Object() ) ).collect( Collectors.toList() ),
                new int[]{0}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( new ArrayList<>() ) ).collect( Collectors.toList() ),
                new int[]{0}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( Stream.of( 1, "test" ).collect( Collectors.toList() ) ) ).collect( Collectors.toList() ),
                new int[]{2}
            ),

            new ImmutablePair<>(
                Stream.of( CRawTerm.from( Stream.of( 1, 1, "test" ).collect( Collectors.toSet() ) ) ).collect( Collectors.toList() ),
                new int[]{2}
            ),

            new ImmutablePair<>(
                Stream.of(
                    CRawTerm.from( Stream.of( "abcd", "xyz", 12, 12 ).collect( Collectors.toSet() ) ),
                    CRawTerm.from( Stream.of( 1, 2, 3, 3, 4, 4 ).collect( Collectors.toList() )  )
                ).collect( Collectors.toList() ),
                new int[]{3, 6}
            )

        ).toArray();
    }

    /**
     * test size
     */
    @Test
    @UseDataProvider( "generate" )
    public final void testsize( final Pair<List<ITerm>, int[]> p_input )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSize().execute(
            null,
            false,
            p_input.getLeft(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertArrayEquals(
            MessageFormat.format( "elements {0}", p_input.getLeft() ),
            l_return.stream().map( ITerm::<Number>raw ).mapToInt( Number::intValue ).toArray(),
            p_input.getRight()
        );
    }


    /**
     * test call
     *
     * @param p_args command line arguments
     */
    @SuppressWarnings( "unchecked" )
    public static void main( final String[] p_args )
    {
        Arrays.stream( TestCActionCollection.generate() )
              .map( i -> (Pair<List<ITerm>, int[]>) i )
              .forEach( i -> new TestCActionCollection().testsize( i ) );
    }
}
