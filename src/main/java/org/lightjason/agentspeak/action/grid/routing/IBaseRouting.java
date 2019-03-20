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
import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * base routing structure
 */
public abstract class IBaseRouting implements IRouting, Serializable
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -7599848829150929706L;
    /**
     * distance
     */
    protected final IDistance m_distance;
    /**
     * search direction
     */
    private final ISearchDirection m_searchdirection;
    /**
     * walkable function
     */
    private final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> m_walkable;

    /**
     * ctor
     * @param p_distance distance
     * @param p_searchdirection search direction
     */
    protected IBaseRouting( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection )
    {
        this( p_distance, p_searchdirection, ( g, p ) -> Objects.isNull( g.getQuick( (int) p.getQuick( 0 ), (int) p.getQuick( 1 ) ) ) );
    }

    /**
     * ctor
     *  @param p_distance distance
     * @param p_searchdirection search direction
     * @param p_walkable walkable function
     */
    protected IBaseRouting( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection,
                            @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable
    )
    {
        m_distance = p_distance;
        m_walkable = p_walkable;
        m_searchdirection = p_searchdirection;
    }

    /**
     * returns a stream of neighbour positions
     *
     * @param p_grid grid
     * @param p_current current position
     * @return position stream
     */
    protected final Stream<DoubleMatrix1D> neighbour( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current )
    {
        return m_searchdirection.apply( p_grid, p_current, m_walkable );
    }

    /**
     * builds the path recursive on the node structure
     *
     * @param p_end final node (target position)
     * @return position stream
     */
    protected static Stream<DoubleMatrix1D> constructpath( @Nonnull final INode p_end )
    {
        return Stream.concat(
            Objects.isNull( p_end.get() )
            ? Stream.of()
            : constructpath( p_end.get() ),
            Stream.of( p_end.position() )
        );
    }

    /**
     * reorganize a priority queue
     *
     * @param p_queue queue
     */
    protected static void reorganizequeue( @Nonnull final Queue<INode> p_queue )
    {
        final List<INode> l_nodes = p_queue.parallelStream().collect( Collectors.toList() );
        p_queue.clear();
        p_queue.addAll( l_nodes );
    }


    /**
     * comparator for score
     */
    protected static final class CScoreComparator implements Serializable, Comparator<INode>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -7298299975848106509L;
        /**
         * score map
         */
        private final Map<INode, Double> m_score;

        /**
         * ctor
         *
         * @param p_score score map
         */
        CScoreComparator( @Nonnull final Map<INode, Double> p_score )
        {
            m_score = p_score;
        }

        @Override
        public int compare( @Nonnull final INode p_value1, @Nonnull final INode p_value2 )
        {
            return m_score.getOrDefault( p_value1, 0D ).doubleValue() < m_score.getOrDefault( p_value2, 0D ).doubleValue()
                   ? -1
                   : 1;
        }
    }
}
