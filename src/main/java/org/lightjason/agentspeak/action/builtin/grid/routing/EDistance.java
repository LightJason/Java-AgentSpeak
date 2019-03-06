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

package org.lightjason.agentspeak.action.builtin.grid.routing;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.jet.math.tdouble.DoubleFunctions;
import edu.umd.cs.findbugs.annotations.NonNull;


/**
 * distance algorithms
 */
public enum EDistance implements IDistance
{
    MANHATTAN
    {
        @Override
        public Number apply( @NonNull final DoubleMatrix1D p_value1, @NonNull final DoubleMatrix1D p_value2 )
        {
            return p_value1.copy().assign( p_value2, DoubleFunctions.minus ).assign( DoubleFunctions.abs ).zSum();
        }
    },

    EUCLIDEAN
    {
        @Override
        public Number apply( @NonNull final DoubleMatrix1D p_value1, @NonNull final DoubleMatrix1D p_value2 )
        {
            return Math.sqrt( p_value1.copy().assign( p_value2, DoubleFunctions.minus ).assign( DoubleFunctions.pow( 2 ) ).zSum() );
        }
    },

    CHEBYSHEV
    {
        @Override
        public Number apply( @NonNull final DoubleMatrix1D p_value1, @NonNull final DoubleMatrix1D p_value2 )
        {
            return p_value1.copy().assign( p_value2, DoubleFunctions.minus ).getMaxLocation()[0];
        }
    }

}
