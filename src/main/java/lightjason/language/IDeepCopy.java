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

package lightjason.language;

import lightjason.common.IPath;


/**
 * interface to create deep-copy of objects
 *
 * @note methods must be thread-safe if necessary
 */
public interface IDeepCopy<T> extends ICopy
{
    /**
     * clones the object (shallow-copy)
     *
     * @param p_prefix add a path (only one path element is supported)
     * to the functor or returns a shallow-copy
     * @return new instance of the object
     */
    T deepcopy( final IPath... p_prefix );

    /**
     * clones the object (shallow-copy)
     * without full-qualified path, only
     * suffix is used
     *
     * @return new instance of the object
     */
    T deepcopySuffix();

}
