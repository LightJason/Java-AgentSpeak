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

package org.lightjason.agentspeak.beliefbase;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.storage.CMultiStorage;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.beliefbase.view.IViewGenerator;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.language.CLiteral;

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
        final int l_max = 1000;
        final IView<IAgent<?>> l_beliefbase = new CBeliefbasePersistent<>( new CMultiStorage<>() ).create( "root" );
        final IViewGenerator<IAgent<?>> l_gen = new CGenerator();

        IntStream.range( 0, l_max )
                 .boxed()
                 .map( i -> CLiteral.from( RandomStringUtils.random( 12, "~abcdefghijklmnopqrstuvwxyz/".toCharArray() ) ) )
                 .forEach( i -> l_beliefbase.generate( l_gen, i.functorpath() ).add( i ) );

        assertEquals( "number of beliefs is incorrect", l_beliefbase.size(), l_max );
        System.out.println( l_beliefbase );
    }


    /**
     * manual test of tree structure
     */
    @Test
    public final void testManual()
    {
        final IView<IAgent<?>> l_beliefbase = new CBeliefbasePersistent<>( new CMultiStorage<>() ).create( "root" );
        final IViewGenerator<IAgent<?>> l_gen = new CGenerator();

        l_beliefbase.add( CLiteral.from( "toplevel" ) )

                    .generate( l_gen, CPath.from( "first" ) )
                    .add( CLiteral.from( "first/sub1" ) )
                    .add( CLiteral.from( "first/sub2" ) )

                    .generate( l_gen, CPath.from( "second/sub" ) )
                    .add( CLiteral.from( "second/sub3" ) )
                    .add( CLiteral.from( "second/sub4" ) )

                    .add( CLiteral.from( "second/sub/sub5" ) );


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
    private static final class CGenerator implements IViewGenerator<IAgent<?>>
    {

        @Override
        public final IView<IAgent<?>> apply( final String p_name, final IView<IAgent<?>> p_parent )
        {
            return new CBeliefbasePersistent<>( new CMultiStorage<>() ).create( p_name, p_parent );
        }
    }

}
