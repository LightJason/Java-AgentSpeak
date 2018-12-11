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
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * defuzzification with weighted-average-method
 */
public final class CWOA<E extends Enum<?>> extends IBaseDefuzzification<E>
{
    // http://www.nid.iitkgp.ernet.in/DSamanta/courses/archive/sca/Archives/Chapter%205%20Defuzzification%20Methods.pdf
    // https://arxiv.org/pdf/1612.00742.pdf
    // https://pdfs.semanticscholar.org/b63b/91843261d8cb9b13f991f08bf77b16ef5e87.pdf


    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_default fuzzy enum type
     */
    public CWOA( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final E p_default )
    {
        super( p_class, p_default );
    }

    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_default fuzzy enum type
     * @param p_success success function
     */
    public CWOA( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final E p_default,
                 @NonNull final BiFunction<E, Class<? extends IFuzzySet<E>>, Boolean> p_success )
    {
        super( p_class, p_default, p_success );
    }

    @Nonnull
    @Override
    public E defuzzify( @Nonnull final Stream<IFuzzyValue<?>> p_value )
    {
        final IFuzzyValue<?>[] l_values = p_value.toArray( IFuzzyValue<?>[]::new );
        if ( l_values.length < 2 )
            return l_values.length == 0 ? m_default : m_class.getEnumConstants()[l_values[0].get().ordinal()].get();

        final Number l_result = Arrays.stream( l_values ).mapToDouble( i -> i.fuzzy() .doubleValue() * ( i.get().ordinal() ) ).sum()
                                / Arrays.stream( l_values ).mapToDouble( i -> i.fuzzy().doubleValue() ).sum();

        return this.indexvalue( l_result.intValue() );
    }

}
