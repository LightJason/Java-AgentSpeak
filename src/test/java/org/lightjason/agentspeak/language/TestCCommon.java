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

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.Arrays;
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
        Assert.assertTrue(
            Arrays.stream( CCommon.ECompression.values() )
                  .allMatch( i -> CCommon.ECompression.exist( i.toString() ) )
        );

        Assert.assertTrue(
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
        Assert.assertEquals( 0.0, CCommon.levenshtein( "kitten", "kitten", 1, 10, 100 ), 0 );
        Assert.assertEquals( 17.0, CCommon.levenshtein( "kitten", "sitting", 2, 20, 200 ), 0 );
        Assert.assertEquals( 23.0, CCommon.levenshtein( "kitten", "singing", 3, 30, 300 ), 0 );
    }

    /**
     * test normalized-compression-distance
     */
    @Test
    public void ncd()
    {
        Assert.assertArrayEquals(
            Stream.of( 0.0, 0.0, 0.0, 0.0, 0.0 ).toArray(),
            Arrays.stream( CCommon.ECompression.values() )
                  .map( i ->  CCommon.ncd( i, "xxx", "xxx" ) )
                  .toArray()
        );

        Assert.assertArrayEquals(
            Stream.of( 0.05, 0.13043478260869565, 0.2727272727272727, Double.NaN, 0.0 ).toArray(),
            Arrays.stream( CCommon.ECompression.values() )
                  .map( i ->  CCommon.ncd( i, "bar", "box" ) )
                  .toArray()
        );

    }
}
