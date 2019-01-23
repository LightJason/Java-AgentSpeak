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

package org.lightjason.agentspeak.action.builtin.math.blas.vector;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import com.codepoetics.protonpack.StreamUtils;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * returns dot-product of vectors.
 * The action calculates for each tupel of vectors
 * the dot-product, so the argument number must be odd
 *
 * {@code [D1|D2] = .math/blas/vector(V1,V2, [V3, V4] );}
 *
 * @see https://en.wikipedia.org/wiki/Dot_product
 */
public final class CDotProduct extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -953714393424081234L;

    /**
     * ctor
     */
    public CDotProduct()
    {
        super( 4 );
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 2;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final List<DoubleMatrix1D> l_arguments = CCommon.flatten( p_argument ).map( ITerm::<DoubleMatrix1D>raw ).collect( Collectors.toList() );
        if ( l_arguments.size() % 2 == 1 )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "wrongargumentnumber" )
            );

        StreamUtils.windowed( l_arguments.stream(), 2 )
                   .map( i -> i.get( 0 ).zDotProduct( i.get( 1 ) ) )
                   .map( CRawTerm::of )
                   .forEach( p_return::add );

        return Stream.of();
    }
}
