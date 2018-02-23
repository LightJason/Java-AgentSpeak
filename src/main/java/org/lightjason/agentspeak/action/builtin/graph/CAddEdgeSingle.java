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

package org.lightjason.agentspeak.action.builtin.graph;

import edu.uci.ics.jung.graph.Graph;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * adds a single edge to the multiple graph.
 * The action adds to each graph the first triple
 * (edge identifier, start vertex, end vertex)
 * to all other arguments are graphs, the action
 * never fails
 *
 * @code graph/addedge( Edge, StartVertex, EndVertex, Graph1, Graph2, Graph3 ); @endcode
 */
public final class CAddEdgeSingle extends IApplySingle
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1893938682357853616L;

    @Override
    protected final int skipsize()
    {
        return 3;
    }

    @Override
    protected final void apply( final boolean p_parallel, @Nonnull final Graph<Object, Object> p_graph,
                                @Nonnull final List<ITerm> p_window, @Nonnull final List<ITerm> p_return )
    {
        p_graph.addEdge( p_window.get( 0 ).raw(), p_window.get( 1 ).raw(), p_window.get( 2 ).raw(), p_graph.getDefaultEdgeType() );
    }

}
