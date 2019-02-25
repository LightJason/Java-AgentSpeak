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

package org.lightjason.agentspeak.action.builtin.math;

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;


/**
 * action for calculating the euclidian length.
 * Calculates for each pair of arguments \f$ \sqrt{ x_{i}^{2} + y_{i}^{2} } \f$ and
 * fail iif the number of arguments are odd, it unflats all list elements
 *
 * {@code [A|B|C] = .math/hypot( 1, [2, [3]], [4, 5]);}
 */
public final class CHypot extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3560235052118385438L;

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        if ( p_argument.size() % 2 == 1 )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumentsnoteven" )
            );

        StreamUtils.windowed( CCommon.flatten( p_argument ), 2, 2 )
                   .map( i -> Math.hypot(
                       i.get( 0 ).<Number>raw().doubleValue(),
                       i.get( 1 ).<Number>raw().doubleValue()
                   ) )
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }

}
