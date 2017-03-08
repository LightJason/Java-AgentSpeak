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

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


/**
 * builds a date-time object from elements.
 * The actions creates form an eight-tuple a date-time
 * object and never fails, the seventh argument within
 * the tuple is the time-zone, which can be set to empty
 * or "current", so the system-default zone is used
 *
 * @code O1 = datetime/build( Year, Month, Day, Hour, Minutes, Second, NanoSeconds, "current" );  @endcode
 */
public final class CBuild extends IBuildinAction
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                         final List<ITerm> p_annotation )
    {
        StreamUtils.windowed(
            CCommon.flatcollection( p_argument ),
        8
        )
                   .map( CBuild::apply )
                   .map( CRawTerm::from )
                   .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

    /**
     * creates the date-time object
     *
     * @param p_elements term elements
     * @return date-time object
     */
    private static ZonedDateTime apply( final List<ITerm> p_elements )
    {
        return ZonedDateTime.of(

            // year, month, day
            p_elements.get( 0 ).<Number>raw().intValue(),
            p_elements.get( 1 ).<Number>raw().intValue(),
            p_elements.get( 2 ).<Number>raw().intValue(),

            // hours, minutes, seconds, nano-seconds
            p_elements.get( 3 ).<Number>raw().intValue(),
            p_elements.get( 4 ).<Number>raw().intValue(),
            p_elements.get( 5 ).<Number>raw().intValue(),
            p_elements.get( 6 ).<Number>raw().intValue(),

            // zone id if empty or current, system-default will be used
            ( "current".equalsIgnoreCase( p_elements.get( 7 ).<String>raw().trim() ) ) || ( p_elements.get( 7 ).<String>raw().trim().isEmpty() )
            ? ZoneId.systemDefault()
            : ZoneId.of( p_elements.get( 7 ).<String>raw().trim() )
        );
    }

}
