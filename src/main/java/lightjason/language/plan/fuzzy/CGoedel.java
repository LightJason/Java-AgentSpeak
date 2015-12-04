/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.plan.fuzzy;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * Gödel implication
 */
public class CGoedel implements ITNorm<CBoolean>
{
    @Override
    public Supplier<CBoolean> supplier()
    {
        return null;
    }

    @Override
    public BiConsumer<CBoolean, CBoolean> accumulator()
    {
        return null;
    }

    @Override
    public BinaryOperator<CBoolean> combiner()
    {
        return null;
    }

    @Override
    public Function<CBoolean, CBoolean> finisher()
    {
        return null;
    }

    @Override
    public Set<Characteristics> characteristics()
    {
        return null;
    }
}
