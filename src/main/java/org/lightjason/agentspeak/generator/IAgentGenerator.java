/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.fuzzy.defuzzification.CCrisp;
import org.lightjason.agentspeak.language.fuzzy.operator.IFuzzyBundle;
import org.lightjason.agentspeak.language.fuzzy.operator.bool.CBundle;
import org.lightjason.agentspeak.language.fuzzy.operator.bool.CComplement;
import org.lightjason.agentspeak.language.fuzzy.operator.bool.CIntersection;
import org.lightjason.agentspeak.language.unify.CUnifier;
import org.lightjason.agentspeak.language.unify.IUnifier;


/**
 * generator interface to create agents
 */
public interface IAgentGenerator<T extends IAgent<?>> extends IGenerator<T>
{
    /**
     * default fuzzy bundle
     */
    IFuzzyBundle<Boolean> DEFAULTFUZZYBUNDLE = new CBundle( new CIntersection(), new CCrisp<>( new CComplement() ) );
    /**
     * default unification
     */
    IUnifier DEFAULTUNIFIER = new CUnifier();

}
