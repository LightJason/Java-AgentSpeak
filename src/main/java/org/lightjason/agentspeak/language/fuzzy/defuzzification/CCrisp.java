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

package org.lightjason.agentspeak.language.fuzzy.defuzzification;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.operator.IFuzzyComplement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * defuzzification to a crisp value
 *
 * @tparam T fuzzy type
 * @tparam S agent type
 */
public final class CCrisp<T> implements IDefuzzification<T>
{

    /**
     * fuzzy complement
     */
    private final IFuzzyComplement<T> m_complement;

    /**
     * ctor
     *
     * @param p_complement fuzzy complement operator
     */
    public CCrisp( @Nonnull final IFuzzyComplement<T> p_complement )
    {
        m_complement = p_complement;
    }


    @Nullable
    @Override
    public T defuzzify( @Nonnull final IFuzzyValue<T> p_value )
    {
        return p_value.fuzzy() <= 0.5 ? m_complement.complement( p_value ).value() : p_value.value();
    }

    @Nonnull
    @Override
    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return p_agent;
    }
}
