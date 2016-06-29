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

package org.lightjason.agentspeak.language.execution.fuzzy.operator.bool;

import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValueMutable;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValueMutable;
import org.lightjason.agentspeak.language.execution.fuzzy.operator.IFuzzyOperator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * fuzzy-boolean conjunction / intersection
 */
public final class CIntersection implements IFuzzyOperator<Boolean>
{

    @Override
    public final Supplier<IFuzzyValueMutable<Boolean>> supplier()
    {
        return CIntersection::factory;
    }

    @Override
    public final BiConsumer<IFuzzyValueMutable<Boolean>, IFuzzyValue<Boolean>> accumulator()
    {
        return ( i, j ) -> i.fuzzy( Math.min( i.fuzzy(), j.fuzzy() ) ).value( i.value() && j.value() );
    }

    @Override
    public final BinaryOperator<IFuzzyValueMutable<Boolean>> combiner()
    {
        return ( i, j ) -> i.fuzzy( Math.min( i.fuzzy(), j.fuzzy() ) ).value( i.value() && j.value() );
    }

    @Override
    public final Function<IFuzzyValueMutable<Boolean>, IFuzzyValue<Boolean>> finisher()
    {
        return IFuzzyValueMutable::immutable;
    }

    @Override
    public final Set<Characteristics> characteristics()
    {
        return Collections.<Characteristics>emptySet();
    }

    @Override
    @SafeVarargs
    public final IFuzzyValue<Boolean> result( final IFuzzyValue<Boolean>... p_values )
    {
        return Arrays.stream( p_values ).collect( this );
    }

    /**
     * factory of the initialize value
     *
     * @return fuzzy value
     */
    private static IFuzzyValueMutable<Boolean> factory()
    {
        return CFuzzyValueMutable.from( true, 1 );
    }

}
