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

package org.lightjason.agentspeak.action.builtin.math.shape;

import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.error.context.CActionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * action check if a point is within a triangle.
 * The first three tuple of arguments defines the triangle
 * coordinate (x- / y-position), all other tuples are the tuples
 * of x- / y-position
 *
 * {@code [In1|In2] = .math/shape/intriangle( [[350,320], [25,375], 40,55], [160,270], 0,0 );}
 * @see https://en.wikipedia.org/wiki/Barycentric_coordinate_system
 */
public final class CInTriangle extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 6631790597145945788L;

    /**
     * ctor
     */
    public CInTriangle()
    {
        super( 3 );
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 8;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<Double> l_arguments = CCommon.flatten( p_argument )
                                                .map( ITerm::<Number>raw )
                                                .mapToDouble( Number::doubleValue )
                                                .boxed()
                                                .collect( Collectors.toList() );
        if ( l_arguments.size() < 8 )
            throw new CActionIllegealArgumentException( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumentnumber", 8, l_arguments.size() ) );

        StreamUtils.windowed( l_arguments.stream().skip( 6 ), 2, 2 )
                   .peek( i ->
                   {
                       i.add(
                           l_arguments.get( 1 ) * l_arguments.get( 4 )
                           - l_arguments.get( 0 ) * l_arguments.get( 5 )
                           + ( l_arguments.get( 5 ) - l_arguments.get( 1 ) ) * i.get( 0 )
                           + ( l_arguments.get( 0 ) - l_arguments.get( 4 ) ) * i.get( 1 )
                       );

                       i.add(
                           l_arguments.get( 0 ) * l_arguments.get( 3 )
                           - l_arguments.get( 1 ) * l_arguments.get( 2 )
                           + ( l_arguments.get( 1 ) - l_arguments.get( 3 ) ) * i.get( 0 )
                           + ( l_arguments.get( 2 ) - l_arguments.get( 0 ) ) * i.get( 1 )
                       );
                   } )
                   .map( i -> i.get( 2 ) > 0 && i.get( 3 ) > 0
                              && i.get( 2 ) + i.get( 3 ) < -l_arguments.get( 3 ) * l_arguments.get( 4 )
                                                            + l_arguments.get( 1 ) * ( -l_arguments.get( 2 ) + l_arguments.get( 3 ) )
                                                            + l_arguments.get( 0 ) * ( l_arguments.get( 3 ) - l_arguments.get( 5 ) )
                                                            + l_arguments.get( 2 ) * l_arguments.get( 5 )
                   )
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }

}
