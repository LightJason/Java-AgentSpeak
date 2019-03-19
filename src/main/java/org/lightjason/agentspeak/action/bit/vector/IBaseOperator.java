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

package org.lightjason.agentspeak.action.bit.vector;

import cern.colt.matrix.tbit.BitVector;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * defines an abstract operator for bit vector
 */
public abstract class IBaseOperator extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 8112058782234338444L;

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public final Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                                 @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<BitVector> l_arguments = CCommon.flatten( p_argument )
                                                   .map( ITerm::<BitVector>raw )
                                                   .collect( Collectors.toList() );

        l_arguments.stream()
                   .skip( 1 )
                   .parallel()
                   .forEach( i -> this.apply( i, l_arguments.get( 0 ) ) );

        return Stream.of();
    }


    /**
     * apply method to apply operation
     *
     * @param p_target bit vector which will modifed
     * @param p_source source of modification
     */
    protected abstract void apply( @Nonnull final BitVector p_target, @Nonnull final BitVector p_source );
}
