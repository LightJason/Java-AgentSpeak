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

package org.lightjason.agentspeak.action.bit.matrix;

import cern.colt.matrix.tbit.BitMatrix;
import org.lightjason.agentspeak.common.IPath;

import javax.annotation.Nonnull;


/**
 * performs the logical xor operation to all bit matrices.
 * The action runs the logical xor operator, the first
 * argument is the bit matrix, that is combined with
 * all other bit matrices, so \f$ m_i = m_i \text{ ^ } m_1 \f$
 * is performed
 *
 * {@code .math/bit/matrix/xor( Matrix, Matrix1, Matrix2 );}
 */
public final class CXor extends IBaseOperator
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1472108124053352992L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CXor.class, "math", "bit", "matrix" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Override
    protected void apply( @Nonnull final BitMatrix p_target, @Nonnull final BitMatrix p_source )
    {
        p_target.xor( p_source );
    }

}
