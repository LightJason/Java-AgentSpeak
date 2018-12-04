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

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.newfuzzy.defuzzyfication.IDefuzzification;
import org.lightjason.agentspeak.language.newfuzzy.membership.IFuzzyElement;
import org.lightjason.agentspeak.language.newfuzzy.membership.IFuzzyMembership;

import javax.annotation.Nonnull;


/**
 * fuzzy bundle
 */
public final class CFuzzyBundle implements IFuzzyBundle
{

    /**
     * fuzzy element
     */
    private final IFuzzyElement<?> m_element;
    /**
     * fuzzy membership
     */
    private final IFuzzyMembership<?> m_membership;
    /**
     * defuzzification
     */
    private final IDefuzzification<?> m_defuzzification;

    /**
     * ctor
     *
     * @param p_element fuzzy element
     * @param p_membership fuzzy membership
     * @param p_defuzzification defuzzyfication
     */
    public CFuzzyBundle( @NonNull final IFuzzyElement<?> p_element, @NonNull final IFuzzyMembership<?> p_membership,
                         @NonNull final IDefuzzification<?> p_defuzzification )
    {
        m_element = p_element;
        m_membership = p_membership;
        m_defuzzification = p_defuzzification;
    }


    @Override
    public IFuzzyElement<?> element()
    {
        return m_element;
    }

    @NonNull
    @Override
    public IFuzzyMembership<?> membership()
    {
        return m_membership;
    }

    @Nonnull
    @Override
    public IDefuzzification<?> defuzzification()
    {
        return m_defuzzification;
    }
}
