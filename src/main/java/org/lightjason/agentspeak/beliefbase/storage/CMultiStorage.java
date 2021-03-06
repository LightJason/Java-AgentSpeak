/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;


/**
 * thread-safe storage of the data of
 * single- and multi-elements
 *
 * @tparam N multi-element type
 * @tparam M single-element type
 * @tparam T agent type
 */
public final class CMultiStorage<N, M> extends IBaseStorage<N, M>
{
    /**
     * map with elements
     **/
    private final SetMultimap<String, N> m_multielements = Multimaps.synchronizedSetMultimap( LinkedHashMultimap.create() );
    /**
     * map with single elements
     **/
    private final Map<String, M> m_singleelements = new ConcurrentHashMap<>();


    @Nonnull
    @Override
    public Stream<N> streammulti()
    {
        return m_multielements.values().stream();
    }

    @Nonnull
    @Override
    public Stream<M> streamsingle()
    {
        return m_singleelements.values().stream();
    }

    @Override
    public boolean containsmulti( @Nonnull final String p_key )
    {
        return m_multielements.containsKey( p_key );
    }

    @Override
    public boolean containssingle( @Nonnull final String p_key )
    {
        return m_singleelements.containsKey( p_key );
    }

    @Override
    public boolean putmulti( @Nonnull final String p_key, final N p_value )
    {
        return m_multielements.put( p_key, p_value );
    }

    @Override
    public boolean putsingle( @Nonnull final String p_key, final M p_value )
    {
        return !p_value.equals( m_singleelements.put( p_key, p_value ) );
    }

    @Override
    public boolean removemulti( @Nonnull final String p_key, final N p_value )
    {
        return m_multielements.remove( p_key, p_value );
    }

    @Override
    public boolean removesingle( @Nonnull final String p_key )
    {
        return Objects.nonNull( m_singleelements.remove( p_key ) );
    }

    @Override
    public M single( @Nonnull final String p_key )
    {
        return m_singleelements.get( p_key );
    }

    @Override
    public M singleordefault( @Nonnull final String p_key, final M p_default )
    {
        return m_singleelements.getOrDefault( p_key, p_default );
    }

    @Nonnull
    @Override
    public Collection<N> multi( @Nonnull final String p_key )
    {
        return m_multielements.get( p_key );
    }

    @Override
    public IStorage<N, M> clear()
    {
        m_multielements.clear();
        m_singleelements.clear();
        return this;
    }

    @Override
    public boolean isempty()
    {
        return m_multielements.isEmpty() && m_singleelements.isEmpty();
    }

    @Override
    public int size()
    {
        return m_multielements.asMap().values().stream().mapToInt( Collection::size ).sum();
    }

    @Override
    public String toString()
    {
        return MessageFormat.format(
            "{0} {1}",
            m_multielements.isEmpty() ? "" : m_multielements.values(),
            m_singleelements.isEmpty() ? "" : m_singleelements.values()
        ).trim();
    }
}
