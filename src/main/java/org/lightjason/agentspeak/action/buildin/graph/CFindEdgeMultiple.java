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

import edu.uci.ics.jung.graph.Graph;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.List;


/**
 * returns all edges between vertices for a graph instance.
 * The action returns for each tuple of vertices the edge of a single
 * graph instance, the action never fails
 *
 * @code [E1|E2|E3] = graph/findedgesingle( Graph, Vertex1, Vertex2, [Vertex3, Vertex4, [Vertex5, Vertex6]] ); @endcode
 */
public final class CFindEdgeMultiple extends IApplyMultiple
{
    @Override
    protected final int windowsize()
    {
        return 2;
    }

    @Override
    protected final void apply( final Graph<Object, Object> p_graph, final List<ITerm> p_window, final List<ITerm> p_return
    )
    {
        p_return.add(
            CRawTerm.from(
                p_graph.findEdge( p_window.get( 0 ).raw(), p_window.get( 1 ).raw() )
            )
        );
    }
}
