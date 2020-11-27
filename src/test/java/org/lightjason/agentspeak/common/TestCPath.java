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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
        Assertions.assertEquals( Stream.of( "a", "b", "c" ).collect( CPath.collect() ).toString(), "a/b/c" );
    }

    /**
     * test hash collision
     */
    @Test
    public void collision()
    {
        Assertions.assertNotEquals(
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
        Assertions.assertEquals( CPath.of( "foo/bar" ), "foo/bar" );
        Assertions.assertNotEquals( CPath.of( "foo/bar" ), CPath.of( "bar/foo" ) );
    }

    /**
     * test ctor
     */
    @Test
    public void ctor()
    {
        Assertions.assertTrue( new CPath().empty() );
        Assertions.assertEquals( "a/b/c", new CPath( "a", "b", "c" ).toString() );
    }

    /**
     * test starts- and ends-with
     */
    @Test
    public void startsendswith()
    {
        Assertions.assertTrue( CPath.of( "x/y/z" ).startswith( "x/y" ) );
        Assertions.assertTrue( CPath.of( "x/y/z" ).endswith( "y/z" ) );
    }

    /**
     * test lower and upper
     */
    @Test
    public void lowerupper()
    {
        final IPath l_path = CPath.of( "A/b/C/d" );

        Assertions.assertEquals( "a/b/c/d", l_path.lower().toString() );
        Assertions.assertEquals( "a/b/c/d", l_path.toString() );
        Assertions.assertEquals( "A/B/C/D", l_path.upper().toString() );
        Assertions.assertEquals( "A/B/C/D", l_path.toString() );
    }

    /**
     * test empty separator
     */
    @Test
    public void noseparator()
    {
        Assertions.assertThrows(
            NoSuchElementException.class,
            () -> CPath.of( "a/b" ).separator( "" )
        );
    }

    /**
     * test remove from path
     */
    @Test
    public void remove()
    {
        final IPath l_path = CPath.of( "x/y/z/a/b/c" );

        Assertions.assertEquals( "x/y/z/a/b/c", l_path.toString() );
        Assertions.assertEquals( "x/y/z", l_path.remove( 3, 6 ).toString() );
    }

    /**
     * test path remove suoofix
     */
    @Test
    public void suffix()
    {
        final IPath l_path = CPath.of( "u/v" );
        Assertions.assertEquals( "v", l_path.removesuffix() );
        Assertions.assertEquals( "u", l_path.toString() );
    }

    /**
     * test normalize
     */
    @Test
    public void normalize()
    {
        Assertions.assertEquals( "a/b/c", CPath.of( "a/b/./c" ).toString() );
        Assertions.assertEquals( "c", CPath.of( "a/b/../../c" ).toString() );
        Assertions.assertEquals( "c", CPath.of( "a/b/../../../c" ).toString() );
    }

    /**
     * test push calls
     */
    @Test
    public void push()
    {
        final IPath l_path = CPath.of( "a/b" );

        Assertions.assertEquals( "a/b", l_path.toString() );
        l_path.pushfront( "x/y" );
        Assertions.assertEquals( "x/y/a/b", l_path.toString() );
        l_path.pushback( "u/v" );
        Assertions.assertEquals( "x/y/a/b/u/v", l_path.toString() );
    }

    /**
     * test empty path
     */
    @Test
    public void emptypath()
    {
        Assertions.assertTrue( IPath.EMPTY.empty() );

        Assertions.assertEquals( "", IPath.EMPTY.toString() );

        Assertions.assertEquals( IPath.EMPTY, new CPath() );

        Assertions.assertEquals( 0, IPath.EMPTY.size() );

        Assertions.assertEquals( 0, IPath.EMPTY.compareTo( new CPath() ) );

        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.append( "a" ) );

        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.remove( 1 ) );
        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.remove( 1, 5 ) );

        Assertions.assertEquals( "", IPath.EMPTY.get( 3 ) );

        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.lower() );
        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.upper() );

        Assertions.assertEquals( "", IPath.EMPTY.suffix() );
        Assertions.assertEquals( "", IPath.EMPTY.removesuffix() );

        Assertions.assertEquals( 0, IPath.EMPTY.stream().count() );

        Assertions.assertFalse( IPath.EMPTY.startswith( "" ) );
        Assertions.assertFalse( IPath.EMPTY.endswith( "" ) );
        Assertions.assertFalse( IPath.EMPTY.startswith( CPath.of( "t" ) ) );
        Assertions.assertFalse( IPath.EMPTY.endswith( CPath.of( "t" ) ) );

        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.reverse() );

        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.pushback( "i" ) );
        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.pushfront( "j" ) );
        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.pushback( CPath.of( "i" ) ) );
        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.pushfront( CPath.of( "j" ) ) );

        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.separator( ":" ) );
        Assertions.assertEquals( "", IPath.EMPTY.separator() );

        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.append( CPath.of( "a" ) ) );

        Assertions.assertEquals( "", IPath.EMPTY.path( ":" ) );
        Assertions.assertEquals( "", IPath.EMPTY.path() );

        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.subpath( 5 ) );
        Assertions.assertEquals( IPath.EMPTY, IPath.EMPTY.subpath( 5, 7 ) );

    }
}
