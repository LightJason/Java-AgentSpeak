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

package org.lightjason.agentspeak.common;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;

import java.util.stream.Stream;


/**
 * test of path structure
 */
public final class TestCPath extends IBaseTest
{

    /**
     * test stream collector
     */
    @Test
    public final void collector()
    {
        Assert.assertEquals( Stream.of( "a", "b", "c" ).collect( CPath.collect() ).toString(), "a/b/c" );
    }

    /**
     * test hash collision
     */
    @Test
    public final void collision()
    {
        Assert.assertNotEquals(
            CPath.from( "hashcollision/Ea" ),
            CPath.from( "hashcollision/FB" )
        );
    }

    /**
     * compare string and path
     */
    @Test
    public final void comparing()
    {
        Assert.assertEquals( CPath.from( "foo/bar" ), "foo/bar" );
        Assert.assertNotEquals( CPath.from( "foo/bar" ), CPath.from( "bar/foo" ) );
    }


    /**
     * manual test
     *
     * @param p_args commandline arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCPath().invoketest();
    }

}
