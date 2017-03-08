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

package org.lightjason.agentspeak.action.buildin.datetime;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * action for getting the current time.
 * The action returns the time elements,
 * the actions parses the string arguments
 * and for each argument the time values are
 * returnes, the action fails on wrong input
 *
 * @note if the string is empty or "now" the current
 * time is returned
 * @code [Hour|Minute|Second|Nano|Zone] = datetime/time( "now" ); @endcode
 */
public final class CTime extends IDateTime
{

    @Override
    protected final boolean elements( final ZonedDateTime p_datetime, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( p_datetime.getHour() ) );
        p_return.add( CRawTerm.from( p_datetime.getMinute() ) );
        p_return.add( CRawTerm.from( p_datetime.getSecond() ) );
        p_return.add( CRawTerm.from( p_datetime.getNano() ) );
        p_return.add( CRawTerm.from( p_datetime.getZone() ) );

        return true;
    }

}
