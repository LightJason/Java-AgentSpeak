/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

import cern.colt.bitvector.BitVector;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.stream.Collectors;


/**
 * sets bit position by index and value.
 * The first argument is the bit vector, the second
 * argument is a boolean value or number value (0 = false),
 * all other values are index positions, each index bit
 * within the bit vector will be set to the given value,
 * the action never fails
 *
 * @code math/bit/vector/set( BitVector, true, 1, [3, 7]); @endcode
 */
public final class CSet extends IBuildinAction
{
    /**
     * ctor
     */
    public CSet()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 3;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        final boolean l_value = CCommon.rawvalueAssignableTo( l_arguments.get( 1 ), Number.class )
                                ? l_arguments.get( 1 ).<Number>raw().intValue() != 0
                                : l_arguments.get( 1 ).<Boolean>raw();

        l_arguments.stream()
                   .skip( 2 )
                   .map( ITerm::<Number>raw )
                   .mapToInt( Number::intValue )
                   .boxed()
                   .forEach( i -> l_arguments.get( 0 ).<BitVector>raw().put( i, l_value ) );

        return CFuzzyValue.from( true );
    }
}
