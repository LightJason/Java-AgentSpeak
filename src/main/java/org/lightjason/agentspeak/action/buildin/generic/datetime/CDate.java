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

package org.lightjason.agentspeak.action.buildin.generic.datetime;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * action for getting the current date
 */
public final class CDate extends IBuildinAction
{

    /**
     * ctor
     */
    public CDate()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 0;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final ZonedDateTime l_date = p_argument.size() == 1 ? ZonedDateTime.parse( p_argument.get( 0 ).toAny() ) : ZonedDateTime.now();

        p_return.add( CRawTerm.from( l_date.getDayOfMonth() ) );
        p_return.add( CRawTerm.from( l_date.getMonthValue() ) );
        p_return.add( CRawTerm.from( l_date.getYear() ) );
        p_return.add( CRawTerm.from( l_date.getDayOfWeek() ) );
        p_return.add( CRawTerm.from( l_date.getDayOfYear() ) );
        p_return.add( CRawTerm.from( l_date.getZone() ) );

        return CFuzzyValue.from( true );
    }

}
