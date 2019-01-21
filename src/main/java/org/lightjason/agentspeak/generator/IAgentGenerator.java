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

package org.lightjason.agentspeak.generator;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.fuzzy.bundle.IFuzzyBundle;
import org.lightjason.agentspeak.language.fuzzy.defuzzyfication.IDefuzzification;
import org.lightjason.agentspeak.language.fuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.fuzzy.set.IFuzzySet;
import org.lightjason.agentspeak.language.unifier.CUnifier;
import org.lightjason.agentspeak.language.unifier.IUnifier;

import javax.annotation.Nonnull;


/**
 * generator interface to create agents
 */
public interface IAgentGenerator<T extends IAgent<?>> extends IGenerator<T>
{
    /**
     * default fuzzy bundle
     *
     * @bug not implemented yet
     */
    IFuzzyBundle DEFAULTFUZZYBUNDLE = new IFuzzyBundle()
    {
        @Override
        public IFuzzySet<?> set()
        {
            return null;
        }

        @NonNull
        @Override
        public IFuzzyMembership<?> membership()
        {
            return null;
        }

        @Nonnull
        @Override
        public IDefuzzification defuzzification()
        {
            return null;
        }

        @Nonnull
        @Override
        public IAgent<?> update( @Nonnull final IAgent<?> p_agent )
        {
            return null;
        }
    };

    /**
     * default unification
     */
    IUnifier DEFAULTUNIFIER = new CUnifier();

}
