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

package org.lightjason.agentspeak.action.builtin.string;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;


/**
 * action to decodes a string with Base64.
 * The decoded string version is created of each string argument, which is
 * based64 encoded, the action fails on decoding error. Return null on encoding errors
 *
 * {@code [A|B] = .string/base64decode( "aGVsbG8=", "QWdlbnRTcGVhayhMKysp" );}
 *
 * @see https://en.wikipedia.org/wiki/Base64
 */
public final class CBase64Decode extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2067392870344691806L;
    /**
     * action name
     */
    private static final IPath NAME = CPath.of( "string/base64decode" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        CCommon.flatten( p_argument )
               .map( ITerm::<String>raw )
               .forEach( i -> CBase64Decode.apply( p_context, i, p_return ) );

        return Stream.of();
    }

    /**
     * create a string with encoding
     *
     * @param p_context execution context
     * @param p_string byte character
     * @param p_return return list
     */
    private static void apply( @Nonnull final IContext p_context, @Nonnull final String p_string, @Nonnull final List<ITerm> p_return )
    {
        try
        {
            p_return.add(
                CRawTerm.of(
                    new String( Base64.getDecoder().decode( p_string.getBytes( Charset.forName( "UTF-8" ) ) ), StandardCharsets.UTF_8 )
                )
            );
        }
        catch ( final IllegalArgumentException l_exception )
        {
            throw new CExecutionIllegealArgumentException( p_context, l_exception );
        }
    }

}
