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

package org.lightjason.agentspeak.action.builtin.math.bit.matrix;

import cern.colt.bitvector.BitMatrix;
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
 * returns for the index tuple a numeric value.
 * The action returns for the first argument, which
 * is a bit matrix, all numeric values for all
 * given index tuples (0 = false, 1 = true),
 * the action fails on wrong input
 *
 * @code [B1|B2] = math/bit/matrix/numericvalue( BitMatrix, 1, 2, [Row, Column] ); @endcode
 */
public final class CNumericValue extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -6053540018011561601L;

    /**
     * ctor
     */
    public CNumericValue()
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
                   2
        ).mapToLong( i -> l_arguments.get( 0 ).<BitMatrix>raw().getQuick( i.get( 1 ), i.get( 0 ) ) ? 1 : 0 )
            .boxed()
            .map( CRawTerm::from )
            .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
