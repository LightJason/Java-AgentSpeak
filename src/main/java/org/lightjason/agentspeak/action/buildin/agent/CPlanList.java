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

package org.lightjason.agentspeak.action.buildin.agent;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * action to get plan-information as list.
 * The action returns a list of tuples with
 * the a string (trigger definition) and the
 * plan literal, the action never fails
 *
 * @code L = agent/planlist(); @endcode
 */
public final class CPlanList extends IBuildinAction
{

    @Override
    public final int minimalArgumentNumber()
    {
        return 0;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return )
    {
        final List<?> l_list = p_context.agent()
                 .plans()
                 .values()
                 .stream()
                 .map( i -> i.getLeft().trigger() )
                 .sorted()
                 .distinct()
                 .map( i -> new AbstractMap.SimpleImmutableEntry<>( i.type().toString(), i.literal() ) )
                 .collect( Collectors.toList() );

        p_return.add(
            CRawTerm.from(
                p_parallel
                ? Collections.synchronizedList( l_list )
                : l_list
            )
        );

        return CFuzzyValue.from( true );
    }
}
