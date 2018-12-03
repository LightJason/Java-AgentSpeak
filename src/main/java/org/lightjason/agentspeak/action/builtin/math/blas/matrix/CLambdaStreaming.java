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

package org.lightjason.agentspeak.action.builtin.math.blas.matrix;

import cern.colt.matrix.tdouble.DoubleMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IBaseLambdaStreaming;

import java.util.Arrays;
import java.util.stream.Stream;


/**
 * streaming a blas matrix
 */
public final class CLambdaStreaming extends IBaseLambdaStreaming<DoubleMatrix2D>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 4021047814924138636L;

    @Override
    public Stream<?> apply( final DoubleMatrix2D p_matrix )
    {
        return Arrays.stream( p_matrix.vectorize().toArray() ).boxed();
    }

    @NonNull
    @Override
    public Class<?> assignable()
    {
        return DoubleMatrix2D.class;
    }
}
