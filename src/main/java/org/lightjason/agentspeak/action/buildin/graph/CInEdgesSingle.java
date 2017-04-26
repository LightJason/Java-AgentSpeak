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

import edu.uci.ics.jung.graph.AbstractGraph;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.List;


/**
 * returns incomming edges of a vertex of each graph instance.
 * The actions returns a list incoming edges of a vertex for
 * each graph instance, the first argument is the vertex, all
 * other graphs, the action never fails
 *
 * @code [OE1|OE2] = graph/inedgessingle( Vertex, Graph1, Graph2 ); @endcode
 */
public final class CInEdgesSingle extends IApplySingle
{

    @Override
    protected final int skipsize()
    {
        return 1;
    }

    @Override
    protected final void apply( final AbstractGraph<Object, Object> p_graph, final List<ITerm> p_window, final List<ITerm> p_return )
    {
        p_return.add(
            CRawTerm.from(
                new ArrayList<>( p_graph.getInEdges( p_window.get( 0 ).raw() ) )
            )
        );
    }

}
