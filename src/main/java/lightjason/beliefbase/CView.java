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

package lightjason.beliefbase;


import lightjason.agent.IAgent;
import lightjason.common.CCommon;
import lightjason.common.CPath;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;
import org.apache.commons.lang3.tuple.Pair;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * view of a beliefbase
 */
public class CView implements IView
{
    /**
     * view name
     */
    protected final String m_name;
    /**
     * reference to the beliefbase context
     */
    protected final IBeliefBase m_beliefbase;
    /**
     * parent name
     */
    private final IView m_parent;

    /**
     * ctor
     *
     * @param p_name view name
     * @param p_beliefbase reference to the beliefbase context
     */
    public CView( final String p_name, final IBeliefBase p_beliefbase )
    {
        this( p_name, p_beliefbase, null );
    }

    /**
     * ctor
     *
     * @param p_name view name
     * @param p_beliefbase reference to the beliefbase context
     * @param p_parent reference to the parent view
     */
    public CView( final String p_name, final IBeliefBase p_beliefbase, final IView p_parent )
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
    public final boolean add( final ILiteral p_literal )
    {
        return this.add( p_literal, null );
    }

    @Override
    public final IView add( final IView p_view )
    {
        /*
        // check first, if a view with an equal storage exists  on the path
        for ( IView l_view = this; l_view != null; )
        {
            if ( this.getStorage().equals( p_view.getStorage() ) )
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "equal", p_view.getName(), l_view.getPath() ) );

            l_view = l_view.getParent();
        }
        */
        return m_beliefbase.add( p_view.clone( this ) );
    }

    @Override
    public final boolean modify( final ILiteral p_before, final ILiteral p_after )
    {
        return false;
    }

    @Override
    public final void clear()
    {
        m_beliefbase.clear();
    }

    @Override
    public final <E extends IView> E create( final String p_name )
    {
        return m_beliefbase.create( p_name );
    }

    @Override
    public final <L extends IStorage<Pair<Boolean, ILiteral>, IView>> L getStorage()
    {
        return m_beliefbase.getStorage();
    }

    @Override
    public final boolean isEmpty()
    {
        return m_beliefbase.isEmpty();
    }

    @Override
    public final boolean remove( final IView p_view )
    {
        return m_beliefbase.remove( p_view );
    }

    @Override
    public final int size()
    {
        return m_beliefbase.size();
    }

    @Override
    public final IView add( final CPath p_path, final IView p_view )
    {
        return this.add( p_path.normalize(), p_view, null );
    }

    @Override
    public final IView add( final CPath p_path, final IView p_view, final IGenerator<Object> p_generator
    )
    {
        return walk( p_path.normalize(), this, p_generator ).add( p_view );
    }

    @Override
    public final boolean add( final ILiteral p_literal, final IGenerator<Object> p_generator
    )
    {
        return p_literal.getFunctorPath().isEmpty() ? m_beliefbase.add( p_literal ) : walk( p_literal.getFunctorPath(), this, p_generator ).add( p_literal );
    }

    @Override
    public final boolean containsView( final CPath p_path )
    {
        p_path.normalize();
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return true;

        if ( p_path.size() == 1 )
            return m_beliefbase.getStorage().getSingleElements().containsKey( p_path.get( 0 ) );

        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).containsView( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public final boolean containsLiteral( final CPath p_path )
    {
        p_path.normalize();
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return true;

        if ( p_path.size() == 1 )
            return m_beliefbase.getStorage().getMultiElements().containsKey( p_path.get( 0 ) );

        return walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).containsLiteral( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public final boolean remove( final ILiteral p_literal )
    {
        return p_literal.getFunctorPath().isEmpty() ? m_beliefbase.add( p_literal ) : walk( p_literal.getFunctorPath(), this, null ).remove( p_literal );
    }

    @Override
    public <T extends IAgent> void update( final T p_agent )
    {
        m_beliefbase.update( p_agent );
    }

    @Override
    public final boolean remove( final CPath... p_path )
    {
        if ( ( p_path == null ) || ( p_path.length == 0 ) )
            return false;

        return Arrays.stream( p_path ).parallel().map( i -> {
            i.normalize();
            return i.size() == 1 ? m_beliefbase.remove( i.getSuffix() ) : walk( i.getSubPath( 0, -1 ), this, null ).remove( i );
        } ).allMatch( i -> i );
    }

    @Override
    public IView clone( final IView p_parent )
    {
        return new CView( m_name, m_beliefbase, p_parent );
    }

    @Override
    @SuppressWarnings( {"serial", "unchecked"} )
    public final Set<ILiteral> getLiteral( final CPath... p_path )
    {
        final CPath l_path = this.getPath();
        if ( ( p_path == null ) || ( p_path.length == 0 ) )
            return new HashSet<ILiteral>()
            {{
                addAll( (Collection<? extends ILiteral>) m_beliefbase.getStorage().getMultiElements().values().parallelStream()
                                                                     .map( i -> i.getRight().clone( l_path ) ).collect( Collectors.toSet() ) );
            }};

        return Arrays.stream( p_path )
                     .parallel()
                     .map( i -> i.normalize() )
                     .flatMap( i -> walk( i.getSubPath( 0, -1 ), this, null ).getStorage().getMultiElements().get( i.getSuffix() ).stream()
                                                                             .map( j -> j.getRight() ) )
                     .collect( Collectors.toSet() );
    }

    @Override
    public final CPath getPath()
    {
        return getPath( this, new CPath() );
    }

    @Override
    public final String getName()
    {
        return m_name;
    }

    @Override
    public final IView getParent()
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
        return MessageFormat.format( "[name : {0}, fqn : {1}, storage : {2}]", m_name, this.getPath(), m_beliefbase.getStorage() );
    }

    /**
     * static method to generate FQN path
     *
     * @param p_view current path
     * @param p_path current path
     * @return path modified path
     */
    private static CPath getPath( final IView p_view, final CPath p_path )
    {
        p_path.pushfront( p_view.getName() );
        return !p_view.hasParent() ? p_path : getPath( p_view.getParent(), p_path );
    }

    /**
     * returns a view on the recursive descend
     *
     * @param p_path path (must be normalized)
     * @param p_root start / root node
     * @param p_generator generator object for new views
     * @return view
     *
     * @tparam Q literal type
     * @note path must be normalized
     */
    private static <Q> IView walk( final CPath p_path, final IView p_root, final IGenerator<Q> p_generator )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return p_root;

        // get the next view and if a generator exists and the view is null, generate a new view
        IView l_view = p_root.getStorage().getSingleElements().get( p_path.get( 0 ) );
        if ( ( l_view == null ) && ( p_generator != null ) )
            l_view = p_root.add( p_generator.createBeliefbase( p_path.get( 0 ) ) );

        // if view is null an exception is thrown
        if ( l_view == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( CView.class, "notfound", p_path.get( 0 ), p_path ) );

        // recursive descend
        return walk( p_path.getSubPath( 1 ), l_view, p_generator );
    }
}
