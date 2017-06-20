/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * view which can use a map of maps to represent
 * the hierarchical beliefbase structure
 *
 * @note trigger of beliefs are not generated
 * @tparam T agent type
 */
public final class CViewMap implements IView
{
    /**
     * view name
     */
    private final String m_name;
    /**
     * parent name
     */
    private final IView m_parent;
    /**
     * root map
     */
    private final Map<String, Object> m_data;
    /**
     * path to key converting
     */
    private final Function<String, String> m_pathtokey;
    /**
     * key to literal converting
     */
    private final Function<String, String> m_keytoliteral;
    /**
     * clear consumer
     */
    private final Consumer<Map<String, Object>> m_clearconsumer;
    /**
     * add-view consumer
     */
    private final BiConsumer<Stream<IView>, Map<String, Object>> m_addviewconsumer;
    /**
     * add-literal consumer
     */
    private final BiConsumer<Stream<ILiteral>, Map<String, Object>> m_addliteralconsumer;
    /**
     * remove-view consumer
     */
    private final BiConsumer<Stream<IView>, Map<String, Object>> m_removeviewconsumer;
    /**
     * remove-literal consumer
     */
    private final BiConsumer<Stream<ILiteral>, Map<String, Object>> m_removeliteralconsumer;

    /**
     * ctor
     *
     * @param p_name view name
     * @param p_parent parent view
     * @param p_root map reference map reference
     * @param p_addviewconsumer add-view consumer
     * @param p_addliteralconsumer add-literal consumer
     */
    public CViewMap( @Nonnull final String p_name, @Nullable final IView p_parent, @Nonnull final Map<String, Object> p_root,
                     @Nonnull final BiConsumer<Stream<IView>, Map<String, Object>> p_addviewconsumer,
                     @Nonnull final BiConsumer<Stream<ILiteral>, Map<String, Object>> p_addliteralconsumer,
                     @Nonnull final BiConsumer<Stream<IView>, Map<String, Object>> p_removeviewconsumer,
                     @Nonnull final BiConsumer<Stream<ILiteral>, Map<String, Object>> p_removeliteralconsumer,
                     @Nonnull final Consumer<Map<String, Object>> p_clearconsumer,
                     @Nonnull final Function<String, String> p_pathtokey, @Nonnull final Function<String, String> p_keytoliteral
    )
    {
        m_name = p_name;
        m_parent = p_parent;
        m_data = p_root;
        m_pathtokey = p_pathtokey;
        m_keytoliteral = p_keytoliteral;
        m_clearconsumer = p_clearconsumer;
        m_addviewconsumer = p_addviewconsumer;
        m_addliteralconsumer = p_addliteralconsumer;
        m_removeviewconsumer = p_removeviewconsumer;
        m_removeliteralconsumer = p_removeliteralconsumer;
    }

    @Nonnull
    @Override
    public final Stream<IView> walk( @Nonnull final IPath p_path, @Nullable final IViewGenerator... p_generator )
    {
        return this.walkdown( p_path, p_generator );
    }

    @Nonnull
    @Override
    public IView generate( @Nonnull final IViewGenerator p_generator, @Nonnull final IPath... p_paths )
    {
        return this;
    }

    @Nonnull
    @Override
    public final Stream<IView> root()
    {
        return Stream.concat(
            Stream.of( this ),
            Stream.of( this.parent() ).filter( Objects::nonNull )
        );
    }

    @Nonnull
    @Override
    public final IBeliefbase beliefbase()
    {
        return IBeliefbase.EMPY;
    }

    @Nonnull
    @Override
    public final IPath path()
    {
        final IPath l_path = new CPath();
        this.root().forEach( i -> l_path.pushfront( i.name() ) );
        return l_path;
    }

    @Nonnull
    @Override
    public final String name()
    {
        return m_name;
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

    @Nonnull
    @Override
    public final Stream<ITrigger> trigger()
    {
        return Stream.empty();
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public final Stream<ILiteral> stream( @Nullable final IPath... p_path )
    {
        return ( p_path == null ) || ( p_path.length == 0 )
               ? m_data.entrySet().stream()
                                  .filter( i -> !( i instanceof Map<?, ?> ) )
                                  .map( i -> CLiteral.from( m_keytoliteral.apply( i.getKey() ), this.toterm( i.getValue() ) ) )
               : Arrays.stream( p_path ).flatMap( i -> this.walkdown( i ).flatMap( j -> j.stream() ) );
    }

    @Nonnull
    @Override
    public final Stream<ILiteral> stream( final boolean p_negated, @Nullable final IPath... p_path )
    {
        return p_negated ? Stream.empty() : this.stream( p_path );
    }

    @Nonnull
    @Override
    public final IView clear( @Nullable final IPath... p_path )
    {
        //if ( ( p_path == null ) || ( p_path.length == 0 ) )
        //    m_clearconsumer.

        return this;
    }

    @Nonnull
    @Override
    public final IView add( @Nonnull final Stream<ILiteral> p_literal )
    {
        m_addliteralconsumer.accept( p_literal, m_data );
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
        m_addviewconsumer.accept( Arrays.stream( p_view ), m_data );
        return this;
    }

    @Nonnull
    @Override
    public IView remove( @Nonnull final Stream<ILiteral> p_literal )
    {
        m_removeliteralconsumer.accept( p_literal, m_data );
        return this;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "varargs" )
    public IView remove( @Nonnull final ILiteral... p_literal )
    {
        return this.remove( Arrays.stream( p_literal ) );
    }

    @Nonnull
    @Override
    @SuppressWarnings( "varargs" )
    public final IView remove( @Nonnull final IView... p_view )
    {
        m_removeviewconsumer.accept( Arrays.stream( p_view ), m_data );
        return this;
    }

    @Override
    public boolean containsLiteral( @Nonnull final IPath p_path )
    {
        return false;
    }

    @Override
    public final boolean containsView( @Nonnull final IPath p_path )
    {
        return false;
    }

    @Override
    public final boolean empty()
    {
        return m_data.isEmpty();
    }

    @Override
    public final int size()
    {
        return 0;
    }

    @Nonnull
    @Override
    public final IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        return p_agent;
    }

    @SuppressWarnings( "unchecked" )
    private Stream<IView> walkdown( final IPath p_path, @Nullable final IViewGenerator... p_generator )
    {
        if ( p_path.isEmpty() )
            return Stream.of( this );

        final String l_key = m_pathtokey.apply( p_path.get( 0 ) );
        final Object l_data = m_data.get( l_key );
        return l_data instanceof Map<?, ?>
               ? Stream.concat(
            Stream.of( this ),
            new CViewMap( l_key, this, (Map<String, Object>) l_data,
                          m_addviewconsumer, m_addliteralconsumer, m_removeviewconsumer, m_removeliteralconsumer, m_clearconsumer, m_pathtokey, m_keytoliteral
            ).walk( p_path.getSubPath( 1 ), p_generator )
        )
               : Stream.of( this );
    }

    @SuppressWarnings( "unchecked" )
    private Stream<ITerm> toterm( final Object p_value )
    {
        if ( p_value instanceof Collection<?> )
            return ( (Collection<Object>) p_value ).stream().flatMap( i -> this.toterm( i ) );

        if ( p_value instanceof Map<?, ?> )
            return ( (Map<String, Object>) p_value ).entrySet().stream()
                                                    .map( i -> CLiteral.from( m_keytoliteral.apply( i.getKey() ), this.toterm( i.getValue() ) ) );

        if ( p_value instanceof Integer )
            return Stream.of( CRawTerm.from( ( (Number) p_value ).longValue() ) );

        return Stream.of( CRawTerm.from( p_value ) );
    }
}
