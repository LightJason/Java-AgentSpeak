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


import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.storage.IStorage;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.util.Collection;
import java.util.stream.Stream;


/**
 * beliefbase, reference counting is used to collect the events for each beliefbase view
 */
public final class CBeliefBasePersistent<T extends IAgent<?>> extends IBaseBeliefBase<T>
{
    /**
     * storage with data
     */
    private final IStorage<ILiteral, IView<T>, T> m_storage;

    /**
     * ctor
     *
     * @param p_storage storage
     */
    public CBeliefBasePersistent( final IStorage<ILiteral, IView<T>, T> p_storage )
    {
        if ( p_storage == null )
            throw new CIllegalArgumentException( CCommon.languagestring( this, "empty" ) );
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
        return ( p_object != null ) && ( p_object instanceof IBeliefBase<?> ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final ILiteral add( final ILiteral p_literal )
    {
        if ( m_storage.putMultiElement( p_literal.functor(), p_literal ) )
            super.add( p_literal );
        return p_literal;
    }

    @Override
    public final IView<T> add( final IView<T> p_view )
    {
        m_storage.putSingleElement( p_view.name(), p_view );
        return p_view;
    }

    @Override
    public final IView<T> remove( final IView<T> p_view )
    {
        m_events.asMap().remove( p_view );
        m_storage.removeSingleElement( p_view.name() );
        return p_view;
    }

    @Override
    public final ILiteral remove( final ILiteral p_literal )
    {
        if ( m_storage.removeMultiElement( p_literal.functor(), p_literal ) )
            super.remove( p_literal );
        return p_literal;
    }

    @Override
    public final boolean containsLiteral( final String p_key )
    {
        return m_storage.containsMultiElement( p_key );
    }

    @Override
    public final boolean containsView( final String p_key )
    {
        return m_storage.containsSingleElement( p_key );
    }

    @Override
    public final IView<T> getView( final String p_key )
    {
        return m_storage.getSingleElement( p_key );
    }

    @Override
    public final IView<T> getViewOrDefault( final String p_key, final IView<T> p_default )
    {
        return m_storage.getSingleElementOrDefault( p_key, p_default );
    }

    @Override
    public final Collection<ILiteral> getLiteral( final String p_key )
    {
        return m_storage.getMultiElement( p_key );
    }

    @Override
    public final T update( final T p_agent )
    {
        super.update( p_agent );
        m_storage.streamSingleElements().parallel().forEach( i -> i.update( p_agent ) );
        return m_storage.update( p_agent );
    }

    @Override
    public final IBeliefBase<T> clear()
    {
        // create delete-event for all literals
        m_storage
            .streamMultiElements()
            .parallel()
            .forEach(
                i -> m_events
                    .keySet()
                    .forEach( j -> m_events.put( j, CTrigger.from( ITrigger.EType.DELETEBELIEF, i ) ) )
            );

        m_storage.streamSingleElements().parallel().forEach( i -> i.clear() );
        m_storage.clear();

        return this;
    }

    @Override
    public final boolean isEmpty()
    {
        return m_storage.isEmpty();
    }

    @Override
    public final int size()
    {
        return m_storage.size() + m_storage.streamSingleElements().parallel().mapToInt( IStructure::size ).sum();
    }

    @Override
    public final Stream<ITrigger> getTrigger( final IView<T> p_view )
    {
        return Stream.concat(
            this.getAndClearTrigger( p_view ).parallel(),
            m_storage.streamSingleElements().parallel().flatMap( IView::trigger )
        );
    }

    @Override
    public final Stream<ILiteral> streamLiteral()
    {
        return m_storage.streamMultiElements();
    }

    @Override
    public final Stream<IView<T>> streamView()
    {
        return m_storage.streamSingleElements();
    }

    @Override
    public final String toString()
    {
        return m_storage.toString();
    }



}
