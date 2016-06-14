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


import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * beliefbase, reference counting is used to collect the events for each beliefbase view
 *
 * @todo check reference counting on delete views
 * @see http://docs.oracle.com/javase/8/docs/api/java/lang/ref/PhantomReference.html
 * @see http://docs.oracle.com/javase/8/docs/api/java/lang/ref/WeakReference.html
 * @see https://community.oracle.com/blogs/enicholas/2006/05/04/understanding-weak-references
 *
 * @tparam T agent type
 */
public final class CBeliefBase<T extends IAgent<?>> implements IBeliefBase<T>
{
    /**
     * storage with data
     */
    protected final IStorage<Pair<Boolean, ILiteral>, IView<T>, T> m_storage;
    /**
     * weak reference queue of all masks to avoid memory-leaks of belief events
     */
    protected final ReferenceQueue<IView<T>> m_maskreference = new ReferenceQueue<>();
    /**
     * map with events for a mask
     */
    protected final Map<IView<T>, Set<ITrigger>> m_events = new ConcurrentHashMap<>();

    /**
     * ctor
     *
     * @param p_storage storage
     */
    public CBeliefBase( final IStorage<Pair<Boolean, ILiteral>, IView<T>, T> p_storage )
    {
        if ( p_storage == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "empty" ) );
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
    public final void add( final ILiteral p_literal )
    {
        // create add-event for the literal
        if ( m_storage.putMultiElements( p_literal.getFunctor(), new ImmutablePair<>( p_literal.isNegated(), p_literal ) ) )
            m_events.values().forEach( i -> i.add( CTrigger.from( ITrigger.EType.ADDBELIEF, p_literal ) ) );
    }

    @Override
    public final void add( final IView<T> p_view )
    {
        m_storage.putSingleElements( p_view.getName(), p_view );
    }

    @Override
    public final void remove( final IView<T> p_view )
    {
        m_events.remove( p_view );
        m_storage.removeSingleElements( p_view.getName() );
    }

    @Override
    public final void remove( final ILiteral p_literal )
    {
        // create delete-event for the literal
        if ( m_storage.removeMultiElements( p_literal.getFunctor(), new ImmutablePair<>( p_literal.isNegated(), p_literal ) ) )
            m_events.values().forEach( i -> i.add( CTrigger.from( ITrigger.EType.DELETEBELIEF, p_literal ) ) );
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
    public final Collection<Pair<Boolean, ILiteral>> getLiteral( final String p_key )
    {
        return m_storage.getMultiElement( p_key );
    }

    @Override
    public final T update( final T p_agent )
    {
        // check all references of mask and remove unused references
        Reference<? extends IView<T>> l_reference;
        while ( ( l_reference = m_maskreference.poll() ) != null )
        {
            final IView<T> l_view = l_reference.get();
            if ( l_view != null )
                m_events.remove( l_view );
        }

        // run storage update
        m_storage.streamSingleElements().parallel().forEach( i -> i.update( p_agent ) );
        return m_storage.update( p_agent );
    }

    @Override
    public final IView<T> create( final String p_name )
    {
        // add reference for the mask and the event structure
        final IView<T> l_view = new CView<>( p_name, this );

        new PhantomReference<>( l_view, m_maskreference );
        m_events.put( l_view, Sets.newConcurrentHashSet() );

        return l_view;
    }

    @Override
    public final void clear()
    {
        // create delete-event for all literals
        m_storage
            .streamMultiElements()
            .parallel()
            .forEach(
                j -> m_events
                    .values()
                    .forEach( i -> i.add( CTrigger.from( ITrigger.EType.DELETEBELIEF, j.getRight() ) ) )
            );

        m_storage.streamSingleElements().parallel().forEach( i -> i.clear() );
        m_storage.clear();
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
        // get data or return an empty set
        final Set<ITrigger> l_trigger = m_events.getOrDefault( p_view, Collections.<ITrigger>emptySet() );

        // create copy of all trigger and recursive elements
        final Set<ITrigger> l_copy = Collections.unmodifiableSet(
            Stream.concat(
                l_trigger.parallelStream(),
                m_storage.streamSingleElements().parallel().flatMap( IView::getTrigger )
            )
                  .collect( Collectors.toSet() )
        );

        // clear all trigger elements if no trigger exists return an empty set
        l_trigger.clear();
        return l_copy.stream();
    }

    @Override
    public final Stream<ILiteral> streamLiteral()
    {
        return m_storage.streamMultiElements().map( Pair::getRight );
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
