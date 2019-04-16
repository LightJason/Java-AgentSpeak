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

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
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
    public Stream<N> streammulti()
    {
        return Stream.empty();
    }

    @Nonnull
    @Override
    public Stream<M> streamsingle()
    {
        return m_elements.values().stream();
    }

    @Override
    public boolean containsmulti( @Nonnull final String p_key )
    {
        return false;
    }

    @Override
    public boolean containssingle( @Nonnull final String p_key )
    {
        return m_elements.containsKey( p_key );
    }

    @Override
    public boolean putmulti( @Nonnull final String p_key, final N p_value )
    {
        return false;
    }

    @Override
    public boolean putsingle( @Nonnull final String p_key, final M p_value )
    {
        return !p_value.equals( m_elements.put( p_key, p_value ) );
    }

    @Override
    public boolean removemulti( @Nonnull final String p_key, final N p_value )
    {
        return false;
    }

    @Override
    public boolean removesingle( @Nonnull final String p_key )
    {
        return Objects.nonNull( m_elements.remove( p_key ) );
    }

    @Override
    public M single( @Nonnull final String p_key )
    {
        return m_elements.get( p_key );
    }

    @Override
    public M singleordefault( @Nonnull final String p_key, final M p_default )
    {
        return m_elements.getOrDefault( p_key, p_default );
    }

    @Nonnull
    @Override
    public Collection<N> multi( @Nonnull final String p_key )
    {
        return Collections.emptySet();
    }

    @Override
    public IStorage<N, M> clear()
    {
        m_elements.clear();
        return this;
    }

    @Override
    public boolean isempty()
    {
        return m_elements.isEmpty();
    }

    @Override
    public int size()
    {
        return 0;
    }

}
