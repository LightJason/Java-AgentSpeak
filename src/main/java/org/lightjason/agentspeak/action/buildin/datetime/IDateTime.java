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

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;


/**
 * abstract class for date-time access
 */
public abstract class IDateTime extends IBuildinAction
{

    /**
     * ctor
     */
    protected IDateTime()
    {}

    @Override
    public final int minimalArgumentNumber()
    {
        return 0;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        return CFuzzyValue.from(
            (

                p_argument.size() == 0
                ? Stream.of( CRawTerm.from( "now" ) )
                : CCommon.flatcollection( p_argument )

            )
                .allMatch( i -> this.getdatetime( i, p_return ) )
        );
    }

    /**
     * transfers the date-time element to term elements
     *
     * @param p_datetime date-time definition
     * @param p_return return arguments
     * @return successfull return
     */
    protected abstract boolean elements( @Nonnull final ZonedDateTime p_datetime, @Nonnull final List<ITerm> p_return );

    /**
     * creates the date representation
     *
     * @param p_value date value, empty or now returns the current date
     * @param p_return return arguments
     * @return successfull execution
     */
    private boolean getdatetime( @Nonnull final ITerm p_value, @Nonnull final List<ITerm> p_return )
    {
        if ( CCommon.rawvalueAssignableTo( p_value, ZonedDateTime.class ) )
            return this.elements( p_value.<ZonedDateTime>raw(), p_return );

        try
        {
            return this.elements(
                ( p_value.<String>raw().isEmpty() ) || ( "now".equalsIgnoreCase( p_value.<String>raw() ) )
                ? ZonedDateTime.now()
                : ZonedDateTime.parse( p_value.<String>raw() ),
                p_return
            );
        }
        catch ( final DateTimeParseException l_excaption )
        {
            return false;
        }
    }
}
