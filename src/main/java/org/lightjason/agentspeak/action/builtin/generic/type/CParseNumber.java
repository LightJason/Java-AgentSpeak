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

package org.lightjason.agentspeak.action.builtin.generic.type;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.Map;


/**
 * action for parsing a number from string.
 * Parses each argument to a number value
 * and returns the value, the action fails on
 * parsing errors
 *
 * @code [X|Y|Z] = generic/type/parsenumber( "1.45", ["8.88", "9"] ); @endcode
 */
public final class CParseNumber extends IParse
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1809811719927824635L;

    /**
     * parses the input string
     *
     * @param p_value string value
     * @return tuple with boolean (for parsing error) and term
     */
    @Nonnull
    protected final Map.Entry<Boolean, ITerm> parse( @Nonnull final String p_value )
    {
        try
        {
            return new AbstractMap.SimpleImmutableEntry<>( true, CRawTerm.from( Double.parseDouble( p_value ) ) );
        }
        catch ( final Exception l_exception )
        {
            return new AbstractMap.SimpleImmutableEntry<>( false, CRawTerm.from( null ) );
        }
    }

}
