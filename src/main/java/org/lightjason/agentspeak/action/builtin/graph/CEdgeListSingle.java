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
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * returns all edges of two vertices of each graph instance.
 * The action returns a list of edges between two vertices
 * of each graph instance, the first two
 * arguments are vertices, all other arguments
 * are graphs and return values are lists
 *
 * {@code [L1|L2] = .graph/edgelist( Vertex1, Vertex2, Graph1, Graph2 );}
 */
public final class CEdgeListSingle extends IApplySingle
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 6501468310507608790L;

    @Override
    protected int skipsize()
    {
        return 2;
    }

    @Override
    protected void apply( final boolean p_parallel, @Nonnull final Graph<Object, Object> p_graph,
                          @Nonnull final List<ITerm> p_window, @Nonnull final List<ITerm> p_return )
    {
        final List<?> l_return = new ArrayList<>( p_graph.findEdgeSet( p_window.get( 0 ).raw(), p_window.get( 1 ).raw() ) );

        p_return.add(
            CRawTerm.of(
                p_parallel
                ? Collections.synchronizedList( l_return )
                : l_return
            )
        );
    }

}
