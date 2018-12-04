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

package org.lightjason.agentspeak.language.newfuzzy.membership;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;

import java.util.function.Function;
import java.util.stream.Stream;


/**
 * fuzzy set
 *
 * @tparam T enum type
 */
public interface IFuzzyMembership<E extends Enum<?>> extends Function<Number, IFuzzyValue<E>>
{

    /**
     * returns a raw value if it exist
     *
     * @tparam V raw value of the fuzzy set element
     * @return raw value
     */
    @NonNull
    <V> V raw();

    /**
     * returns the member based on a value
     *
     * @param p_value numeric value
     * @return stream of member values
     */
    @NonNull
    Stream<IFuzzyValue<E>> member( @NonNull final Number p_value );

}
