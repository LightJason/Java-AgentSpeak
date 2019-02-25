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

package org.lightjason.agentspeak.action.builtin.collection.multimap;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
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
 * returns all key values of the multimap.
 * Returns a unique list with all key values of the argument
 * maps, all arguments must be multimaps
 *
 * {@code L = .collection/multimap/keys( MultiMap1, MultiMap2, MultiMap3 );}
 */
public final class CKeys extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -5971820545869138123L;

    /**
     * ctor
     */
    public CKeys()
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
        // arguments are map references
        final List<?> l_result = p_argument.stream()
                                           .flatMap( i -> i.<Multimap<?, ?>>raw().keySet().stream() )
                                           .sorted()
                                           .distinct()
                                           .collect( Collectors.toList() );

        p_return.add(
            CRawTerm.of(
                p_parallel ? Collections.synchronizedList( l_result ) : l_result
            )
        );

        return Stream.of();
    }

}
