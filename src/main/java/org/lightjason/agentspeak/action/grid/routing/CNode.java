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
import cern.colt.matrix.tdouble.algo.DoubleFormatter;
import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


/**
 * route node
 */
public final class CNode implements INode, Serializable
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 8929701508299987546L;
    /**
     * formatter definition
     */
    private static final DoubleFormatter FORMATTER;

    static
    {
        FORMATTER = new DoubleFormatter();
        FORMATTER.setRowSeparator( "; " );
        FORMATTER.setColumnSeparator( "," );
        FORMATTER.setPrintShape( false );
    }

    /**
     * position
     */
    private final DoubleMatrix1D m_position;
    /**
     * parent node
     */
    private final AtomicReference<INode> m_parent = new AtomicReference<>();


    /**
     * ctor
     * @param p_position node position
     */
    public CNode( @Nonnull final DoubleMatrix1D p_position )
    {
        m_position = p_position;
    }


    @Override
    public DoubleMatrix1D position()
    {
        return m_position;
    }

    @Override
    public INode get()
    {
        return m_parent.get();
    }

    @Override
    public void accept( final INode p_parent )
    {
        m_parent.set( p_parent );
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode( m_position.toArray() );
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return p_object instanceof INode && p_object.hashCode() == this.hashCode();
    }

    @Override
    public String toString()
    {
        final INode l_parent = m_parent.get();
        return MessageFormat.format( "({0})[{1}]", FORMATTER.toString( m_position ), Objects.nonNull( l_parent ) ? l_parent : "" );
    }

    /**
     * factory
     *
     * @param p_position position
     * @return node
     */
    public static INode of( @NonNull final DoubleMatrix1D p_position )
    {
        return new CNode( p_position );
    }
}
