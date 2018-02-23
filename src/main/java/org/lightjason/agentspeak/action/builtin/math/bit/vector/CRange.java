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

package org.lightjason.agentspeak.action.builtin.math.bit.vector;

import cern.colt.matrix.tbit.BitVector;
import com.codepoetics.protonpack.StreamUtils;
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
import java.util.stream.Collectors;


/**
 * returns a new bit vector, based on the current.
 * The action returns a new bit vector based on the
 * range, the first argument is a bit vector,
 * all other arguments are tuples of ranges,
 * the action fails on wrong input, index starts
 * at zero, the action never fails
 *
 * @code [V1|V2] = math/bit/vector/range( BitVector, 0, 1, [3, 5] ); @endcode
 */
public final class CRange extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2022532423038334824L;

    /**
     * ctor
     */
    public CRange()
    {
        super( 4 );
    }

    @Nonnegative
    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public final IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                               @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );
        if ( l_arguments.size() % 2 == 0 )
            return CFuzzyValue.from( false );

        StreamUtils.windowed(
            l_arguments.stream()
                       .skip( 1 )
                       .map( ITerm::<Number>raw )
                       .mapToInt( Number::intValue )
                       .boxed(),
            2,
            2
        ).map( i -> l_arguments.get( 0 ).<BitVector>raw().partFromTo( i.get( 0 ), i.get( 1 ) ) )
                   .map( CRawTerm::from )
                   .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
