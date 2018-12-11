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

package org.lightjason.agentspeak.language.newfuzzy.defuzzyfication;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.newfuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * defuzzification first-of-maxima-methods
 *
 * @tparam E
 */
public final class CFOM<E extends Enum<?>> extends IBaseDefuzzification<E>
{
    /**
     * ctor
     *
     * @param p_class fuzzy set class
     */
    public CFOM( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final E p_default )
    {
        super( p_class, p_default );
    }

    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_success success function
     */
    public CFOM( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final E p_default,
                 @NonNull final BiFunction<E, Class<? extends IFuzzySet<E>>, Boolean> p_success )
    {
        super( p_class, p_default, p_success );
    }

    @Nonnull
    @Override
    public E defuzzify( @Nonnull final Stream<IFuzzyValue<?>> p_value )
    {
        return this.index2enum(
            p_value.reduce(
                CFuzzyValue.of( m_default, 0 ),
                ( i, j ) -> i.fuzzy().doubleValue() < j.fuzzy().doubleValue() ? j : i
            ).get().ordinal()
        );
    }
}
