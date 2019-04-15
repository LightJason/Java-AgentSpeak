/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;

import javax.annotation.Nonnull;
import java.util.Arrays;
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
     * serial id
     */
    private static final long serialVersionUID = 7363854226211845413L;
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
     *
     * @param p_packages package name for searching
     */
    public CLambdaStreamingGenerator( @Nonnull final String... p_packages )
    {
        this( Arrays.stream( p_packages ) );
    }

    /**
     * ctor
     *
     * @param p_usecache cache using
     * @param p_packages package name for searching
     */
    public CLambdaStreamingGenerator( boolean p_usecache, @Nonnull final String... p_packages )
    {
        this( p_usecache, Arrays.stream( p_packages ) );
    }

    /**
     * ctor
     *
     * @param p_packages stream of package names for searching
     */
    public CLambdaStreamingGenerator( @NonNull final Stream<String> p_packages )
    {
        this( true, p_packages );
    }

    /**
     * ctor
     *
     * @param p_usecache cache using
     * @param p_packages stream of package names for searching
     */
    public CLambdaStreamingGenerator( boolean p_usecache, @NonNull final Stream<String> p_packages )
    {
        m_usecache = p_usecache;
        m_packages = p_packages.collect( Collectors.toUnmodifiableSet() );
    }

    @Override
    public ILambdaStreaming<?> apply( @NonNull final Class<?> p_class )
    {
        final Set<Class<?>> l_hierarchie = org.lightjason.agentspeak.language.CCommon.classhierarchie( p_class ).collect( Collectors.toSet() );
        return m_usecache
               ? this.withcache( l_hierarchie )
               : this.withoutcache( l_hierarchie );
    }

    /**
     * search lambda expression without cache
     *
     * @param p_hierarchie hierarchie of the searching class
     * @return lambda streaming object
     */
    private ILambdaStreaming<?> withoutcache( @NonNull final Set<Class<?>> p_hierarchie )
    {
        return CCommon.lambdastreamingFromPackage( m_packages.toArray( String[]::new ) )
                      .parallel()
                      .filter( i -> i.assignable().anyMatch( p_hierarchie::contains ) )
                      .findFirst()
                      .orElse( ILambdaStreaming.EMPTY );
    }

    /**
     * search lambda expression within cache
     *
     * @param p_hierarchie hierarchie of the searching class
     * @return lambda streaming object
     */
    private ILambdaStreaming<?> withcache( @NonNull final Set<Class<?>> p_hierarchie )
    {
        // get lambda from cache if possible
        final Optional<? extends ILambdaStreaming<?>> l_cache = p_hierarchie.stream()
                                                                            .map( m_lambdas::get )
                                                                            .filter( Objects::nonNull )
                                                                            .findFirst();

        if ( l_cache.isPresent() )
            return l_cache.get();

        // search lambda object
        final ILambdaStreaming<?> l_stream = this.withoutcache( p_hierarchie );
        l_stream.assignable().forEach( i -> m_lambdas.putIfAbsent( i, l_stream ) );
        return l_stream;
    }

}
