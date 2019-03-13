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

package org.lightjason.agentspeak.action.builtin.grid.routing.algorithm;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.builtin.grid.routing.CNode;
import org.lightjason.agentspeak.action.builtin.grid.routing.EDistance;
import org.lightjason.agentspeak.action.builtin.grid.routing.ESearchDirection;
import org.lightjason.agentspeak.action.builtin.grid.routing.IBaseRouting;
import org.lightjason.agentspeak.action.builtin.grid.routing.IDistance;
import org.lightjason.agentspeak.action.builtin.grid.routing.INode;
import org.lightjason.agentspeak.action.builtin.grid.routing.ISearchDirection;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * a-star algorithm
 *
 * @see https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public final class CAStar extends IBaseRouting
{
    /**
     * default approximation weight
     */
    private static final double APROXIMATIONWEIGHT = 1;
    /**
     * approximation weight
     */
    private final Number m_weight;

    // https://github.com/jonasnick/A-star/blob/master/astar/AStar.java
    // https://github.com/pshafer/dstar
    // https://github.com/shu8i/AStar_DStarLite/blob/master/src/cs440/assignment1/control/AStar.java
    // https://github.com/shu8i/AStar_DStarLite/blob/master/src/cs440/assignment1/control/AdaptiveAStar.java

    /**
     * ctor
     */
    public CAStar()
    {
        this( EDistance.MANHATTAN, ESearchDirection.NEVER, APROXIMATIONWEIGHT );
    }

    /**
     * ctor
     *
     * @param p_distance distance
     */
    public CAStar( @Nonnull final IDistance p_distance )
    {
        this( p_distance, ESearchDirection.NEVER, APROXIMATIONWEIGHT );
    }

    /**
     * ctor
     *
     * @param p_distance distance
     * @param p_searchdirection search direction
     */
    public CAStar( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection )
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
    public CAStar( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection, @Nonnull final Number p_weight )
    {
        super( p_distance, p_searchdirection );
        m_weight = p_weight;
    }

    /**
     * ctor
     *
     * @param p_distance distance
     * @param p_searchdirection search direction
     * @param p_walkable walkable check
     * @param p_weight approximation weight
     */
    public CAStar( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection,
                   @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable,
                   @Nonnull final Number p_weight )
    {
        super( p_distance, p_searchdirection, p_walkable );
        m_weight = p_weight;
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
            new CComparator( l_fscore )
        );

        l_openlist.add( CNode.of( p_start ) );
        final INode l_end = CNode.of( p_end );

        while ( !l_openlist.isEmpty() )
        {
            final INode l_current = l_openlist.remove();
            if ( l_current.equals( l_end ) )
                return constructpath( l_current );

            l_closedlist.add( l_current );
            this.neighbour( p_grid, l_current.position() )
                .map( CNode::of )
                .forEach( i -> this.score( p_grid, l_current, i, l_end, l_openlist, l_closedlist, l_gscore, l_fscore ) );

            // reorganize queue
            final List<INode> l_nodes = l_openlist.parallelStream().collect( Collectors.toList() );
            l_openlist.clear();
            l_openlist.addAll( l_nodes );
        }


        return Stream.of();
    }

    /**
     * calculates the score and updates the openlist
     *
     * @param p_grid grid
     * @param p_current current node
     * @param p_end end node
     * @param p_neigbour neighbour nodes
     * @param p_openlist openlist
     * @param p_closedlist closed list
     * @param p_gscore g-score map
     * @param p_fscore f-score map
     */
    private void score( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final INode p_current, @Nonnull final INode p_neigbour, final INode p_end,
                        @Nonnull final Queue<INode> p_openlist, @Nonnull final Set<INode> p_closedlist,
                        @Nonnull final Map<INode, Double> p_gscore, @Nonnull final Map<INode, Double> p_fscore )
    {
        if ( p_closedlist.contains( p_neigbour ) )
            return;

        final double l_gscore = p_gscore.getOrDefault( p_current, 0D ) + m_distance.apply( p_current.position(), p_neigbour.position() ).doubleValue();
        if ( p_openlist.contains( p_neigbour ) && l_gscore >= p_gscore.getOrDefault( p_neigbour, 0D ) )
            return;

        p_neigbour.accept( p_current );
        p_gscore.put( p_neigbour, l_gscore );
        p_fscore.put( p_neigbour, l_gscore + m_weight.doubleValue() * m_distance.heuristic( p_neigbour.position(), p_end.position() ).doubleValue() );

        p_openlist.add( p_neigbour );
    }

    /**
     * comparator for f-score
     */
    private static final class CComparator implements Comparator<INode>
    {
        /**
         * f-score map
         */
        private final Map<INode, Double> m_fscore;

        /**
         * ctor
         *
         * @param p_fscore f-score map
         */
        CComparator( @Nonnull final Map<INode, Double> p_fscore )
        {
            m_fscore = p_fscore;
        }

        @Override
        public int compare( @Nonnull final INode p_value1, @Nonnull final INode p_value2 )
        {
            return m_fscore.getOrDefault( p_value1, 0D ).doubleValue() < m_fscore.getOrDefault( p_value2, 0D ).doubleValue()
                   ? -1
                   : 1;
        }
    }

}
