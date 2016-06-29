/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
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

import org.lightjason.agentspeak.language.ILiteral;

import java.util.Collection;
import java.util.stream.Stream;


/**
 * metric on collections returns the size of symmetric difference
 *
 * @see http://mathworld.wolfram.com/SymmetricDifference.html
 */
public final class CSymmetricDifference implements IMetric
{

    @Override
    public final double calculate( final Collection<ILiteral> p_first, final Collection<ILiteral> p_second )
    {
        return Stream.concat( p_first.stream(), p_second.stream() )
                    .sorted()
                    .distinct()
                    .parallel()
                    .filter( i -> !( p_first.contains( i ) && ( p_second.contains( i ) ) ) )
                    .count();
    }

}
