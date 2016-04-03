/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.language.execution.fuzzy;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * class for streaming reduce operations
 * of fuzzy-logical conjunction
 */
public final class CBooleanConjunction implements IFuzzyCollector<Boolean>
{

    @Override
    public final Supplier<IFuzzyValueMutable<Boolean>> supplier()
    {
        return CBooleanConjunction::factory;
    }

    @Override
    public final BiConsumer<IFuzzyValueMutable<Boolean>, IFuzzyValue<Boolean>> accumulator()
    {
        return ( i, j ) -> {
            i.setFuzzy( Math.min( i.getFuzzy(), j.getFuzzy() ) );
            i.setValue( i.getValue() && j.getValue() );
        };
    }

    @Override
    public final BinaryOperator<IFuzzyValueMutable<Boolean>> combiner()
    {
        return null;
    }

    @Override
    public final Function<IFuzzyValueMutable<Boolean>, IFuzzyValue<Boolean>> finisher()
    {
        return null;
    }

    @Override
    public final Set<Characteristics> characteristics()
    {
        return null;
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
