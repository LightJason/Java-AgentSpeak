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

package org.lightjason.agentspeak.action.builtin.datetime;

import org.joda.time.Days;
import org.joda.time.Instant;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;



/**
 * returns the days between two dates.
 * The actions returns the number of days between
 * two date-time objects. A positive value will be
 * returned iif the first date-time item is before
 * the second one, a negative value will be returned
 * iif the first date-time item is after the second
 * date-time item
 *
 * {@code [D1|D2] = .datetime/daysbetween( DateTime1, DateTime2, DateTime3, DateTime4 );}
 */
public final class CDaysBetween extends IBetween
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 2204654538944733054L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CDaysBetween.class, "datetime" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnull
    @Override
    protected Stream<?> apply( @Nonnull final Stream<List<Instant>> p_datetime )
    {
        return p_datetime
            .map( i -> Days.daysBetween( i.get( 0 ), i.get( 1 ) ) )
            .mapToDouble( Days::getDays )
            .boxed();
    }

}
