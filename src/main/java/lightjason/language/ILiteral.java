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


import com.google.common.collect.Multimap;
import lightjason.common.CPath;

import java.util.Collection;
import java.util.List;


/**
 * literal interface
 *
 * @note closed world assumption, no negation marker needed
 */
public interface ILiteral extends ITerm, Comparable<ILiteral>
{

    /**
     * clones the literal
     *
     * @param p_prefix add a path to the functor
     * @return new instance of the literal
     */
    ILiteral clone( final CPath p_prefix );

    /**
     * clones the literal
     *
     * @return new instance of the literal
     */
    ILiteral clone();

    /**
     * clones the literal without full-qualified
     * path, only suffix is used
     *
     * @return new instance of the literal
     */
    ILiteral cloneWithoutPath();

    /**
     * returns the optional annotations
     *
     * @return annotation terms
     */
    Multimap<CPath, ILiteral> getAnnotation();

    /**
     * returns the optional value terms
     *
     * @return value terms
     */
    Multimap<CPath, ITerm> getValues();

    Collection<ITerm> values( final CPath... p_path );

    /**
     * returns the values as ordered list
     *
     * @return value list
     */
    List<ITerm> getOrderedValues();

    /**
     * getter of the literal for the negation
     */
    boolean isNegated();

    /**
     * returns if the literal has an @ prefix
     *
     * @return prefix is set
     */
    boolean hasAt();
}
