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

package org.lightjason.agentspeak.consistency.metric;


import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;

import java.util.stream.Collectors;
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
        m_deleteweight = p_deleteweight;
    }


    @Override
    public Double apply( final Stream<? extends ITerm> p_first, final Stream<? extends ITerm> p_second )
    {
        return CCommon.levenshtein(
            p_first.map( Object::toString ).collect( Collectors.joining( "" ) ),
            p_second.map( Object::toString ).collect( Collectors.joining( "" ) ),
            m_insertweight, m_replaceweight, m_deleteweight
        );
    }

}
