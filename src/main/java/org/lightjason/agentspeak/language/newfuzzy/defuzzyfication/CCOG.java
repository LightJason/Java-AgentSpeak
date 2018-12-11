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
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * defuzzification with center-of-gravity
 *
 * @tparam E fuzzy set enum type
 */
public final class CCOG<E extends Enum<?>> extends IBaseDefuzzification<E>
{
    /**
     * ctor
     *
     * @param p_class fuzzy set class
     */
    public CCOG( @NonNull final Class<? extends IFuzzySet<E>> p_class )
    {
        super( p_class );
    }

    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_success success function
     */
    public CCOG( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final BiFunction<E, Class<? extends IFuzzySet<E>>, Boolean> p_success )
    {
        super( p_class, p_success );
    }

    @Nonnull
    @Override
    public E defuzzify( @Nonnull final Stream<IFuzzyValue<?>> p_value )
    {
        final IFuzzyValue<?>[] l_values = p_value.toArray( IFuzzyValue<?>[]::new );
        if ( l_values.length == 1 )
            return m_class.getEnumConstants()[l_values[0].get().ordinal()].get();


        return null;
    }

}
