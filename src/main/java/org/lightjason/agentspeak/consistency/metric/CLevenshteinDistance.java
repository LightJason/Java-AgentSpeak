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
    /**
     * cost / weight of insert operation
     */
    private final double m_insertweight;
    /**
     * cost / weight of replace operation
     */
    private final double m_replaceweight;
    /**
     * cost / weight of delete operation
     */
    private final double m_deleteweight;

    /**
     * ctor
     */
    public CLevenshteinDistance()
    {
        this( 1, 1, 1 );
    }

    /**
     * ctor
     *
     * @param p_insertweight weight / cost of insert character
     * @param p_replaceweight weight / cost of replace character
     * @param p_deleteweight weight / cost of delete character
     */
    public CLevenshteinDistance( final double p_insertweight, final double p_replaceweight, final double p_deleteweight )
    {
        m_insertweight = p_insertweight;
        m_replaceweight = p_replaceweight;
        m_deleteweight = p_replaceweight;
    }


    @Override
    public final Double apply( final Stream<? extends ITerm> p_first, final Stream<? extends ITerm> p_second )
    {
        return this.levenshtein(
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
    private double levenshtein( final String p_first, final String p_second )
    {
        // the array of distances
        double[] l_cost = IntStream.range( 0, p_first.length() + 1 ).mapToDouble( i -> i ).toArray();
        double[] l_newcost = new double[l_cost.length];

        for ( int j = 1; j < p_second.length() + 1; j++ )
        {
            l_newcost[0] = j;

            // calculate cost of operation for all characters
            for ( int i = 1; i < l_cost.length; i++ )
                l_newcost[i] = min(
                    l_cost[i - 1] + ( p_first.charAt( i - 1 ) == p_second.charAt( j - 1 ) ? 0 : m_replaceweight ),
                    l_newcost[i - 1] + m_deleteweight,
                    l_cost[i] + m_insertweight
                );

            final double[] l_swap = l_cost;
            l_cost = l_newcost;
            l_newcost = l_swap;
        }

        return l_cost[p_first.length()];
    }


    /**
     * returns the minimum of three elemens
     *
     * @param p_first first value
     * @param p_second second value
     * @param p_third third value
     * @return minimum
     */
    private static double min( final double p_first, final double p_second, final double p_third )
    {
        return Math.min( Math.min( p_first, p_second ), p_third );
    }


}
