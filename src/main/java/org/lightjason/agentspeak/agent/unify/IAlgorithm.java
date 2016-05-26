/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.agent.unify;

import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.Set;
import java.util.stream.Stream;


/**
 * unify algorithm
 */
public interface IAlgorithm
{

    /**
     * unify algorithm
     *
     * @param p_variables unified variables
     * @param p_source source stream of terms
     * @param p_target target stream of terms with variables (must be a deep-copy)
     * @return boolean of unify success
     *
     * @tparam T term type
     */
    <T extends ITerm> boolean unify( final Set<IVariable<?>> p_variables, final Stream<T> p_source, final Stream<T> p_target );

}
