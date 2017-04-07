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

package org.lightjason.agentspeak.action.buildin;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.action.buildin.collection.multimap.CCreate;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * test multimap action
 */
public final class TestCActionCollectionMultimap
{

    /**
     * test create
     */
    @Test
    public final void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            null,
            false,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof Multimap<?, ?> );
        Assert.assertTrue( l_return.get( 0 ).<Multimap<?, ?>>raw().isEmpty() );
    }

    /**
     * test synchronized create
     */
    @Test
    public final void createsynchronized()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            null,
            true,
            Collections.emptyList(),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).raw().getClass(), Multimaps.synchronizedSetMultimap( HashMultimap.create() ).getClass() );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        final TestCActionCollectionMultimap l_test = new TestCActionCollectionMultimap();

        l_test.create();
        l_test.createsynchronized();

    }

}
