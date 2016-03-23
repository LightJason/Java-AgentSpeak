/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.common;


import lightjason.error.CIllegalArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * class to create a path structure
 */
public final class CPath implements IPath
{
    public static final String DEFAULTSEPERATOR = "/";
    /**
     * empty path
     **/
    public static final IPath EMPTY = new CPath( Collections.<String>emptyList() );
    /**
     * list with path parts *
     */
    private List<String> m_path = new ArrayList<>();
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
    }

    /**
     * copy-ctor
     *
     * @param p_path path object
     */
    public CPath( final IPath p_path )
    {
        m_separator = p_path.getSeparator();
        m_path.addAll( p_path.stream().collect( Collectors.toList() ) );
    }

    /**
     * ctor
     *
     * @param p_varargs path component
     */
    public CPath( final String... p_varargs )
    {
        if ( ( p_varargs != null ) && ( p_varargs.length > 0 ) )
            this.initialize( StringUtils.join( p_varargs, m_separator ) );
    }

    /**
     * ctor
     *
     * @param p_collection string collection
     */
    public CPath( final Collection<String> p_collection )
    {
        if ( ( p_collection != null ) && ( !p_collection.isEmpty() ) )
            m_path.addAll( p_collection );
    }

    /**
     * private ctor for internal access
     *
     * @param p_path list path
     */
    private CPath( final List<String> p_path )
    {
        m_path = p_path;
    }

    /**
     * creates a path object from different items
     *
     * @param p_varargs list of strings
     * @return path object
     */
    public static IPath createPath( final String... p_varargs )
    {
        if ( ( p_varargs == null ) || ( p_varargs.length < 1 ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( IPath.class, "createpath" ) );

        return new CPath( p_varargs );
    }

    /**
     * creates a path object by splitting a string
     *
     * @param p_varargs list of string (first element is the seperator)
     * @return path object
     */
    public static IPath createSplitPath( final String... p_varargs )
    {
        if ( ( p_varargs == null ) || ( p_varargs.length < 2 ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( IPath.class, "createpath" ) );

        return new CPath(
                Arrays.asList( p_varargs ).subList( 1, p_varargs.length ).stream()
                      .flatMap( i -> Arrays.stream( StringUtils.split( i, p_varargs[0] ) ) )
                      .collect( Collectors.toList() )
        );
    }

    /**
     * factor method to build path
     *
     * @param p_string input string
     * @return path
     */
    public static IPath from( final String p_string )
    {
        return ( p_string == null ) || ( p_string.isEmpty() ) ? EMPTY : createSplitPath( DEFAULTSEPERATOR, p_string );
    }

    @Override
    public final IPath append( final IPath p_path )
    {
        final IPath l_path = new CPath( this );
        l_path.pushback( p_path );
        return l_path;
    }

    @Override
    public final IPath append( final String p_path )
    {
        final IPath l_path = new CPath( this );
        l_path.pushback( p_path );
        return l_path;
    }

    @Override
    public final IPath remove( final int p_index )
    {
        m_path.remove( p_index );
        return this;
    }

    @Override
    public final IPath remove( final int p_start, final int p_end )
    {
        m_path.subList( p_start, p_end ).clear();
        return this;
    }

    @Override
    public final boolean endsWith( final IPath p_path )
    {
        if ( p_path.size() > this.size() )
            return false;

        for ( int i = 0; i < p_path.size(); ++i )
            if ( !this.get( i - p_path.size() ).equals( p_path.get( i ) ) )
                return false;

        return true;
    }

    @Override
    public final String get( final int p_index )
    {
        return p_index < 0 ? m_path.get( m_path.size() + p_index ) : m_path.get( p_index );
    }

    @Override
    public final String getPath( final String p_separator )
    {
        return StringUtils.join( m_path, p_separator );
    }

    @Override
    public final String getPath()
    {
        return StringUtils.join( m_path, m_separator );
    }

    @Override
    public final String getSeparator()
    {
        return m_separator;
    }

    @Override
    public final IPath setSeparator( final String p_separator )
    {
        if ( ( p_separator == null ) || ( p_separator.isEmpty() ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "separatornotempty" ) );

        m_separator = p_separator;
        return this;
    }

    @Override
    public final IPath toLower()
    {
        m_path = m_path.stream().map( i -> i.toLowerCase() ).collect( Collectors.toList() );
        return this;
    }

    @Override
    public final IPath toUpper()
    {
        m_path = m_path.stream().map( i -> i.toUpperCase() ).collect( Collectors.toList() );
        return this;
    }

    @Override
    public final IPath getSubPath( final int p_fromIndex )
    {
        return this.getSubPath( p_fromIndex, this.size() );
    }

    @Override
    public final IPath getSubPath( final int p_fromIndex, final int p_toIndex )
    {
        return new CPath( m_path.subList( p_fromIndex, p_toIndex >= 0 ? p_toIndex : this.size() + p_toIndex ) ).setSeparator( m_separator );
    }

    @Override
    public final String getSuffix()
    {
        return m_path.get( m_path.size() == 0 ? 0 : m_path.size() - 1 );
    }

    @Override
    public final int hashCode()
    {
        return m_path.stream().mapToInt( i -> i.hashCode() ).sum();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        if ( p_object instanceof IPath )
            return this.hashCode() == p_object.hashCode();
        if ( p_object instanceof String )
            return p_object.hashCode() == this.getPath().hashCode();

        return false;
    }

    @Override
    public final String toString()
    {
        return this.getPath();
    }

    /**
     * check if the path is empty
     *
     * @return empty flag
     */
    public final boolean isEmpty()
    {
        return m_path.isEmpty();
    }

    @Override
    public final Iterator<IPath> iterator()
    {
        return new Iterator<IPath>()
        {
            private int m_index;

            @Override
            public boolean hasNext()
            {
                return m_index < m_path.size();
            }

            @Override
            public IPath next()
            {
                return new CPath( CCommon.convertCollectionToArray( String[].class, m_path.subList( 0, ++m_index ) ) );
            }
        };
    }

    @Override
    public final IPath pushback( final IPath p_path )
    {
        m_path.addAll( p_path.stream().collect( Collectors.toList() ) );
        return this;
    }

    @Override
    public final IPath pushback( final String p_path )
    {
        this.pushback( new CPath( p_path ) );
        return this;
    }

    @Override
    public final IPath pushfront( final String p_path )
    {
        this.pushfront( new CPath( p_path ) );
        return this;
    }

    @Override
    public final IPath pushfront( final IPath p_path )
    {
        final ArrayList<String> l_path = p_path.stream().collect( Collectors.toCollection( ArrayList<String>::new ) );
        l_path.addAll( m_path );
        m_path = l_path;
        return this;
    }

    @Override
    public final String removeSuffix()
    {
        if ( this.isEmpty() )
            return null;

        final String l_suffix = this.getSuffix();
        if ( m_path.size() > 0 )
            m_path.remove( m_path.size() - 1 );
        return l_suffix;
    }

    @Override
    public final IPath reverse()
    {
        Collections.reverse( m_path );
        return this;
    }

    @Override
    public final int size()
    {
        return m_path.size();
    }

    @Override
    public final boolean startsWith( final IPath p_path )
    {
        if ( p_path.size() > this.size() )
            return false;

        for ( int i = 0; i < p_path.size(); ++i )
            if ( !this.get( i ).equals( p_path.get( i ) ) )
                return false;

        return true;
    }

    @Override
    public final boolean startsWith( final String p_path )
    {
        return this.startsWith( new CPath( p_path ) );
    }

    @Override
    public final Stream<String> stream()
    {
        return m_path.stream();
    }

    @Override
    public final Stream<String> parallelStream()
    {
        return m_path.parallelStream();
    }

    @Override
    public final IPath normalize()
    {
        if ( m_path.isEmpty() )
            return this;

        m_path = m_path.stream().filter( i -> !i.equals( "." ) ).collect( Collectors.toList() );

        final String l_last = m_path.get( m_path.size() - 1 );
        m_path = IntStream.range( 0, m_path.size() - 1 ).boxed().filter( i -> !m_path.get( i + 1 ).equals( ".." ) ).map( i -> m_path.get( i ) ).collect(
                Collectors.toList() );
        if ( !l_last.equals( ".." ) )
            m_path.add( l_last );

        return this;
    }

    @Override
    public final int compareTo( final IPath p_path )
    {
        return Integer.compare( this.hashCode(), p_path.hashCode() );
    }


    /**
     * splits the string data
     *
     * @param p_fqn full path
     */
    private void initialize( final String p_fqn )
    {
        m_path = Arrays.stream( p_fqn.split( m_separator ) ).filter( i -> !i.isEmpty() ).collect( Collectors.toList() );
        if ( m_path.size() == 0 )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "pathempty" ) );
    }
}
