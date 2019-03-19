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

package org.lightjason.agentspeak.action.grid.routing;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Stream;


/**
 * jps routing algorithm
 *
 * @see https://en.wikipedia.org/wiki/Jump_point_search
 */
public final class CJPSRouting extends IBaseRouting
{
    /**
     * default approximation weight
     */
    private static final double APROXIMATIONWEIGHT = 1;
    /**
     * approximation weight
     */
    private final Number m_weight;

    /**
     * ctor
     */
    public CJPSRouting()
    {
        this( EDistance.MANHATTAN, ESearchDirection.NEVER, APROXIMATIONWEIGHT );
    }

    /**
     * ctor
     *
     * @param p_distance distance
     */
    public CJPSRouting( @Nonnull final IDistance p_distance )
    {
        this( p_distance, ESearchDirection.NEVER, APROXIMATIONWEIGHT );
    }

    /**
     * ctor
     *
     * @param p_distance distance
     * @param p_searchdirection search direction
     */
    public CJPSRouting( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection )
    {
        this( p_distance, p_searchdirection, APROXIMATIONWEIGHT );
    }

    /**
     * ctor
     *
     * @param p_distance distance
     * @param p_searchdirection search direction
     * @param p_weight approximation weight
     */
    public CJPSRouting( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection, @Nonnull final Number p_weight )
    {
        super( p_distance, p_searchdirection );
        m_weight = p_weight;

        if ( p_distance == EDistance.MANHATTAN && p_searchdirection != ESearchDirection.NEVER )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "manhattendigitalmovement" ) );
    }

    @Override
    public Stream<DoubleMatrix1D> apply( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_start, @Nonnull final DoubleMatrix1D p_end )
    {
        // distance to start + estimate to end
        final Map<INode, Double> l_fscore = new ConcurrentHashMap<>();
        // distance to start (parent's g-score + distance from parent)
        final Map<INode, Double> l_gscore = new ConcurrentHashMap<>();

        // closed list
        final Set<INode> l_closedlist = Collections.synchronizedSet( new HashSet<>() );
        // we want the nodes with the lowest projected f value to be checked first
        final Queue<INode> l_openlist = new PriorityBlockingQueue<>(
            (int) p_grid.size() / 4,
            new CScoreComparator( l_fscore )
        );

        l_openlist.add( CNode.of( p_start ) );
        final INode l_end = CNode.of( p_end );

        while ( !l_openlist.isEmpty() )
        {
            final INode l_current = l_openlist.remove();
            if ( l_current.equals( l_end ) )
                return constructpath( l_current );

            l_closedlist.add( l_current );
            /*
            this.neighbour( p_grid, l_current.position() )
                .parallel()
                .map( CNode::of )
                .forEach( i -> this.score( p_grid, l_current, i, l_end, l_openlist, l_closedlist, l_gscore, l_fscore ) );
            */

            reorganizequeue( l_openlist );
        }


        return Stream.of();
    }
}
