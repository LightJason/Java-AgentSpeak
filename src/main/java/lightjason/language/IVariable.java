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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.language;

/**
 * variable defintion
 *
 * @param T data type
 */
public interface IVariable<T> extends ITerm
{

    /**
     * sets the value
     *
     * @param p_value
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
     * returns allocated state
     *
     * @return boolean flag
     */
    boolean isAllocated();

    /**
     * checkes assinable of the value
     *
     * @param p_class class
     * @return assignable (on null always true)
     */
    boolean isValueAssignableTo( final Class<?> p_class );

    /**
     * override clonable interface definition
     *
     * @return clones variables
     */
    IVariable<T> clone();

}
