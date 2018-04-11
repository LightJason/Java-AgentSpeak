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

package org.lightjason.agentspeak.common;

import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.StringUtils;
import org.lightjason.agentspeak.error.CIllegalArgumentException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * class to create a path structure
 */
public final class CPath implements IPath
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8502900889333744887L;
    /**
     * list with path parts *
     */
    private final List<String> m_path;
    /**
     * separator of the path elements *
     */
    private String m_separator = DEFAULTSEPERATOR;

    /**
     * copy-ctor with arguments
     *
     * @param p_path path object
     * @param p_varargs string arguments
     */
    public CPath( final IPath p_path, final String... p_varargs )
    {
        this( p_path );
        m_path.addAll( Arrays.asList( p_varargs ) );
        this.normalize();
    }

    /**
     * copy-ctor
     *
     * @param p_path path object
     */
    public CPath( @Nonnull final IPath p_path )
    {
        m_path = p_path.stream().collect( CPath.collectorfactory() );
        m_separator = p_path.separator();
    }

    /**
     * ctor
     *
     * @param p_varargs path component
     */
    public CPath( @Nullable final String... p_varargs )
    {
        if ( Objects.isNull( p_varargs ) || p_varargs.length == 0 )
            m_path = CPath.listfactory();
        else
        {
            m_path = Arrays.stream( StringUtils.join( p_varargs, m_separator ).split( m_separator ) )
                           .map( String::trim )
                           .filter( i -> !i.isEmpty() )
                           .collect( CPath.collectorfactory() );
            if ( m_path.size() == 0 )
                throw new CIllegalArgumentException( CCommon.languagestring( this, "pathempty" ) );
        }
        this.normalize();
    }

    /**
     * ctor
     *
     * @param p_stream string collection
     */
    public CPath( @Nonnull final Stream<String> p_stream )
    {
        m_path = p_stream.collect( CPath.collectorfactory() );
        this.normalize();
    }

    /**
     * private ctor for empty path
     */
    private CPath()
    {
        m_path = Collections.emptyList();
    }

    /**
     * creates a path object of different items
     *
     * @param p_varargs list of strings
     * @return path object
     */
    @Nonnull
    public static IPath createPath( @Nonnull final String... p_varargs )
    {
        return new CPath( p_varargs );
    }

    /**
     * creates a path object by splitting a string
     *
     * @param p_varargs list of string (first element is the seperator)
     * @return path object
     */
    @Nonnull
    public static IPath createPathWithSeperator( @Nonnull final String... p_varargs )
    {
        return new CPath(
            Arrays.asList( p_varargs ).subList( 1, p_varargs.length ).stream()
                  .flatMap( i -> Arrays.stream( StringUtils.split( i, p_varargs[0] ) ) )
        );
    }

    /**
     * factor method to build path
     *
     * @param p_string input string
     * @return path
     */
    @Nonnull
    public static IPath of( @Nonnull final String p_string )
    {
        return p_string.isEmpty() ? EMPTY : createPathWithSeperator( DEFAULTSEPERATOR, p_string );
    }

    @Nonnull
    @Override
    public IPath append( @Nonnull final IPath p_path )
    {
        return new CPath( this ).pushback( p_path );
    }

    @Nonnull
    @Override
    public IPath append( @Nonnull final String p_path )
    {
        return new CPath( this ).pushback( p_path );
    }

    @Nonnull
    @Override
    public IPath remove( final int p_index )
    {
        if ( !m_path.isEmpty() )
            m_path.remove( p_index );
        return this;
    }

    @Nonnull
    @Override
    public IPath remove( final int p_start, final int p_end )
    {
        m_path.subList( p_start, p_end ).clear();
        return this;
    }

    @Override
    public synchronized boolean endswith( @Nonnull final IPath p_path )
    {
        return p_path.size() <= this.size()
               && IntStream.range( 0, p_path.size() ).boxed().parallel().allMatch( i -> this.get( i - p_path.size() ).equals( p_path.get( i ) ) );
    }

    @Override
    public boolean startswith( @Nonnull final IPath p_path )
    {
        return p_path.size() <= this.size()
               && IntStream.range( 0, p_path.size() ).boxed().parallel().allMatch( i -> this.get( i ).equals( p_path.get( i ) ) );

    }

    @Nonnull
    @Override
    public String get( final int p_index )
    {
        return p_index < 0 ? m_path.get( m_path.size() + p_index ) : m_path.get( p_index );
    }

    @Nonnull
    @Override
    public String path( @Nonnull final String p_separator )
    {
        return StringUtils.join( m_path, p_separator );
    }

    @Nonnull
    @Override
    public String path()
    {
        return StringUtils.join( m_path, m_separator );
    }

    @Nonnull
    @Override
    public String separator()
    {
        return m_separator;
    }

    @Nonnull
    @Override
    public IPath separator( @Nonnull final String p_separator )
    {
        if ( p_separator.isEmpty() )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "separatornotempty" ) );

        m_separator = p_separator;
        return this;
    }

    @Nonnull
    @Override
    public synchronized IPath lower()
    {
        IntStream.range( 0, m_path.size() ).boxed().parallel().forEach( i -> m_path.set( i, m_path.get( i ).toLowerCase( Locale.ROOT ) ) );
        return this;
    }

    @Nonnull
    @Override
    public synchronized IPath upper()
    {
        IntStream.range( 0, m_path.size() ).boxed().parallel().forEach( i -> m_path.set( i, m_path.get( i ).toUpperCase( Locale.ROOT ) ) );
        return this;
    }

    @Nonnull
    @Override
    public IPath subpath( final int p_fromindex )
    {
        return this.subpath( p_fromindex, this.size() );
    }

    @Nonnull
    @Override
    public IPath subpath( final int p_fromindex, final int p_toindex )
    {
        return new CPath(
            p_toindex == 0
            ? Stream.empty()
            : IntStream.range(
                p_fromindex,
                p_toindex > 0 ? p_toindex : this.size() + p_toindex
            )
            .mapToObj( m_path::get )
        ).separator( m_separator );
    }

    @Nonnull
    @Override
    public synchronized String suffix()
    {
        return m_path.isEmpty()
               ? ""
               : m_path.get( m_path.size() - 1 );
    }

    @Override
    public int hashCode()
    {
        final Hasher l_hasher = org.lightjason.agentspeak.language.CCommon.getTermHashing();
        m_path.forEach( i -> l_hasher.putString( i, Charsets.UTF_8 ) );
        return l_hasher.hash().hashCode();
    }

    @Override
    @SuppressFBWarnings( "EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS" )
    public boolean equals( final Object p_object )
    {
        return p_object instanceof IPath && this.hashCode() == p_object.hashCode()
               || p_object instanceof String && this.path().hashCode() == p_object.hashCode();
    }

    @Override
    public String toString()
    {
        return this.path();
    }

    /**
     * check if the path is empty
     *
     * @return empty flag
     */
    public boolean empty()
    {
        return m_path.isEmpty();
    }

    @Nonnull
    @Override
    public IPath pushback( @Nonnull final IPath p_path )
    {
        p_path.stream().forEach( m_path::add );
        return this;
    }

    @Nonnull
    @Override
    public IPath pushback( @Nonnull final String p_path )
    {
        this.pushback( new CPath( p_path ) );
        return this;
    }

    @Nonnull
    @Override
    public IPath pushfront( @Nonnull final String p_path )
    {
        this.pushfront( new CPath( p_path ) );
        return this;
    }

    @Nonnull
    @Override
    public synchronized IPath pushfront( @Nonnull final IPath p_path )
    {
        final List<String> l_path = Stream.concat( p_path.stream(), m_path.stream() ).collect( Collectors.toList() );
        m_path.clear();
        m_path.addAll( l_path );
        return this;
    }

    @Nonnull
    @Override
    public String removesuffix()
    {
        if ( this.empty() )
            return "";

        final String l_suffix = this.suffix();
        if ( m_path.size() > 0 )
            m_path.remove( m_path.size() - 1 );
        return l_suffix;
    }

    @Nonnull
    @Override
    public IPath reverse()
    {
        Collections.reverse( m_path );
        return this;
    }

    @Override
    public int size()
    {
        return m_path.size();
    }

    @Override
    public boolean startswith( final String p_path )
    {
        return this.startswith( new CPath( p_path ) );
    }

    @Nonnull
    @Override
    public Stream<String> stream()
    {
        return m_path.stream();
    }

    @Override
    public int compareTo( @Nonnull final IPath p_path )
    {
        return Integer.compare( this.hashCode(), p_path.hashCode() );
    }

    /**
     * normalize the internal path
     */
    private synchronized void normalize()
    {
        if ( m_path.isEmpty() )
            return;

        // create path-copy and nomalize (remove dot, double-dot and empty values)
        final List<String> l_dotremove = m_path.stream()
                                               .filter( i -> Objects.nonNull( i ) && !i.isEmpty() && !".".equals( i ) )
                                               .collect( Collectors.toList() );
        if ( l_dotremove.isEmpty() )
            return;

        final String l_last = l_dotremove.get( l_dotremove.size() - 1 );
        final List<String> l_backremove = IntStream.range( 0, l_dotremove.size() - 1 )
                                                   .boxed()
                                                   .filter( i -> !l_dotremove.get( i + 1 ).equals( ".." ) )
                                                   .map( l_dotremove::get )
                                                   .collect( Collectors.toList() );
        if ( !"..".equals( l_last ) )
            l_backremove.add( l_last );

        // clear internal path and add optimized path
        m_path.clear();
        m_path.addAll( l_backremove );
    }

    /**
     * collector factory
     *
     * @return collector
     */
    @Nonnull
    private static Collector<String, List<String>, List<String>> collectorfactory()
    {
        return new Collector<String, List<String>, List<String>>()
        {
            @Override
            public Supplier<List<String>> supplier()
            {
                return CopyOnWriteArrayList<String>::new;
            }

            @Override
            public BiConsumer<List<String>, String> accumulator()
            {
                return List::add;
            }

            @Override
            public BinaryOperator<List<String>> combiner()
            {
                return ( i, j ) ->
                {
                    i.addAll( j );
                    return i;
                };
            }

            @Override
            public Function<List<String>, List<String>> finisher()
            {
                return i -> i;
            }

            @Override
            public Set<Characteristics> characteristics()
            {
                return Collections.emptySet();
            }
        };
    }

    /**
     * list factory
     *
     * @return list
     */
    @Nonnull
    private static List<String> listfactory()
    {
        return new CopyOnWriteArrayList<>();
    }

    /**
     * returns a collector to build a path of strings
     *
     * @return collector
     */
    public static Collector<String, IPath, IPath> collect()
    {
        return new CPathCollector();
    }

    /**
     * path collector
     */
    private static final class CPathCollector implements Collector<String, IPath, IPath>
    {

        @Override
        public Supplier<IPath> supplier()
        {
            return () -> new CPath( Stream.empty() );
        }

        @Override
        public BiConsumer<IPath, String> accumulator()
        {
            return IPath::pushback;
        }

        @Override
        public BinaryOperator<IPath> combiner()
        {
            return IPath::pushback;
        }

        @Override
        public Function<IPath, IPath> finisher()
        {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics()
        {
            return Collections.emptySet();
        }
    }
}
