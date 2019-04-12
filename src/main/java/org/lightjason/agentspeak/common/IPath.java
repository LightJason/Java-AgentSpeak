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

package org.lightjason.agentspeak.common;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.stream.Stream;


/**
 * interface of a path
 *
 * @note implement equals with String object, so a path object can be checked to a String
 */
public interface IPath extends Serializable, Comparable<IPath>
{
    /**
     * default seperator
     */
    String DEFAULTSEPERATOR = "/";

    /**
     * empty path
     **/
    IPath EMPTY = new IPath()
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -8529008893337445887L;

        @Nonnull
        @Override
        public IPath append( @Nonnull final IPath p_path )
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath append( @Nonnull final String p_path )
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath remove( final int p_index )
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath remove( final int p_start, final int p_end )
        {
            return this;
        }

        @Override
        public boolean empty()
        {
            return true;
        }

        @Nonnull
        @Override
        public String get( final int p_index )
        {
            return "";
        }

        @Nonnull
        @Override
        public String path( final String p_separator )
        {
            return "";
        }

        @Nonnull
        @Override
        public String path()
        {
            return "";
        }

        @Nonnull
        @Override
        public String separator()
        {
            return "";
        }

        @Nonnull
        @Override
        public IPath separator( @Nonnull final String p_separator )
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath lower()
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath upper()
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath subpath( final int p_fromindex )
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath subpath( final int p_fromindex, final int p_toindex )
        {
            return this;
        }

        @Nonnull
        @Override
        public String suffix()
        {
            return "";
        }

        @Nonnull
        @Override
        public IPath pushback( @Nonnull final IPath p_path )
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath pushback( @Nonnull final String p_path )
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath pushfront( @Nonnull final String p_path )
        {
            return this;
        }

        @Nonnull
        @Override
        public IPath pushfront( @Nonnull final IPath p_path )
        {
            return this;
        }

        @Nonnull
        @Override
        public String removesuffix()
        {
            return "";
        }

        @Nonnull
        @Override
        public IPath reverse()
        {
            return this;
        }

        @Override
        public int size()
        {
            return 0;
        }

        @Override
        public boolean endswith( @Nonnull final IPath p_path )
        {
            return false;
        }

        @Override
        public boolean endswith( @Nonnull final String p_path )
        {
            return false;
        }

        @Override
        public boolean startswith( final IPath p_path )
        {
            return false;
        }

        @Override
        public boolean startswith( final String p_path )
        {
            return false;
        }

        @Nonnull
        @Override
        public Stream<String> stream()
        {
            return Stream.empty();
        }

        @Override
        public int compareTo( @Nonnull final IPath p_path )
        {
            return Integer.compare( p_path.hashCode(), this.hashCode() );
        }

        @Override
        public int hashCode()
        {
            return 0;
        }

        @Override
        @SuppressFBWarnings( "EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS" )
        public boolean equals( final Object p_object )
        {
            return p_object instanceof IPath && this.hashCode() == p_object.hashCode()
                   || p_object instanceof String && this.path().hashCode() == p_object.hashCode();
        }

        @Override
        public String toString()
        {
            return "";
        }
    };

    /**
     * appends a path at the current and returns a new object
     *
     * @param p_path path
     * @return new path
     */
    @Nonnull
    IPath append( @Nonnull final IPath p_path );

    /**
     * appends a string at the current path and returns the new object
     *
     * @param p_path string with path
     * @return new path
     */
    @Nonnull
    IPath append( @Nonnull final String p_path );

    /**
     * removes an element
     *
     * @param p_index index position
     * @return return the changed object
     */
    @Nonnull
    IPath remove( final int p_index );

    /**
     * removes all elements of start index until end index (exclusive)
     *
     * @param p_start start index
     * @param p_end end index (exclusive)
     * @return return the changed object
     */
    @Nonnull
    IPath remove( final int p_start, final int p_end );

    /**
     * check if the path is empty
     *
     * @return empty flag
     */
    boolean empty();

    /**
     * returns an part of the path
     *
     * @param p_index index position (negativ index is element of the end)
     * @return element
     */
    @Nonnull
    String get( final int p_index );

    /**
     * returns the full path as string with an individual separator
     *
     * @param p_separator separator
     * @return string path
     */
    @Nonnull
    String path( final String p_separator );

    /**
     * returns the full path as string
     *
     * @return string path
     */
    @Nonnull
    String path();

    /**
     * returns the separator
     *
     * @return separator
     */
    @Nonnull
    String separator();

    /**
     * sets the separator
     *
     * @param p_separator separator
     * @return path object
     */
    @Nonnull
    IPath separator( @Nonnull final String p_separator );

    /**
     * changes all elements to lower-case
     *
     * @return object
     */
    @Nonnull
    IPath lower();

    /**
     * changes all elements to uppercase
     *
     * @return object
     */
    @Nonnull
    IPath upper();

    /**
     * creates a path of the start index until the end
     *
     * @param p_fromindex start index
     * @return path
     */
    @Nonnull
    IPath subpath( final int p_fromindex );

    /**
     * creates a path of the indices
     *
     * @param p_fromindex start index
     * @param p_toindex end index (exclusive) / negative values of the end
     * @return path
     */
    @Nonnull
    IPath subpath( final int p_fromindex, final int p_toindex );

    /**
     * returns the last part of the path
     *
     * @return string
     */
    @Nonnull
    String suffix();

    /**
     * adds a path at the end
     *
     * @param p_path path
     * @return return the changed object
     */
    @Nonnull
    IPath pushback( @Nonnull final IPath p_path );

    /**
     * adds a path at the end
     *
     * @param p_path string path
     * @return return the changed object
     */
    @Nonnull
    IPath pushback( @Nonnull final String p_path );

    /**
     * adds a path at the front
     *
     * @param p_path string path
     * @return return the changed object
     */
    @Nonnull
    IPath pushfront( @Nonnull final String p_path );

    /**
     * adds a path to the front of the path
     *
     * @param p_path path
     * @return return the changed object
     */
    @Nonnull
    IPath pushfront( @Nonnull final IPath p_path );

    /**
     * remove the suffix of the path
     *
     * @return last item of the path
     */
    @Nonnull
    String removesuffix();

    /**
     * reverse path
     *
     * @return return the changed object
     */
    @Nonnull
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
    boolean startswith( final IPath p_path );

    /**
     * check of a path starts with another path
     *
     * @param p_path path
     * @return boolean
     */
    boolean startswith( final String p_path );

    /**
     * check of a path ends with another path
     *
     * @param p_path path
     * @return boolean
     */
    boolean endswith( @Nonnull final IPath p_path );

    /**
     * check of a path ends with another path
     *
     * @param p_path path
     * @return boolean
     */
    boolean endswith( @Nonnull final String p_path );

    /**
     * stream over elements
     *
     * @return sequential stream
     */
    @Nonnull
    Stream<String> stream();

}
