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
        this( p_position, null );
    }
    /**
     * ctor
     *
     * @param p_parent parent node
     */
    public CJumpPoint( @NonNull final DoubleMatrix1D p_position, @Nullable final INode p_parent )
    {
        m_parent = p_parent;
        m_position = p_position;
    }

    @NonNull
    @Override
    public DoubleMatrix1D position()
    {
        return m_position;
    }

    @Nullable
    @Override
    public INode parent()
    {
        return m_parent;
    }

}
