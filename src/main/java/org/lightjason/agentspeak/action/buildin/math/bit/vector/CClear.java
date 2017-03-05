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

package org.lightjason.agentspeak.action.buildin.math.bit.vector;

import jdk.nashorn.internal.runtime.BitVector;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * sets the indexed bit to false within the bit vector.
 * The action gets bit vectors and index positions and
 * in each bit vector the given bit positions are set
 * to false, the action never fails
 *
 * @code math/bit/vector/clear( BitVector1, 0, 1, BitVector2, [3, 5] ); @endcode
 */
public final class CClear extends IBuildinAction
{
    /**
     * ctor
     */
    public CClear()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        final int[] l_index = l_arguments.parallelStream()
                                         .filter( i -> CCommon.rawvalueAssignableTo( i, Number.class ) )
                                         .map( ITerm::<Number>raw )
                                         .mapToInt( Number::intValue )
                                         .toArray();

        l_arguments.parallelStream()
                   .filter( i -> CCommon.rawvalueAssignableTo( i, BitVector.class ) )
                   .map( ITerm::<BitVector>raw )
                   .forEach( i -> Arrays.stream( l_index ).forEach( i::clear ) );

        return CFuzzyValue.from( true );
    }
}
