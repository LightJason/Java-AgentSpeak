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

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.execution.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.newfuzzy.value.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * interface of a fuzzy set
 *
 * @tparam U any input
 * @tparam E fuzzy element type
 */
public interface IFuzzySet<U, E extends Enum<?>> extends Function<U, Stream<IFuzzyValue<E>>>, BiConsumer<IAgent<?>, IInstantiable>
{

    /**
     * returns the definition of success
     *
     * @return success fuzzy value stream
     */
    @Nonnull
    Stream<IFuzzyValue<E>> success();

    /**
     * returns the definition of fail
     *
     * @return fail fuzzy value stream
     */
    @Nonnull
    Stream<IFuzzyValue<E>> fail();

    /**
     * test for equality of two fuzzy values
     *
     * @param p_lhs left-hand argument
     * @param p_rhs right-hand argument
     * @return equality
     */
    boolean elementequal( @NonNull final IFuzzyValue<E> p_lhs, @NonNull final IFuzzyValue<E> p_rhs );

}
