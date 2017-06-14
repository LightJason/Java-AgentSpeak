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

package org.lightjason.agentspeak.language;

import javax.annotation.Nonnull;


/**
 * interface to define a structure with native data
 */
public interface IRawStructure<T>
{
    /**
     * returns allocated state
     *
     * @return boolean flag
     */
    boolean allocated();

    /**
     * checkes assignable of the value
     *
     * @param p_class class
     * @return assignable (on null always true)
     */
    boolean valueassignableto( @Nonnull final Class<?>... p_class );

    /**
     * throws an illegal state exception
     * iif the variable is not allocated
     *
     * @return object itself
     *
     * @throws IllegalStateException on non-allocated
     */
    @Nonnull
    T thrownotallocated() throws IllegalStateException;

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
    T throwvaluenotassignableto( @Nonnull final Class<?>... p_class ) throws IllegalArgumentException;

}
