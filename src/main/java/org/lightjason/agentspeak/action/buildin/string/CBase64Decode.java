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

package org.lightjason.agentspeak.action.buildin.string;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;


/**
 * action to decodes a string with Base64.
 * Creates from each string argument, which is
 * based64 encoded the decoded string version,
 * the action never fails
 *
 * @code [A|B] = generic/string/base64decode( "aGVsbG8=", "QWdlbnRTcGVhayhMKysp" ); @endcode
 * @note return null on encoding errors
 * @see https://en.wikipedia.org/wiki/Base64
 */
public final class CBase64Decode extends IBuildinAction
{

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
        CCommon.flatcollection( p_argument )
               .map( ITerm::<String>raw )
               .map( i -> CBase64Decode.from( Base64.getDecoder().decode( i.getBytes( Charset.forName( "UTF-8" ) ) ) ) )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

    /**
     * create a string with enconding
     *
     * @param p_character byte character
     * @return string or null on error
     */
    private static String from( final byte[] p_character )
    {
        try
        {
            return new String( p_character, "UTF-8" );
        }
        catch ( final UnsupportedEncodingException l_exception )
        {
            return null;
        }
    }

}
