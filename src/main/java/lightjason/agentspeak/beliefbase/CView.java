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

package lightjason.agentspeak.beliefbase;


import lightjason.agentspeak.agent.IAgent;
import lightjason.agentspeak.common.CCommon;
import lightjason.agentspeak.common.CPath;
import lightjason.agentspeak.common.IPath;
import lightjason.agentspeak.error.CIllegalArgumentException;
import lightjason.agentspeak.language.ILiteral;
import lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;
import org.apache.commons.lang3.tuple.Pair;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * view of a beliefbase
 */
public final class CView implements IView
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
    public final Stream<ITrigger> getTrigger()
    {
        // remove the root element (position 0), because the root element
        // is not used on the agent (asl) side
        final IPath l_path = this.getPath().remove( 0 );
        return m_beliefbase.getTrigger( this ).map( i -> i.shallowcopy( l_path ) );
    }

    @Override
    public final boolean add( final ILiteral p_literal )
    {
        return this.add( p_literal, null );
    }

    @Override
    public final IView add( final IView p_view )
    {
        this.root()
            .filter( i -> p_view.getStorage().equals( i.getStorage() ) )
            .findAny()
            .ifPresent( i -> {
                throw new CIllegalArgumentException( CCommon.getLanguageString( this, "equal", p_view.getPath(), i.getPath() ) );
            } );

        return m_beliefbase.add( p_view.clone( this ) );
    }

    @Override
    public final void clear( final IPath... p_path )
    {
        if ( ( p_path == null ) || ( p_path.length == 0 ) )
            m_beliefbase.clear();
        else
            Arrays.stream( p_path ).parallel()
                  .forEach( i -> this.walk( i.normalize(), this, null ).clear() );
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
    public final IAgent update( final IAgent p_agent )
    {
        return m_beliefbase.update( p_agent );
    }

    @Override
    public final IView add( final IPath p_path, final IView p_view )
    {
        return this.add( p_path.normalize(), p_view, null );
    }

    @Override
    public final IView add( final IPath p_path, final IView p_view, final IGenerator p_generator
    )
    {
        return this.walk( p_path.normalize(), this, p_generator ).add( p_view );
    }

    @Override
    public final boolean add( final ILiteral p_literal, final IGenerator p_generator
    )
    {
        return p_literal.getFunctorPath().isEmpty()
               ? m_beliefbase.add( p_literal )
               : this.walk( p_literal.getFunctorPath(), this, p_generator ).add( p_literal.shallowcopySuffix() );
    }

    @Override
    public final boolean containsView( final IPath p_path )
    {
        p_path.normalize();
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return true;

        return p_path.size() == 1
               ? m_beliefbase.getStorage().getSingleElements().containsKey( p_path.get( 0 ) )
               : this.walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).containsView( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public final boolean containsLiteral( final IPath p_path )
    {
        p_path.normalize();
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return true;

        return p_path.size() == 1
               ? m_beliefbase.getStorage().getMultiElements().containsKey( p_path.get( 0 ) )
               : this.walk( p_path.getSubPath( 0, p_path.size() - 1 ), this, null ).containsLiteral( p_path.getSubPath( p_path.size() - 1, p_path.size() ) );
    }

    @Override
    public final boolean remove( final ILiteral p_literal )
    {
        return p_literal.getFunctorPath().isEmpty()
               ? m_beliefbase.remove( p_literal )
               : this.walk( p_literal.getFunctorPath(), this ).remove( p_literal.shallowcopySuffix() );
    }

    @Override
    public final boolean remove( final IPath... p_path )
    {
        if ( ( p_path == null ) || ( p_path.length == 0 ) )
            return false;

        return Arrays.stream( p_path ).parallel().map( i -> {
            i.normalize();
            return i.size() == 1 ? m_beliefbase.remove( i.getSuffix() ) : this.walk( i.getSubPath( 0, -1 ), this ).remove( i );
        } ).allMatch( i -> i );
    }

    @Override
    public final IView clone( final IView p_parent )
    {
        return new CView( m_name, m_beliefbase, p_parent );
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
                   m_beliefbase.getStorage().getMultiElements().values().parallelStream().map( i -> i.getRight().shallowcopy( l_path ) ),
                   m_beliefbase.getStorage().getSingleElements().values().parallelStream().flatMap( i -> i.parallelStream()
                                                                                                          .map( j -> j.shallowcopy( l_path ) )
                   )
               )

               :
               Arrays.stream( p_path )
                     .parallel()
                     .map( i -> i.normalize() )
                     .flatMap( i -> this.walk( i.getSubPath( 0, -1 ), this, null ).getStorage().getMultiElements().get( i.getSuffix() )
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

               ?
               Stream.concat(
                   m_beliefbase.getStorage().getMultiElements().values().parallelStream()
                               .filter( i -> i.getLeft() == p_negated )
                               .map( i -> i.getRight().shallowcopy( l_path ) ),
                   m_beliefbase.getStorage().getSingleElements().values().parallelStream().flatMap( i -> i.parallelStream( p_negated )
                                                                                                          .map( j -> j.shallowcopy( l_path ) )
                   )
               )

               :
               Arrays.stream( p_path )
                     .parallel()
                     .map( i -> i.normalize() )
                     .flatMap( i -> this.walk( i.getSubPath( 0, -1 ), this, null ).getStorage().getMultiElements().get( i.getSuffix() )
                                        .parallelStream()
                                        .filter( j -> j.getLeft() == p_negated )
                                        .map( j -> j.getRight().shallowcopy( l_path ) ) );
    }

    @Override
    public final IPath getPath()
    {
        final IPath l_path = new CPath();
        this.root().forEach( i -> l_path.pushfront( i.getName() ) );
        return l_path;
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
        return MessageFormat.format( "{0} [name : {1}, fqn : {2}, storage : {3}]", super.toString(), m_name, this.getPath(), m_beliefbase.getStorage() );
    }

    /**
     * stream to root
     *
     * @return stream to root
     */
    public final Stream<IView> root()
    {
        return Stream.concat(
            Stream.of( this ),
            Stream.of( this.getParent() ).filter( i -> i != null )
        );
    }

    /**
     * returns a view on the recursive descend
     *
     * @param p_path path (must be normalized)
     * @param p_root start / root node
     * @param p_generator generator object for new views
     * @return view
     *
     * @note path must be normalized
     */
    protected final synchronized IView walk( final IPath p_path, final IView p_root, final IGenerator p_generator )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return p_root;

        // get the next view and if the view is null, generate a new view
        IView l_view = p_root.getStorage().getSingleElements().get( p_path.get( 0 ) );
        if ( l_view == null )
            l_view = p_root.add( p_generator.generate( p_path.get( 0 ) ) );

        // if view is null an exception is thrown
        if ( l_view == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( CView.class, "notfound", p_path.get( 0 ), p_root.getPath() ) );

        return this.walk( p_path.getSubPath( 1 ), l_view, p_generator );
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
    protected final IView walk( final IPath p_path, final IView p_root )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return p_root;

        // if view is null an exception is thrown
        final IView l_view = p_root.getStorage().getSingleElements().get( p_path.get( 0 ) );
        if ( l_view == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( CView.class, "notfound", p_path.get( 0 ), p_root.getPath() ) );

        return this.walk( p_path.getSubPath( 1 ), l_view );
    }
}