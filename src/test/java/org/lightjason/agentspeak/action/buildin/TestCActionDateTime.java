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

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.buildin.datetime.CBuild;
import org.lightjason.agentspeak.action.buildin.datetime.CCreate;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test action datetime
 */
public final class TestCActionDateTime extends IBaseTest
{

    /**
     * test create error
     */
    @Test
    public final void createerror()
    {
        Assert.assertFalse(
            new CCreate().execute(
                null,
                false,
                Stream.of( "error" ).map( CRawTerm::from ).collect( Collectors.toList() ),
                Collections.emptyList(),
                Collections.emptyList()
            ).value()
        );
    }

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
            Stream.of( "", "2007-12-03T10:15:30+01:00[Europe/Paris]", "now" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 3 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ZonedDateTime );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof ZonedDateTime );
        Assert.assertTrue( l_return.get( 2 ).raw() instanceof ZonedDateTime );

        Assert.assertTrue( l_return.get( 0 ).<ZonedDateTime>raw().isBefore( l_return.get( 2 ).<ZonedDateTime>raw() ) );
    }


    /**
     * test build
     */
    @Test
    public final void build()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBuild().execute(
            null,
            false,
            Stream.of( 2013, 3, 13, 12, 11, 10, 9, "current", 2013, 3, 13, 12, 11, 10, 9, "Europe/Moscow" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ZonedDateTime );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof ZonedDateTime );

        Assert.assertEquals( l_return.get( 0 ).<ZonedDateTime>raw().getYear(), l_return.get( 1 ).<ZonedDateTime>raw().getYear() );
        Assert.assertEquals( l_return.get( 0 ).<ZonedDateTime>raw().getMonthValue(), l_return.get( 1 ).<ZonedDateTime>raw().getMonthValue() );
        Assert.assertEquals( l_return.get( 0 ).<ZonedDateTime>raw().getDayOfMonth(), l_return.get( 1 ).<ZonedDateTime>raw().getDayOfMonth() );

        Assert.assertEquals( l_return.get( 0 ).<ZonedDateTime>raw().getHour(), l_return.get( 1 ).<ZonedDateTime>raw().getHour() );
        Assert.assertEquals( l_return.get( 0 ).<ZonedDateTime>raw().getMinute(), l_return.get( 1 ).<ZonedDateTime>raw().getMinute() );
        Assert.assertEquals( l_return.get( 0 ).<ZonedDateTime>raw().getSecond(), l_return.get( 1 ).<ZonedDateTime>raw().getSecond() );
        Assert.assertEquals( l_return.get( 0 ).<ZonedDateTime>raw().getNano(), l_return.get( 1 ).<ZonedDateTime>raw().getNano() );

        Assert.assertNotEquals( l_return.get( 0 ).<ZonedDateTime>raw().getZone(), l_return.get( 1 ).<ZonedDateTime>raw().getZone() );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionDateTime().invoketest();
    }

}
