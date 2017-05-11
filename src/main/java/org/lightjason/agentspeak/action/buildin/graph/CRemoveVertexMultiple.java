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

package org.lightjason.agentspeak.action.buildin.graph;

import edu.uci.ics.jung.graph.Graph;
import org.lightjason.agentspeak.language.ITerm;

import java.util.List;


/**
 * removes any vertices from a single graph instance.
 * The action removes from the first graph argument all vertices,
 * the action never fails
 *
 * @code graph/removevertexmultiple( Graph, Vertex1, Vertex2 ); @endcode
 */
public final class CRemoveVertexMultiple extends IApplyMultiple
{

    @Override
    protected final int windowsize()
    {
        return 1;
    }

    @Override
    protected final void apply( final boolean p_parallel, final Graph<Object, Object> p_graph, final List<ITerm> p_window, final List<ITerm> p_return )
    {
        p_graph.removeVertex( p_window.get( 0 ).raw() );
    }

}
