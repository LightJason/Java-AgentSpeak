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

package org.lightjason.agentspeak.action.builtin.blas.vector;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
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
 * assigns a value or vector to all elements.
 * The action assign the first argument to all
 * other arguments which must be vectors
 *
 * {@code
 * .math/blas/vector/assign(2, Vector1, [Vector2, Vector3] );
 * .math/blas/vector/assign( AssignVector, Vector1, [Vector2, Vector3] );
 * }
 */
public final class CAssign extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1152654348119408050L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CAssign.class, "math", "blas", "vector" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
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
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );


        if ( !l_arguments.stream()
                       .skip( 1 )
                       .parallel()
                       .allMatch( i ->
                       {

                           if ( CCommon.isssignableto( l_arguments.get( 0 ), Number.class ) )
                           {
                               i.<DoubleMatrix1D>raw().assign( l_arguments.get( 0 ).<Number>raw().doubleValue() );
                               return true;
                           }

                           if ( CCommon.isssignableto( l_arguments.get( 0 ), DoubleMatrix1D.class ) )
                           {
                               i.<DoubleMatrix1D>raw().assign( l_arguments.get( 0 ).<DoubleMatrix1D>raw() );
                               return true;
                           }

                           return false;

                       } ) )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "argumenterror" )
            );

        return Stream.of();
    }
}
