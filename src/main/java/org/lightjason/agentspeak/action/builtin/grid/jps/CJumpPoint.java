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

package org.lightjason.agentspeak.action.builtin.grid.jps;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;


/**
 * jump-point
 */
public final class CJumpPoint implements INode
{
    /**
     * position
     */
    private final DoubleMatrix1D m_position;
    /**
     * g-score
     */
    private final Number m_gscore;
    /**
     * h-score
     */
    private final Number m_hscore;
    /**
     * parent node
     */
    private final INode m_parent;

    /**
     * ctor
     *
     * @param p_position position
     */
    public CJumpPoint( @NonNull final DoubleMatrix1D p_position )
    {
        this( p_position, 0, 0, null );
    }

    /**
     * ctor
     *
     * @param p_gscore g-score
     * @param p_hscore h-score
     */
    public CJumpPoint( @NonNull final DoubleMatrix1D p_position, final double p_gscore, final double p_hscore )
    {
        this( p_position, p_gscore, p_hscore, null );
    }

    /**
     * ctor
     *
     * @param p_gscore g-score
     * @param p_hscore h-score
     * @param p_parent parent node
     */
    public CJumpPoint( @NonNull final DoubleMatrix1D p_position, final double p_gscore, final double p_hscore, @Nullable final INode p_parent )
    {
        m_gscore = p_gscore;
        m_hscore = p_hscore;
        m_parent = p_parent;
        m_position = p_position;
    }

    @NonNull
    @Override
    public DoubleMatrix1D position()
    {
        return m_position;
    }

    @Override
    public Number gscore()
    {
        return m_gscore;
    }

    @Override
    public Number fscore()
    {
        return m_gscore.doubleValue() + m_hscore.doubleValue();
    }

    @Nullable
    @Override
    public INode parent()
    {
        return m_parent;
    }

    @Override
    public int compareTo( @NonNull final INode p_node )
    {
        return this.fscore().doubleValue() > p_node.fscore().doubleValue() ?  1 : -1;
    }
}
