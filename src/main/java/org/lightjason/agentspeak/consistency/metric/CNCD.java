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

package org.lightjason.agentspeak.consistency.metric;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;

import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * metric based on the normalized-compression-distance
 *
 * @see https://en.wikipedia.org/wiki/Normalized_compression_distance
 */
public final class CNCD implements IMetric
{
    /**
     * compression algorithm
     */
    private final CCommon.ECompression m_compression;

    /**
     * ctor
     */
    public CNCD()
    {
        this( CCommon.ECompression.BZIP );
    }

    /**
     * ctor
     *
     * @param p_compression compression algorithm
     */
    public CNCD( final CCommon.ECompression p_compression )
    {
        m_compression = p_compression;
    }

    @Override
    public Double apply( final Stream<? extends ITerm> p_first, final Stream<? extends ITerm> p_second )
    {
        return CCommon.ncd(
            m_compression,
            p_first.map( Object::toString ).collect( Collectors.joining( "" ) ),
            p_second.map( Object::toString ).collect( Collectors.joining( "" ) )
        );
    }

}
