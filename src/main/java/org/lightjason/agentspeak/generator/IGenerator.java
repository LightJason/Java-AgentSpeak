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

package org.lightjason.agentspeak.generator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;


/**
 * interface of generators
 */
public interface IGenerator<T>
{

    /**
     * generates a single object
     *
     * @param p_data any object data
     * @return object
     *
     * @note can return null which will be ignored than on multiple generation
     */
    @Nonnull
    T generatesingle( @Nullable final Object... p_data );

    /**
     * generates a stream of objects
     *
     * @param p_number number of objects within the stream
     * @param p_data any object data
     * @return object stream
     */
    @Nonnull
    Stream<T> generatemultiple( final int p_number, @Nullable final Object... p_data );

}
