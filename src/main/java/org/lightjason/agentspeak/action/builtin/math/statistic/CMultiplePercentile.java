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

package org.lightjason.agentspeak.action.builtin.math.statistic;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * returns percentile value.
 * gets percentile value in $[0,100]$ of the statistic
 *
 * {@code [V1|V2|V3] = .math/statistic/multiplepercentile( Statistic, 50, [25, 75] );}
 */
public final class CMultiplePercentile extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -6804920667837260894L;

    /**
     * ctor
     */
    public CMultiplePercentile()
    {
        super( 3 );
    }

    @Override
    public int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                           @Nonnull final List<ITerm> p_return
    )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( l_arguments.size() < 2 )
            return CFuzzyValue.of( false );

        final DescriptiveStatistics l_statistic = l_arguments.get( 0 ).raw();
        l_arguments.stream()
                   .skip( 1 )
                   .map( ITerm::<Number>raw )
                   .mapToDouble( i -> l_statistic.getPercentile( i.doubleValue() ) )
                   .mapToObj( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }
}
