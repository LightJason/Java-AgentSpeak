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
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * returns for any static object a percentile value.
 * The actions reutnrs for any statistic value the
 * given percentile value
 *
 * {@code [V1|V2|V3] = .math/statistic/multiplepercentile( 2, Statistic1, [Statistic2, Statistic3] );}
 */
public final class CSinglePercentile extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1554327666105611014L;

    /**
     * ctor
     */
    public CSinglePercentile()
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
            throw new CExecutionIllegealArgumentException( p_context, org.lightjason.agentspeak.common.CCommon.languagestring( this, "wrongargumentnumber" ) );

        final double l_value = l_arguments.get( 0 ).<Number>raw().doubleValue();
        l_arguments.stream()
                   .skip( 1 )
                   .mapToDouble( i -> i.<DescriptiveStatistics>raw().getPercentile( l_value ) )
                   .mapToObj( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }

}
