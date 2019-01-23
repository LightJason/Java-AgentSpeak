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
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * returns a sublist within the index range.
 * Creates a sublist of an existing list by an index range,
 * first argument is the list object, all other arguments
 * are tuples of ranges \f$ [ \text{lower-bound}, \text{upper-bound} ) \f$
 *
 * {@code [L1|L2] = .collection/list/get( L, 2, 5, [4, 6] );}
 */
public final class CSubList extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 5029899221891965636L;

    /**
     * ctor
     */
    public CSubList()
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
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = Stream.concat( Stream.of( p_argument.get( 0 ) ), CCommon.flatten( p_argument.stream().skip( 1 ) ) )
                                              .collect( Collectors.toList() );

        if ( l_arguments.size() % 2 == 0 || l_arguments.size() < 3 )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumentseven" )
            );

        StreamUtils.windowed(
            l_arguments.stream()
                       .skip( 1 )
                       .map( ITerm::<Number>raw )
                       .map( Number::intValue ),
            2,
            2
        )
                   .map( i -> l_arguments.get( 0 ).<List<?>>raw().subList( i.get( 0 ), i.get( 1 ) ) )
                   .map( i -> p_parallel ? Collections.synchronizedList( i ) : i )
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }

}
