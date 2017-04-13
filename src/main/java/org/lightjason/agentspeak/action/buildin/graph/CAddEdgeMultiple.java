/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.graph;

import com.codepoetics.protonpack.StreamUtils;
import edu.uci.ics.jung.graph.Graph;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * add multiple edges to a single graph instance.
 * Adds multiple edges to a single graph instance, the first
 * argument is the graph reference, and all other triples
 * are the edges, the first argument of the triple is the
 * edge identifier, the second the start vertex, and the third
 * the end vertex, the action never fails
 *
 * @code graph/addedgemultiple( Graph, [ "edgeid1", StartVertex1, EndVertex1 ], "edgeid2", StartVertex2, EndVertex2 ); @endcode
 */
public final class CAddEdgeMultiple extends IBuildinAction
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        StreamUtils.windowed(
            CCommon.flatcollection( p_argument )
               .skip( 1 )
               .map( ITerm::raw ),
               3,
               3
        )
                   .forEach( i -> p_argument.get( 0 ).<Graph<Object, Object>>raw().addEdge( i.get( 0 ), i.get( 1 ), i.get( 2 ) ) );

        return CFuzzyValue.from( true );
    }
}
