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

package org.lightjason.agentspeak.language.newfuzzy.norm;

import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.function.BinaryOperator;


/**
 * interface of fuzzy set operator
 *
 * @see https://en.wikipedia.org/wiki/Fuzzy_set_operations
 * @see https://en.wikipedia.org/wiki/T-norm
 */
public interface IFuzzyNorm<E extends Enum<?>> extends BinaryOperator<IFuzzyValue<E>>
{

    // https://stackoverflow.com/questions/24308146/why-is-a-combiner-needed-for-reduce-method-that-converts-type-in-java-8
    // https://www.logicbig.com/tutorials/core-java-tutorial/java-util-stream/reduction.html
    // https://de.wikipedia.org/wiki/T-Norm
    // http://www.nicodubois.com/bois5.2.htm
    // http://reinarz.org/dirk/fuzzykugel/fuzzy.html
    // https://www.mathworks.com/help/fuzzy/fuzzy-inference-process.html

}
