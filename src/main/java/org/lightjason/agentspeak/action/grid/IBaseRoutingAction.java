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

package org.lightjason.agentspeak.action.grid;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import com.codepoetics.protonpack.StreamUtils;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.action.grid.routing.IRouting;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * abstract routing action
 */
public abstract class IBaseRoutingAction extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 7630522658943341653L;
    /**
     * routing algorithm
     */
    protected final IRouting m_routing;

    /**
     * ctor
     *
     * @param p_routing routing
     */
    protected IBaseRoutingAction( final IRouting p_routing )
    {
        m_routing = p_routing;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        final AtomicInteger l_group = new AtomicInteger();
        final List<DoubleMatrix1D> l_route = StreamUtils.windowed(
            StreamUtils.windowed(
                l_arguments.stream()
                           .skip( 1 )
                           .flatMap( IBaseRoutingAction::unpack ),
                2
            ).flatMap( i -> pack( i.get( 0 ), i.get( 1 ), l_group ) ),
            2,
            2
        ).flatMap( i -> m_routing.apply( l_arguments.get( 0 ).raw(), i.get( 0 ), i.get( 1 ) ) ).collect( Collectors.toList() );

        return null;
    }

    /**
     * unpacks the term to a number stream
     *
     * @param p_term term value
     * @return number stream
     */
    private static Stream<Number> unpack( @Nonnull final ITerm p_term )
    {
        if ( CCommon.isssignableto( p_term, Number.class ) )
            return Stream.of( p_term.<Number>raw() );

        if ( CCommon.isssignableto( p_term, DoubleMatrix1D.class ) )
            return Arrays.stream( p_term.<DoubleMatrix1D>raw().toArray() ).boxed().map( i -> i );

        return Stream.of();
    }

    /**
     * packs a pair of numbers to a double matrix
     *
     * @param p_value1 first value
     * @param p_value2 second value
     * @param p_used switch to avoid windowed structure
     * @return double matrix stream
     */
    private static Stream<DoubleMatrix1D> pack( @NonNull final Number p_value1, @Nonnull final Number p_value2, @Nonnull final AtomicInteger p_used )
    {
        return p_used.getAndIncrement() % 2 == 0
               ? Stream.of( new DenseDoubleMatrix1D( new double[]{p_value1.doubleValue(), p_value2.doubleValue()} ) )
               : Stream.of();
    }
}
