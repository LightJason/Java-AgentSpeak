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

import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.List;


/**
 * action for getting the current date.
 * The action returns the date elements
 * of a parsed string or date-time object,
 * if the string is empty or "now" the current
 * date-time is used
 *
 * {@code [Year|Month|Day|DayOfWeek|DayOfYear] = .datetime/date( "now" );}
 */
public final class CDate extends IDateTime
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 4490065527882057197L;
    /**
     * action name
     */
    private static final IPath NAME = CPath.of( "datetime/date" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Override
    protected boolean elements( @Nonnull final ZonedDateTime p_datetime, @Nonnull final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.of( p_datetime.getYear() ) );
        p_return.add( CRawTerm.of( p_datetime.getMonthValue() ) );
        p_return.add( CRawTerm.of( p_datetime.getDayOfMonth() ) );
        p_return.add( CRawTerm.of( p_datetime.getDayOfWeek() ) );
        p_return.add( CRawTerm.of( p_datetime.getDayOfYear() ) );
        return true;
    }

}
