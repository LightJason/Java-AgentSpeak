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

import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * creates the intersection between lists.
 * All arguments are lists and the action returns the
 * intersection \f$ \cap M_i \forall i \in \mathbb{N} \f$, the action fails never
 *
 * {@code I = collection/list/intersect( [1,2,[3,4]], [3,4,[8,9]], [1,2,3,5] );}
 */
public final class CIntersect extends IBuiltinAction
{

    /**
     * serial id
     */
    private static final long serialVersionUID = 7453409804177199062L;

    /**
     * ctor
     */
    public CIntersect()
    {
        super( 3 );
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        // all arguments must be lists (build unique list of all elements and check all collection if an element exists in each collection)
        final List<Object> l_result = CCommon.flatten( p_argument )
                                        .parallel()
                                        .map( ITerm::raw )
                                        .distinct()
                                        .filter(
                                            i -> p_argument.parallelStream()
                                                           .allMatch( j -> j.<Collection<ITerm>>raw()
                                                               .parallelStream()
                                                               .map( ITerm::raw )
                                                               .collect( Collectors.toList() )
                                                               .contains( i )
                                                           )
                                        ).collect( Collectors.toList() );
        l_result.sort( Comparator.comparing( Object::hashCode ) );

        p_return.add( CRawTerm.of(
            p_parallel ? Collections.synchronizedList( l_result ) : l_result
        ) );

        return CFuzzyValue.of( true );
    }

}
