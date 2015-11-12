/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.generic.implementation;


import lightjason.common.CPath;
import lightjason.generic.IBeliefBase;
import lightjason.generic.IBeliefBaseMask;
import lightjason.generic.ILiteral;
import lightjason.generic.IStorage;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


/**
 * mask of a beliefbase
 *
 * @tparam P type of the beliefbase element
 */
@SuppressWarnings( "serial" )
public class CMask<T> implements IBeliefBaseMask<T>
{
    /**
     * path separator
     */
    protected String m_pathseparator = CPath.DEFAULTSEPERATOR;
    /**
     * mask name
     */
    protected final String m_name;
    /**
     * reference to the beliefbase context
     */
    protected final IBeliefBase<T> m_beliefbase;
    /**
     * parent name
     */
    private final IBeliefBaseMask<T> m_parent;

    /**
     * ctor
     *
     * @param p_name name of the mask
     * @param p_beliefbase reference to the beliefbase context
     */
    public CMask( final String p_name, final IBeliefBase<T> p_beliefbase )
    {
        this( p_name, p_beliefbase, null );
    }

    /**
     * private ctor
     *
     * @param p_name name of the mask
     * @param p_beliefbase reference to the beliefbase context
     * @param p_parent reference to the parent mask
     */
    public CMask( final String p_name, final IBeliefBase<T> p_beliefbase, final IBeliefBaseMask<T> p_parent )
    {
        if ( ( p_name == null ) || ( p_name.isEmpty() ) )
            throw new IllegalArgumentException( MessageFormat.format( "name need not to be empty", "" ) );
        if ( p_beliefbase == null )
            throw new IllegalArgumentException( MessageFormat.format( "beliefbase need not to be empty", "" ) );

        m_name = p_name;
        m_beliefbase = p_beliefbase;
        m_parent = p_parent;
    }

    @Override
    public void add( final CPath p_path, final ILiteral<T> p_literal )
    {
        this.add( p_path, p_literal, null );
    }

    @Override
    public IBeliefBaseMask<T> add( final CPath p_path, final IBeliefBaseMask<T> p_mask )
    {
        return this.add( p_path, p_mask, null );
    }

    @Override
    public IBeliefBaseMask<T> add( final CPath p_path, final IGenerator<T> p_generator )
    {
        return walk( p_path, this, p_generator );
    }

    @Override
    public IBeliefBaseMask<T> add( final CPath p_path, final IBeliefBaseMask<T> p_mask, final IGenerator<T> p_generator
    )
    {
        return walk( p_path, this, p_generator ).add( p_mask );
    }

    @Override
    public void add( final CPath p_path, final ILiteral<T> p_literal, final IGenerator<T> p_generator
    )
    {
        walk( p_path, this, p_generator ).add( p_literal );
    }

    @Override
    public boolean containsMask( final CPath p_path )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return true;

        if ( p_path.size() == 1 )
            return m_beliefbase.getStorage().containsSingleElement( p_path.get( 0 ) );

        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).containsMask( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public boolean containsLiteral( final CPath p_path )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return true;

        if ( p_path.size() == 1 )
            return m_beliefbase.getStorage().containsMultiElement( p_path.get( 0 ) );

        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).containsLiteral( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public boolean remove( final CPath p_path, final ILiteral<T> p_literal )
    {
        return walk( p_path, this, null ).remove( p_literal );
    }

    @Override
    public boolean remove( final CPath p_path, final IBeliefBaseMask<T> p_mask )
    {
        return walk( p_path, this, null ).remove( p_mask );
    }

    @Override
    public boolean remove( final CPath p_path )
    {
        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).remove( p_path.getSuffix() );
    }

    @Override
    public IBeliefBaseMask<T> clone( final IBeliefBaseMask<T> p_parent )
    {
        return new CMask<>( m_name, m_beliefbase, p_parent );
    }

    @Override
    public Map<CPath, Set<ILiteral<T>>> getLiterals( final CPath p_path )
    {
        return walk( p_path, this, null ).getLiterals();
    }

    @Override
    public Map<CPath, Set<ILiteral<T>>> getLiterals()
    {
        final CPath l_path = this.getFQNPath();
        return new HashMap<CPath, Set<ILiteral<T>>>()
        {{
            for ( final Iterator<IBeliefBaseMask<T>> l_iterator = m_beliefbase.getStorage().iteratorSingleElement(); l_iterator.hasNext(); )
                for ( final Map.Entry<CPath, Set<ILiteral<T>>> l_item : l_iterator.next().getLiterals().entrySet() )
                {
                    final Set<ILiteral<T>> l_set = getOrDefault( l_item.getKey(), new HashSet<>() );
                    l_set.addAll( l_item.getValue() );
                    putIfAbsent( l_item.getKey(), l_set );
                }

            for ( final Iterator<ILiteral<T>> l_iterator = m_beliefbase.getStorage().iteratorMultiElement(); l_iterator.hasNext(); )
            {
                final ILiteral<T> l_literal = l_iterator.next();
                final CPath l_literalpath = l_path.append( l_literal.getFunctor().get() );

                final Set<ILiteral<T>> l_set = getOrDefault( l_literalpath, new HashSet<>() );
                l_set.add( l_literal.clone( l_path ) );
                putIfAbsent( l_literalpath, l_set );
            }
        }};
    }

    @Override
    public Set<ILiteral<T>> getLiteral( final CPath p_path )
    {
        return walk( p_path.getSubPath( 0, -1 ), this, null ).getStorage().getMultiElement( p_path.getSuffix() );
    }

    @Override
    public IBeliefBaseMask<T> getMask( final CPath p_path )
    {
        return walk( p_path.getSubPath( 0, -1 ), this, null ).getStorage().getSingleElement( p_path.getSuffix() );
    }

    @Override
    public Map<CPath, IBeliefBaseMask<T>> getMasks( final CPath p_path )
    {
        return walk( p_path, this, null ).getMasks();
    }

    @Override
    public Map<CPath, IBeliefBaseMask<T>> getMasks()
    {
        return new HashMap<CPath, IBeliefBaseMask<T>>()
        {{
            for ( final Iterator<IBeliefBaseMask<T>> l_iterator = m_beliefbase.getStorage().iteratorSingleElement(); l_iterator.hasNext(); )
            {
                final IBeliefBaseMask<T> l_mask = l_iterator.next();
                put( l_mask.getFQNPath(), l_mask );
            }
        }};
    }

    @Override
    public CPath getFQNPath()
    {
        final CPath l_path = new CPath();
        l_path.setSeparator( m_pathseparator );
        return getFQNPath( this, l_path );
    }

    @Override
    public IBeliefBaseMask<T> setPathSeparator( final String p_separator )
    {
        m_pathseparator = p_separator;
        return this;
    }

    @Override
    public String getName()
    {
        return m_name;
    }

    @Override
    public IBeliefBaseMask<T> getParent()
    {
        return m_parent;
    }

    @Override
    public boolean hasParent()
    {
        return m_parent != null;
    }

    @Override
    public int hashCode()
    {
        return 47 * m_name.hashCode() + 49 * m_beliefbase.hashCode();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{ name : {0}, fqn : {1}, storage : {2} }", m_name, this.getFQNPath(), m_beliefbase.getStorage() );
    }

    @Override
    public void add( final ILiteral<T> p_literal )
    {
        m_beliefbase.add( p_literal );
    }

    @Override
    public IBeliefBaseMask<T> add( final IBeliefBaseMask<T> p_mask )
    {
        // check first, if a mask with an equal storage exists  on the path
        for ( IBeliefBaseMask<T> l_mask = this; l_mask != null; )
        {
            if ( this.getStorage().equals( p_mask.getStorage() ) )
                throw new IllegalArgumentException( MessageFormat.format( "storages {0} are equal to {1}", p_mask.getName(), l_mask.getFQNPath() ) );

            l_mask = l_mask.getParent();
        }

        return m_beliefbase.add( p_mask.clone( this ) );
    }

    @Override
    public void clear()
    {
        m_beliefbase.clear();
    }

    @Override
    public <E extends IBeliefBaseMask<T>> E createMask( final String p_name )
    {
        return m_beliefbase.createMask( p_name );
    }

    @Override
    public <L extends IStorage<ILiteral<T>, IBeliefBaseMask<T>>> L getStorage()
    {
        return m_beliefbase.getStorage();
    }

    @Override
    public boolean isEmpty()
    {
        return m_beliefbase.isEmpty();
    }

    @Override
    public boolean remove( final IBeliefBaseMask<T> p_mask )
    {
        return m_beliefbase.remove( p_mask );
    }

    @Override
    public boolean remove( final ILiteral<T> p_literal )
    {
        return m_beliefbase.remove( p_literal );
    }

    @Override
    public boolean remove( final String p_name )
    {
        return m_beliefbase.remove( p_name );
    }

    @Override
    public void update()
    {
        m_beliefbase.update();
    }

    @Override
    public int sizeMask()
    {
        return m_beliefbase.sizeMask();
    }

    @Override
    public int sizeLiteral()
    {
        return m_beliefbase.sizeLiteral();
    }

    @Override
    public int size()
    {
        return m_beliefbase.size();
    }

    @Override
    public Iterator<ILiteral<T>> iteratorLiteral()
    {
        // the fqn path is build through the tree traversing, so only the current path is build
        final CPath l_path = new CPath( m_name ).setSeparator( m_pathseparator );
        return new Iterator<ILiteral<T>>()
        {
            /**
             * stack with iterators
             **/
            final Stack<Iterator<ILiteral<T>>> m_stack = new Stack<Iterator<ILiteral<T>>>()
            {{
                add( CMask.this.getStorage().iteratorMultiElement() );
                for ( final Iterator<IBeliefBaseMask<T>> l_iterator = CMask.this.getStorage().iteratorSingleElement(); l_iterator.hasNext(); )
                    add( l_iterator.next().iteratorLiteral() );
            }};

            @Override
            public boolean hasNext()
            {
                if ( m_stack.isEmpty() )
                    return false;
                if ( m_stack.peek().hasNext() )
                    return true;

                m_stack.pop();
                return this.hasNext();
            }

            @Override
            public ILiteral<T> next()
            {
                return m_stack.peek().next().clone( l_path );
            }

        };
    }

    @Override
    public Iterator<IBeliefBaseMask<T>> iteratorBeliefBaseMask()
    {
        return new Iterator<IBeliefBaseMask<T>>()
        {
            /** stack with iterator **/
            final Stack<Iterator<IBeliefBaseMask<T>>> m_stack = new Stack<Iterator<IBeliefBaseMask<T>>>()
            {{
                add( CMask.this.getStorage().iteratorSingleElement() );
                for ( final Iterator<IBeliefBaseMask<T>> l_iterator = CMask.this.getStorage().iteratorSingleElement(); l_iterator.hasNext(); )
                    add( l_iterator.next().iteratorBeliefBaseMask() );
            }};

            @Override
            public boolean hasNext()
            {
                if ( m_stack.isEmpty() )
                    return false;
                if ( m_stack.peek().hasNext() )
                    return true;

                m_stack.pop();
                return this.hasNext();
            }

            @Override
            public IBeliefBaseMask<T> next()
            {
                return m_stack.peek().next();
            }
        };
    }

    /**
     * static method to generate FQN path
     *
     * @param p_mask curretn path
     * @param p_path current path
     * @return path
     *
     * @tparam Q type of the beliefbase elements
     */
    private static <Q> CPath getFQNPath( final IBeliefBaseMask<Q> p_mask, final CPath p_path )
    {
        p_path.pushfront( p_mask.getName() );
        return !p_mask.hasParent() ? p_path : getFQNPath( p_mask.getParent(), p_path );
    }

    /**
     * returns a mask on the recursive descend
     *
     * @param p_path path
     * @param p_root start / root node
     * @param p_generator generator object for new masks
     * @return mask
     *
     * @note a path can contains ".." to use the parent object
     * @tparam Q literal type
     */
    private static <Q> IBeliefBaseMask<Q> walk( final CPath p_path, final IBeliefBaseMask<Q> p_root, final IGenerator<Q> p_generator )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return p_root;

        // get the next mask (on ".." the parent is returned otherwise the child is used)
        IBeliefBaseMask<Q> l_mask = "..".equals( p_path.get( 0 ) ) ? p_root.getParent() : p_root.getStorage().getSingleElement( p_path.get( 0 ) );

        // if a generator is exists and the mask is null, a new mask is created and added to the current
        if ( ( l_mask == null ) && ( p_generator != null ) && ( !( "..".equals( p_path.get( 0 ) ) ) ) )
            l_mask = p_root.add( p_generator.createBeliefbase( p_path.get( 0 ) ) );

        // if mask null an exception is thrown
        if ( l_mask == null )
            throw new IllegalArgumentException( MessageFormat.format( "path element {0} in {1} not found", p_path.get( 0 ), p_path ) );

        // recursive descend
        return walk( p_path.getSubPath( 1 ), l_mask, p_generator );
    }
}
