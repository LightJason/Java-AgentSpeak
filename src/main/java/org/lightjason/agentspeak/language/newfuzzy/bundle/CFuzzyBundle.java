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

package org.lightjason.agentspeak.language.newfuzzy.bundle;

import org.lightjason.agentspeak.language.newfuzzy.defuzzyfication.IDefuzzification;
import org.lightjason.agentspeak.language.newfuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;


/**
 * fuzzy bundle
 */
public final class CFuzzyBundle<T extends Enum<?>> implements IFuzzyBundle<T>
{
    /**
     * fuzzy set
     */
    private final IFuzzySet<T> m_set;
    /**
     * defuzzification
     */
    private final IDefuzzification<T> m_defuzzyfication;

    /**
     * ctor
     *
     * @param p_set fuzzy set
     * @param p_defuzzyfication defuzzyfication
     */
    public CFuzzyBundle( @Nonnull final IFuzzySet<T> p_set, @Nonnull final IDefuzzification<T> p_defuzzyfication )
    {
        m_set = p_set;
        m_defuzzyfication = p_defuzzyfication;
    }

    @Nonnull
    @Override
    public final IFuzzySet<T> fuzzyset()
    {
        return m_set;
    }

    @Nonnull
    @Override
    public final IDefuzzification<T> defuzzification()
    {
        return m_defuzzyfication;
    }
}
