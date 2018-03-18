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

package org.lightjason.agentspeak.language.execution.action.lambda;

import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.execution.action.IBaseExecution;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * lambda initialize with multiple arguments
 */
public final class CLambdaInitialize extends IBaseExecution<IExecution[]>
{
    private final Set<ILambdaStreaming<?>> m_streaming = new HashSet<>();

    /**
     * ctor
     *
     * @param p_value data
     */
    public CLambdaInitialize( @Nonnull final IExecution[] p_value )
    {
        super( p_value );
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                         @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_return = new ArrayList<>();
        Arrays.stream( m_value )
              .forEach( i -> i.execute( p_parallel, p_context, p_argument, l_return ) );

        if ( l_return.size() == 1 )
            p_return.add( CRawTerm.from( this.buildstream( l_return.get( 0 ) ) ) );

        if ( l_return.size() == 2 )
            p_return.add( CRawTerm.from( this.buildstream( l_return.get( 0 ), l_return.get( 1 ) ) ) );

        if ( l_return.size() == 3 )
            p_return.add( CRawTerm.from( this.buildstream( l_return.get( 0 ), l_return.get( 1 ), l_return.get( 2 ) ) ) );

        // @todo multiple arguments

        return CFuzzyValue.from( true );
    }

    /**
     * build a stream
     *
     * @param p_value value
     * @return stream
     */
    private Stream<?> buildstream( @Nonnull final ITerm p_value )
    {
        return p_value.raw() instanceof Number
               ? LongStream.range( 0, p_value.<Number>raw().longValue() ).boxed()
               : this.streaming( p_value.raw() );
    }

    /**
     * build stream
     *
     * @param p_start start value
     * @param p_end end value
     * @return stream
     */
    private Stream<?> buildstream( @Nonnull final ITerm p_start, @Nonnull final ITerm p_end )
    {
        return ( p_start.raw() instanceof Number ) && ( p_end.raw() instanceof Number )
               ? LongStream.range( p_start.<Number>raw().longValue(), p_end.<Number>raw().longValue() )
                           .boxed()
               : CCommon.streamconcat(
                    this.streaming( p_start.raw() ),
                    this.streaming( p_end.raw() )
               );
    }

    /**
     * build stream
     *
     * @param p_start start value
     * @param p_end end value
     * @param p_step step value
     * @return stream
     */
    private Stream<?> buildstream( @Nonnull final ITerm p_start, @Nonnull final ITerm p_end, @Nonnull final ITerm p_step )
    {
        return ( p_start.raw() instanceof Number ) && ( p_end.raw() instanceof Number ) && ( p_step.raw() instanceof Number )
               ? LongStream.range( p_start.<Number>raw().longValue(), p_end.<Number>raw().longValue() / p_step.<Number>raw().longValue() )
                           .map( i -> i * p_step.<Number>raw().longValue() )
                           .boxed()
               : CCommon.streamconcat(
                   this.streaming( p_start.raw() ),
                   this.streaming( p_end.raw() ),
                   this.streaming( p_step.raw() )
               );
    }

    /**
     * streaming operator
     *
     * @param p_value value
     * @return object stream
     */
    private Stream<Object> streaming( @Nonnull final Object p_value )
    {
        return m_streaming.parallelStream()
                          .filter( i -> i.instaceof( p_value ) )
                          .findFirst()
                          .orElse( ILambdaStreaming.EMPTY )
                          .apply( p_value );

    }

}
