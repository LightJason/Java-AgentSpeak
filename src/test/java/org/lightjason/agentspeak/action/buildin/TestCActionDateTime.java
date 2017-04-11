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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.action.buildin.datetime.CApplyDays;
import org.lightjason.agentspeak.action.buildin.datetime.CApplyHours;
import org.lightjason.agentspeak.action.buildin.datetime.CApplyMinutes;
import org.lightjason.agentspeak.action.buildin.datetime.CApplyNanoSeconds;
import org.lightjason.agentspeak.action.buildin.datetime.CApplySeconds;
import org.lightjason.agentspeak.action.buildin.datetime.CApplyYears;
import org.lightjason.agentspeak.action.buildin.datetime.CBuild;
import org.lightjason.agentspeak.action.buildin.datetime.CCreate;
import org.lightjason.agentspeak.action.buildin.datetime.CTime;
import org.lightjason.agentspeak.action.buildin.datetime.CZoneid;
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
@RunWith( DataProviderRunner.class )
public final class TestCActionDateTime extends IBaseTest
{

    /**
     * data provider generator of apply-minus tests
     * @return data
     */
    @DataProvider
    public static Object[] generateapplyminus()
    {
        return Stream.of(

            testcaseapply( new CApplyYears(), "2009-05-04T10:17:13-05:00[America/New_York]", 6, "2003-05-04T10:17:13-05:00[America/New_York]" ),
            testcaseapply( new CApplyDays(), "2008-05-04T10:17:13-05:00[America/New_York]", 5, "2008-04-29T10:17:13-04:00[America/New_York]" ),
            testcaseapply( new CApplyHours(), "2009-01-15T10:16:13+00:00[Europe/London]", 52, "2009-01-13T06:16:13Z[Europe/London]" ),
            testcaseapply( new CApplyMinutes(), "2007-01-15T10:23:13+00:00[Europe/London]", 187, "2007-01-15T07:16:13Z[Europe/London]" ),
            testcaseapply( new CApplySeconds(), "2006-01-15T10:23:13+00:00[Europe/London]", 4200, "2006-01-15T09:13:13Z[Europe/London]" ),
            testcaseapply( new CApplyNanoSeconds(), "2005-01-15T10:23:13+00:00[Europe/London]", 10200, "2005-01-15T10:23:12.999989800Z[Europe/London]" )

        ).toArray();
    }


    /**
     * data provider generator of apply-plus tests
     * @return data
     */
    @DataProvider
    public static Object[] generateapplyplus()
    {
        return Stream.of().toArray();
    }


    /**
     * generate test-case of apply definition
     *
     * @param p_action used-action
     * @param p_datetime referenced date time
     * @param p_value value of the action arguments
     * @param p_result result time string
     * @return test object
     */
    private static Object testcaseapply( final IAction p_action,final String p_datetime, final int p_value, final String p_result )
    {
        return new ImmutableTriple<>( p_action, new ImmutablePair<>( ZonedDateTime.parse( p_datetime ), p_value ), p_result );
    }



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
            Stream.of( 2013, 3, 13, 12, 11, 10, 9, "current", 2013, 3, 13, 12, 11, 10, 9, "Europe/Moscow" ).map( CRawTerm::from )
                  .collect( Collectors.toList() ),
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
     * test time
     */
    @Test
    public final void time()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CTime().execute(
            null,
            false,
            Stream.of( "2007-12-03T10:15:30+01:00[Europe/Moscow]" ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 4 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 10 );
        Assert.assertEquals( l_return.get( 1 ).<Number>raw(), 15 );
        Assert.assertEquals( l_return.get( 2 ).<Number>raw(), 30 );
        Assert.assertEquals( l_return.get( 3 ).<Number>raw(), 0 );
    }


    /**
     * test zone-id
     */
    @Test
    public final void zoneid()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CZoneid().execute(
            null,
            false,
            Stream.of( "2006-10-04T10:17:13-05:00[America/New_York]", "2006-10-04T10:17:13+00:00[Europe/London]" ).map( CRawTerm::from )
                  .collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertEquals( l_return.get( 0 ).raw(), "America/New_York" );
        Assert.assertEquals( l_return.get( 1 ).raw(), "Europe/London" );
    }


    /**
     * test apply-days minus
     */
    @Test
    @UseDataProvider( "generateapplyminus" )
    public final void applysminus( final Triple<IAction, Pair<ZonedDateTime, Integer>, String> p_value )
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_value.getLeft().execute(
            null,
            false,
            Stream.of( "minus", p_value.getMiddle().getRight(), p_value.getMiddle().getLeft() ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).raw(), ZonedDateTime.parse( p_value.getRight() ) );
    }

    /**
     * test apply-days plus
     */
    @Test
    @UseDataProvider( "generateapplyplus" )
    public final void applyplus( final Triple<IAction, Pair<ZonedDateTime, Integer>, String> p_value )
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_value.getLeft().execute(
            null,
            false,
            Stream.of( "plus", p_value.getMiddle().getRight(), p_value.getMiddle().getLeft() ).map( CRawTerm::from ).collect( Collectors.toList() ),
            l_return,
            Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).raw(), ZonedDateTime.parse( p_value.getRight() ) );
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
