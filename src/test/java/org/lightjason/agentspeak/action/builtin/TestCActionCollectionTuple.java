/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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

package org.lightjason.agentspeak.action.builtin;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.builtin.listsettuple.tuple.CCreate;
import org.lightjason.agentspeak.action.builtin.listsettuple.tuple.CFlat;
import org.lightjason.agentspeak.action.builtin.listsettuple.tuple.CSet;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test collection tuple
 */
public final class TestCActionCollectionTuple extends IBaseTest
{

    /**
     * test tuple creating
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "abcd", 123, "foobar", true ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );

        Assert.assertEquals( "abcd", l_return.get( 0 ).<AbstractMap.Entry<String, ?>>raw().getKey() );
        Assert.assertEquals( 123, l_return.get( 0 ).<AbstractMap.Entry<?, Number>>raw().getValue() );

        Assert.assertEquals( "foobar", l_return.get( 1 ).<AbstractMap.Entry<String, ?>>raw().getKey() );
        Assert.assertTrue( l_return.get( 1 ).<AbstractMap.Entry<?, Boolean>>raw().getValue() );
    }

    /**
     * test tuple creating error
     */
    @Test( expected = CExecutionIllegealArgumentException.class )
    public void createerror()
    {
        new CCreate().execute(
                false,
                IContext.EMPTYPLAN,
                Stream.of( "x" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                Collections.emptyList()
        );
    }


    /**
     * test tuple set
     */
    @Test
    public void set()
    {
        final AbstractMap.Entry<String, String> l_data = new AbstractMap.SimpleEntry<>( "foo", "bar" );

        new CSet().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "blubblub", l_data ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );

        Assert.assertEquals( "blubblub", l_data.getValue() );
    }


    /**
     * test tuple flat
     */
    @Test
    public void flat()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CFlat().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( new AbstractMap.SimpleEntry<>( "foo", "bar" ), new AbstractMap.SimpleEntry<>( 1, 2 ) ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 4, l_return.size() );

        Assert.assertEquals( "foo", l_return.get( 0 ).raw() );
        Assert.assertEquals( "bar", l_return.get( 1 ).raw() );

        Assert.assertEquals( 1, l_return.get( 2 ).<Number>raw() );
        Assert.assertEquals( 2, l_return.get( 3 ).<Number>raw() );
    }

}
