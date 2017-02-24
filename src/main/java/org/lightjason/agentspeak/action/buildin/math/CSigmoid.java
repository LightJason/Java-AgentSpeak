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

package org.lightjason.agentspeak.action.buildin.math;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.function.Function;


/**
 * action for calculating a parameterized sigmoid function.
 * Action calculates the sigmoid function for each value, the definition
 * of the function is \f$ \frac{\alpha}{ \beta + e^{ - \gamma \cdot t }} \f$
 * \f$ \alpha \f$ is the first, \f$ \beta \f$ the second and \f$ \gamma \f$ the third
 * argument, all values beginning at the fourth position will be used for t, so the
 * action returns all values but and is never failing.
 * @code [A | B | C] = math/sigmoid( 1,1,1, 10,20,30 ); @endcode
 *
 * @see https://en.wikipedia.org/wiki/Sigmoid_function
 */
public final class CSigmoid extends IBuildinAction
{

    @Override
    public final int minimalArgumentNumber()
    {
        return 3;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final Function<Double, Double> l_sigmoid = (i) ->
            p_argument.get( 0 ).<Double>raw() / ( p_argument.get( 1 ).<Double>raw() + Math.exp( -p_argument.get( 2 ).<Double>raw() ) * i );

        CCommon.flatcollection( p_argument )
               .mapToDouble( i -> i.<Number>raw().doubleValue() )
               .boxed()
               .map( l_sigmoid )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

}
