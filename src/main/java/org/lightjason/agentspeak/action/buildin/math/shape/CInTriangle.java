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

package org.lightjason.agentspeak.action.buildin.math.shape;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * action check if a point is within a triangle
 *
 * @see https://en.wikipedia.org/wiki/Barycentric_coordinate_system
 */
public final class CInTriangle extends IBuildinAction
{

    /**
     * ctor
     */
    public CInTriangle()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 8;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // arguments are: x-value, y-value (index 0 / 1),
        // triange point 0 x-value, y-value (index 2 / 3),
        // triange point 1 x-value, y-value (index 4 / 5),
        // triange point 2 x-value, y-value (index 6 / 7)
        final List<Double> l_point = p_argument.stream()
                                               .map( ITerm::<Number>raw )
                                               .mapToDouble( Number::doubleValue )
                                               .boxed()
                                               .collect( Collectors.toList() );

        final double l_xvalue = l_point.get( 3 ) * l_point.get( 6 )
                                - l_point.get( 2 ) * l_point.get( 7 )
                                + ( l_point.get( 7 ) - l_point.get( 3 ) ) * l_point.get( 0 )
                                + ( l_point.get( 2 ) - l_point.get( 6 ) ) * l_point.get( 1 );

        final double l_yvalue = l_point.get( 2 ) * l_point.get( 5 )
                                - l_point.get( 3 ) * l_point.get( 4 )
                                + ( l_point.get( 3 ) - l_point.get( 5 ) ) * l_point.get( 0 )
                                + ( l_point.get( 4 ) - l_point.get( 2 ) ) * l_point.get( 1 );

        if ( ( l_xvalue <= 0 ) || ( l_yvalue <= 0 ) )
        {
            p_return.add( CRawTerm.from( false ) );
            return CFuzzyValue.from( true );
        }

        p_return.add( CRawTerm.from(
            l_xvalue + l_yvalue < -l_point.get( 5 ) * l_point.get( 6 )
                                  + l_point.get( 3 ) * ( -l_point.get( 4 ) + l_point.get( 6 ) )
                                  + l_point.get( 2 ) * ( l_point.get( 5 ) - l_point.get( 7 ) )
                                  + l_point.get( 4 ) * l_point.get( 7 )
        ) );
        return CFuzzyValue.from( true );
    }

}
