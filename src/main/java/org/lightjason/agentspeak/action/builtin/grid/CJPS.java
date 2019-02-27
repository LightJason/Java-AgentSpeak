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

package org.lightjason.agentspeak.action.builtin.grid;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import com.codepoetics.protonpack.functions.TriFunction;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class CJPS extends IBaseAction
{
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CRemove.class, "grid" );

    private static final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> ISOCCUPIED = ( g, p ) -> Objects.nonNull( g.getQuick( (int) p.getQuick( 0 ), (int) p.getQuick( 1 ) ) );
    private static final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> ISNOTINSIDE = ( g, p ) -> p.getQuick( 0 ) < 0 || p.getQuick( 1 ) < 0 || p.getQuick( 0 ) >= g.rows() || p.getQuick( 1 ) >= g.columns();
    private static final TriFunction<ObjectMatrix2D, DoubleMatrix1D, List<DoubleMatrix1D>, Boolean> ISNOTNEIGhBOUR = ( g, p, l ) -> ISNOTINSIDE.apply( g, p ) || l.contains( p );



    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        // https://github.com/SteveRabin/JPSPlusWithGoalBounding/tree/master/JPSPlusGoalBounding
        // https://github.com/kevinsheehan/jps/blob/master/src/org/ksdev/jps/JPS.java
        // https://github.com/unrealgamer/Java-JPS/blob/master/JavaJPS/src/com/unreal/jps/Program.java
        // https://harablog.wordpress.com/2011/09/07/jump-point-search/

        return Stream.of();
    }


    private static void route( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_start, @Nonnull final DoubleMatrix1D p_end )
    {
        final TreeSet<CJumpPoint> l_open = Stream.of( new CJumpPoint( p_start ) ).collect( Collectors.toCollection( TreeSet::new ) );
        final List<DoubleMatrix1D> l_closed = new ArrayList<>()
        final List<DoubleMatrix1D> l_path = new ArrayList<>();

        while (!l_open.isEmpty())
        {
            final CJumpPoint l_current = l_open.pollFirst();

            // final position is reached
            if ( l_current.coordinate().equals( p_end ) )
            {
                l_path.add( p_end );
                final CJumpPoint l_parent =
            }
        }

    }



    /**
     * jump-point with a static class
     * "static" means that the class can exists without the CJPSPlus object
     * "final" no inheritance can be create
     */
    private static final class CJumpPoint implements Comparable<CJumpPoint>
    {
        /**
         * for avoid zero-jump-points we create exactly one
         */
        public static final CJumpPoint ZERO = new CJumpPoint();

        /**
         * jump-point g-score value
         */
        private double m_gscore;

        /**
         * jump-point h-score value
         */
        private double m_hscore;

        /**
         * parent node of current JumpPoint
         */
        private CJumpPoint m_parent;
        /**
         * position
         */
        private final DoubleMatrix1D m_coordinate;

        /**
         * ctor
         */
        private CJumpPoint()
        {
            this( new DenseDoubleMatrix1D( 0 ), null );
        }

        /**
         * ctor
         *
         * @param p_coordinate postion value
         */
        CJumpPoint( final DoubleMatrix1D p_coordinate )
        {
            this( p_coordinate, null )
        }

        /**
         * ctor
         *
         * @param p_coordinate postion value
         * @param p_parent parent of the current jump point
         */
        CJumpPoint( final DoubleMatrix1D p_coordinate, final CJumpPoint p_parent )
        {
            m_coordinate = p_coordinate;
            m_parent = p_parent;
        }

        @Override
        public int compareTo( final CJumpPoint p_jumppoint )
        {
            return this.fscore() > p_jumppoint.fscore() ?  1 : -1;
        }

        /**
         * getter for coordinate
         *
         * @return coordinate
         */
        final DoubleMatrix1D coordinate()
        {
            return m_coordinate;
        }

        /**
         * getter for g-score
         *
         * @return g-score
         */
        final double gscore()
        {
            return m_gscore;
        }

        /**
         * getter for f_score
         *
         * @return f-score
         */
        final double fscore()
        {
            return m_hscore + m_gscore;
        }

        /**
         * getter for parent
         *
         * @return parent
         */
        final CJumpPoint parent()
        {
            return m_parent;
        }

        /**
         * setter for g_score
         *
         * @return CJumpPoint
         */
        final CJumpPoint gscore( final double p_gscore )
        {
            m_gscore = p_gscore;
            return this;
        }

        /**
         * setter for h_score
         *
         * @return CJumpPoint
         */
        final CJumpPoint hscore( final double p_hscore )
        {
            m_hscore = p_hscore;
            return this;
        }

    }
}
