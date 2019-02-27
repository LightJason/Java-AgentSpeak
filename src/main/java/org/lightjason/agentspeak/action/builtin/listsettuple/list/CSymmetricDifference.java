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

package org.lightjason.agentspeak.action.builtin.listsettuple.list;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * creates the symmetric difference between lists (difference of union and intersection).
 * Creates the symmetric difference of all arguments, so all arguments are collections and the action will return
 * a list with the symmetric difference \f$ (\mathbb{X} \setminus \mathbb{Y}) \cup (\mathbb{B} \setminus \mathbb{A}) \f$
 *
 * {@code D = .collection/list/symmetricdifference( [1,2,[3,4]], [7,8,9,4], [[1,2], [3]] );}
 *
 * @see https://en.wikipedia.org/wiki/Symmetric_difference
 */
public final class CSymmetricDifference extends IBaseAction
{

    /**
     * serial id
     */
    private static final long serialVersionUID = 7657032978898575726L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CSymmetricDifference.class, "collection", "list" );

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
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        // create a multiset and counts the occurence of element -> on an odd number the element will be returned
        final Multiset<Object> l_count = ConcurrentHashMultiset.create();
        CCommon.flatten( p_argument ).parallel().map( ITerm::raw ).forEach( l_count::add );
        final List<Object> l_result = l_count.entrySet()
                                             .parallelStream()
                                             .filter( i -> !( i.getCount() % 2 == 0 ) )
                                             .map( Multiset.Entry::getElement )
                                             .sorted( Comparator.comparing( Object::hashCode ) )
                                             .collect( Collectors.toList() );

        p_return.add(
            CRawTerm.of(
                p_parallel
                ? Collections.synchronizedList( l_result )
                : l_result
            )
        );

        return Stream.of();
    }

}
