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
import java.util.List;


/**
 * returns of any edge the vertices from a single graph instance.
 * The actions returns for any edges the connected vertices
 * of a single graph instance,
 * the action never fails
 *
 * {@code [V1|V2|V3|V4] = graph/endpointmultiple( Graph, Edge1, Edge2 );}
 */
public final class CEndPointMultiple extends IApplyMultiple
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 8037926558399331691L;

    @Override
    protected final int windowsize()
    {
        return 1;
    }

    @Override
    protected final void apply( final boolean p_parallel, @Nonnull final Graph<Object, Object> p_graph,
                                @Nonnull final List<ITerm> p_window, @Nonnull final List<ITerm> p_return )
    {
        p_graph.getEndpoints( p_window.get( 0 ).raw() )
               .stream()
               .map( CRawTerm::from )
               .forEach( p_return::add );
    }

}
