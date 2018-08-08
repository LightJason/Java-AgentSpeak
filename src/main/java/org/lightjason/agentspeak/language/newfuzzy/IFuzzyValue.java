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

package org.lightjason.agentspeak.language.newfuzzy;

import javax.annotation.Nonnull;


/**
 * fuzzy value
 * @tparam T enum type
 */
public interface IFuzzyValue<T extends Enum<?>>
{
    /**
     * returns the value
     * @return returns type
     */
    @Nonnull
    T value();

    /**
     * returns the fuzzy number
     *
     * @return fuzzy number
     */
    Number fuzzy();


    /**
     * returns raw enum type
     *
     * @tparam N enum type
     * @return casted value
     */
    @Nonnull
    <N extends T> N raw();
}
