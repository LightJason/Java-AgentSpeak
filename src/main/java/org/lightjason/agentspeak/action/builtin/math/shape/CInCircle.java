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
 * action check if a point is within a circle.
 * The action checks if a point is within a circle,
 * the first three arguments defines the circle (x- / y-position
 * and radius), all other arguments will be used as tuples
 * (x- / y-position) which defines the point
 *
 * {@code [In1|In2] = .math/shape/incircle( 1,1,1,  2,2.5, [3,4] );}
 */
public final class CInCircle extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7618753272977347282L;

    /**
     * ctor
     */
    public CInCircle()
    {
        super( 3 );
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
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<Double> l_arguments = CCommon.flatten( p_argument )
                                                .map( ITerm::<Number>raw )
                                                .mapToDouble( Number::doubleValue )
                                                .boxed()
                                                .collect( Collectors.toList() );
        if ( l_arguments.size() < 5 )
            return CFuzzyValue.of( false );


        StreamUtils.windowed( l_arguments.stream().skip( 3 ), 2, 2 )
                   .map( i -> Math.hypot( i.get( 0 ) - l_arguments.get( 0 ), i.get( 1 ) - l_arguments.get( 1 ) ) <= Math.pow( l_arguments.get( 2 ), 2 ) )
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }

}
