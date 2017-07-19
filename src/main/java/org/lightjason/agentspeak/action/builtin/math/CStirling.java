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

package org.lightjason.agentspeak.action.builtin.math;

import com.codepoetics.protonpack.StreamUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;


/**
 * action for calculating stirling number.
 * The  action calculates the stirling number
 * with \f$ S(n,k)=\left\{\begin{matrix} n \\ k \end{matrix}\right\}= S_n^{(k)} \f$
 * of each tuple of the unflatten argument list, n is the first value of the tupel
 * and k is the second value of the tupel, the action fails never
 *
 * @code [S1|S2] = math/stirling(2,3, [4,5]); @endcode
 * @see https://en.wikipedia.org/wiki/Stirling_number
 */
public final class CStirling extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -9056209645109382946L;

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        StreamUtils.windowed(
            CCommon.flatten( p_argument )
                   .map( ITerm::<Number>raw )
                   .mapToInt( Number::intValue )
                   .boxed(),
            2,
            2
        )
                   .map( i -> CombinatoricsUtils.stirlingS2( i.get( 0 ), i.get( 1 ) ) )
                   .map( CRawTerm::from )
                   .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

}
