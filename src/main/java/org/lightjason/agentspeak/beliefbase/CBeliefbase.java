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

package org.lightjason.agentspeak.beliefbase;

import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.storage.IStorage;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.Stream;


/**
 * beliefbase to generate any event-based data by reference counting
 */
public final class CBeliefbase extends IBaseBeliefbase
{
    /**
     * storage with data
     */
    private final IStorage<ILiteral, IView> m_storage;

    /**
     * ctor
     *
     * @param p_storage storage
     */
    public CBeliefbase( @Nonnull final IStorage<ILiteral, IView> p_storage )
    {
        m_storage = p_storage;
    }

    @Override
    public final int hashCode()
    {
        return m_storage.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IBeliefbase ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Nonnull
    @Override
    public final ILiteral add( @Nonnull final ILiteral p_literal )
    {
        return m_storage.putMultiElement( p_literal.functor(), p_literal )
               ? super.add( p_literal )
               : p_literal;
    }

    @Nonnull
    @Override
    public final IView add( @Nonnull final IView p_view )
    {
        m_storage.putSingleElement( p_view.name(), p_view );
        return p_view;
    }

    @Nonnull
    @Override
    public final IView remove( @Nonnull final IView p_view )
    {
        m_storage.removeSingleElement( this.internalremove( p_view ).name() );
        return p_view;
    }

    @Nonnull
    @Override
    public final ILiteral remove( @Nonnull final ILiteral p_literal )
    {
        return m_storage.removeMultiElement( p_literal.functor(), p_literal )
               ? super.remove( p_literal )
               : p_literal;
    }

    @Override
    public final boolean containsLiteral( @Nonnull final String p_key )
    {
        return m_storage.containsMultiElement( p_key );
    }

    @Override
    public final boolean containsView( @Nonnull final String p_key )
    {
        return m_storage.containsSingleElement( p_key );
    }

    @Nonnull
    @Override
    public final IView view( @Nonnull final String p_key )
    {
        return m_storage.getSingleElement( p_key );
    }

    @Nonnull
    @Override
    public final IView viewOrDefault( @Nonnull final String p_key, @Nullable final IView p_default )
    {
        return m_storage.getSingleElementOrDefault( p_key, p_default );
    }

    @Nonnull
    @Override
    public final Collection<ILiteral> literal( @Nonnull final String p_key )
    {
        return m_storage.getMultiElement( p_key );
    }

    @Nonnull
    @Override
    public final IAgent<?> update( @Nonnull final IAgent<?> p_agent )
    {
        super.update( p_agent );
        m_storage.streamSingleElements().parallel().forEach( i -> i.update( p_agent ) );
        return m_storage.update( p_agent );
    }

    @Nonnull
    @Override
    public final IBeliefbase clear()
    {
        // create delete-event for all literals
        m_storage
            .streamMultiElements()
            .parallel()
            .forEach( i -> this.event( ITrigger.EType.DELETEBELIEF, i ) );

        m_storage.streamSingleElements().parallel().forEach( i -> i.clear() );
        m_storage.clear();

        return this;
    }

    @Override
    public final boolean empty()
    {
        return m_storage.empty();
    }

    @Override
    public final int size()
    {
        return m_storage.size() + m_storage.streamSingleElements().parallel().mapToInt( IStructure::size ).sum();
    }

    @Nonnull
    @Override
    public final Stream<ITrigger> trigger( @Nonnull final IView p_view )
    {
        return Stream.concat(
            super.trigger( p_view ).parallel(),
            m_storage.streamSingleElements().parallel().flatMap( IView::trigger )
        );
    }

    @Nonnull
    @Override
    public final Stream<ILiteral> streamLiteral()
    {
        return m_storage.streamMultiElements();
    }

    @Nonnull
    @Override
    public final Stream<IView> streamView()
    {
        return m_storage.streamSingleElements();
    }

    @Override
    public final String toString()
    {
        return m_storage.toString();
    }



}
