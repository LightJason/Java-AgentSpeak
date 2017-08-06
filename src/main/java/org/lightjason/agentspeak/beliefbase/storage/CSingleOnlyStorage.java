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

package org.lightjason.agentspeak.beliefbase.storage;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


/**
 * storage only for single elements
 */
public final class CSingleOnlyStorage<N, M> extends IBaseStorage<N, M>
{
    /**
     * map with single elements
     **/
    private final Map<String, M> m_elements = new ConcurrentHashMap<>();

    @Nonnull
    @Override
    public final Stream<N> streamMultiElements()
    {
        return Stream.empty();
    }

    @Nonnull
    @Override
    public Stream<M> streamSingleElements()
    {
        return m_elements.values().stream();
    }

    @Override
    public final boolean containsMultiElement( @Nonnull final String p_key )
    {
        return false;
    }

    @Override
    public final boolean containsSingleElement( @Nonnull final String p_key )
    {
        return m_elements.containsKey( p_key );
    }

    @Override
    public final boolean putMultiElement( @Nonnull final String p_key, final N p_value )
    {
        return false;
    }

    @Override
    public final boolean putSingleElement( @Nonnull final String p_key, final M p_value )
    {
        return !p_value.equals( m_elements.put( p_key, p_value ) );
    }

    @Override
    public final boolean putSingleElementIfAbsent( @Nonnull final String p_key, final M p_value )
    {
        return !p_value.equals( m_elements.putIfAbsent( p_key, p_value ) );
    }

    @Override
    public final boolean removeMultiElement( @Nonnull final String p_key, final N p_value )
    {
        return false;
    }

    @Override
    public final boolean removeSingleElement( @Nonnull final String p_key )
    {
        return m_elements.remove( p_key ) != null;
    }

    @Override
    public final M getSingleElement( @Nonnull final String p_key )
    {
        return m_elements.get( p_key );
    }

    @Override
    public final M getSingleElementOrDefault( @Nonnull final String p_key, final M p_default )
    {
        return m_elements.getOrDefault( p_key, p_default );
    }

    @Nonnull
    @Override
    public final Collection<N> getMultiElement( @Nonnull final String p_key )
    {
        return Collections.emptySet();
    }

    @Override
    public final void clear()
    {
        m_elements.clear();
    }

    @Override
    public final boolean empty()
    {
        return m_elements.isEmpty();
    }

    @Override
    public final int size()
    {
        return 0;
    }
}
