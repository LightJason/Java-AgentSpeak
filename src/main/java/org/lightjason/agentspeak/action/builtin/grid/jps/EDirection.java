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


/**
 * direction definition
 */
public enum EDirection implements IDirection
{
    NORTH
    {
        @Override
        public DoubleMatrix1D apply( final DoubleMatrix1D p_current )
        {
            return p_current.copy().assign( new double[]{p_current.get( 1 ) - 1, p_current.get( 0 )} );
        }
    },
    EAST
    {
        @Override
        public DoubleMatrix1D apply( final DoubleMatrix1D p_current )
        {
            return p_current.copy().assign( new double[]{p_current.get( 1 ), p_current.get( 0 ) + 1} );
        }
    },
    SOUTH
    {
        @Override
        public DoubleMatrix1D apply( final DoubleMatrix1D p_current )
        {
            return p_current.copy().assign( new double[]{p_current.get( 1 ) + 1, p_current.get( 0 )} );
        }
    },
    WEST
    {
        @Override
        public DoubleMatrix1D apply( final DoubleMatrix1D p_current )
        {
            return p_current.copy().assign( new double[]{p_current.get( 1 ), p_current.get( 0 ) - 1} );
        }
    },
    NORTHEAST
    {
        @Override
        public DoubleMatrix1D apply( final DoubleMatrix1D p_current )
        {
            return p_current.copy().assign( new double[]{p_current.get( 1 ) - 1, p_current.get( 0 ) + 1} );
        }
    },
    NORTHWEST
    {
        @Override
        public DoubleMatrix1D apply( final DoubleMatrix1D p_current )
        {
            return p_current.copy().assign( new double[]{p_current.get( 0 ) - 1, p_current.get( 1 ) - 1} );
        }
    },
    SOUTHEAST
    {
        @Override
        public DoubleMatrix1D apply( final DoubleMatrix1D p_current )
        {
            return p_current.copy().assign( new double[]{p_current.get( 1 ) + 1, p_current.get( 0 ) + 1} );
        }
    },
    SOUTHWEST
    {
        @Override
        public DoubleMatrix1D apply( final DoubleMatrix1D p_current )
        {
            return p_current.copy().assign( new double[]{p_current.get( 1 ) + 1, p_current.get( 0 ) - 1} );
        }
    }
}
