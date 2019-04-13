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

    /**
     * test path remove suoofix
     */
    @Test
    public void suffix()
    {
        final IPath l_path = CPath.of( "u/v" );
        Assert.assertEquals( "v", l_path.removesuffix() );
        Assert.assertEquals( "u", l_path.toString() );
    }

    /**
     * test normalize
     */
    @Test
    public void normalize()
    {
        Assert.assertEquals( "a/b/c", CPath.of( "a/b/./c" ).toString() );
        Assert.assertEquals( "c", CPath.of( "a/b/../../c" ).toString() );
        Assert.assertEquals( "c", CPath.of( "a/b/../../../c" ).toString() );
    }

    /**
     * test push calls
     */
    @Test
    public void push()
    {
        final IPath l_path = CPath.of( "a/b" );

        Assert.assertEquals( "a/b", l_path.toString() );
        l_path.pushfront( "x/y" );
        Assert.assertEquals( "x/y/a/b", l_path.toString() );
        l_path.pushback( "u/v" );
        Assert.assertEquals( "x/y/a/b/u/v", l_path.toString() );
    }

    /**
     * test empty path
     */
    @Test
    public void emptypath()
    {
        Assert.assertTrue( IPath.EMPTY.empty() );

        Assert.assertEquals( "", IPath.EMPTY.toString() );

        Assert.assertEquals( IPath.EMPTY, new CPath() );

        Assert.assertEquals( 0, IPath.EMPTY.size() );

        Assert.assertEquals( 0, IPath.EMPTY.compareTo( new CPath() ) );

        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.append( "a" ) );

        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.remove( 1 ) );
        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.remove( 1, 5 ) );

        Assert.assertEquals( "", IPath.EMPTY.get( 3 ) );

        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.lower() );
        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.upper() );

        Assert.assertEquals( "", IPath.EMPTY.suffix() );
        Assert.assertEquals( "", IPath.EMPTY.removesuffix() );

        Assert.assertEquals( 0, IPath.EMPTY.stream().count() );

        Assert.assertFalse( IPath.EMPTY.startswith( "" ) );
        Assert.assertFalse( IPath.EMPTY.endswith( "" ) );
        Assert.assertFalse( IPath.EMPTY.startswith( CPath.of( "t" ) ) );
        Assert.assertFalse( IPath.EMPTY.endswith( CPath.of( "t" ) ) );

        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.reverse() );

        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.pushback( "i" ) );
        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.pushfront( "j" ) );
        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.pushback( CPath.of( "i" ) ) );
        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.pushfront( CPath.of( "j" ) ) );

        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.separator( ":" ) );
        Assert.assertEquals( "", IPath.EMPTY.separator() );

        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.append( CPath.of( "a" ) ) );

        Assert.assertEquals( "", IPath.EMPTY.path( ":" ) );
        Assert.assertEquals( "", IPath.EMPTY.path() );

        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.subpath( 5 ) );
        Assert.assertEquals( IPath.EMPTY, IPath.EMPTY.subpath( 5, 7 ) );

    }
}
