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

package org.lightjason.agentspeak.beliefbase.storage;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * thread-safe storage of the data of
 * single- and multi-elements, a multi-element
 * can be stored once and will be replaced iif
 * a new one is added
 *
 * @tparam N multi-element type
 * @tparam M single-element type
 * @tparam T agent type
 */
public final class CSingleStorage<N, M> extends IBaseStorage<N, M>
{
    /**
     * map with elements
     **/
    private final Map<String, N> m_multielements = Collections.synchronizedMap( new LinkedHashMap<>() );
    /**
     * map with single elements
     **/
    private final Map<String, M> m_singleelements = new ConcurrentHashMap<>();

    /**
     * ctor
     */
    public CSingleStorage()
    {
        super();
    }

    @Nonnull
    @Override
    public Stream<N> streamMultiElements()
    {
        return m_multielements.values().stream();
    }

    @Nonnull
    @Override
    public Stream<M> streamSingleElements()
    {
        return m_singleelements.values().stream();
    }

    @Override
    public boolean containsMultiElement( @Nonnull final String p_key )
    {
        return m_multielements.containsKey( p_key );
    }

    @Override
    public boolean containsSingleElement( @Nonnull final String p_key )
    {
        return m_singleelements.containsKey( p_key );
    }

    @Override
    public boolean putMultiElement( @Nonnull final String p_key, final N p_value )
    {
        return !p_value.equals( m_multielements.put( p_key, p_value ) );
    }

    @Override
    public boolean putSingleElement( @Nonnull final String p_key, final M p_value )
    {
        return !p_value.equals( m_singleelements.put( p_key, p_value ) );
    }

    @Override
    public boolean putSingleElementIfAbsent( @Nonnull final String p_key, final M p_value )
    {
        return !p_value.equals( m_singleelements.putIfAbsent( p_key, p_value ) );
    }

    @Override
    public boolean removeMultiElement( @Nonnull final String p_key, final N p_value )
    {
        return p_value.equals( m_multielements.remove( p_key ) );
    }

    @Override
    public boolean removeSingleElement( @Nonnull final String p_key )
    {
        return Objects.nonNull( m_singleelements.remove( p_key ) );
    }

    @Override
    public M getSingleElement( @Nonnull final String p_key )
    {
        return m_singleelements.get( p_key );
    }

    @Override
    public M getSingleElementOrDefault( @Nonnull final String p_key, final M p_default )
    {
        return m_singleelements.getOrDefault( p_key, p_default );
    }

    @Nonnull
    @Override
    public Collection<N> getMultiElement( @Nonnull final String p_key )
    {
        return Stream.of( m_multielements.get( p_key ) ).collect( Collectors.toSet() );
    }

    @Override
    public void clear()
    {
        m_multielements.clear();
        m_singleelements.clear();
    }

    @Override
    public boolean empty()
    {
        return m_multielements.isEmpty() && m_singleelements.isEmpty();
    }

    @Override
    public int size()
    {
        return m_multielements.size();
    }


    @Override
    public String toString()
    {
        return MessageFormat.format( "[multi elements: {0}, single elements: {1}]", m_multielements, m_singleelements );
    }

}
