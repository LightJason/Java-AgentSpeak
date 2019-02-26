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

package org.lightjason.agentspeak.action.builtin.bit.vector;

import cern.colt.matrix.tbit.BitVector;

import javax.annotation.Nonnull;


/**
 * performs the logical not-and operation to all bit vectors.
 * The action runs the logical not-and operator, the first
 * argument is the bit vector, that is combined with
 * all other bit vectors, so \f$ v_i = v_i \text{ && not } v_1 \f$
 * is performed
 *
 * {@code .math/bit/vector/nand( Vector, Vector1, Vector2 );}
 */
public final class CNAnd extends IOperator
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3934894253651233928L;

    @Override
    protected void apply( @Nonnull final BitVector p_target, @Nonnull final BitVector p_source )
    {
        p_target.andNot( p_source );
    }

}
