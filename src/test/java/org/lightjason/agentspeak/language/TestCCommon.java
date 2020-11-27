/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test language common
 */
public final class TestCCommon extends IBaseTest
{
    /**
     * test compression
     */
    @Test
    public void compression()
    {
        Assertions.assertTrue(
            Arrays.stream( CCommon.ECompression.values() )
                  .allMatch( i -> CCommon.ECompression.exist( i.toString() ) )
        );

        Assertions.assertTrue(
            Arrays.stream( CCommon.ECompression.values() )
                  .allMatch( i -> CCommon.ECompression.of( i.toString() ).equals( i ) )
        );
    }

    /**
     * test levenshtein-distance
     */
    @Test
    public void levenshtein()
    {
        Assertions.assertEquals( 0.0, CCommon.levenshtein( "kitten", "kitten", 1, 10, 100 ), 0 );
        Assertions.assertEquals( 17.0, CCommon.levenshtein( "kitten", "sitting", 2, 20, 200 ), 0 );
        Assertions.assertEquals( 23.0, CCommon.levenshtein( "kitten", "singing", 3, 30, 300 ), 0 );
    }

    /**
     * test normalized-compression-distance
     */
    @Test
    public void ncd()
    {
        Assertions.assertArrayEquals(
            Stream.of( 0.0, 0.0, 0.0, 0.0, 0.0 ).toArray(),
            Arrays.stream( CCommon.ECompression.values() )
                  .map( i ->  CCommon.ncd( i, "xxx", "xxx" ) )
                  .toArray()
        );

        Assertions.assertArrayEquals(
            Stream.of( 0.05, 0.13043478260869565, 0.2727272727272727, Double.NaN, 0.0 ).toArray(),
            Arrays.stream( CCommon.ECompression.values() )
                  .map( i ->  CCommon.ncd( i, "bar", "box" ) )
                  .toArray()
        );

    }

    /**
     * test replace context
     */
    @Test
    public void replaceconntext()
    {
        final IVariable<?> l_variable = new CVariable<>( "F", 5 );

        Assertions.assertEquals(
            CCommon.replacebycontext(
                new CLocalContext(
                    IAgent.EMPTY,
                    l_variable
                ),
                new CVariable<>( "F" )
            ),
            l_variable
        );

        Assertions.assertThrows( NoSuchElementException.class, () -> CCommon.replacebycontext( IContext.EMPTYPLAN, new CVariable<>( "F" ) ) );
    }

    /**
     * test flatten stream
     */
    @Test
    public void flattenstream()
    {
        Assertions.assertArrayEquals(
            Stream.of( "stream", 1, 1, 2 ).toArray(),
            CCommon.flatten( Stream.of(
                CRawTerm.of( "stream" ),
                CRawTerm.of( 1 ),
                CRawTerm.of( Stream.of( 1, 2 ).collect( Collectors.toList() ) )
            ) ).map( ITerm::raw ).toArray()
        );
    }

}
