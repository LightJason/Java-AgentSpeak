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

package org.lightjason.agentspeak.consistency.metric;


import org.lightjason.agentspeak.language.ITerm;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * metric based on levenshtein distance
 *
 * @see https://en.wikipedia.org/wiki/Levenshtein_distance
 */
public final class CLevenshteinDistance implements IMetric
{
    @Override
    public final double calculate( final Stream<? extends ITerm> p_first, final Stream<? extends ITerm> p_second
    )
    {
        return CLevenshteinDistance.levenshtein(
            p_first.map( Object::toString ).collect( Collectors.joining( "" ) ),
            p_second.map( Object::toString ).collect( Collectors.joining( "" ) )
        );
    }

    /**
     * calculates the levenshtein distance
     *
     * @param p_first first string
     * @param p_second second string
     * @return distance
     * @see https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
     */
    private static double levenshtein( final String p_first, final String p_second )
    {

        IntStream.range( 0, p_first.length() ).parallel().mapToLong( i -> p_first.charAt( i ) == p_second.charAt( i ) ? 0 : 1 ).sum()


    }


}
