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

package org.lightjason.agentspeak.common;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.NoSuchElementException;
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
    public void collector()
    {
        Assert.assertEquals( Stream.of( "a", "b", "c" ).collect( CPath.collect() ).toString(), "a/b/c" );
    }

    /**
     * test hash collision
     */
    @Test
    public void collision()
    {
        Assert.assertNotEquals(
            CPath.of( "hashcollision/Ea" ),
            CPath.of( "hashcollision/FB" )
        );
    }

    /**
     * compare string and path
     */
    @Test
    public void comparing()
    {
        Assert.assertEquals( CPath.of( "foo/bar" ), "foo/bar" );
        Assert.assertNotEquals( CPath.of( "foo/bar" ), CPath.of( "bar/foo" ) );
    }

    /**
     * test ctor
     */
    @Test
    public void ctor()
    {
        Assert.assertTrue( new CPath().empty() );
        Assert.assertEquals( "a/b/c", new CPath( "a", "b", "c" ).toString() );
    }

    /**
     * test starts- and ends-with
     */
    @Test
    public void startsendswith()
    {
        Assert.assertTrue( CPath.of( "x/y/z" ).startswith( "x/y" ) );
        Assert.assertTrue( CPath.of( "x/y/z" ).endswith( "y/z" ) );
    }

    /**
     * test lower and upper
     */
    @Test
    public void lowerupper()
    {
        final IPath l_path = CPath.of( "A/b/C/d" );

        Assert.assertEquals( "a/b/c/d", l_path.lower().toString() );
        Assert.assertEquals( "a/b/c/d", l_path.toString() );
        Assert.assertEquals( "A/B/C/D", l_path.upper().toString() );
        Assert.assertEquals( "A/B/C/D", l_path.toString() );
    }

    /**
     * test empty separator
     */
    @Test( expected = NoSuchElementException.class )
    public void noseparator()
    {
        CPath.of( "a/b" ).separator( "" );
    }

    /**
     * test remove from path
     */
    @Test
    public void remove()
    {
        final IPath l_path = CPath.of( "x/y/z/a/b/c" );

        Assert.assertEquals( "x/y/z/a/b/c", l_path.toString() );
        Assert.assertEquals( "x/y/z", l_path.remove( 3, 6 ).toString() );
    }
}
