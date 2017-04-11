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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Stream;


/**
 * abstract class to calculate difference
 * between date-time elements
 */
public abstract class IBetween extends IBuildinAction
{
    /**
     * ctor
     */
    protected IBetween()
    {}

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        this.apply(
            StreamUtils.windowed(
                CCommon.flatcollection( p_argument )
                       .map( ITerm::<ZonedDateTime>raw )
                       .map( i ->  new DateTime( i.toInstant().toEpochMilli(), DateTimeZone.forTimeZone( TimeZone.getTimeZone( i.getZone() ) ) ) )
                       .map( Instant::new ),
                2,
                2
            )
        )
            .map( CRawTerm::from )
            .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

    /**
     * calculating method
     *
     * @param p_datetime stream with list of date-time elements
     * @return output stream
     */
    protected abstract Stream<?> apply( final Stream<List<Instant>> p_datetime );

}
