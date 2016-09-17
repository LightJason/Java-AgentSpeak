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

package org.lightjason.agentspeak.action.buildin.math.statistic;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.apache.commons.math3.distribution.AbstractRealDistribution;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * create a (set) of random values
 */
public final class CRandomSample extends IBuildinAction
{

    /**
     * ctor
     */
    public CRandomSample()
    {
        super( 3 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // first argument distribution reference, second optional value number of random values
        if ( p_argument.size() > 1 )
            p_return.add( CRawTerm.from(
                p_parallel
                ? Collections.synchronizedList( Arrays.stream(
                    CCommon.<AbstractRealDistribution, ITerm>raw( p_argument.get( 0 ) )
                        .sample( CCommon.<Number, ITerm>raw( p_argument.get( 1 ) ).intValue() )
                ).boxed().collect( Collectors.toList() ) )
                : Arrays.stream(
                    CCommon.<AbstractRealDistribution, ITerm>raw( p_argument.get( 0 ) )
                        .sample( CCommon.<Number, ITerm>raw( p_argument.get( 1 ) ).intValue() )
                ).boxed().collect( Collectors.toList() )
            ) );
        else
            p_return.add( CRawTerm.from( CCommon.<AbstractRealDistribution, ITerm>raw( p_argument.get( 0 ) ).sample() ) );

        return CFuzzyValue.from( true );
    }

}
