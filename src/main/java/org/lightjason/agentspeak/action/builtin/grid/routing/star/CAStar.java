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

package org.lightjason.agentspeak.action.builtin.grid.routing.star;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.collections.buffer.PriorityBuffer;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.builtin.grid.routing.CNode;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDirection;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDistance;
import org.lightjason.agentspeak.action.builtin.grid.routing.IBaseRouting;
import org.lightjason.agentspeak.action.builtin.grid.routing.INode;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * a-star algorithm
 *
 * @see https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public final class CAStar extends IBaseRouting
{

    protected final PriorityBuffer m_heap = new PriorityBuffer();

    // https://github.com/jonasnick/A-star/blob/master/astar/AStar.java
    // https://github.com/pshafer/dstar
    // https://github.com/shu8i/AStar_DStarLite/blob/master/src/cs440/assignment1/control/AStar.java
    // https://github.com/shu8i/AStar_DStarLite/blob/master/src/cs440/assignment1/control/AdaptiveAStar.java



    public CAStar( @Nonnull final EDistance p_distance )
    {
        super( p_distance );
    }

    public CAStar( @Nonnull final EDistance p_distance, @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
    {
        super( p_distance, p_walkable );
    }

    @Override
    public Stream<DoubleMatrix1D> apply( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_start, @Nonnull final DoubleMatrix1D p_end )
    {
        // distance to start + estimate to end
        final Map<INode, Double> l_fscore = new ConcurrentHashMap<>();
        // distance to start (parent's g-score + distance from parent)
        final Map<INode, Double> l_gscore = new ConcurrentHashMap<>();
        // heuristic distance between nodes
        final Map<INode, Double> l_hscore = new ConcurrentHashMap<>();

        // closed list
        final Set<INode> l_closedlist = Collections.synchronizedSet( new HashSet<>() );
        // we want the nodes with the lowest projected f value to be checked first
        final Queue<INode> l_openlist = new PriorityBlockingQueue<>(
            (int) p_grid.size() / 4,
            Comparator.comparingDouble( i -> l_fscore.getOrDefault( i, 0d ) )
        );

        l_openlist.add( new CNode( p_start ) );
        while ( !l_openlist.isEmpty() )
        {
            final INode l_current = l_openlist.remove();
            if ( l_current.position().equals( p_end ) )
                return constructpath( l_current );

            l_closedlist.add( l_current );
            this.neighbour( p_grid, l_current ).forEach( i -> this.score( p_grid, l_current, i, l_openlist, l_gscore, l_hscore, l_fscore, 10 ) );
        }


        return Stream.of();
    }

    /**
     * calculates the score and updates the openlist
     *
     * @param p_grid grid
     * @param p_current current node
     * @param p_other neighbour nodes
     * @param p_openlist openlist
     * @param p_gscore g-score map
     * @param p_hscore h-score map
     * @param p_fscore f-score map
     * @param p_weight weight
     */
    private void score( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final INode p_current, @Nonnull final INode p_other,
                        @Nonnull final Queue<INode> p_openlist, @Nonnull final Map<INode, Double> p_gscore, @Nonnull final Map<INode, Double> p_hscore,
                        @Nonnull final Map<INode, Double> p_fscore, final @Nonnull Number p_weight )
    {
        final double l_gscore = p_gscore.getOrDefault( p_current, 0D ) + m_distance.apply( p_current.position(), p_other.position() ).doubleValue();

        if ( p_openlist.contains( p_other ) && l_gscore >= p_gscore.getOrDefault( p_other, 0D ) )
            return;

        final double l_hscore = p_weight.doubleValue() + m_distance.heuristic( p_current.position(), p_other.position() ).doubleValue();

        p_gscore.put( p_other, l_gscore );
        p_hscore.put( p_other, l_hscore );
        p_fscore.put( p_other, l_gscore + l_hscore );

        p_other.accept( p_current );
        p_openlist.add( p_other );
    }

    /**
     * neighbour calculation
     *
     * @param p_grid grid
     * @param p_current current node
     * @return neighbour stream
     */
    private Stream<INode> neighbour( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final INode p_current )
    {
        return Stream.of(
            walkable( p_grid, p_current.position(), EDirection.NORTH ),
            walkable( p_grid, p_current.position(), EDirection.EAST ),
            walkable( p_grid, p_current.position(), EDirection.SOUTH ),
            walkable( p_grid, p_current.position(), EDirection.WEST )
        ).filter( Pair::getKey ).map( Pair::getValue ).map( CNode::new );
    }


}
