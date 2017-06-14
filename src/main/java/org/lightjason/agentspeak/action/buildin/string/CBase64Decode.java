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

package org.lightjason.agentspeak.action.buildin.string;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;


/**
 * action to decodes a string with Base64.
 * The decoded string version is created from each string argument, which is
 * based64 encoded, the action fails on decoding error.
 *
 * @code [A|B] = string/base64decode( "aGVsbG8=", "QWdlbnRTcGVhayhMKysp" ); @endcode
 * @note return null on encoding errors
 * @see https://en.wikipedia.org/wiki/Base64
 */
public final class CBase64Decode extends IBuildinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2067392870344691806L;

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument,  @Nonnull final List<ITerm> p_return )
    {
        return CFuzzyValue.from(
            CCommon.flatten( p_argument )
               .map( ITerm::<String>raw )
               .allMatch( i -> CBase64Decode.apply( i, p_return ) )
        );
    }

    /**
     * create a string with encoding
     *
     * @param p_string byte character
     * @param p_return return list
     * @return boolean successful run
     */
    private static boolean apply( @Nonnull final String p_string, @Nonnull final List<ITerm> p_return )
    {
        try
        {
            p_return.add(
                CRawTerm.from(
                    new String( Base64.getDecoder().decode( p_string.getBytes( Charset.forName( "UTF-8" ) ) ), "UTF-8" )
                )
            );

            return true;
        }
        catch ( final IllegalArgumentException | UnsupportedEncodingException l_exception )
        {
            return false;
        }
    }

}
