/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp@lightjason.org)                      #
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

package org.lightjason.agentspeak.beliefbase;


import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * view of a beliefbase
 *
 * @tparam T agent type
 */
public final class CView<T extends IAgent<?>> implements IView<T>
{
    /**
     * view name
     */
    protected final String m_name;
    /**
     * reference to the beliefbase context
     */
    protected final IBeliefBase<T> m_beliefbase;
    /**
     * parent name
     */
    private final IView<T> m_parent;

    /**
     * ctor
     *
     * @param p_name view name
     * @param p_beliefbase reference to the beliefbase context
     */
    public CView( final String p_name, final IBeliefBase<T> p_beliefbase )
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
    public CView( final String p_name, final IBeliefBase<T> p_beliefbase, final IView<T> p_parent )
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
    public final Stream<ITrigger> getTrigger()
    {
        // remove the root element (position 0), because the root element
        // is not used on the agent (asl) side
        final IPath l_path = this.getPath().remove( 0 );
        return m_beliefbase.getTrigger( this ).map( i -> i.shallowcopy( l_path ) );
    }

    @Override
    public final IView<T> generate( final IPath p_path, final IViewGenerator<T> p_generator )
    {
        this.walkgenerate( p_path.normalize(), this, p_generator );
        return this;
    }

    @Override
    public final IView<T> add( final ILiteral p_literal )
    {
        this.walk( p_literal.getFunctorPath().normalize(), this )
            .getBeliefbase()
            .add( p_literal.shallowcopySuffix() );
        return this;
    }

    @Override
    public final IView<T> add( final IPath p_path, final IView<T> p_view )
    {
        this.walk( p_path.normalize(), this ).add( p_view );
        return this;
    }

    @Override
    public final IView<T> add( final IView<T> p_view )
    {
        this.root()
            .filter( i -> p_view.getStorage().equals( i.getStorage() ) )
            .findAny()
            .ifPresent( i -> {
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "equal", p_view.getPath(), i.getPath() ) );
            } );

        m_beliefbase.add( p_view.clone( this ) );
        return this;
    }

    @Override
    public final IView<T> remove( final IView<T> p_view )
    {
        m_beliefbase.remove( p_view );
        return this;
    }

    @Override
    public final IView<T> remove( final ILiteral p_literal )
    {
        this.walk( p_literal.getFunctorPath().normalize(), this )
            .getBeliefbase()
            .remove( p_literal.shallowcopySuffix() );
        return this;
    }

    @Override
    public final IView<T> remove( final IPath... p_path )
    {
        if ( ( p_path != null ) && ( p_path.length > 0 ) )
            Arrays.stream( p_path ).parallel()
                  .map( IPath::normalize )
                  .forEach( i -> {
                      if ( i.size() == 1 )
                        m_beliefbase.remove( i.getSuffix() );
                      else
                        this.walk( i.getSubPath( 0, -1 ), this ).remove( i );
                  } );

        return this;
    }

    @Override
    public final IView<T> clear( final IPath... p_path )
    {
        if ( ( p_path == null ) || ( p_path.length == 0 ) )
            m_beliefbase.clear();
        else
            Arrays.stream( p_path ).parallel()
                  .forEach( i -> this.walk( i.normalize(), this ).clear() );

        return this;
    }

    @Override
    public final IView<T> create( final String p_name )
    {
        return m_beliefbase.create( p_name );
    }

    @Override
    public final IView<T> clone( final IView<T> p_parent )
    {
        return new CView<>( m_name, m_beliefbase, p_parent );
    }

    @Override
    public final T update( final T p_agent )
    {
        return m_beliefbase.update( p_agent );
    }

    @Override
    public final boolean containsview( final IPath p_path )
    {
        p_path.normalize();
        return p_path.isEmpty() || ( p_path.size() == 1
               ? m_beliefbase.getStorage().containsSingleElementKey( p_path.get( 0 ) )
               : this.walk( p_path.getSubPath( 0, p_path.size() - 1 ), this ).containsview( p_path.getSubPath( p_path.size() - 1, p_path.size() ) ) );
    }

    @Override
    public final boolean containsliteral( final IPath p_path )
    {
        p_path.normalize();
        if ( p_path.isEmpty() )
            return true;

        return p_path.size() == 1
               ? m_beliefbase.getStorage().containsMultiElementKey( p_path.get( 0 ) )
               : this.walk( p_path.getSubPath( 0, p_path.size() - 1 ), this ).containsliteral( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public final Stream<ILiteral> parallelStream( final IPath... p_path )
    {
        return this.stream( p_path ).parallel();
    }

    @Override
    public final Stream<ILiteral> stream( final IPath... p_path )
    {
        // build path relative to this view
        final IPath l_path = this.getPath().getSubPath( 1 );
        return ( p_path == null ) || ( p_path.length == 0 )

               ?
               Stream.concat(
                   m_beliefbase.streamLiteral().parallel().map( i -> i.shallowcopy( l_path ) ),
                   m_beliefbase.streamView().parallel().flatMap( i -> i.parallelStream().map( j -> j.shallowcopy( l_path ) )
                   )
               )

               :
               Arrays.stream( p_path )
                     .parallel()
                     .map( IPath::normalize )
                     .flatMap( i -> this.walk( i.getSubPath( 0, -1 ), this ).getBeliefbase().getStorage().getMultiElements().get( i.getSuffix() )
                                        .parallelStream()
                                        .map( j -> j.getRight().shallowcopy( l_path ) ) );
    }

    @Override
    public final Stream<ILiteral> parallelStream( final boolean p_negated, final IPath... p_path )
    {
        return this.stream( p_negated, p_path ).parallel();
    }

    @Override
    public final Stream<ILiteral> stream( final boolean p_negated, final IPath... p_path )
    {
        // build path relative to this view
        final IPath l_path = this.getPath().getSubPath( 1 );
        return ( p_path == null ) || ( p_path.length == 0 )

               ? Stream.concat(
                    m_beliefbase.streamLiteral().parallel()
                        .filter( i -> i.isNegated() == p_negated )
                        .map( i -> i.shallowcopy( l_path ) ),
                    m_beliefbase.streamView().parallel().flatMap( i -> i.parallelStream( p_negated ).map( j -> j.shallowcopy( l_path ) ) )
               )

               : Arrays.stream( p_path )
                    .parallel()
                    .map( IPath::normalize )
                    .flatMap( i -> this.walk( i.getSubPath( 0, -1 ), this ).getStorage().getMultiElements().get( i.getSuffix() )
                                        .parallelStream()
                                        .filter( j -> j.getLeft() == p_negated )
                                        .map( j -> j.getRight().shallowcopy( l_path ) ) );
    }

    @Override
    public final IBeliefBase<T> getBeliefbase()
    {
        return m_beliefbase;
    }

    @Override
    public final <L extends IStorage<Pair<Boolean, ILiteral>, IView<T>, T>> L getStorage()
    {
        return m_beliefbase.getStorage();
    }

    @Override
    public final boolean isEmpty()
    {
        return m_beliefbase.isEmpty();
    }

    @Override
    public final int size()
    {
        return m_beliefbase.size();
    }

    /**
     * stream to root
     *
     * @return stream to root
     */
    public final Stream<IView<T>> root()
    {
        return Stream.concat(
            Stream.of( this ),
            Stream.of( this.getParent() ).filter( i -> i != null )
        );
    }

    @Override
    public final String getName()
    {
        return m_name;
    }

    @Override
    public final IPath getPath()
    {
        final IPath l_path = new CPath();
        this.root().forEach( i -> l_path.pushfront( i.getName() ) );
        return l_path;
    }

    @Override
    public final IView<T> getParent()
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
        return MessageFormat.format( "{0} [name : {1}, fqn : {2}, storage : {3}]", super.toString(), m_name, this.getPath(), m_beliefbase.getStorage() );
    }

    /**
     * returns a view on the recursive descend
     *
     * @param p_path path (must be normalized)
     * @param p_root start / root node
     * @param p_generator generator object for new views
     *
     * @note path must be normalized
     */
    private synchronized void walkgenerate( final IPath p_path, final IView<T> p_root, final IViewGenerator<T> p_generator )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return;

        // get the next view and if the view is null, generate a new view
        final IView<T> l_view = p_root.getStorage().getSingleElements().getOrDefault( p_path.get( 0 ), p_generator.generate( p_path.get( 0 ) ) );
        p_root.getStorage().getSingleElements().put( l_view.getName(), l_view.clone( p_root ) );

        this.walkgenerate( p_path.getSubPath( 1 ), l_view, p_generator );
    }

    /**
     * returns a view on the recursive descend
     *
     * @param p_path path (must be normalized)
     * @param p_root start / root node
     * @return view
     *
     * @note path must be normalized
     */
    private IView<T> walk( final IPath p_path, final IView<T> p_root )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return p_root;

        // if view is null an exception is thrown
        final IView<T> l_view = p_root.getStorage().getSingleElements().get( p_path.get( 0 ) );
        if ( l_view == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "notfound", p_path.get( 0 ), p_root.getPath() ) );

        return this.walk( p_path.getSubPath( 1 ), l_view );
    }
}
