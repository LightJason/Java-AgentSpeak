/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L)                                  #
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

package org.lightjason.agentspeak.beliefbase;

import com.google.common.collect.Sets;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.view.CView;
import org.lightjason.agentspeak.beliefbase.view.IView;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.CTrigger;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


/**
 * default behaviour of a beliefbase
 *
 * @todo check reference counting on delete views
 * @tparam T agent type
 * @see http://docs.oracle.com/javase/8/docs/api/java/lang/ref/PhantomReference.html
 * @see http://docs.oracle.com/javase/8/docs/api/java/lang/ref/WeakReference.html
 * @see https://community.oracle.com/blogs/enicholas/2006/05/04/understanding-weak-references
 */
@SuppressWarnings( "serial" )
public abstract class IBaseBeliefbase<T extends IAgent<?>> implements IBeliefbase<T>
{
    /**
     * map with events for a mask
     */
    protected final CEventMap m_events = new CEventMap();
    /**
     * weak reference queue of all masks to avoid memory-leaks of belief events
     */
    private final ReferenceQueue<IView<T>> m_maskreference = new ReferenceQueue<>();


    @Override
    public final IView<T> create( final String p_name )
    {
        return this.eventreference( new CView<>( p_name, this ) );
    }

    @Override
    public final IView<T> create( final String p_name, final IView<T> p_parent )
    {
        return this.eventreference( new CView<>( p_name, this, p_parent ) );
    }

    @Override
    public ILiteral add( final ILiteral p_literal )
    {
        return this.event( ITrigger.EType.ADDBELIEF, p_literal );
    }

    @Override
    public ILiteral remove( final ILiteral p_literal )
    {
        return this.event( ITrigger.EType.DELETEBELIEF, p_literal );
    }

    @Override
    public T update( final T p_agent )
    {
        // check all references of mask and remove unused references
        Reference<? extends IView<T>> l_reference;
        while ( ( l_reference = m_maskreference.poll() ) != null )
        {
            final IView<T> l_view = l_reference.get();
            if ( l_view != null )
                m_events.remove( l_view );
        }

        return p_agent;
    }

    @Override
    public Stream<ITrigger> trigger( final IView<T> p_view )
    {
        return this.getAndClearTrigger( p_view ).parallel();
    }

    /**
     * push an event and literal to the event map
     *
     * @param p_event event
     * @param p_literal literal
     */
    protected ILiteral event( final ITrigger.EType p_event, final ILiteral p_literal )
    {
        m_events.keySet().forEach( i -> m_events.put( i, CTrigger.from( p_event, p_literal ) ) );
        return p_literal;
    }

    /**
     * adds a view to the event referencing structure
     *
     * @param p_view view
     * @return input view
     */
    protected IView<T> eventreference( final IView<T> p_view )
    {
        new PhantomReference<>( p_view, m_maskreference );
        m_events.put( p_view );
        return p_view;
    }

    /**
     * copy of all trigger values
     *
     * @param p_view trigger of this view
     * @return set with trigger values
     */
    protected final Stream<ITrigger> getAndClearTrigger( final IView<T> p_view )
    {
        final Collection<ITrigger> l_trigger = m_events.getOrDefault( p_view, Collections.<ITrigger>emptySet() );
        final Set<ITrigger> l_result = new HashSet<>( l_trigger );
        l_trigger.clear();
        return l_result.stream();
    }


    /**
     * class to represent the event structure
     */
    protected final class CEventMap extends ConcurrentHashMap<IView<T>, Set<ITrigger>> implements Map<IView<T>, Set<ITrigger>>
    {
        /**
         * add an empty key structure
         *
         * @param p_key key object
         * @return value
         */
        public synchronized Set<ITrigger> put( final IView<T> p_key  )
        {
            return super.put( p_key, Sets.newConcurrentHashSet() );
        }

        /**
         * puts a trigger into the set
         *
         * @param p_key key object
         * @param p_value trigger value
         * @return set
         */
        public Set<ITrigger> put( final IView<T> p_key, final ITrigger p_value )
        {
            final Set<ITrigger> l_set = m_events.getOrDefault( p_key, Sets.newConcurrentHashSet() );
            l_set.add( p_value );
            return this.putIfAbsent( p_key, l_set );
        }
    }

}
