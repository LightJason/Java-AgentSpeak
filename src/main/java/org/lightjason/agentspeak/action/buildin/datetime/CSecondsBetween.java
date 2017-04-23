/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.datetime;

import org.joda.time.Instant;
import org.joda.time.Seconds;

import java.util.List;
import java.util.stream.Stream;



/**
 * returns the seconds between two dates.
 * The actions returns the number of seconds between
 * two date-time objects, the action never
 * fails
 *
 * @code [S1|S2] = datetime/secondsbetween( DateTime1, DateTime2, DateTime3, DateTime4 ); @endcode
 * @note a positive value will be returned iif the first date-time item is before the second one, a negative value
 * will be returned iif the first date-time item is after the second date-time item
 */
public final class CSecondsBetween extends IBetween
{

    @Override
    protected final Stream<?> apply( final Stream<List<Instant>> p_datetime )
    {
        return p_datetime
            .map( i -> Seconds.secondsBetween( i.get( 0 ), i.get( 1 ) ) )
            .mapToLong( Seconds::getSeconds )
            .boxed();
    }

}
