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

package org.lightjason.agentspeak.action.builtin.math.blas.vector;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import org.lightjason.agentspeak.language.execution.action.lambda.ILambdaStreaming;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * streaming a vector
 */
public final class CLambdaStreaming implements ILambdaStreaming<DoubleMatrix1D>
{
    @Override
    public final boolean instaceof( @Nonnull final Object p_object )
    {
        return p_object instanceof DoubleMatrix1D;
    }

    @Override
    public final Stream<?> apply( final DoubleMatrix1D p_vector )
    {
        return Arrays.stream( p_vector.toArray() ).boxed();
    }
}
