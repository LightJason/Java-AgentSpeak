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

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.execution.IContext;

import java.util.stream.Stream;


/**
 * literal interface
 *
 * @note closed world assumption, no negation marker needed
 */
public interface ILiteral extends ITerm, IShallowCopy<ILiteral>, Comparable<ILiteral>
{

    /**
     * returns a stream over value items
     *
     * @param p_path optional filtering value names
     * (filtering values within values)
     * @return stream
     */
    Stream<ITerm> values( final IPath... p_path );

    /**
     * returns a stream over the ordered values
     * in sequential ordering
     */
    Stream<ITerm> orderedvalues( final IPath... p_path );

    /**
     * check for empty values
     *
     * @return empty flag
     */
    boolean emptyValues();

    /**
     * returns the hash of the value
     *
     * @return hash
     */
    int valuehash();

    /**
     * getter of the literal for the negation
     *
     * @return negated flag
     */
    boolean negated();

    /**
     * returns if the literal has an @ prefix
     *
     * @return prefix is set
     */
    boolean hasAt();

    /**
     * unifies variables if exists
     *
     * @param p_context current execution context
     * @return new literal instance with unified variables
     *
     * @note un-unifyable variables passwd into the result literal
     */
    ILiteral unify( final IContext p_context );

    /**
     * allocate all variables with the current context
     *
     * @param p_context current execution context
     * @return literal with replaced variable
     */
    ILiteral allocate( final IContext p_context );

}
