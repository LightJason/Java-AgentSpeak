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

package org.lightjason.agentspeak.generator;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * lambda-streaming static generator
 */
public final class CLambdaStreamingStaticGenerator implements ILambdaStreamingGenerator
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -3751613362348384867L;
    /**
     * loaded lambdas
     */
    private final Map<Class<?>, ILambdaStreaming<?>> m_lambdas;


    /**
     * ctor
     */
    public CLambdaStreamingStaticGenerator()
    {
        m_lambdas = Collections.emptyMap();
    }

    /**
     * ctor
     *
     * @param p_lambda collections with lambda
     */
    public CLambdaStreamingStaticGenerator( @NonNull final Collection<ILambdaStreaming<?>> p_lambda )
    {
        this( p_lambda.stream() );
    }

    /**
     * ctor
     *
     * @param p_lambda stream with lambda
     */
    public CLambdaStreamingStaticGenerator( @NonNull final Stream<ILambdaStreaming<?>> p_lambda )
    {
        final Map<Class<?>, ILambdaStreaming<?>> l_lambda = new HashMap<>();
        p_lambda.forEach( i -> i.assignable().forEach( j -> l_lambda.putIfAbsent( j, i ) ) );
        m_lambdas = Collections.unmodifiableMap( l_lambda );
    }

    @Override
    public ILambdaStreaming<?> apply( @NonNull final Class<?> p_class )
    {
        final Optional<? extends ILambdaStreaming<?>> l_lambda = org.lightjason.agentspeak.language.CCommon.classhierarchie( p_class )
                                                                                                           .map( m_lambdas::get )
                                                                                                           .filter( Objects::nonNull )
                                                                                                           .findFirst();

        return l_lambda.isPresent() ? l_lambda.get() : ILambdaStreaming.EMPTY;
    }
}
