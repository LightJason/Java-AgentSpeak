/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.common;

import java.util.stream.Stream;


/**
 * interface of a path
 */
public interface IPath extends Comparable<IPath>
{

    /**
     * appends a path at the current and returns a new object
     *
     * @param p_path path
     * @return new path
     */
    IPath append( final IPath p_path );

    /**
     * appends a string at the current path and returns the new object
     *
     * @param p_path string with path
     * @return new path
     */
    IPath append( final String p_path );

    /**
     * removes an element
     *
     * @param p_index index position
     * @return return the changed object
     */
    IPath remove( final int p_index );

    /**
     * removes all elements from start index until end index (exclusive)
     *
     * @param p_start start index
     * @param p_end end index (exclusive)
     * @return return the changed object
     */
    IPath remove( final int p_start, final int p_end );

    /**
     * check of a path ends with another path
     *
     * @param p_path path
     * @return boolean
     */
    boolean endsWith( final IPath p_path );

    /**
     * check if the path is empty
     *
     * @return empty flag
     */
    boolean isEmpty();

    /**
     * returns an part of the path
     *
     * @param p_index index position (negativ index is element from the end)
     * @return element
     */
    String get( final int p_index );

    /**
     * returns the full path as string with an individual separator
     *
     * @param p_separator separator
     * @return string path
     */
    String getPath( final String p_separator );

    /**
     * returns the full path as string
     *
     * @return string path
     */
    String getPath();

    /**
     * returns the separator
     *
     * @return separator
     */
    String getSeparator();

    /**
     * sets the separator
     *
     * @param p_separator separator
     * @return path object
     */
    IPath setSeparator( final String p_separator );

    /**
     * changes all elements to lower-case
     *
     * @return object
     */
    IPath toLower();

    /**
     * changes all elements to uppercase
     *
     * @return object
     */
    IPath toUpper();

    /**
     * creates a path of the start index until the end
     *
     * @param p_fromIndex start index
     * @return path
     */
    IPath getSubPath( final int p_fromIndex );

    /**
     * creates a path of the indices
     *
     * @param p_fromIndex start index
     * @param p_toIndex end index (exclusive) / negative values from the end
     * @return path
     */
    IPath getSubPath( final int p_fromIndex, final int p_toIndex );

    /**
     * returns the last part of the path
     *
     * @return string
     */
    String getSuffix();

    /**
     * adds a path at the end
     *
     * @param p_path path
     * @return return the changed object
     */
    IPath pushback( final IPath p_path );

    /**
     * adds a path at the end
     *
     * @param p_path string path
     * @return return the changed object
     */
    IPath pushback( final String p_path );

    /**
     * adds a path at the front
     *
     * @param p_path string path
     * @return return the changed object
     */
    IPath pushfront( final String p_path );

    /**
     * adds a path to the front of the path
     *
     * @param p_path path
     * @return return the changed object
     */
    IPath pushfront( final IPath p_path );

    /**
     * remove the suffix from the path
     *
     * @return last item of the path
     */
    String removeSuffix();

    /**
     * reverse path
     *
     * @return return the changed object
     */
    IPath reverse();

    /**
     * returns the number of path elements
     *
     * @return size
     */
    int size();

    /**
     * check of a path starts with another path
     *
     * @param p_path path
     * @return boolean
     */
    boolean startsWith( final IPath p_path );

    /**
     * check of a path starts with another path
     *
     * @param p_path path
     * @return boolean
     */
    boolean startsWith( final String p_path );

    /**
     * stream over elements
     *
     * @return sequential stream
     */
    Stream<String> stream();

    /**
     * parallel stream over elements
     *
     * @return parallel stream
     */
    Stream<String> parallelStream();

    /**
     * normalizes the path (remove dot and dot-dot elements)
     *
     * @return self reference
     */
    IPath normalize();

}
