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

package lightjason.agentspeak.beliefbase;

import lightjason.agentspeak.language.CLiteral;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;


/**
 * test of beliefbase view
 */
public final class TestCView
{

    /**
     * random test tree structure
     */
    @Test
    public final void testTree()
    {
        final int l_max = 10;
        final IView l_beliefbase = new CBeliefBase( new CStorage<>() ).create( "root" );
        final IView.IGenerator l_gen = new CGenerator();

        IntStream.range( 0, l_max )
                 .boxed()
                 .forEach( i -> l_beliefbase.add( CLiteral.from( RandomStringUtils.random( 12, "~abcdefghijklmnopqrstuvwxyz/".toCharArray() ) ), l_gen ) );

        assertEquals( "number of beliefs is incorrect", l_beliefbase.size(), l_max );
        System.out.println( l_beliefbase );
    }


    /**
     * manual test of tree structure
     */
    @Test
    public final void testManual()
    {
        final IView l_beliefbase = new CBeliefBase( new CStorage<>() ).create( "root" );
        final IView.IGenerator l_gen = new CGenerator();

        l_beliefbase.add( CLiteral.from( "toplevel" ) );

        l_beliefbase.add( CLiteral.from( "first/sub1" ), l_gen );
        l_beliefbase.add( CLiteral.from( "first/sub2" ) );

        l_beliefbase.add( CLiteral.from( "second/sub1" ), l_gen );
        l_beliefbase.add( CLiteral.from( "second/sub2" ) );

        l_beliefbase.add( CLiteral.from( "second/sub/sub1" ), l_gen );


        assertEquals( "number of beliefs is incorrect", l_beliefbase.size(), 6 );
        System.out.println( l_beliefbase );
    }


    /**
     * manuell running test
     *
     * @param p_args arguments
     */
    public static void main( final String[] p_args )
    {
        final TestCView l_test = new TestCView();

        l_test.testTree();
        l_test.testManual();
    }


    /**
     * test belief generator
     */
    private static final class CGenerator implements IView.IGenerator
    {
        @Override
        public final IView generate( final String p_name )
        {
            return new CBeliefBase( new CStorage<>() ).create( p_name );
        }
    }

}
