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

package org.lightjason.agentspeak.language.newfuzzy.membership;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgentUpdateable;
import org.lightjason.agentspeak.language.newfuzzy.IFuzzyValue;

import java.util.function.Function;
import java.util.stream.Stream;


/**
 * membership function
 *
 * @tparam E fuzzy element type
 */
public interface IFuzzyMembership<E extends Enum<?>> extends IAgentUpdateable, Function<Number, Stream<IFuzzyValue<?>>>
{
    // https://de.wikipedia.org/wiki/Fuzzylogik#Ausschlie%C3%9Fende-ODER-Schaltung
    // http://www.nid.iitkgp.ernet.in/dsamanta/courses/archive/sca/Archives/Chapter%205%20Defuzzification%20Methods.pdf
    // https://profs.basu.ac.ir/khotanlou/upload_file/459.1778.file_ref.1938.2401.pdf

    /**
     * returns a stream of fuzzy values which
     * represent a successful structure
     *
     * @return fuzzy value stream
     */
    @NonNull
    Stream<IFuzzyValue<?>> success();

    /**
     * returns a stream of fuzzy values which
     * represent a fail structure
     *
     * @return fuzzy value stream
     */
    @NonNull
    Stream<IFuzzyValue<?>> fail();

    /**
     * stream of numbers of the range positions
     *
     * @param p_value fuzzy enum value
     * @return stream of positions
     */
    Stream<Number> range( @NonNull E p_value );

}
