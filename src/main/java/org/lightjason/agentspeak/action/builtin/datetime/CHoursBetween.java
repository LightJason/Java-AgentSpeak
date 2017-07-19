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

package org.lightjason.agentspeak.action.builtin.datetime;

import org.joda.time.Hours;
import org.joda.time.Instant;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;



/**
 * returns the hours between two dates.
 * The actions returns the number of hours between
 * two date-time objects, the action never
 * fails
 *
 * @code [H1|H2] = datetime/hoursbetween( DateTime1, DateTime2, DateTime3, DateTime4 ); @endcode
 * @note a positive value will be returned iif the first date-time item is before the second one, a negative value
 * will be returned iif the first date-time item is after the second date-time item
 */
public final class CHoursBetween extends IBetween
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2772776076412167604L;

    @Nonnull
    @Override
    protected final Stream<?> apply( @Nonnull final Stream<List<Instant>> p_datetime )
    {
        return p_datetime
            .map( i -> Hours.hoursBetween( i.get( 0 ), i.get( 1 ) ) )
            .mapToLong( Hours::getHours )
            .boxed();
    }

}
