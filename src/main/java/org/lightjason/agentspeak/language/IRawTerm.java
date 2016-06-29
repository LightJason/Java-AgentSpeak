/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.language;


/**
 * interface of raw terms
 */
public interface IRawTerm<T> extends ITerm
{
    /**
     * returns the raw valuw
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
    <N> N typed();

    /**
     * returns allocated state
     *
     * @return boolean flag
     */
    boolean allocated();

    /**
     * throws an illegal state exception
     * iif the raw term is not allocated
     *
     * @param p_name optional name text for the error message
     * @return object itself
     *
     * @throws IllegalStateException on non-allocated
     */
    IRawTerm<T> throwNotAllocated( final String... p_name ) throws IllegalStateException;

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
    IRawTerm<T> throwValueNotAssignableTo( final Class<?>... p_class ) throws IllegalArgumentException;

}
