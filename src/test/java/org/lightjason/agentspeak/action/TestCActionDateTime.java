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

package org.lightjason.agentspeak.action;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lightjason.agentspeak.action.datetime.CApplyDays;
import org.lightjason.agentspeak.action.datetime.CApplyHours;
import org.lightjason.agentspeak.action.datetime.CApplyMinutes;
import org.lightjason.agentspeak.action.datetime.CApplyMonths;
import org.lightjason.agentspeak.action.datetime.CApplyNanoSeconds;
import org.lightjason.agentspeak.action.datetime.CApplySeconds;
import org.lightjason.agentspeak.action.datetime.CApplyYears;
import org.lightjason.agentspeak.action.datetime.CBuild;
import org.lightjason.agentspeak.action.datetime.CCreate;
import org.lightjason.agentspeak.action.datetime.CDaysBetween;
import org.lightjason.agentspeak.action.datetime.CHoursBetween;
import org.lightjason.agentspeak.action.datetime.CMinutesBetween;
import org.lightjason.agentspeak.action.datetime.CMonthsBetween;
import org.lightjason.agentspeak.action.datetime.CSecondsBetween;
import org.lightjason.agentspeak.action.datetime.CTime;
import org.lightjason.agentspeak.action.datetime.CYearsBetween;
import org.lightjason.agentspeak.action.datetime.CZoneid;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

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
            testcaseapply( new CApplyYears(), "2010-05-04T10:17:13Z[America/New_York]", 6, "2004-05-04T10:17:13Z[America/New_York]" ),
            testcaseapply( new CApplyMonths(), "2009-05-04T10:17:13Z[America/New_York]", 7, "2008-10-04T10:17:13Z[America/New_York]" ),
            testcaseapply( new CApplyDays(), "2008-05-04T10:17:13Z[America/New_York]", 5, "2008-04-29T10:17:13Z[America/New_York]" ),
            testcaseapply( new CApplyHours(), "2009-01-15T15:16:13Z[Europe/London]", 36, "2009-01-14T03:16:13Z[Europe/London]" ),
            testcaseapply( new CApplyMinutes(), "2007-01-15T10:23:13Z[Europe/London]", 187, "2007-01-15T07:16:13Z[Europe/London]" ),
            testcaseapply( new CApplySeconds(), "2006-01-15T10:23:13Z[Europe/London]", 4200, "2006-01-15T09:13:13Z[Europe/London]" ),
            testcaseapply( new CApplyNanoSeconds(), "2005-01-15T10:23:13Z[Europe/London]", 10200, "2005-01-15T10:23:12.999989800Z[Europe/London]" )
        ).toArray();
    }


    /**
     * data provider generator of apply-plus tests
     * @return data
     */
    @DataProvider
    public static Object[] generateapplyplus()
    {
        return Stream.of(
            testcaseapply( new CApplyYears(), "2010-05-04T10:17:13Z[America/New_York]", 12, "2022-05-04T10:17:13Z[America/New_York]" ),
            testcaseapply( new CApplyMonths(), "2009-05-04T10:17:13Z[America/New_York]", 8, "2010-01-04T06:17:13-05:00[America/New_York]" ),
            testcaseapply( new CApplyDays(), "2008-05-04T10:17:13Z[America/New_York]", 3, "2008-05-07T10:17:13Z[America/New_York]" ),
            testcaseapply( new CApplyHours(), "2009-01-15T10:16:13Z[Europe/London]", 120, "2009-01-20T10:16:13Z[Europe/London]" ),
            testcaseapply( new CApplyMinutes(), "2007-01-15T10:23:13Z[Europe/London]", 240, "2007-01-15T14:23:13Z[Europe/London]" ),
            testcaseapply( new CApplySeconds(), "2006-01-15T10:23:13Z[Europe/London]", 7205, "2006-01-15T12:23:18Z[Europe/London]" ),
            testcaseapply( new CApplyNanoSeconds(), "2005-01-15T10:23:13Z[Europe/London]", 15715, "2005-01-15T10:23:13.000015715Z[Europe/London]" )
        ).toArray();
    }


    /**
     * data provider generator of between tests
     *
     * @return data
     */
    @DataProvider
    public static Object[] generatebetween()
    {
        return Stream.of(
            testcasebetween(
                new CYearsBetween(),
                Stream.of(
                    "2000-01-15T10:16:13Z[Europe/London]", "2000-01-15T10:16:13Z[Europe/London]",
                    "2000-01-15T10:23:13Z[Europe/London]", "2020-05-04T10:17:13Z[America/New_York]"
                ),
                Stream.of( 0, 20 )
            ),

            testcasebetween(
                new CMonthsBetween(),
                Stream.of(
                    "1999-01-15T10:16:13Z[Europe/London]", "1999-01-15T10:16:13Z[Europe/London]",
                    "1999-01-15T10:16:13Z[Europe/London]", "2001-01-15T10:16:13Z[Europe/London]"
                ),
                Stream.of( 0, 24 )
            ),

            testcasebetween(
                new CDaysBetween(),
                Stream.of(
                    "1998-01-15T10:23:13Z[Europe/London]", "1998-01-15T10:23:13Z[Europe/London]",
                    "1998-06-15T10:23:13Z[Europe/London]", "1998-01-15T10:23:13Z[Europe/London]"
                ),
                Stream.of( 0, -151 )
            ),

            testcasebetween(
                new CHoursBetween(),
                Stream.of(
                    "1997-05-04T10:17:13Z[America/New_York]", "1997-05-04T10:17:13Z[America/New_York]",
                    "1997-05-04T18:12:13Z[America/New_York]", "1997-05-04T10:17:13Z[America/New_York]"
                ),
                Stream.of( 0, -7 )
            ),

            testcasebetween(
                new CMinutesBetween(),
                Stream.of(
                    "1996-01-15T10:23:13Z[Europe/Paris]", "1996-01-15T10:23:13Z[Europe/Paris]",
                    "1996-01-15T10:23:13Z[Europe/Paris]", "1996-01-15T16:23:13Z[Europe/Paris]"

                ),
                Stream.of( 0, 360 )
            ),

            testcasebetween(
                new CSecondsBetween(),
                Stream.of(
                    "1995-01-15T10:23:13Z[Europe/Madrid]", "1995-01-15T10:23:13Z[Europe/Madrid]",
                    "1995-02-15T10:23:13Z[Europe/Madrid]", "1995-02-14T10:23:13Z[Europe/Madrid]"
                ),
                Stream.of( 0, -86400 )
            )

        ).toArray();
    }


    /**
     * generate test-case of apply definition
     *
     * @param p_action used-action
     * @param p_datetime referenced date-time
     * @param p_value value of the action arguments
     * @param p_result result time string
     * @return test object
     */
    private static Object testcaseapply( final IAction p_action, final String p_datetime, final int p_value, final String p_result )
    {
        return new ImmutableTriple<>( p_action, new ImmutablePair<>( ZonedDateTime.parse( p_datetime ), p_value ), p_result );
    }


    /**
     *
     * @param p_action used-action
     * @param p_datetime date-time values
     * @param p_results between results of tuples
     * @return test obejct
     */
    private static Object testcasebetween( final IAction p_action, final Stream<String> p_datetime, final Stream<Number> p_results )
    {
        return new ImmutableTriple<>( p_action, p_datetime.map( ZonedDateTime::parse ).map( CRawTerm::of ), p_results );
    }



    /**
     * test create error
     */
    @Test( expected = CExecutionIllegealArgumentException.class )
    public void createerror()
    {
        new CCreate().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( "error" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            Collections.emptyList()
        );
    }

    /**
     * test create
     */
    @Test
    public void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "", "2007-12-03T10:15:30+01:00[Europe/Paris]", "now" ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 3, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ZonedDateTime );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof ZonedDateTime );
        Assert.assertTrue( l_return.get( 2 ).raw() instanceof ZonedDateTime );

        Assert.assertTrue( l_return.get( 0 ).<ZonedDateTime>raw().isBefore( l_return.get( 2 ).<ZonedDateTime>raw() ) );
    }


    /**
     * test build
     */
    @Test
    public void build()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBuild().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 2013, 3, 13, 12, 11, 10, 9, "current", 2013, 3, 13, 12, 11, 10, 9, "Europe/Moscow" ).map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof ZonedDateTime );
        Assert.assertTrue( l_return.get( 1 ).raw() instanceof ZonedDateTime );

        Assert.assertEquals( l_return.get( 1 ).<ZonedDateTime>raw().getYear(), l_return.get( 0 ).<ZonedDateTime>raw().getYear() );
        Assert.assertEquals( l_return.get( 1 ).<ZonedDateTime>raw().getMonthValue(), l_return.get( 0 ).<ZonedDateTime>raw().getMonthValue() );
        Assert.assertEquals( l_return.get( 1 ).<ZonedDateTime>raw().getDayOfMonth(), l_return.get( 0 ).<ZonedDateTime>raw().getDayOfMonth() );

        Assert.assertEquals( l_return.get( 1 ).<ZonedDateTime>raw().getHour(), l_return.get( 0 ).<ZonedDateTime>raw().getHour() );
        Assert.assertEquals( l_return.get( 1 ).<ZonedDateTime>raw().getMinute(), l_return.get( 0 ).<ZonedDateTime>raw().getMinute() );
        Assert.assertEquals( l_return.get( 1 ).<ZonedDateTime>raw().getSecond(), l_return.get( 0 ).<ZonedDateTime>raw().getSecond() );
        Assert.assertEquals( l_return.get( 1 ).<ZonedDateTime>raw().getNano(), l_return.get( 0 ).<ZonedDateTime>raw().getNano() );

        Assert.assertNotEquals( l_return.get( 1 ).<ZonedDateTime>raw().getZone(), l_return.get( 0 ).<ZonedDateTime>raw().getZone() );
    }


    /**
     * test time
     */
    @Test
    public void time()
    {
        final List<ITerm> l_return = new ArrayList<>();

        Assert.assertTrue(
            "action execute error",
            execute(
                new CTime(),
                false,
                Stream.of( "2007-12-03T10:15:30+03:00[Europe/Moscow]" ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );

        Assert.assertEquals( 4, l_return.size() );
        Assert.assertEquals( 10, l_return.get( 0 ).<Number>raw() );
        Assert.assertEquals( 15, l_return.get( 1 ).<Number>raw() );
        Assert.assertEquals( 30, l_return.get( 2 ).<Number>raw() );
        Assert.assertEquals( 0, l_return.get( 3 ).<Number>raw() );
    }


    /**
     * test zone-id
     */
    @Test
    public void zoneid()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CZoneid().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "2006-10-04T10:17:13-05:00[America/New_York]", "2006-10-04T10:17:13+00:00[Europe/London]" ).map( CRawTerm::of )
                  .collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( "America/New_York", l_return.get( 0 ).raw() );
        Assert.assertEquals( "Europe/London", l_return.get( 1 ).raw() );
    }


    /**
     * test apply-days minus
     *
     * @param p_value tripel action, input data and result
     */
    @Test
    @UseDataProvider( "generateapplyminus" )
    public void applysminus( final Triple<IAction, Pair<ZonedDateTime, Integer>, String> p_value )
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_value.getLeft().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "minus", p_value.getMiddle().getRight(), p_value.getMiddle().getLeft() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( ZonedDateTime.parse( p_value.getRight() ), l_return.get( 0 ).raw() );
    }

    /**
     * test apply-days plus
     *
     * @param p_value tripel action, input data and result
     */
    @Test
    @UseDataProvider( "generateapplyplus" )
    public void applyplus( final Triple<IAction, Pair<ZonedDateTime, Integer>, String> p_value )
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_value.getLeft().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( "plus", p_value.getMiddle().getRight(), p_value.getMiddle().getLeft() ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( ZonedDateTime.parse( p_value.getRight() ), l_return.get( 0 ).raw() );
    }

    /**
     * test between
     *
     * @param p_value tripel action, input data and result
     */
    @Test
    @UseDataProvider( "generatebetween" )
    public void between( final Triple<IAction, Stream<ITerm>, Stream<Number>> p_value )
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_value.getLeft().execute(
            false, IContext.EMPTYPLAN,
            p_value.getMiddle().collect( Collectors.toList() ),
            l_return
        );

        Assert.assertArrayEquals(
            p_value.getRight().mapToLong( Number::longValue ).toArray(),
            l_return.stream().map( ITerm::<Number>raw ).mapToLong( Number::longValue ).toArray()
        );
    }

}
