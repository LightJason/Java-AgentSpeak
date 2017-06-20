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
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Stream;


/**
 * view which can use a map of maps to represent
 * the hierarchical beliefbase structure
 *
 * @note trigger of beliefs are not generated
 * @tparam T agent type
 */
public final class CViewMap<T extends IAgent<?>> implements IView<T>
{
    /**
     * view name
     */
    private final String m_name;
    /**
     * parent name
     */
    private final IView<T> m_parent;
    /**
     * root map
     */
    private final Map<String, Object> m_data;
    /**
     * add-view consumer
     */
    private final BiConsumer<Stream<IView<T>>, Map<String, ?>> m_addviewconsumer;
    /**
     * add-literal consumer
     */
    private final BiConsumer<Stream<ILiteral>, Map<String, ?>> m_addliteralconsumer;
    /**
     * remove-view consumer
     */
    private final BiConsumer<Stream<IView<T>>, Map<String, ?>> m_removeviewconsumer;
    /**
     * remove-literal consumer
     */
    private final BiConsumer<Stream<ILiteral>, Map<String, ?>> m_removeliteralconsumer;

    /**
     * ctor
     *
     * @param p_name view name
     * @param p_parent parent view
     * @param p_root map reference map reference
     * @param p_addviewconsumer add-view consumer
     * @param p_addliteralconsumer add-literal consumer
     */
    public CViewMap( @Nonnull final String p_name, @Nullable final IView<T> p_parent, @Nonnull final Map<String, Object> p_root,
                     @Nonnull final BiConsumer<Stream<IView<T>>, Map<String, ?>> p_addviewconsumer,
                     @Nonnull final BiConsumer<Stream<ILiteral>, Map<String, ?>> p_addliteralconsumer,
                     @Nonnull final BiConsumer<Stream<IView<T>>, Map<String, ?>> p_removeviewconsumer,
                     @Nonnull final BiConsumer<Stream<ILiteral>, Map<String, ?>> p_removeliteralconsumer
    )
    {
        m_name = p_name;
        m_parent = p_parent;
        m_data = p_root;
        m_addviewconsumer = p_addviewconsumer;
        m_addliteralconsumer = p_addliteralconsumer;
        m_removeviewconsumer = p_removeviewconsumer;
        m_removeliteralconsumer = p_removeliteralconsumer;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "unchecked" )
    public final <N extends IAgent<?>> IView<N> raw()
    {
        return (IView<N>) this;
    }

    @Nonnull
    @Override
    @SafeVarargs
    public final Stream<IView<T>> walk( @Nonnull final IPath p_path, @Nullable final IViewGenerator<T>... p_generator )
    {

        return null;
    }

    @Nonnull
    @Override
    public IView<T> generate( @Nonnull final IViewGenerator<T> p_generator, @Nonnull final IPath... p_paths )
    {
        return this;
    }

    @Nonnull
    @Override
    public final Stream<IView<T>> root()
    {
        return Stream.concat(
            Stream.of( this ),
            Stream.of( this.parent() ).filter( Objects::nonNull )
        );
    }

    @Nonnull
    @Override
    public final IBeliefbase<T> beliefbase()
    {
        return IBeliefbase.EMPY.raw();
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
    public final IView<T> parent()
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
    public final Stream<ILiteral> stream( @Nullable final IPath... p_path )
    {
        return Stream.empty();
    }

    @Nonnull
    @Override
    public final Stream<ILiteral> stream( final boolean p_negated, @Nullable final IPath... p_path )
    {
        return null;
    }

    @Nonnull
    @Override
    public final IView<T> clear( @Nullable final IPath... p_path )
    {
        return this;
    }

    @Nonnull
    @Override
    public final IView<T> add( @Nonnull final Stream<ILiteral> p_literal )
    {
        m_addliteralconsumer.accept( p_literal, m_data );
        return this;
    }

    @Nonnull
    @Override
    public final IView<T> add( @Nonnull final ILiteral... p_literal )
    {
        return this.add( Arrays.stream( p_literal ) );
    }

    @Nonnull
    @Override
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    public final IView<T> add( @Nonnull final IView<T>... p_view )
    {
        m_addviewconsumer.accept( Arrays.stream( p_view ), m_data );
        return this;
    }

    @Nonnull
    @Override
    public IView<T> remove( @Nonnull final Stream<ILiteral> p_literal )
    {
        m_removeliteralconsumer.accept( p_literal, m_data );
        return this;
    }

    @Nonnull
    @Override
    @SuppressWarnings( "varargs" )
    public IView<T> remove( @Nonnull final ILiteral... p_literal )
    {
        return this.remove( Arrays.stream( p_literal ) );
    }

    @Nonnull
    @Override
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    public final IView<T> remove( @Nonnull final IView<T>... p_view )
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
        return false;
    }

    @Override
    public final int size()
    {
        return 0;
    }

    @Nonnull
    @Override
    public final T update( @Nonnull final T p_agent )
    {
        return p_agent;
    }
}
