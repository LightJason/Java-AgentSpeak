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

package org.lightjason.agentspeak.action.builtin.generic.type;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * converts a value into the string represenation.
 * The action converts any argument into the string
 * represenation, the action never fails
 *
 * @code @endcode
 */
public final class CToString extends ICast
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5084494896831207543L;

    /**
     * cast / translates value
     *
     * @param p_value term value
     * @param p_return return arguments
     * @return successful boolean
     */
    protected final boolean cast( @Nonnull final ITerm p_value, @Nonnull final List<ITerm> p_return )
    {
        p_return.add( CRawTerm.from( p_value.raw().toString() ) );
        return true;
    }

}
