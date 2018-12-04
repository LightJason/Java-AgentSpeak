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

import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * interface of fuzzy set operator
 *
 * @see https://en.wikipedia.org/wiki/Fuzzy_set_operations
 * @see https://en.wikipedia.org/wiki/T-norm
 */
public interface IFuzzyOperator<V extends IFuzzyValue<?>> extends BiFunction<V, V, Stream<V>>
{

    // https://stackoverflow.com/questions/24308146/why-is-a-combiner-needed-for-reduce-method-that-converts-type-in-java-8
    // https://www.logicbig.com/tutorials/core-java-tutorial/java-util-stream/reduction.html
    // https://de.wikipedia.org/wiki/T-Norm
    // http://www.nicodubois.com/bois5.2.htm
    // http://reinarz.org/dirk/fuzzykugel/fuzzy.html
    // https://www.mathworks.com/help/fuzzy/fuzzy-inference-process.html

    // http://www.lab4inf.fh-muenster.de/lab4inf/docs/Fuzzy_Logic_and_Control/03-FCL-Fuzzy_Inferenz.pdf
    // https://www.informatik.uni-ulm.de/ni/Lehre/SS04/ProsemSC/ausarbeitungen/Bank.pdf
    // https://www4.fh-swf.de/media/downloads/fbin/download_4/lehmann_1/internci2/ci2/skripte/fuzzy_3/Fuzzy-Tutorial_1_Gra_Leh30Teil.pdf

    // https://www.sciencedirect.com/science/article/pii/S0019995879907307

}
