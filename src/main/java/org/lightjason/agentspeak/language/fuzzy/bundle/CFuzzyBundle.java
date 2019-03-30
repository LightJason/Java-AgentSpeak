/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.fuzzy.bundle;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.fuzzy.defuzzyfication.IDefuzzification;
import org.lightjason.agentspeak.language.fuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.fuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;


/**
 * fuzzy bundle
 */
public final class CFuzzyBundle implements IFuzzyBundle
{

    /**
     * fuzzy set
     */
    private final Class<? extends IFuzzySet<?>> m_set;
    /**
     * fuzzy membership
     */
    private final IFuzzyMembership<?> m_membership;
    /**
     * defuzzification
     */
    private final IDefuzzification m_defuzzification;

    /**
     * ctor
     *
     * @param p_set fuzzy set
     * @param p_membership fuzzy membership
     * @param p_defuzzification defuzzyfication
     */
    public CFuzzyBundle( @NonNull final Class<? extends IFuzzySet<?>> p_set, @NonNull final IFuzzyMembership<?> p_membership,
                         @NonNull final IDefuzzification p_defuzzification
    )
    {
        m_set = p_set;
        m_membership = p_membership;
        m_defuzzification = p_defuzzification;
    }


    @Override
    public IFuzzySet<?> set()
    {
        // one element must exist, so return the first element
        return m_set.getEnumConstants()[0];
    }

    @NonNull
    @Override
    public IFuzzyMembership<?> membership()
    {
        return m_membership;
    }

    @Nonnull
    @Override
    public IDefuzzification defuzzification()
    {
        return m_defuzzification;
    }

    @Nonnull
    @Override
    public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return m_defuzzification.update( m_membership.update( p_agent ) );
    }
}
