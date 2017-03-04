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

import cern.colt.bitvector.BitVector;


/**
 * performs the logical and operation to all bit vectors.
 * The action runs the logical and operator, the first
 * argument is the bit vector, that is combined with
 * all other bit vectors, so \f$ v_i = v_i \text{ && } v_1 \f$
 * is performed
 *
 * @code math/bit/vector/and( Vector, Vector1, Vector2 ); @endcode
 */
public final class CAnd extends IOperator
{

    @Override
    protected final void apply( final BitVector p_target, final BitVector p_source )
    {
        p_target.and( p_source );
    }

}
