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

package org.lightjason.agentspeak.beliefbase;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.beliefbase.view.CView;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.beliefbase.view.IViewGenerator;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test of beliefbase view
 */
public final class TestCView extends IBaseTest
{

    /**
     * random test tree structure
     */
    @Test
    public void tree()
    {
        final int l_max = 1000;
        final IView l_beliefbase = new CBeliefbase( new CMultiStorage<>() ).create( "root" );
        final IViewGenerator l_generator = new CGenerator();

        IntStream.range( 0, l_max )
                 .boxed()
                 .map( i -> CLiteral.of( RandomStringUtils.random( 12, "~abcdefghijklmnopqrstuvwxyz/".toCharArray() ) ) )
                 .forEach( i -> l_beliefbase.generate( l_generator, i.functorpath() ).add( i ) );

        Assert.assertEquals( "number of beliefs is incorrect", l_max, l_beliefbase.size() );
    }


    /**
     * manual test of tree structure
     */
    @Test
    public void manual()
    {
        final IView l_view = buildview();

        Assert.assertEquals( "number of beliefs is incorrect", 8, l_view.size() );

        Assert.assertArrayEquals(
            Stream.of( "toplevel[]", "first/sub1[]", "first/sub1[1]", "first/sub1[2]",
                       "first/sub2[]", "second/sub3[]", "second/sub4[]", "second/second/sub/sub5[]" ).toArray(),
            l_view.stream().map( Object::toString ).toArray()
        );

        Assert.assertArrayEquals(
            Stream.of( "sub1[]", "sub1[1]", "sub1[2]" ).toArray(),
            l_view.stream( CPath.of( "first/sub1" ) ).map( Object::toString ).toArray()
        );
    }

    /**
     * test contains, empty, clear
     */
    @Test
    public void containsclearempty()
    {
        final IView l_view = buildview();

        l_view.add( CLiteral.of( "xxx" ) );

        Assert.assertFalse( l_view.isempty() );
        Assert.assertTrue( l_view.containsliteral( CPath.of( "first/sub1" ) ) );
        Assert.assertTrue( l_view.containsliteral( CPath.of( "xxx" ) ) );
        Assert.assertTrue( l_view.containsview( CPath.of( "first" ) ) );
        Assert.assertEquals( l_view, l_view.remove( l_view.beliefbase().view( "first" ) ) );
        Assert.assertFalse( l_view.containsview( CPath.of( "first" ) ) );
        Assert.assertTrue( l_view.containsview( CPath.of( "second/sub" ) ) );

        l_view.clear( CPath.of( "second" ) );
        Assert.assertFalse( l_view.isempty() );
        Assert.assertTrue( l_view.containsview( CPath.of( "second" ) ) );
        Assert.assertTrue( l_view.beliefbase().view( "second" ).isempty() );

        l_view.clear();
        Assert.assertTrue( l_view.isempty() );
    }

    /**
     * test ctor exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void viewctorerror()
    {
        new CView( "", IBeliefbase.EMPY );
    }

    /**
     * create a view with beliefbase
     *
     * @return view
     */
    private static IView buildview()
    {
        final IView l_view = new CBeliefbase( new CMultiStorage<>() ).create( "root" );
        final IViewGenerator l_gen = new CGenerator();

        l_view.add( CLiteral.of( "toplevel" ) )

              .generate( l_gen, CPath.of( "first" ) )
              .add( CLiteral.of( "first/sub1" ) )
              .add( CLiteral.of( "first/sub1", CRawTerm.of( 1 ) ) )
              .add( CLiteral.of( "first/sub1", CRawTerm.of( 2 ) ) )
              .add( CLiteral.of( "first/sub2" ) )

              .generate( l_gen, CPath.of( "second/sub" ) )
              .add( CLiteral.of( "second/sub3" ) )
              .add( CLiteral.of( "second/sub4" ) )

              .add( CLiteral.of( "second/sub/sub5" ) );

        return l_view;
    }


    /**
     * test belief generator
     */
    private static final class CGenerator implements IViewGenerator
    {

        @Override
        public IView apply( final String p_name, final IView p_parent )
        {
            return new CBeliefbase( new CMultiStorage<>() ).create( p_name, p_parent );
        }
    }

}
