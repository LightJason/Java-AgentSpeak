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

package org.lightjason.agentspeak.action.buildin.math.interpolate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * action to create interpolated values.
 * The action interpolates a single value, the first
 * argument is the value (x-position), all
 * other interpolating functions, the action never fails
 * interpolation
 *
 * @code [A|B|C] = math/interpolate/multipleinterpolate( 5, InterpolatingFunction1, [InterpolatingFunction2, [InterpolatingFunction3]] ); @endcode
 * @see https://en.wikipedia.org/wiki/Polynomial_interpolation
 */
public final class CMultipleInterpolate extends IBuildinAction
{

    /**
     * ctor
     */
    public CMultipleInterpolate()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, final IContext p_context, final List<ITerm> p_argument,
                                               final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );

        l_arguments.stream()
                   .skip( 1 )
                   .map( ITerm::<UnivariateFunction>raw )
                   .mapToDouble( i -> i.value( l_arguments.get( 0 ).<Number>raw().doubleValue() ) )
                   .boxed()
                   .map( CRawTerm::from )
                   .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }

}
