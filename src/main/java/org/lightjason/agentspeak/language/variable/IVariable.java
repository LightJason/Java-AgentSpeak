/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language.variable;

import org.lightjason.agentspeak.language.IShallowCopy;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;


/**
 * variable defintion
 *
 * @tparam T data type
 */
public interface IVariable<T> extends ITerm, IShallowCopy<IVariable<T>>
{

    /**
     * sets the value
     *
     * @param p_value value
     * @return the object itself
     */
    @Nonnull
    IVariable<T> set( final T p_value );

    /**
     * returns allocated state
     *
     * @return boolean flag
     */
    boolean allocated();

    /**
     * flag to define a "any variable"
     *
     * @return flag for any variable
     */
    boolean any();

    /**
     * flag to check if variable has is
     * concurrency- / thread-safe
     */
    boolean mutex();

    /**
     * throws an illegal state exception
     * iif the variable is not allocated
     *
     * @return object itself
     *
     * @throws IllegalStateException on non-allocated
     */
    @Nonnull
    IVariable<T> throwNotAllocated() throws IllegalStateException;

    /**
     * checkes assignable of the value
     *
     * @param p_class class
     * @return assignable (on null always true)
     */
    boolean valueAssignableTo( final Class<?>... p_class );

    /**
     * throws an illegal argument exception
     * iif the value is not assignable to the
     * class
     *
     * @param p_class assignable class
     * @return object itself
     *
     * @throws IllegalArgumentException on assignable error
     */
    @Nonnull
    IVariable<T> throwValueNotAssignableTo( final Class<?>... p_class ) throws IllegalArgumentException;

}
