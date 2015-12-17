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

package lightjason.beliefbase;


import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;

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
public class CMask implements IBeliefBaseMask
{
    /**
     * mask name
     */
    protected final String m_name;
    /**
     * reference to the beliefbase context
     */
    protected final IBeliefBase m_beliefbase;
    /**
     * parent name
     */
    private final IBeliefBaseMask m_parent;

    /**
     * ctor
     *
     * @param p_name name of the mask
     * @param p_beliefbase reference to the beliefbase context
     */
    public CMask( final String p_name, final IBeliefBase p_beliefbase )
    {
        this( p_name, p_beliefbase, null );
    }

    /**
     * ctor
     *
     * @param p_name name of the mask
     * @param p_beliefbase reference to the beliefbase context
     * @param p_parent reference to the parent mask
     */
    public CMask( final String p_name, final IBeliefBase p_beliefbase, final IBeliefBaseMask p_parent )
    {
        if ( ( p_name == null ) || ( p_name.isEmpty() ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "empty" ) );
        if ( p_beliefbase == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "beliefbaseempty" ) );

        m_name = p_name;
        m_beliefbase = p_beliefbase;
        m_parent = p_parent;
    }

    @Override
    public void add( final CPath p_path, final ILiteral p_literal )
    {
        this.add( p_path, p_literal, null );
    }

    @Override
    public IBeliefBaseMask add( final CPath p_path, final IBeliefBaseMask p_mask )
    {
        return this.add( p_path, p_mask, null );
    }

    @Override
    public IBeliefBaseMask add( final CPath p_path, final IGenerator<Object> p_generator )
    {
        return walk( p_path, this, p_generator );
    }

    @Override
    public IBeliefBaseMask add( final CPath p_path, final IBeliefBaseMask p_mask, final IGenerator<Object> p_generator
    )
    {
        return walk( p_path, this, p_generator ).add( p_mask );
    }

    @Override
    public void add( final CPath p_path, final ILiteral p_literal, final IGenerator<Object> p_generator
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
    public boolean remove( final CPath p_path, final ILiteral p_literal )
    {
        return walk( p_path, this, null ).remove( p_literal );
    }

    @Override
    public boolean remove( final CPath p_path, final IBeliefBaseMask p_mask )
    {
        return walk( p_path, this, null ).remove( p_mask );
    }

    @Override
    public boolean remove( final CPath p_path )
    {
        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).remove( p_path.getSuffix() );
    }

    @Override
    public IBeliefBaseMask clone( final IBeliefBaseMask p_parent )
    {
        return new CMask( m_name, m_beliefbase, p_parent );
    }

    @Override
    public Map<CPath, Set<ILiteral>> getLiterals( final CPath p_path )
    {
        return walk( p_path, this, null ).getLiterals();
    }

    @Override
    public Map<CPath, Set<ILiteral>> getLiterals()
    {
        final CPath l_path = this.getFQNPath();
        return new HashMap<CPath, Set<ILiteral>>()
        {{
            for ( final Iterator<IBeliefBaseMask> l_iterator = m_beliefbase.getStorage().iteratorSingleElement(); l_iterator.hasNext(); )
                for ( final Map.Entry<CPath, Set<ILiteral>> l_item : l_iterator.next().getLiterals().entrySet() )
                {
                    final Set<ILiteral> l_set = getOrDefault( l_item.getKey(), new HashSet<>() );
                    l_set.addAll( l_item.getValue() );
                    putIfAbsent( l_item.getKey(), l_set );
                }

            for ( final Iterator<ILiteral> l_iterator = m_beliefbase.getStorage().iteratorMultiElement(); l_iterator.hasNext(); )
            {
                final ILiteral l_literal = l_iterator.next();
                final CPath l_literalpath = l_path.append( l_literal.getFunctor() );

                final Set<ILiteral> l_set = getOrDefault( l_literalpath, new HashSet<>() );
                l_set.add( l_literal.clone( l_path ) );
                putIfAbsent( l_literalpath, l_set );
            }
        }};
    }

    @Override
    public Set<ILiteral> getLiteral( final CPath p_path )
    {
        return walk( p_path.getSubPath( 0, -1 ), this, null ).getStorage().getMultiElement( p_path.getSuffix() );
    }

    @Override
    public IBeliefBaseMask getMask( final CPath p_path )
    {
        return walk( p_path.getSubPath( 0, -1 ), this, null ).getStorage().getSingleElement( p_path.getSuffix() );
    }

    @Override
    public Map<CPath, IBeliefBaseMask> getMasks( final CPath p_path )
    {
        return walk( p_path, this, null ).getMasks();
    }

    @Override
    public Map<CPath, IBeliefBaseMask> getMasks()
    {
        

        return new HashMap<CPath, IBeliefBaseMask>()
        {{
            for ( final Iterator<IBeliefBaseMask> l_iterator = m_beliefbase.getStorage().iteratorSingleElement(); l_iterator.hasNext(); )
            {
                final IBeliefBaseMask l_mask = l_iterator.next();
                put( l_mask.getFQNPath(), l_mask );
            }
        }};
    }

    @Override
    public CPath getFQNPath()
    {
        return getFQNPath( this, new CPath() );
    }

    @Override
    public final String getName()
    {
        return m_name;
    }

    @Override
    public final IBeliefBaseMask getParent()
    {
        return m_parent;
    }

    @Override
    public final boolean hasParent()
    {
        return m_parent != null;
    }

    @Override
    public final int hashCode()
    {
        return m_name.hashCode() + m_beliefbase.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "[name : {0}, fqn : {1}, storage : {2}]", m_name, this.getFQNPath(), m_beliefbase.getStorage() );
    }

    @Override
    public void add( final ILiteral p_literal )
    {
        m_beliefbase.add( p_literal );
    }

    @Override
    public IBeliefBaseMask add( final IBeliefBaseMask p_mask )
    {
        // check first, if a mask with an equal storage exists  on the path
        for ( IBeliefBaseMask l_mask = this; l_mask != null; )
        {
            if ( this.getStorage().equals( p_mask.getStorage() ) )
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "equal", p_mask.getName(), l_mask.getFQNPath() ) );

            l_mask = l_mask.getParent();
        }

        return m_beliefbase.add( p_mask.clone( this ) );
    }

    @Override
    public final void clear()
    {
        m_beliefbase.clear();
    }

    @Override
    public <E extends IBeliefBaseMask> E createMask( final String p_name )
    {
        return m_beliefbase.createMask( p_name );
    }

    @Override
    public <L extends IStorage<ILiteral, IBeliefBaseMask>> L getStorage()
    {
        return m_beliefbase.getStorage();
    }

    @Override
    public final boolean isEmpty()
    {
        return m_beliefbase.isEmpty();
    }

    @Override
    public boolean remove( final IBeliefBaseMask p_mask )
    {
        return m_beliefbase.remove( p_mask );
    }

    @Override
    public boolean remove( final ILiteral p_literal )
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
    public final int sizeMask()
    {
        return m_beliefbase.sizeMask();
    }

    @Override
    public final int sizeLiteral()
    {
        return m_beliefbase.sizeLiteral();
    }

    @Override
    public final int size()
    {
        return m_beliefbase.size();
    }

    @Override
    public Iterator<ILiteral> iteratorLiteral()
    {
        // the fqn path is build through the tree traversing, so only the current path is build
        final CPath l_path = CPath.from( m_name );
        return new Iterator<ILiteral>()
        {
            /**
             * stack with iterators
             **/
            final Stack<Iterator<ILiteral>> m_stack = new Stack<Iterator<ILiteral>>()
            {{
                add( CMask.this.getStorage().iteratorMultiElement() );
                for ( final Iterator<IBeliefBaseMask> l_iterator = CMask.this.getStorage().iteratorSingleElement(); l_iterator.hasNext(); )
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
            public ILiteral next()
            {
                return m_stack.peek().next().clone( l_path );
            }

        };
    }

    @Override
    public Iterator<IBeliefBaseMask> iteratorBeliefBaseMask()
    {
        return new Iterator<IBeliefBaseMask>()
        {
            /** stack with iterator **/
            final Stack<Iterator<IBeliefBaseMask>> m_stack = new Stack<Iterator<IBeliefBaseMask>>()
            {{
                add( CMask.this.getStorage().iteratorSingleElement() );
                for ( final Iterator<IBeliefBaseMask> l_iterator = CMask.this.getStorage().iteratorSingleElement(); l_iterator.hasNext(); )
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
            public IBeliefBaseMask next()
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
    private static <Q> CPath getFQNPath( final IBeliefBaseMask p_mask, final CPath p_path )
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
    private static <Q> IBeliefBaseMask walk( final CPath p_path, final IBeliefBaseMask p_root, final IGenerator<Q> p_generator )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return p_root;

        // get the next mask (on ".." the parent is returned otherwise the child is used)
        IBeliefBaseMask l_mask = "..".equals( p_path.get( 0 ) ) ? p_root.getParent() : p_root.getStorage().getSingleElement( p_path.get( 0 ) );

        // if a generator is exists and the mask is null, a new mask is created and added to the current
        if ( ( l_mask == null ) && ( p_generator != null ) && ( !( "..".equals( p_path.get( 0 ) ) ) ) )
            l_mask = p_root.add( p_generator.createBeliefbase( p_path.get( 0 ) ) );

        // if mask null an exception is thrown
        if ( l_mask == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( CMask.class, "notfound", p_path.get( 0 ), p_path ) );

        // recursive descend
        return walk( p_path.getSubPath( 1 ), l_mask, p_generator );
    }
}
