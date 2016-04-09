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

package lightjason.beliefbase;


import com.google.common.collect.Sets;
import lightjason.agent.IAgent;
import lightjason.common.CCommon;
import lightjason.error.CIllegalArgumentException;
import lightjason.language.ILiteral;
import lightjason.language.instantiable.plan.trigger.CTrigger;
import lightjason.language.instantiable.plan.trigger.ITrigger;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * default beliefbase
 *
 * @todo event storing must be implement, use weak-reference or reference-counting to store view relation with event replication
 * (event methods: clear, add, remove(Literal | String), modify -> event is generated on successfully operation)
 * @todo reference counting with http://docs.oracle.com/javase/8/docs/api/java/lang/ref/PhantomReference.html /
 * http://docs.oracle.com/javase/8/docs/api/java/lang/ref/WeakReference.html
 * https://community.oracle.com/blogs/enicholas/2006/05/04/understanding-weak-references /
 * @todo check if a map with hash values optimize runtime access
 */
public final class CBeliefBase implements IBeliefBase
{
    /**
     * storage with data
     */
    protected final IStorage<Pair<Boolean, ILiteral>, IView> m_storage;
    /**
     * weak reference queue of all masks to avoid memory-leaks of belief events
     */
    protected final ReferenceQueue<IView> m_maskreference = new ReferenceQueue<>();
    /**
     * map with events for a mask
     */
    protected final Map<IView, Set<ITrigger>> m_events = new ConcurrentHashMap<>();

    /**
     * ctor
     *
     * @param p_storage storage
     */
    public CBeliefBase( final IStorage<Pair<Boolean, ILiteral>, IView> p_storage )
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
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final boolean add( final ILiteral p_literal )
    {
        // create add-event for the literal
        m_events.values().stream().forEach( i -> i.add( CTrigger.from( ITrigger.EType.ADDBELIEF, p_literal ) ) );
        return m_storage.getMultiElements().put( p_literal.getFunctor(), new ImmutablePair<>( p_literal.isNegated(), p_literal ) );
    }

    @Override
    public final IView add( final IView p_view )
    {
        m_storage.getSingleElements().put( p_view.getName(), p_view );
        return p_view;
    }

    @Override
    public final boolean change( final ILiteral p_before, final ILiteral p_after )
    {
        if ( ( !p_before.getFQNFunctor().equals( p_after.getFQNFunctor() ) ) && ( p_before.isNegated() == p_after.isNegated() ) )
            throw new CIllegalArgumentException( CCommon.getLanguageString( this, "functorequal", p_before, p_after ) );

        // create modified-event for the changed literal
        m_events.values().stream().forEach( i -> i.add( CTrigger.from( ITrigger.EType.CHANGEBELIEF, p_after ) ) );

        // run remove and add manually
        return m_storage.getMultiElements().remove( p_before.getFunctor(), new ImmutablePair<>( p_before.isNegated(), p_before ) )
               && m_storage.getMultiElements().put( p_after.getFunctor(), new ImmutablePair<>( p_after.isNegated(), p_after ) );
    }

    @Override
    public final void clear()
    {
        // create delete-event for all literals
        m_storage.getMultiElements().values().stream()
                 .forEach( j -> m_events.values().stream()
                                        .forEach( i -> i.add( CTrigger.from( ITrigger.EType.CHANGEBELIEF, j.getRight() ) ) )
                 );

        m_storage.getSingleElements().values().parallelStream().forEach( i -> i.clear() );
        m_storage.clear();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <E extends IView> E create( final String p_name )
    {
        // add reference for the mask and the event structure
        final IView l_view = new CView( p_name, this );

        new PhantomReference<>( l_view, m_maskreference );
        m_events.put( l_view, Sets.newConcurrentHashSet() );

        return (E) l_view;
    }

    @Override
    public final boolean isEmpty()
    {
        return m_storage.isEmpty();
    }

    @Override
    public final boolean remove( final IView p_view )
    {
        m_events.remove( p_view );
        return m_storage.getSingleElements().remove( p_view.getName() ) != null;
    }

    @Override
    public final boolean remove( final ILiteral p_literal )
    {
        // create delete-event for the literal
        m_events.values().stream().forEach( i -> i.add( CTrigger.from( ITrigger.EType.DELETEBELIEF, p_literal ) ) );
        return m_storage.getMultiElements().remove( p_literal.getFunctor(), new ImmutablePair<>( p_literal.isNegated(), p_literal ) );
    }

    @Override
    public void update( final IAgent p_agent )
    {
        // check all references of mask and remove unused references
        Reference<? extends IView> l_reference;
        while ( ( l_reference = m_maskreference.poll() ) != null )
            m_events.remove( l_reference.get() );

        // run storage update
        m_storage.update( p_agent );
        m_storage.getSingleElements().values().parallelStream().forEach( i -> i.update( p_agent ) );
    }

    @Override
    public final int size()
    {
        return m_storage.getMultiElements().size() + m_storage.getSingleElements().values().parallelStream().mapToInt( i -> i.size() ).sum();
    }

    /**
     * @bug get with view does not work correct
     */
    @Override
    @SuppressWarnings( "serial" )
    public final Set<ITrigger> getTrigger( final IView p_view )
    {
        // get data or return an empty set
        final Set<ITrigger> l_trigger = m_events.getOrDefault( p_view, Collections.<ITrigger>emptySet() );

        // create copy of all trigger and recursive elements
        final Set<ITrigger> l_copy = Collections.unmodifiableSet(
                Stream.concat(
                        l_trigger.parallelStream(),
                        m_storage.getSingleElements().values().parallelStream().flatMap( i -> i.getTrigger().stream() )
                )
                      .collect( Collectors.toSet() )
        );

        // clear all trigger elements if no trigger exists return an empty set
        l_trigger.clear();
        return l_copy;
    }

    @Override
    public final boolean remove( final String p_name )
    {
        // @todo check trigger event
        final boolean l_single = m_storage.getSingleElements().remove( p_name ) != null;
        final boolean l_multi = m_storage.getMultiElements().removeAll( p_name ) != null;
        return l_single || l_multi;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public final <L extends IStorage<Pair<Boolean, ILiteral>, IView>> L getStorage()
    {
        return (L) m_storage;
    }

}
