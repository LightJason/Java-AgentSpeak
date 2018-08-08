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

package org.lightjason.agentspeak.language.newfuzzy.set;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * fuzzy set
 *
 * @tparam T enum type
 */
public interface IFuzzySet<T extends Enum<?>> extends BiFunction<T, Number, IFuzzyValue<T>>
{

    /**
     * returns the definition of success
     *
     * @param p_agent agent of the execution
     * @param p_execution execution element
     * @return success fuzzy value
     */
    @Nonnull
    Stream<IFuzzyValue<T>> success( @Nonnull final IAgent<?> p_agent, @Nonnull final IExecution p_execution );

    /**
     * returns the definition of fail
     *
     * @param p_agent agent of the execution
     * @param p_execution execution element
     * @return fail fuzzy value
     */
    @Nonnull
    Stream<IFuzzyValue<T>> fail( @Nonnull final IAgent<?> p_agent, @Nonnull final IExecution p_execution );

}
