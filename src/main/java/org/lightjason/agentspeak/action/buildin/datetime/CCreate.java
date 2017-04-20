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

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * returns a date-time object.
 * The action returns a date-time object based
 * on the string input arguments, the action
 * parses the string if the string is empty or
 * "now" the current date-time will returned, the
 * action never fails
 *
 * @code [O1|O2] = datetime/create( "now", "2007-12-03T10:15:30+01:00[Europe/Paris]" ); @endcode
 */
public final class CCreate extends IDateTime
{

    @Override
    protected final boolean elements( final ZonedDateTime p_datetime, final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( p_datetime ) );
        return true;
    }

}
