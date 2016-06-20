/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.action.buildin.generic.datetime;

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
 * action to create a date-time structure
 */
public final class CDateTime extends IBuildinAction
{
    @Override
    public int getMinimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                         final List<ITerm> p_annotation
    )
    {
        final ZonedDateTime l_datetime;
        if ( p_argument.size() == 1 )
            l_datetime = ZonedDateTime.parse( CCommon.raw( p_argument.get( 0 ) ) );
        else
        {
            final int[] l_parts = new int[7];

            // read day-month-year structure
            l_parts[0] = CCommon.raw( p_argument.get( 0 ) );
            l_parts[1] = CCommon.raw( p_argument.get( 1 ) );
            l_parts[2] = CCommon.raw( p_argument.get( 2 ) );

            // if is set read hour-.minutes
            if ( p_argument.size() >= 4 )
            {
                l_parts[3] = CCommon.raw( p_argument.get( 3 ) );
                l_parts[4] = CCommon.raw( p_argument.get( 4 ) );
            }

            // if is set read seconds
            if ( p_argument.size() >= 5 )
                l_parts[5] = CCommon.raw( p_argument.get( 5 ) );

            // if is set read nanoseconds
            if ( p_argument.size() >= 6 )
                l_parts[6] = CCommon.raw( p_argument.get( 6 ) );

            // create date-time and add zone-id
            l_datetime = ZonedDateTime.of(
                l_parts[0],
                l_parts[1],
                l_parts[2],
                l_parts[3],
                l_parts[4],
                l_parts[5],
                l_parts[6],
                p_argument.size() > 6 ? ZoneId.systemDefault() : ZoneId.of( CCommon.raw( p_argument.get( 7 ) ) )
            );
        }

        p_return.add( CRawTerm.from( l_datetime ) );
        return CFuzzyValue.from( true );
    }
}
