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
 * action for calculating binomial coefficient.
 * Calcluates \f$ \binom{n}{k} \f$, where n is the
 * first argument and k the second of each given input
 * tupel, the action fails never
 *
 * {@code [B1|B2] = math/binomial( 49, 6,  30, 5 );}
 * @see https://en.wikipedia.org/wiki/Binomial_coefficient
 */
public final class CBinomial extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7542134505799374485L;

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
                   .map( i -> CombinatoricsUtils.binomialCoefficient( i.get( 0 ), i.get( 1 ) ) )
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return CFuzzyValue.of( true );
    }

}
