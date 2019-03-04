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
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * lambda-streaming lazy-loader generator
 */
public final class CLambdaStreamingGenerator implements ILambdaStreamingGenerator
{
    /**
     * loaded lambdas
     */
    private final Map<Class<?>, ILambdaStreaming<?>> m_lambdas = new ConcurrentHashMap<>();
    /**
     * Java package for searching
     */
    private final Set<String> m_packages;
    /**
     * use cache
     */
    private final boolean m_usecache;


    /**
     * ctor
     */
    public CLambdaStreamingGenerator()
    {
        this( Stream.of(), true );
    }

    /**
     * ctor
     *
     * @param p_packages stream of package names for searching
     */
    public CLambdaStreamingGenerator( @NonNull final Stream<String> p_packages )
    {
        this( p_packages, true );
    }

    /**
     * ctor
     *
     * @param p_packages stream of package names for searching
     * @param p_usecache cache using
     */
    public CLambdaStreamingGenerator( @NonNull final Stream<String> p_packages, boolean p_usecache )
    {
        m_usecache = p_usecache;
        m_packages = p_packages.collect( Collectors.toUnmodifiableSet() );
    }

    @Override
    public ILambdaStreaming<?> apply( @NonNull final Class<?> p_class )
    {
        final Set<Class<?>> l_hierarchie = CCommon.classhierarchie( p_class ).collect( Collectors.toSet() );

        if ( !m_usecache )
            return org.lightjason.agentspeak.common.CCommon.lambdastreamingFromPackage( m_packages.toArray( String[]::new ) )
                                                           .parallel()
                                                           .filter( i -> i.assignable().anyMatch( l_hierarchie::contains ) )
                                                           .peek( i -> m_lambdas.putIfAbsent( p_class, i ) )
                                                           .findFirst()
                                                           .orElse( ILambdaStreaming.EMPTY );
        else
        {
            // get lambda from cache
            final Optional<? extends ILambdaStreaming<?>> l_cache = l_hierarchie.stream()
                                                                                .map( m_lambdas::get )
                                                                                .filter( Objects::nonNull )
                                                                                .findFirst();

            if ( l_cache.isPresent() )
                return l_cache.get();


            // search lambda object
            return org.lightjason.agentspeak.common.CCommon.lambdastreamingFromPackage( m_packages.toArray( String[]::new ) )
                                                           .parallel()
                                                           .filter( i -> i.assignable().anyMatch( l_hierarchie::contains ) )
                                                           .peek( i -> m_lambdas.putIfAbsent( p_class, i ) )
                                                           .findFirst()
                                                           .orElse( ILambdaStreaming.EMPTY );
        }
    }

}
