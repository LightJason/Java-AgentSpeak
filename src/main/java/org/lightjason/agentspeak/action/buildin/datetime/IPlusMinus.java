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


import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * abstract class to calculate
 * plus / minus on date-time objects
 */
public abstract class IPlusMinus extends IBuildinAction
{
    /**
     * ctor
     */
    protected IPlusMinus()
    {
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        switch ( l_arguments.get( 0 ).<String>raw().trim().toLowerCase( Locale.ROOT ) )
        {

            case "minus" :
                this.applyminus(
                    l_arguments.stream()
                               .skip( 2 )
                               .map( ITerm::<ZonedDateTime>raw ),
                    l_arguments.get( 1 ).<Number>raw().longValue()
                )
                    .map( CRawTerm::from )
                    .forEach( p_return::add );

                return CFuzzyValue.from( true );


            case "plus" :
                this.applyplus(
                    l_arguments.stream()
                               .skip( 2 )
                               .map( ITerm::<ZonedDateTime>raw ),
                    l_arguments.get( 1 ).<Number>raw().longValue()
                )
                    .map( CRawTerm::from )
                    .forEach( p_return::add );

                return CFuzzyValue.from( true );


            default:
                return CFuzzyValue.from( false );

        }
    }

    /**
     * apply function for plus operator
     *
     * @param p_datetime date-time object stream
     * @param p_value value
     * @return return object stream
     */
    protected abstract Stream<?> applyminus( final Stream<ZonedDateTime> p_datetime, final long p_value );

    /**
     * apply function for minus operator
     *
     * @param p_datetime date-time object stream
     * @param p_value value
     * @return return object stream
     */
    protected abstract Stream<?> applyplus( final Stream<ZonedDateTime> p_datetime, final long p_value );

}
