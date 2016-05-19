/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agentspeak.action.buildin.generic.datetime;

import lightjason.agentspeak.action.buildin.IBuildinAction;
import lightjason.agentspeak.language.CCommon;
import lightjason.agentspeak.language.ITerm;
import lightjason.agentspeak.language.execution.IContext;
import lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

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
    public final int getMinimalArgumentNumber()
    {
        return 0;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final ZonedDateTime l_date = p_argument.size() == 1 ? ZonedDateTime.parse( CCommon.getRawValue( p_argument.get( 0 ) ) ) : ZonedDateTime.now();

        p_return.add( CCommon.getRawValue( l_date.getDayOfMonth() ) );
        p_return.add( CCommon.getRawValue( l_date.getMonthValue() ) );
        p_return.add( CCommon.getRawValue( l_date.getYear() ) );
        p_return.add( CCommon.getRawValue( l_date.getDayOfWeek() ) );
        p_return.add( CCommon.getRawValue( l_date.getDayOfYear() ) );
        p_return.add( CCommon.getRawValue( l_date.getZone() ) );

        return CFuzzyValue.from( true );
    }

}
