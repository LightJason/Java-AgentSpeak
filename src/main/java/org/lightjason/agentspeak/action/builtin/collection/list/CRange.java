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

package org.lightjason.agentspeak.action.builtin.collection.list;

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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * creates a list with a integer ranged list.
 * The action creates a list of integer values within the
 * range \f$ [\text{argument 1}, \text{argument 2}) \f$,
 * the action need a even number of arguments, for each
 * tuple a ranged list will be returned
 *
 * {@code [L1|L2] = .collection/list/create(0, 10, [2, 9]);}
 */
public final class CRange extends IBuiltinAction
{

    /**
     * serial id
     */
    private static final long serialVersionUID = 4072747153980493017L;

    /**
     * ctor
     */
    public CRange()
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
        final List<Integer> l_arguments = CCommon.flatten( p_argument )
                                                 .map( ITerm::<Number>raw )
                                                 .mapToInt( Number::intValue )
                                                 .boxed()
                                                 .collect( Collectors.toList() );
        if ( l_arguments.isEmpty() || l_arguments.size() % 2 == 1 )
            return CFuzzyValue.of( false );

        StreamUtils.windowed(
            l_arguments.stream(),
            2,
            2
        )
            .map( i -> IntStream.range( i.get( 0 ), i.get( 1 ) ).boxed().collect( Collectors.toList() ) )
            .map( i -> p_parallel ? Collections.synchronizedList( i ) : i )
            .map( CRawTerm::of )
            .forEach( p_return::add );

        return Stream.of();
    }

}
