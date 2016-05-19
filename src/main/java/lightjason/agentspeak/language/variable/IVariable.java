/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agentspeak.language.variable;

import lightjason.agentspeak.language.IShallowCopy;
import lightjason.agentspeak.language.ITerm;


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
    IVariable<T> set( final T p_value );

    /**
     * gets the value
     *
     * @return value
     */
    T get();

    /**
     * gets the value with cast
     *
     * @return casted value
     *
     * @tparam N casted type
     */
    <N> N getTyped();

    /**
     * returns allocated state
     *
     * @return boolean flag
     */
    boolean isAllocated();

    /**
     * flag to define a "any variable"
     *
     * @return flag for any variable
     */
    boolean isAny();

    /**
     * flag to check if variable has is
     * concurrency- / thread-safe
     */
    boolean hasMutex();

    /**
     * throws an illegal state exception
     * iif the variable is not allocated
     *
     * @return object itself
     *
     * @throws IllegalStateException on non-allocated
     */
    IVariable<T> throwNotAllocated() throws IllegalStateException;

    /**
     * checkes assignable of the value
     *
     * @param p_class class
     * @return assignable (on null always true)
     */
    boolean isValueAssignableTo( final Class<?>... p_class );

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
    IVariable<T> throwValueNotAssignableTo( final Class<?>... p_class ) throws IllegalArgumentException;

}
