/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * creates the symmetric difference between lists (difference of union and intersection)
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
    public final int getMinimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // create a multiset and counts the occurence of element -> on an odd number the element will be returned
        final Multiset<?> l_count = ConcurrentHashMultiset.create();
        CCommon.flatList( p_argument ).parallelStream().forEach( i -> l_count.add( CCommon.raw( i ) ) );
        final List<?> l_result = l_count.entrySet().parallelStream().filter( i -> i.getCount() % 2 == 1 ).collect( Collectors.toList() );

        p_return.add( CRawTerm.from(
            p_parallel
            ? Collections.synchronizedList( l_result )
            : l_result
        ) );

        return CFuzzyValue.from( true );
    }

}
