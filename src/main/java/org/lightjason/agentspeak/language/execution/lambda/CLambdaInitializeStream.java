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

package org.lightjason.agentspeak.language.execution.lambda;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * lambda initialize with streaming arguments
 */
public final class CLambdaInitializeStream extends IBaseExecution<IExecution[]>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4625794081981849579L;
    /**
     * streaming elements
     */
    private final Set<ILambdaStreaming<?>> m_streaming;

    /**
     * ctor
     *
     * @param p_value data
     * @param p_streaming lambda streaming
     */
    public CLambdaInitializeStream( @Nonnull final Stream<IExecution> p_value, @Nonnull final Set<ILambdaStreaming<?>> p_streaming )
    {
        super( p_value.toArray( IExecution[]::new ) );
        m_streaming = p_streaming;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                         @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_return = CCommon.argumentlist();
        final IFuzzyValue<Boolean> l_result = Arrays.stream( m_value )
                                                    .map( i -> i.execute( p_parallel, p_context, p_argument, l_return ) )
                                                    .filter( i -> !i.value() )
                                                    .findFirst()
                                                    .orElse( CFuzzyValue.of( true ) );

        if ( !l_result.value() )
            return l_result;

        if ( l_return.size() == 0 )
            return CFuzzyValue.of( false );


        p_return.add(
            CRawTerm.of(
                l_return.stream()
                        .flatMap( this::streaming )
            )
        );

        return l_result;
    }

    /**
     * streaming operator
     *
     * @param p_value value
     * @return object stream
     */
    private Stream<?> streaming( @Nonnull final ITerm p_value )
    {
        return m_streaming.parallelStream()
                          .filter( i -> i.instaceof( p_value.raw() ) )
                          .findFirst()
                          .orElse( ILambdaStreaming.EMPTY )
                          .apply( p_value.raw() );

    }

    @Override
    public String toString()
    {
        return Arrays.stream( m_value ).map( Object::toString ).collect( Collectors.joining( ", " ) );
    }
}
