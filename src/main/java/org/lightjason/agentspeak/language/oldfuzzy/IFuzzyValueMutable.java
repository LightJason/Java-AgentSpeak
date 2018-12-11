/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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

package org.lightjason.agentspeak.language.oldfuzzy;


import javax.annotation.Nonnull;


/**
 * interface for a mutable fuzzy value
 *
 * @tparam T fuzzy type
 */
public interface IFuzzyValueMutable<T> extends IFuzzyValue<T>
{

    /**
     * sets the fuzzy value
     *
     * @param p_value new value
     * @return self reference
     */
    @Nonnull
    IFuzzyValueMutable<T> value( final T p_value );

    /**
     * sets the weight
     *
     * @param p_value weight
     * @return self reference
     */
    @Nonnull
    IFuzzyValueMutable<T> fuzzy( final double p_value );

    /**
     * returns an immutable instance of the object
     *
     * @return immutable instance
     */
    @Nonnull
    IFuzzyValue<T> immutable();

}
