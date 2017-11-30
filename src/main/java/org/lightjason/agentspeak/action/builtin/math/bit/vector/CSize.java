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

package org.lightjason.agentspeak.action.builtin.math.bit.vector;

import cern.colt.matrix.tbit.BitVector;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;


/**
 * returns the size of the vector.
 * All input vector objects will return
 * their size (number of bits), the action
 * never fails
 *
 * @code [A|B] = math/bit/vector/size( Vector1, Vector2 ); @endcode
 */
public final class CSize extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -1728749054110390648L;

    /**
     * ctor
     */
    public CSize()
    {
        super( 4 );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        CCommon.flatten( p_argument )
               .map( ITerm::<BitVector>raw )
               .map( BitVector::size )
               .map( CRawTerm::from )
               .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
