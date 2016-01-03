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
 * # along with this program. If not, see <http://www.gnu.org/licenses/>.               #
 * ######################################################################################
 * @endcond
 */

package lightjason.beliefbase;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;


/**
 * mask of a beliefbase
 *
 * @tparam P type of the beliefbase element
 */
@SuppressWarnings( "serial" )
public class CMask implements IMask
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
    private final IMask m_parent;

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
    public CMask( final String p_name, final IBeliefBase p_beliefbase, final IMask p_parent )
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
    public void add( final ILiteral p_literal )
    {
        this.add( p_literal, null );
    }

    @Override
    public IMask add( final IMask p_mask )
    {
        // check first, if a mask with an equal storage exists  on the path
        for ( IMask l_mask = this; l_mask != null; )
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
    public <E extends IMask> E create( final String p_name )
    {
        return m_beliefbase.create( p_name );
    }

    @Override
    public <L extends IStorage<ILiteral, IMask>> L getStorage()
    {
        return m_beliefbase.getStorage();
    }

    @Override
    public final boolean isEmpty()
    {
        return m_beliefbase.isEmpty();
    }

    @Override
    public boolean remove( final IMask p_mask )
    {
        return m_beliefbase.remove( p_mask );
    }

    @Override
    public void update()
    {
        m_beliefbase.update();
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
                add( CMask.this.iteratorLiteral() );
                CMask.this.getStorage().getSingleElements().values().stream().forEach( i -> add( i.iteratorLiteral() ) );
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
    public Iterator<IMask> iteratorBeliefBaseMask()
    {
        return new Iterator<IMask>()
        {
            /**
             * stack with iterator
             **/
            final Stack<Iterator<IMask>> m_stack = new Stack<Iterator<IMask>>()
            {{
                add( CMask.this.getStorage().getSingleElements().values().iterator() );
                CMask.this.getStorage().getSingleElements().values().stream().forEach( i -> add( i.iteratorBeliefBaseMask() ) );
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
            public IMask next()
            {
                return m_stack.peek().next();
            }
        };
    }

    @Override
    public IMask add( final CPath p_path, final IMask p_mask )
    {
        return this.add( p_path, p_mask, null );
    }

    @Override
    public IMask add( final CPath p_path, final IGenerator<Object> p_generator )
    {
        return walk( p_path, this, p_generator );
    }

    @Override
    public IMask add( final CPath p_path, final IMask p_mask, final IGenerator<Object> p_generator
    )
    {
        return walk( p_path, this, p_generator ).add( p_mask );
    }

    @Override
    public void add( final ILiteral p_literal, final IGenerator<Object> p_generator
    )
    {
        if ( p_literal.getFunctorPath().isEmpty() )
            m_beliefbase.add( p_literal );
        else
            walk( p_literal.getFunctorPath(), this, p_generator ).add( p_literal );
    }

    @Override
    public boolean containsMask( final CPath p_path )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return true;

        if ( p_path.size() == 1 )
            return m_beliefbase.getStorage().getSingleElements().containsKey( p_path.get( 0 ) );

        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).containsMask( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public boolean containsLiteral( final CPath p_path )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return true;

        if ( p_path.size() == 1 )
            return m_beliefbase.getStorage().getMultiElements().containsKey( p_path.get( 0 ) );

        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).containsLiteral( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public boolean remove( final ILiteral p_literal )
    {
        if ( p_literal.getFunctorPath().isEmpty() )
        {
            m_beliefbase.add( p_literal );
            return true;
        }

        return walk( p_literal.getFunctorPath(), this, null ).remove( p_literal );
    }

    @Override
    public boolean remove( final CPath p_path, final IMask p_mask )
    {
        return walk( p_path, this, null ).remove( p_mask );
    }

    @Override
    public boolean remove( final CPath p_path )
    {
        if ( p_path.size() == 1 )
        {
            m_beliefbase.remove( p_path.getSuffix() );
            return true;
        }

        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).remove( p_path );
    }

    @Override
    public IMask clone( final IMask p_parent )
    {
        return new CMask( m_name, m_beliefbase, p_parent );
    }

    @Override
    public SetMultimap<CPath, ILiteral> getLiterals( final CPath p_path )
    {
        return walk( p_path, this, null ).getLiterals();
    }

    @Override
    public SetMultimap<CPath, ILiteral> getLiterals()
    {
        final CPath l_path = this.getFQNPath();
        final SetMultimap<CPath, ILiteral> l_map = HashMultimap.create();

        m_beliefbase.getStorage().getMultiElements().values().stream().forEach( i -> {

            final ILiteral l_literal = i.clone( l_path.append( i.getFunctorPath() ) );
            l_map.put( l_literal.getFQNFunctor(), l_literal );

        } );

        m_beliefbase.getStorage().getSingleElements().values().forEach( i -> l_map.putAll( i.getLiterals() ) );

        return l_map;
    }

    @Override
    public Set<ILiteral> getLiteral( final CPath p_path )
    {
        return walk( p_path.getSubPath( 0, -1 ), this, null ).getStorage().getMultiElements().get( p_path.getSuffix() );
    }

    @Override
    public IMask getMask( final CPath p_path )
    {
        return walk( p_path.getSubPath( 0, -1 ), this, null ).getStorage().getSingleElements().get( p_path.getSuffix() );
    }

    @Override
    public Map<CPath, IMask> getMasks( final CPath p_path )
    {
        return walk( p_path, this, null ).getMasks();
    }

    @Override
    public Map<CPath, IMask> getMasks()
    {
        return m_beliefbase.getStorage().getSingleElements().values().stream().collect( Collectors.toMap( i -> i.getFQNPath(), i -> i ) );
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
    public final IMask getParent()
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

    /**
     * static method to generate FQN path
     *
     * @param p_mask curretn path
     * @param p_path current path
     * @return path
     *
     * @tparam Q type of the beliefbase elements
     */
    private static <Q> CPath getFQNPath( final IMask p_mask, final CPath p_path )
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
    private static <Q> IMask walk( final CPath p_path, final IMask p_root, final IGenerator<Q> p_generator )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return p_root;

        // get the next mask (on ".." the parent is returned otherwise the child is used)
        IMask l_mask = "..".equals( p_path.get( 0 ) ) ? p_root.getParent() : p_root.getStorage().getSingleElements().get( p_path.get( 0 ) );

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
