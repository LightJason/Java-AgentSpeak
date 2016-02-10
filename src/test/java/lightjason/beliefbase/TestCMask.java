/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.beliefbase;

import lightjason.language.CLiteral;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;


/**
 *
 */
public final class TestCMask
{

    /**
     * test tree structure
     */
    @Test
    public void testTree()
    {
        final int l_max = 10;
        final IMask l_beliefbase = new CBeliefBase( new CBeliefStorage<>() ).create( "root" );

        IntStream.range( 0, l_max ).boxed().forEach(
                i -> l_beliefbase.add( new CLiteral( RandomStringUtils.random( 10, "abcdefghijklmnopqrstuvwxyz".toCharArray() ) ) ) );

        assertEquals( "number of beliefs is incorrect", l_beliefbase.size(), l_max );
    }

}
