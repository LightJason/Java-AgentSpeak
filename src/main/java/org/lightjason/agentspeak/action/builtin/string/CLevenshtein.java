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
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * calculates the levenshtein distance.
 * The action returns the levenshtein distance
 * between string inputs, for the first string
 * argument the distance will be calculated to the rest,
 * if there are numerical arguments the first will be used
 * for the inserting weight, second replace weight and third
 * for the delete weight
 *
 * {@code [A|B] = .string/levenshtein( 1,1.5,3, "start", "end", "starting" );}
 *
 * @see https://en.wikipedia.org/wiki/Levenshtein_distance
 */
public final class CLevenshtein extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -7085898562771050640L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CLevenshtein.class, "string" );

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
        // extract string arguments
        final List<String> l_strings = CCommon.flatten( p_argument )
                                              .filter( i -> CCommon.isssignableto( i, String.class ) )
                                              .map( ITerm::<String>raw )
                                              .collect( Collectors.toList() );

        if ( l_strings.size() < 2 )
            throw new CExecutionIllegealArgumentException( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumentnumberwrong", 2 ) );


        // create weight
        final List<Double> l_weights = CCommon.flatten( p_argument )
                                              .filter( i -> CCommon.isssignableto( i, Number.class ) )
                                              .map( ITerm::<Number>raw )
                                              .mapToDouble( Number::doubleValue )
                                              .boxed()
                                              .collect( Collectors.toList() );

        // if weights not set, set defaults
        IntStream.range( l_weights.size(), 3 ).forEach( i -> l_weights.add( 1.0 ) );

        // create distance
        l_strings.stream()
                 .skip( 1 )
                 .mapToDouble( i -> CCommon.levenshtein( l_strings.get( 0 ), i, l_weights.get( 1 ), l_weights.get( 1 ), l_weights.get( 2 ) ) )
                 .boxed()
                 .map( CRawTerm::of )
                 .forEach( p_return::add );

        return Stream.of();
    }
}
