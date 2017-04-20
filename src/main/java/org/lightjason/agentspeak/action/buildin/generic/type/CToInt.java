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

package org.lightjason.agentspeak.action.buildin.generic.type;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.List;


/**
 * action to cast a value to an integer value.
 * Cast any argument into an integer number,
 * the action fails on casting errors
 *
 * @code [N1|N2] = generic/type/tofloat( X, Y ); @endcode
 */
public final class CToInt extends ICast
{

    @Override
    protected final boolean cast( final ITerm p_value, final List<ITerm> p_return )
    {
        try
        {
            p_return.add( CRawTerm.from( p_value.<Number>raw().longValue() ) );
            return true;
        }
        catch ( final Exception l_exception )
        {
            return false;
        }
    }

}
