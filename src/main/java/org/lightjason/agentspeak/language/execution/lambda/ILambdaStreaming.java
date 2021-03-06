/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.execution.lambda;

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * streaming interface to define stream structure
 *
 * @tparam T any input type
 */
public interface ILambdaStreaming<T> extends Serializable, Function<T, Stream<?>>
{
    /**
     * empty streaming
     */
    ILambdaStreaming<Object> EMPTY = new ILambdaStreaming<>()
    {
        @Nonnull
        @Override
        public Stream<Class<?>> assignable()
        {
            return Stream.empty();
        }

        @Override
        public Stream<?> apply( final Object p_value )
        {
            return Stream.empty();
        }

        @Override
        public int hashCode()
        {
            return this.assignable().mapToInt( Object::hashCode ).reduce( 0, ( i, j ) -> i ^ j );
        }

        @Override
        public boolean equals( final Object p_object )
        {
            return p_object instanceof ILambdaStreaming<?> && p_object.hashCode() == this.hashCode();
        }
    };

    /**
     * returns the class which matches
     *
     * @return class
     */
    @NonNull
    Stream<Class<?>> assignable();

}
