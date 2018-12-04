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

package org.lightjason.agentspeak.language.newfuzzy.operator;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.newfuzzy.membership.IFuzzyMembership;

import java.util.stream.Stream;


/**
 * fuzzy intersection operator
 *
 * @tparam T fuzzy type
 */
public final class CIntersection<E extends Enum<?>> implements IFuzzyOperator<E>
{
    /**
     * membership function
     */
    private final IFuzzyMembership<E> m_membership;

    /**
     * ctor
     *
     * @param p_membership membership function
     */
    public CIntersection( @NonNull final IFuzzyMembership<E> p_membership )
    {
        m_membership = p_membership;
    }

    @Override
    public Stream<IFuzzyValue<E>> apply( @NonNull final IFuzzyValue<E> p_value1, @NonNull final IFuzzyValue<E> p_value2 )
    {
        return m_membership.apply(
            p_value1.fuzzy().doubleValue() * p_value2.fuzzy().doubleValue()
            / Math.max( 1 - p_value1.fuzzy().doubleValue(), 1 - p_value2.fuzzy().doubleValue() )
        );
    }

}
