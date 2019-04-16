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
    public Stream<N> streamMultiElements()
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
    public boolean containsMultiElement( @Nonnull final String p_key )
    {
        return false;
    }

    @Override
    public boolean containsSingleElement( @Nonnull final String p_key )
    {
        return m_elements.containsKey( p_key );
    }

    @Override
    public boolean putMultiElement( @Nonnull final String p_key, final N p_value )
    {
        return false;
    }

    @Override
    public boolean putSingleElement( @Nonnull final String p_key, final M p_value )
    {
        return !p_value.equals( m_elements.put( p_key, p_value ) );
    }

    @Override
    public boolean removeMultiElement( @Nonnull final String p_key, final N p_value )
    {
        return false;
    }

    @Override
    public boolean removeSingleElement( @Nonnull final String p_key )
    {
        return Objects.nonNull( m_elements.remove( p_key ) );
    }

    @Override
    public M getSingleElement( @Nonnull final String p_key )
    {
        return m_elements.get( p_key );
    }

    @Override
    public M getSingleElementOrDefault( @Nonnull final String p_key, final M p_default )
    {
        return m_elements.getOrDefault( p_key, p_default );
    }

    @Nonnull
    @Override
    public Collection<N> getMultiElement( @Nonnull final String p_key )
    {
        return Collections.emptySet();
    }

    @Override
    public void clear()
    {
        m_elements.clear();
    }

    @Override
    public boolean empty()
    {
        return m_elements.isEmpty();
    }

    @Override
    public int size()
    {
        return 0;
    }

}
