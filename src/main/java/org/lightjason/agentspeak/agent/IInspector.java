/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.agent;

import org.apache.commons.lang3.tuple.Triple;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.instantiable.rule.IRule;

import java.util.Map;
import java.util.stream.Stream;


/**
 * inspector interface to read agent internal data
 */
public interface IInspector
{

    /**
     * inspect sleeping value
     *
     * @param p_value value
     */
    void inspectsleeping( final long p_value );

    /**
     * inspect cycle value
     *
     * @param p_value value
     */
    void inspectcycle( final long p_value );

    /**
     * inspect beliefs
     *
     * @param p_value belief stream
     */
    void inspectbelief( final Stream<ILiteral> p_value );

    /**
     * inspect plans
     *
     * @param p_value plan stream
     */
    void inspectplans( final Stream<Triple<IPlan, Long, Long>> p_value );

    /**
     * inspect rules
     *
     * @param p_value rule stream
     */
    void inspectrules( final Stream<IRule> p_value );

    /**
     * inspect running plans
     *
     * @param p_value literal stream
     */
    void inspectrunningplans( final Stream<ILiteral> p_value );

    /**
     * inspect storage values
     *
     * @param p_value storage values
     */
    void inspectstorage( final Stream<? extends Map.Entry<String, ?>> p_value );

}
