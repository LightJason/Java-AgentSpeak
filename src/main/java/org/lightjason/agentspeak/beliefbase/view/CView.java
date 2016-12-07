/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.beliefbase.view;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.IBeliefbase;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
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
    private final String m_name;
    /**
     * reference to the beliefbase context
     */
    private final IBeliefbase<T> m_beliefbase;
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
    public CView( final String p_name, final IBeliefbase<T> p_beliefbase )
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
    @SuppressWarnings( "unchecked" )
    public CView( final String p_name, final IBeliefbase<T> p_beliefbase, final IView<T> p_parent )
    {
        if ( ( p_name == null ) || ( p_name.isEmpty() ) )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "empty" ) );
        if ( p_beliefbase == null )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "beliefbaseempty" ) );

        m_name = p_name;
        m_beliefbase = p_beliefbase;
        m_parent = p_parent;
    }



    // --- agent operations ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Stream<ITrigger> trigger()
    {
        // remove the root element (position 0), because the root element
        // is not used on the agent (asl) side
        final IPath l_path = this.path().remove( 0 );
        return m_beliefbase.trigger( this ).map( i -> i.shallowcopy( l_path ) );
    }

    @Override
    public final T update( final T p_agent )
    {
        return m_beliefbase.update( p_agent );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------



    // --- operations ------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final IView<T> add( final Stream<ILiteral> p_literal )
    {
        p_literal.parallel()
            .forEach( i -> this.leafview( this.walk( i.functorpath() ) ).beliefbase().add( i.shallowcopysuffix() ) );
        return this;
    }

    @Override
    public final IView<T> add( final ILiteral... p_literal )
    {
        return this.add( Arrays.stream( p_literal ) );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final IView<T> add( final IPath p_path, final IView<T>... p_view )
    {
        Arrays.stream( p_view )
              .parallel()
              .forEach( i -> this.leafview( this.walk( p_path ) ).add( i ) );
        return this;
    }

    @Override
    @SafeVarargs
    public final IView<T> add( final IView<T>... p_view )
    {
        Arrays.stream( p_view )
              .parallel()
              .forEach( i ->
              {
                  this.root()
                      .filter( j -> i.beliefbase().equals( this.beliefbase() ) )
                      .findAny()
                      .ifPresent( j -> {
                          throw new CIllegalArgumentException( CCommon.languagestring( this, "equal", i.path(), j.path() ) );
                      } );
                  m_beliefbase.add( i );
              } );
        return this;
    }

    @Override
    public final IView<T> remove( final IView<T> p_view )
    {
        m_beliefbase.remove( p_view );
        return this;
    }

    @Override
    public final IView<T> remove( final Stream<ILiteral> p_literal )
    {
        p_literal.parallel()
            .forEach( i -> this.leafview( this.walk( i.functorpath() ) ).beliefbase().remove( i.shallowcopysuffix() ) );
        return this;
    }

    @Override
    public final IView<T> remove( final ILiteral... p_literal )
    {
        return this.remove( Arrays.stream( p_literal ) );
    }

    @Override
    public final IView<T> clear( final IPath... p_path )
    {
        if ( ( p_path == null ) || ( p_path.length == 0 ) )
            m_beliefbase.clear();
        else
            Arrays.stream( p_path ).parallel()
                  .forEach( i -> this.leafview( this.walk( i ) ).clear() );

        return this;
    }

    @Override
    public final boolean containsView( final IPath p_path )
    {
        return p_path.isEmpty()
               || ( p_path.size() == 1
                    ? m_beliefbase.containsView( p_path.get( 0 ) )
                    : this.leafview( this.walk( p_path.getSubPath( 0, p_path.size() - 1 ) ) )
                        .containsView( p_path.getSubPath( p_path.size() - 1, p_path.size() ) )
               );
    }

    @Override
    public final boolean containsLiteral( final IPath p_path )
    {
        return p_path.isEmpty()
               || ( p_path.size() == 1
                    ? m_beliefbase.containsLiteral( p_path.get( 0 ) )
                    : this.leafview( this.walk( p_path.getSubPath( 0, p_path.size() - 1 ) ) )
                        .containsLiteral( p_path.getSubPath( p_path.size() - 1, p_path.size() ) )
               );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------




    // --- streaming access ------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final Stream<ILiteral> stream( final IPath... p_path )
    {
        // build path relative to this view
        final IPath l_path = this.path().getSubPath( 1 );
        return ( p_path == null ) || ( p_path.length == 0 )

               ?
               Stream.concat(
                   m_beliefbase.streamLiteral().parallel().map( i -> i.shallowcopy( l_path ) ),
                   m_beliefbase.streamView().parallel().flatMap( i -> i.stream().parallel().map( j -> j.shallowcopy( l_path ) )
                   )
               )

               :
               Arrays.stream( p_path )
                     .parallel()
                     .flatMap( i -> this.leafview( this.walk( i.getSubPath( 0, -1 ) ) ).beliefbase().literal( i.getSuffix() )
                                        .parallelStream()
                                        .map( j -> j.shallowcopy( l_path ) ) );
    }

    @Override
    public final Stream<ILiteral> stream( final boolean p_negated, final IPath... p_path )
    {
        // build path relative to this view
        final IPath l_path = this.path().getSubPath( 1 );
        return ( p_path == null ) || ( p_path.length == 0 )

               ? Stream.concat(
                    m_beliefbase.streamLiteral().parallel()
                        .filter( i -> i.negated() == p_negated )
                        .map( i -> i.shallowcopy( l_path ) ),
                    m_beliefbase.streamView().parallel().flatMap( i -> i.stream( p_negated ).parallel().map( j -> j.shallowcopy( l_path ) ) )
               )

               : Arrays.stream( p_path )
                    .parallel()
                    .flatMap( i -> this.leafview( this.walk( i.getSubPath( 0, -1 ) ) ).beliefbase().literal( i.getSuffix() )
                                       .parallelStream()
                                       .filter( j -> j.negated() == p_negated )
                                       .map( j -> j.shallowcopy( l_path ) ) );
    }

    @Override
    @SafeVarargs
    public final Stream<IView<T>> walk( final IPath p_path, final IViewGenerator<T>... p_generator )
    {
        return this.walkdown( p_path, p_generator );
    }

    @Override
    public final IView<T> generate( final IViewGenerator<T> p_generator, final IPath... p_paths )
    {
        Arrays.stream( p_paths )
            .parallel()
            .forEach( i -> this.walk( i, p_generator ) );
        return this;
    }

    /**
     * inner walking structure of views
     *
     * @param p_path path
     * @param p_generator generator (first argument is used, other elements will be ignored)
     * @return view stream
     */
    @SafeVarargs
    private final Stream<IView<T>> walkdown( final IPath p_path, final IViewGenerator<T>... p_generator )
    {
        if ( ( p_path == null ) || ( p_path.isEmpty() ) )
            return Stream.of( this );

        final IView<T> l_view;
        final String l_root = p_path.get( 0 );
        synchronized ( this )
        {
            // add is run here for avoid overwriting view with a new object reference
            l_view = m_beliefbase.add(
                        m_beliefbase.viewOrDefault(
                            l_root,

                            p_generator == null || p_generator.length == 0
                            ? null
                            : p_generator[0].apply( l_root, this )
                        )
            );
        }
        if ( l_view == null )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "notfound", l_root, this.path() ) );

        return Stream.concat(
            Stream.of( this ),
            l_view.walk( p_path.getSubPath( 1 ), p_generator )
        );
    }

    /**
     * returns the leaf of a view path
     *
     * @param p_stream stream of views
     * @return last / leaf view
     */
    private IView<T> leafview( final Stream<IView<T>> p_stream )
    {
        return p_stream
            .reduce( ( i, j ) -> j )
            .orElse( this );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------



    // --- basic access ----------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public final IBeliefbase<T> beliefbase()
    {
        return m_beliefbase;
    }

    @Override
    public final boolean empty()
    {
        return m_beliefbase.empty();
    }

    @Override
    public final int size()
    {
        return m_beliefbase.size();
    }

    @Override
    public final Stream<IView<T>> root()
    {
        return Stream.concat(
            Stream.of( this ),
            Stream.of( this.parent() ).filter( Objects::nonNull )
        );
    }

    @Override
    public final String name()
    {
        return m_name;
    }

    @Override
    public final IPath path()
    {
        final IPath l_path = new CPath();
        this.root().forEach( i -> l_path.pushfront( i.name() ) );
        return l_path;
    }

    @Override
    public final IView<T> parent()
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
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IView<?> ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} ({1}): [{2}]", this.name(), super.toString(), m_beliefbase );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
