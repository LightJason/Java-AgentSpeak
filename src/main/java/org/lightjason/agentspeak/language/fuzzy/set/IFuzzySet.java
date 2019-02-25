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

package org.lightjason.agentspeak.language.fuzzy.set;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.function.Function;


/**
 * fuzzy set
 *
 * @tparam T enum type
 * @note Enum types must be ordered, at the beginning there are the bad values, at the end there are the good ones,
 * so the enum must be sorted from bad to good
 */
public interface IFuzzySet<E extends Enum<?>> extends Function<Number, IFuzzyValue<?>>
{

    /**
     * returns a raw value if it exist
     *
     * @return raw value
     *
     * @tparam V raw value of the fuzzy set element
     */
    @NonNull
    <V> V raw();

    /**
     * returns a casted enum type
     *
     * @tparam U enum type
     * @return casted enum
     */
    <U extends Enum<?>> U rawenum();

    /**
     * returns a fuzzy value by name and value
     *
     * @param p_name name
     * @param p_number value
     * @return fuzzy value
     */
    @NonNull
    IFuzzyValue<?> apply( @NonNull final String p_name, @NonNull final Number p_number );

}
