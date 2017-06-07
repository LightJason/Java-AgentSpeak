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

package org.lightjason.agentspeak.language.fuzzy.operator;

import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValueMutable;

import java.util.stream.Collector;


/**
 * defines a fuzzy t-norm
 *
 * @tparam T fuzzy type
 */
public interface IFuzzyOperator<T> extends Collector<IFuzzyValue<T>, IFuzzyValueMutable<T>, IFuzzyValue<T>>
{

    /**
     * calculates for a array of values the result
     *
     * @param p_values values
     * @return result value
     */
    @SuppressWarnings( "unchecked" )
    IFuzzyValue<T> result( final IFuzzyValue<T>... p_values );

}
