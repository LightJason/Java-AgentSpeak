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

package org.lightjason.agentspeak.action.buildin.collection.list;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * creates the symmetric difference between lists (difference of union and intersection).
 * Creates the symmetric difference of all arguments, so all arguments are collections and the action will return
 * a list with the symmetric difference \f$ (\mathbb{X} \setminus \mathbb{Y}) \cup (\mathbb{B} \setminus \mathbb{A}) \f$,
 * the action fails never
 *
 * @code D = collection/list/symmetricdifference( [1,2,[3,4]], [7,8,9,4], [[1,2], [3]] ); @endcode
 * @see https://en.wikipedia.org/wiki/Symmetric_difference
 */
public final class CSymmetricDifference extends IBuildinAction
{
    /**
     * ctor
     */
    public CSymmetricDifference()
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
        // create a multiset and counts the occurence of element -> on an odd number the element will be returned
        final Multiset<Object> l_count = ConcurrentHashMultiset.create();
        CCommon.flatcollection( p_argument ).parallel().map( ITerm::raw ).forEach( l_count::add );
        final List<?> l_result = l_count.entrySet().parallelStream().filter( i -> i.getCount() % 2 == 1 ).map( Multiset.Entry::getElement ).collect( Collectors.toList() );

        p_return.add(
            CRawTerm.from(
                p_parallel
                ? Collections.synchronizedList( l_result )
                : l_result
            )
        );

        return CFuzzyValue.from( true );
    }

}
