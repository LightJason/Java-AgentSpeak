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

package org.lightjason.agentspeak.beliefbase.view;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.IBeliefbase;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * view of a beliefbase
 *
 * @tparam T agent type
 */
public final class CView implements IView
{
    /**
     * view name
     */
    private final String m_name;
    /**
     * reference to the beliefbase context
     */
    private final IBeliefbase m_beliefbase;
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
    public CView( @Nonnull final String p_name, @Nonnull final IBeliefbase p_beliefbase )
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
    public CView( @Nonnull final String p_name, @Nonnull final IBeliefbase p_beliefbase, final IView p_parent )
    {
        if ( p_name.isEmpty() )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "empty" ) );

        m_name = p_name;
        m_beliefbase = p_beliefbase;
        m_parent = p_parent;
    }



    // --- agent operations ------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public final Stream<ITrigger> trigger()
    {
        // remove the root element (position 0), because the root element
        // is not used on the agent (asl) side
        final IPath l_path = this.path().remove( 0 );
        return m_beliefbase.trigger( this ).map( i -> i.shallowcopy( l_path ) );
    }

    @Nonnull
    @Override
    public final IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return m_beliefbase.update( p_agent );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------



    // --- operations ------------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public final IView add( @Nonnull final Stream<ILiteral> p_literal )
    {
        p_literal.parallel()
                 .forEach( i -> this.leafview( this.walk( i.functorpath() ) ).beliefbase().add( i.shallowcopysuffix() ) );
        return this;
    }

    @Nonnull
    @Override
    public final IView add( @Nonnull final ILiteral... p_literal )
    {
        return this.add( Arrays.stream( p_literal ) );
    }

    @Nonnull
    @Override
    @SuppressWarnings( "varargs" )
    public final IView add( @Nonnull final IView... p_view )
    {
        Arrays.stream( p_view )
              .parallel()
              .forEach( i ->
              {
                  this.root()
                      .filter( j -> i.beliefbase().equals( this.beliefbase() ) )
                      .findAny()
                      .ifPresent( j ->
                      {
                          throw new CIllegalArgumentException( CCommon.languagestring( this, "equal", i.path(), j.path() ) );
                      } );
                  m_beliefbase.add( i );
              } );
        return this;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "varargs" )
    public final IView remove( @Nonnull final IView... p_view )
    {
        Arrays.stream( p_view ).forEach( m_beliefbase::remove );
        return this;
    }

    @Nonnull
    @Override
    public final IView remove( @Nonnull final Stream<ILiteral> p_literal )
    {
        p_literal.parallel()
            .forEach( i -> this.leafview( this.walk( i.functorpath() ) ).beliefbase().remove( i.shallowcopysuffix() ) );
        return this;
    }

    @Nonnull
    @Override
    public final IView remove( @Nonnull final ILiteral... p_literal )
    {
        return this.remove( Arrays.stream( p_literal ) );
    }

    @Nonnull
    @Override
    public final IView clear( @Nullable final IPath... p_path )
    {
        if ( ( Objects.isNull( p_path ) ) || ( p_path.length == 0 ) )
            m_beliefbase.clear();
        else
            Arrays.stream( p_path ).parallel()
                  .forEach( i -> this.leafview( this.walk( i ) ).clear() );

        return this;
    }

    @Override
    public final boolean containsView( @Nonnull final IPath p_path )
    {
        return !p_path.empty()
               && ( p_path.size() == 1
                    ? m_beliefbase.containsView( p_path.get( 0 ) )
                    : this.leafview( this.walk( p_path.subpath( 0, p_path.size() - 1 ) ) )
                        .containsView( p_path.subpath( p_path.size() - 1, p_path.size() ) )
               );
    }

    @Override
    public final boolean containsLiteral( @Nonnull final IPath p_path )
    {
        return !p_path.empty()
               || ( p_path.size() == 1
                    ? m_beliefbase.containsLiteral( p_path.get( 0 ) )
                    : this.leafview( this.walk( p_path.subpath( 0, p_path.size() - 1 ) ) )
                          .containsLiteral( p_path.subpath( p_path.size() - 1, p_path.size() ) )
               );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------




    // --- streaming access ------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public final Stream<ILiteral> stream( @Nullable final IPath... p_path )
    {
        // build path relative to this view
        final IPath l_path = this.path();
        return ( ( Objects.isNull( p_path ) ) || ( p_path.length == 0 )
               ? Stream.concat( m_beliefbase.streamLiteral(), m_beliefbase.streamView().flatMap( i -> i.stream() ) )
               : Arrays.stream( p_path )
                       .flatMap( i -> this.leafview( this.walk( i.subpath( 0, -1 ) ) ).beliefbase().literal( i.suffix() ).stream() )
        ).map( i -> i.shallowcopy( l_path ) );
    }

    @Nonnull
    @Override
    public final Stream<ILiteral> stream( final boolean p_negated, @Nullable final IPath... p_path )
    {
        // build path relative to this view
        final IPath l_path = this.path();
        return ( ( Objects.isNull( p_path ) ) || ( p_path.length == 0 )
               ? Stream.concat(
                   m_beliefbase.streamLiteral().filter( i -> i.negated() == p_negated ),
                   m_beliefbase.streamView().flatMap( i -> i.stream( p_negated ) )
               )
               : Arrays.stream( p_path )
                       .flatMap( i -> this.leafview( this.walk( i.subpath( 0, -1 ) ) ).beliefbase().literal( i.suffix() ).stream() )
                       .filter( j -> j.negated() == p_negated )
        ).map( i -> i.shallowcopy( l_path ) );
    }

    @Nonnull
    @Override
    public final Stream<IView> walk( @Nonnull final IPath p_path, @Nullable final IViewGenerator... p_generator )
    {
        return this.walkdown( p_path, p_generator );
    }

    @Nonnull
    @Override
    public final IView generate( @Nonnull final IViewGenerator p_generator, @Nonnull final IPath... p_paths )
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
    @SuppressWarnings( "varargs" )
    private Stream<IView> walkdown( @Nonnull final IPath p_path, @Nullable final IViewGenerator... p_generator )
    {
        if ( p_path.empty() )
            return Stream.of( this );

        final IView l_view;
        final String l_root = p_path.get( 0 );

        synchronized ( this )
        {
            // add is run here for avoid overwriting view with a new object reference
            l_view = m_beliefbase.viewOrDefault(
                        l_root,

                        Objects.isNull( p_generator ) || p_generator.length == 0
                        ? null
                        : p_generator[0].apply( l_root, this )
                     );

            if ( Objects.isNull( l_view ) )
                return Stream.empty();
            m_beliefbase.add( l_view );
        }

        return Stream.concat(
            Stream.of( this ),
            l_view.walk( p_path.subpath( 1 ), p_generator )
        );
    }

    /**
     * returns the leaf of a view path
     *
     * @param p_stream stream of views
     * @return last / leaf view
     */
    @Nonnull
    private IView leafview( @Nonnull final Stream<IView> p_stream )
    {
        return p_stream
            .reduce( ( i, j ) -> j )
            .orElse( IView.EMPTY );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------



    // --- basic access ----------------------------------------------------------------------------------------------------------------------------------------

    @Nonnull
    @Override
    public final IBeliefbase beliefbase()
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

    @Nonnull
    @Override
    public final Stream<IView> root()
    {
        return this.hasParent()
               ? Stream.concat( Stream.of( this ), Stream.of( this.parent() ).flatMap( IView::root ) )
               : Stream.empty();
    }

    @Nonnull
    @Override
    public final String name()
    {
        return m_name;
    }

    @Nonnull
    @Override
    public final IPath path()
    {
        return this.root().map( IView::name ).collect( CPath.collect() ).reverse();
    }

    @Nullable
    @Override
    public final IView parent()
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
        return m_name.hashCode() ^ m_beliefbase.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object instanceof IView ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0} ({1}): [{2}]", this.name(), super.toString(), m_beliefbase );
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

}
