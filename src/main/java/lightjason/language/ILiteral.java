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


import lightjason.common.CPath;

import java.util.stream.Stream;


/**
 * literal interface
 *
 * @note closed world assumption, no negation marker needed
 */
public interface ILiteral extends ITerm, Comparable<ILiteral>
{

    /**
     * clones the literal (shallow-copy)
     *
     * @param p_prefix add a path to the functor
     * @return new instance of the literal
     */
    ILiteral shallowclone( final CPath p_prefix );

    /**
     * clones the literal (shallow-copy)
     *
     * @return new instance of the literal
     */
    ILiteral shallowclone();

    /**
     * clones the literal (shallow-copy)
     * without full-qualified path, only
     * suffix is used
     *
     * @return new instance of the literal
     */
    ILiteral shallowcloneSuffixOnly();

    /**
     * returns a stream over value items
     *
     * @param p_path optional filtering value names
     * (filtering values within values)
     * @return stream
     */
    Stream<ITerm> values( final CPath... p_path );

    /**
     * returns a stream over the ordered values
     * in sequential ordering
     */
    Stream<ITerm> orderedvalues( final CPath... p_path );

    /**
     * returns a stream over annotation items
     *
     * @param p_path optional filtering annotation names (filtering all
     * annotations on the same level not within)
     * @return stream
     */
    Stream<ILiteral> annotations( final CPath... p_path );

    /**
     * returns the hash of the annotations
     *
     * @return hash
     */
    int annotationhash();

    /**
     * returns the hash of the value
     *
     * @return hash
     */
    int valuehash();

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
