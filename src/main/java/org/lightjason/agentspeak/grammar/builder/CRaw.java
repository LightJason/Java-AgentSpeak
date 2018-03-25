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

package org.lightjason.agentspeak.grammar.builder;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.lightjason.agentspeak.language.CCommon;

import javax.annotation.Nonnull;
import java.util.Objects;


/**
 * builder for raw structure
 */
public final class CRaw
{
    /**
     * constant prefix
     */
    private static final String CONSTANTPREFIX = "#";

    /**
     *
     */
    private CRaw()
    {
    }

    /**
     * parsing number
     *
     * @param p_number terminal number node
     * @return number value
     */
    @Nonnull
    public static Number numbervalue( @Nonnull final TerminalNode p_number )
    {
        return numbervalue( p_number.getText() );
    }

    /**
     * parsing number
     *
     * @param p_number terminal number node
     * @return number value
     */
    @Nonnull
    public static Number numbervalue( @Nonnull final String p_number )
    {
        // try to match to a numeric constant
        final Double l_constant = CCommon.NUMERICCONSTANT.get( p_number.replace( CONSTANTPREFIX, "" ) );
        if ( Objects.nonNull( l_constant ) )
            return l_constant;

        // otherwise try to parse number value
        return Double.valueOf( p_number );
    }

    /**
     * converts a string token to the type
     *
     * @param p_value string value
     * @return boolean value
     */
    public static boolean logicalvalue( @Nonnull final TerminalNode p_value )
    {
        return ( !p_value.getText().isEmpty() ) && ( ( "true".equals( p_value.getText().replace( CONSTANTPREFIX, "" ) ) )
                                                     || ( "success".equals( p_value.getText().replace( CONSTANTPREFIX, "" ) ) ) );
    }

    /**
     * create a string value without quotes
     *
     * @param p_value string
     * @return string without quotes
     */
    @Nonnull
    public static String stringvalue( @Nonnull final TerminalNode p_value )
    {
        // on a string the single- and double-quotes must be removed
        return p_value.getText().length() < 3 ? "" : p_value.getText().substring( 1, p_value.getText().length() - 1 );
    }

}
