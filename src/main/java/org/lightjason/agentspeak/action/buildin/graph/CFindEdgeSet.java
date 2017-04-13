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
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * returns all edges of two vertices.
 * The action returns a list of edges
 * between two vertices, the first two
 * arguments are vertices, all other arguments
 * are graphs and return values are lists,
 * the action never fails
 *
 * @code [L1|L2] = graph/findedgeset( Vertex1, Vertex2, Graph1, Graph2 ); @endcode
 */
public final class CFindEdgeSet extends IBuildinAction
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        if ( l_arguments.size() < 3 )
            return CFuzzyValue.from( false );

        l_arguments.stream()
                   .skip( 2 )
                   .map( ITerm::<Graph<Object, Object>>raw )
                   .map( i -> i.findEdgeSet( l_arguments.get( 0 ).raw(), l_arguments.get( 1 ).raw() ) )
                   .map( ArrayList::new )
                   .map( CRawTerm::from )
                   .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
